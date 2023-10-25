from django.shortcuts import render, redirect
import json
import logging
import os
import numpy as np
import torch
import torch.nn.functional as F
from torch.utils.data import DataLoader, SequentialSampler
from attrdict import AttrDict
import requests
from io import BytesIO
from django.http import HttpResponse, JsonResponse
from PIL import Image
from django.views.decorators.csrf import csrf_exempt
REST_API_KEY = '472d2c640e9e91717a08a4de64af8974'

def t2i(prompt, negative_prompt):
    try:
        r = requests.post(
            'https://api.kakaobrain.com/v2/inference/karlo/t2i',
            json = {
                'prompt': prompt,
                'negative_prompt': negative_prompt
            },
            headers = {
                'Authorization': f'KakaoAK {REST_API_KEY}',
                'Content-Type': 'application/json'
            }
        )
        # 응답 JSON 형식으로 변환
        response = json.loads(r.content)
        return response.get("images")[0].get("image")
    except Exception as e:
        # 오류 처리: 오류 메시지 출력
        return None

image_url = None

def making_image(request):
    response = None
    # 이미지 URL을 따로 저장할 변수 추가
    if request.method == "GET":
        prompt = request.GET.get("keyword")
        negative_prompt = "sleeping cat, dog, human, ugly face, cropped"
        response = t2i(prompt, negative_prompt)
        
    return JsonResponse({'image_url':response})

@csrf_exempt
def webp_to_jpg(request):
    if request.method == "POST":
        try:
            data = json.loads(request.body.decode('utf-8'))
            webp_url = data.get("karlourl")

            webp_response = requests.get(webp_url)
            webp_image=Image.open(BytesIO(webp_response.content))

            jpg_image = webp_image.convert("RGB")

            jpg_image_io = BytesIO()
            jpg_image.save(jpg_image_io, format="JPEG")

            response = HttpResponse(jpg_image_io.getvalue(), content_type="image/jpeg")
            response["Content-Disposition"] = 'attachment; filename="output.jpg"'
            return response
        except Exception as e:
            return JsonResponse({'error': 'Internal Server Error'}, status=500)
    
    return JsonResponse({'error': 'Invalid Request'}, status=400)

from .utils import (
    CONFIG_CLASSES,
    TOKENIZER_CLASSES,
    MODEL_FOR_SEQUENCE_CLASSIFICATION,
    init_logger,
    set_seed
)

from .processor import seq_cls_load_and_cache_examples as load_and_cache_examples
from .processor import seq_cls_tasks_num_labels as tasks_num_labels
from .processor import seq_cls_processors as processors

logger = logging.getLogger(__name__)

def predict(args, model, dataset, global_step=None):
    sampler = SequentialSampler(dataset)
    dataloader = DataLoader(dataset, sampler=sampler, batch_size=args.eval_batch_size)
    eval_loss = 0.0
    nb_eval_steps = 0
    preds = None
    out_label_ids = None

    for batch in dataloader:
        model.eval()
        batch = tuple(t.to(args.device) for t in batch)

        with torch.no_grad():
            inputs = {
                "input_ids": batch[0],
                "attention_mask": batch[1],
                "labels": batch[3]
            }
            inputs["token_type_ids"] = batch[2]
            outputs = model(**inputs)
            tmp_eval_loss, logits = outputs[:2]
            softmax = F.softmax(logits, dim=1)
            eval_loss += tmp_eval_loss.mean().item()
        nb_eval_steps += 1
        if preds is None:
            preds = logits.detach().cpu().numpy()
            out_label_ids = inputs["labels"].detach().cpu().numpy()
        else:
            preds = np.append(preds, logits.detach().cpu().numpy(), axis=0)
            out_label_ids = np.append(out_label_ids, inputs["labels"].detach().cpu().numpy(), axis=0)

    labels = np.argmax(preds, axis=1)
    #pred = {'label':labels[0], 'softmax':softmax.tolist()}
    pred = softmax.tolist()
    
    format_pred = [[round(value, 2)*100 for value in pred[0]]]

    return format_pred

@csrf_exempt
def nlp(request):  
    try:
        if request.method == 'POST':
            data = json.loads(request.body.decode('utf-8'))
            text = data.get('text','')
            with open("multi.json","r") as f:
                args = AttrDict(json.load(f))

            args.do_train = False
            args.do_eval = False

            init_logger()
            set_seed(args)
            args.output_dir = os.path.join(args.ckpt_dir, args.output_dir)

            processor = processors[args.task](args)
            labels = processor.get_labels()
            
            config = CONFIG_CLASSES[args.model_type].from_pretrained(
                args.model_name_or_path,
                num_labels=tasks_num_labels[args.task],
                id2label={str(i): label for i, label in enumerate(labels)},
                label2id={label: i for i, label in enumerate(labels)},
            )

            tokenizer = TOKENIZER_CLASSES[args.model_type].from_pretrained(
                args.model_name_or_path,
                do_lower_case=args.do_lower_case
            )

            model = MODEL_FOR_SEQUENCE_CLASSIFICATION[args.model_type].from_pretrained(
                args.model_name_or_path,
                config=config
            )

            # GPU or CPU
            args.device = "cpu"

            checkpoint = os.path.join(args.output_dir, "checkpoint-06000")
            global_step = checkpoint.split("-")[-1]
            model = MODEL_FOR_SEQUENCE_CLASSIFICATION[args.model_type].from_pretrained(checkpoint)
            model.to(args.device)
            
            dataset = load_and_cache_examples(args, tokenizer, mode="predict", line=text)
            result = predict(args, model, dataset, global_step="06000")

            return JsonResponse({'result':result})
        else:
            return JsonResponse({'error':'문장 안 들어옴'})
    except Exception as e:
        return JsonResponse({'error':str(e)},status=500)