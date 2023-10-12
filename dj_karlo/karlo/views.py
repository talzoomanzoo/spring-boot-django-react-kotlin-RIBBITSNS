from typing import Any
from django.shortcuts import render, redirect
import json
import requests
from .models import image, Profile
from io import BytesIO
from django.http import HttpResponse, JsonResponse
from PIL import Image
import os
import shutil
import random
import time
from django.db import models

from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from django.views.generic import View
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
        print(f"An error occurred in t2i: {e}")
        return None

image_url = None

def making_image(request):
    response = None
    # 이미지 URL을 따로 저장할 변수 추가
    print("get outside")
    if request.method == "GET":
        prompt = request.GET.get("keyword")
        print("prompt: "+prompt)
        negative_prompt = "sleeping cat, dog, human, ugly face, cropped"
        response = t2i(prompt, negative_prompt)
        print("response: "+response)
        if response:
            image_save = image(keyword=prompt, image_url=response)
            image_save.save()
            # 이미지 생성 후, image_url 변수에 이미지 URL 저장
            request.session['image_url'] = response
            request.session['keyword'] = prompt
        print("session: "+request.session['image_url'])
        
    #return render(request, "index.html", {"image_url": response})
    return JsonResponse({'image_url':response})

def save_selected_image(request):
    image_url = request.session.get('image_url')
    keyword = request.session.get('keyword')

    if image_url:
        # 이미지를 jpg로 변환 및 저장
        # destination_path는 이미지가 저장될 경로를 나타냅니다.
        # 이미지 파일명을 난수로 생성하고, 해당 파일명으로 저장합니다.
        timestamp = int(time.time())
        random_number = random.randint(1, 1000)
        image_filename = f"{timestamp}_{random_number}.jpg"
        destination_dir = os.path.join('media', 'photos')
        os.makedirs(destination_dir, exist_ok=True)
        destination_path = os.path.join(destination_dir, image_filename)
        
        webp_response = requests.get(image_url)
        webp_image = Image.open(BytesIO(webp_response.content))
        jpg_image = webp_image.convert("RGB")
        jpg_image.save(destination_path, format="JPEG")
        
        username=request.POST.get("user")
        request.session['username'] = username

        # 데이터베이스에 이미지 정보 저장 (예: Image 모델을 사용하여)
        # image_path는 이미지 파일의 상대 경로를 나타냅니다.
        profile = Profile(keyword=keyword,profile_dir='photos\\'+image_filename, profile_url=image_url, write_id=username)
        profile.save()
        


        # 세션에서 이미지 URL 삭제
        del request.session['image_url']
        
        #return redirect('karlo:view')
        return Response(redirect('karlo:view'))
    else:
        print("No image to save.")
    return Response("No image to save.")


def view(request):
    username = request.session.get('username')
    try:
        # username에 해당하는 사용자의 최근 프로필 정보를 가져옵니다.
        latest_profile = Profile.objects.filter(write_id=username).latest('write_date')
        profile_dir = latest_profile.profile_dir  # 프로필 이미지의 디렉토리 경로
    except Profile.DoesNotExist:
        profile_dir = None

    #return render(request, "profile.html", {"profile_dir": profile_dir})
    return Response(profile_dir)
