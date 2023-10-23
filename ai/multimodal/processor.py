# coding: utf-8

from cProfile import label
import os
import copy
import json
import logging

import torch
from torch.utils.data import TensorDataset

logger = logging.getLogger(__name__)


class InputExample(object):
    """
    A single training/test example for simple sequence classification.
    """

    def __init__(self, guid, text_a, text_b, label):
        self.guid = guid
        self.text_a = text_a
        self.text_b = text_b
        self.label = label

    def __repr__(self):
        return str(self.to_json_string())

    def to_dict(self):
        """Serializes this instance to a Python dictionary."""
        output = copy.deepcopy(self.__dict__)
        return output

    def to_json_string(self):
        """Serializes this instance to a JSON string."""
        return json.dumps(self.to_dict(), indent=2, sort_keys=True) + "\n"


class InputFeatures(object):
    """A single set of features of data."""

    def __init__(self, input_ids, attention_mask, token_type_ids, label):
        self.input_ids = input_ids
        self.attention_mask = attention_mask
        self.token_type_ids = token_type_ids
        self.label = label

    def __repr__(self):
        return str(self.to_json_string())

    def to_dict(self):
        """Serializes this instance to a Python dictionary."""
        output = copy.deepcopy(self.__dict__)
        return output

    def to_json_string(self):
        """Serializes this instance to a JSON string."""
        return json.dumps(self.to_dict(), indent=2, sort_keys=True) + "\n"


def seq_cls_convert_examples_to_features(args, examples, tokenizer, max_length, task):
    processor = seq_cls_processors[task](args)
    label_list = processor.get_labels()
    logger.info("Using label list {} for task {}".format(label_list, task))
    output_mode = seq_cls_output_modes[task]
    logger.info("Using output mode {} for task {}".format(output_mode, task))

    label_map = {label: i for i, label in enumerate(label_list)}

    def label_from_example(example):
        if output_mode == "classification":
            return label_map[example.label]
        elif output_mode == "regression":
            return float(example.label)
        raise KeyError(output_mode)

    labels = [label_from_example(example) for example in examples]

    #print("> batch_encode_plus")
    batch_encoding = tokenizer.batch_encode_plus(
        [(example.text_a, example.text_b) for example in examples],
        max_length=max_length,
        padding="max_length",
        add_special_tokens=True,
        truncation=True,
    )
    #print("< batch_encode_plus")

    features = []
    for i in range(len(examples)):
        inputs = {k: batch_encoding[k][i] for k in batch_encoding}
        if "token_type_ids" not in inputs:
            inputs["token_type_ids"] = [0] * len(inputs["input_ids"])  # For xlm-roberta

        feature = InputFeatures(**inputs, label=labels[i])
        features.append(feature)

    for i, example in enumerate(examples[:5]):
        logger.info("*** Example ***")
        logger.info("guid: {}".format(example.guid))
        logger.info("input_ids: {}".format(" ".join([str(x) for x in features[i].input_ids])))
        logger.info("attention_mask: {}".format(" ".join([str(x) for x in features[i].attention_mask])))
        logger.info("token_type_ids: {}".format(" ".join([str(x) for x in features[i].token_type_ids])))
        logger.info("label: {}".format(features[i].label))

    return features


class MultiProcessor(object):
    """Processor for the Korean Hate Speech data set """

    def __init__(self, args):
        self.args = args
        self.labels = ["FALSE", "TRUE"]
        self.lcnt = dict()

    def get_labels(self):
        return self.labels

    def get_plabels(self):
        return ["OTHERS", self.args.label]
    
    @classmethod
    def _read_file(cls, input_file):
        """Reads a tab separated value file."""
        with open(input_file, "r", encoding="utf-8") as f:
            lines = []
            for line in f:
                lines.append(line.strip())
            return lines

    def count_labels(self, label):
        if label in self.lcnt:
            self.lcnt[label] += 1
        else:
            self.lcnt[label] = 1

    def _create_examples(self, lines, set_type):
        """Creates examples for the training and dev sets."""
        examples = []
        for (i, line) in enumerate(lines):
            line = line.split("\t")
            guid = "%s-%s" % (set_type, i)            
            if len(line) < 6:
                continue
            text_a = line[5]
            labels = line[4].strip().split(',')
            label = "TRUE" if self.args.label in labels else "FALSE"
            self.count_labels(label)
            if i % 1000 == 0:
                logger.info(f"{label}, {text_a}")
            examples.append(InputExample(guid=guid, text_a=text_a, text_b=None, label=label))
        if set_type != "pred": 
            logger.info(f"label counts: TRUE={self.lcnt['TRUE']}, FALSE={self.lcnt['FALSE']}")
        return examples

    def get_examples(self, mode):
        """
        Args:
            mode: train, dev, test
        """
        file_to_read = None
        if mode == "train":
            file_to_read = self.args.train_file
        elif mode == "dev":
            file_to_read = self.args.dev_file
        elif mode == "test":
            file_to_read = self.args.test_file
            
        logger.info("LOOKING AT {}".format(os.path.join(self.args.data_dir, file_to_read)))
        return self._create_examples(
            self._read_file(os.path.join(self.args.data_dir, file_to_read)), mode
        )

    def make_examples(self, text):
        lines = ["",f"0\t1\t2\t3\tIMMORAL_NONE\t{text}"]
        return self._create_examples(lines, "pred")


class UnethicProcessor(object):
    """Processor for the Korean Hate Speech data set """

    def __init__(self, args):
        self.args = args
        self.labels = ["1", "0"]

    def get_labels(self):
        return self.labels

    def get_plabels(self):
        return ["비윤리", "정상"]

    @classmethod
    def _read_file(cls, input_file):
        """Reads a tab separated value file."""
        with open(input_file, "r", encoding="utf-8") as f:
            lines = []
            for line in f:
                lines.append(line.strip())
            return lines

    def _create_examples(self, lines, set_type):
        """Creates examples for the training and dev sets."""
        examples = []
        for (i, line) in enumerate(lines):
            line = line.split("\t")
            if len(line) < 2:
                continue
            guid = "%s-%s" % (set_type, i)
            text_a = line[1]
            label = line[0]
            if label not in self.labels:
                continue
            if i % 1000 == 0:
                logger.info(line)
            examples.append(InputExample(guid=guid, text_a=text_a, text_b=None, label=label))
        return examples

    def get_examples(self, mode):
        """
        Args:
            mode: train, dev, test
        """
        file_to_read = None
        if mode == "train":
            file_to_read = self.args.train_file
        elif mode == "dev":
            file_to_read = self.args.dev_file
        elif mode == "test":
            file_to_read = self.args.test_file

        logger.info("LOOKING AT {}".format(os.path.join(self.args.data_dir, file_to_read)))
        return self._create_examples(
            self._read_file(os.path.join(self.args.data_dir, file_to_read)), mode
        )

    def make_examples(self, text):
        lines = [f"0\t{text}"]
        return self._create_examples(lines, "pred")


class UnethicMultiProcessor(object):
    """Processor for the Korean Hate Speech data set """

    def __init__(self, args):
        self.args = args
        self.labels = ["정상","욕설","범죄","혐오","차별","비난","선정"]
        # korm
        #self.labels = ["정상", "욕설", "혐오", "비난"]
        # korm3
        #if args.task == "korm3":
        #    self.labels = ["정상", "혐오", "비난"]
        #elif args.task == "korm3a":
        #    self.labels = ["정상", "비난", "차별"]
        #elif args.task == "korm3b":
        #    self.labels = ["정상", "혐오", "차별"]
        #else:
        #    self.labels = ["정상", "비난", "혐오", "차별"]

    def get_labels(self):
        return self.labels

    def get_plabels(self):
        return self.labels

    @classmethod
    def _read_file(cls, input_file):
        """Reads a tab separated value file."""
        with open(input_file, "r", encoding="utf-8") as f:
            lines = []
            for line in f:
                lines.append(line.strip())
            return lines

    def _create_examples(self, lines, set_type):
        """Creates examples for the training and dev sets."""
        examples = []
        #for (i, line) in enumerate(lines[1:]):
        for (i, line) in enumerate(lines):
            line = line.split("\t")
            if len(line) < 2:
                continue
            guid = "%s-%s" % (set_type, i)
            text_a = line[1]
            label = line[0]
            if label not in self.labels:
                continue
            if i % 1000 == 0:
                logger.info(line)
            examples.append(InputExample(guid=guid, text_a=text_a, text_b=None, label=label))
        return examples

    def get_examples(self, mode):
        """
        Args:
            mode: train, dev, test
        """
        file_to_read = None
        if mode == "train":
            file_to_read = self.args.train_file
        elif mode == "dev":
            file_to_read = self.args.dev_file
        elif mode == "test":
            file_to_read = self.args.test_file

        logger.info("LOOKING AT {}".format(os.path.join(self.args.data_dir, file_to_read)))
        return self._create_examples(
            self._read_file(os.path.join(self.args.data_dir, file_to_read)), mode
        )

    def make_examples(self, text):
        lines = [f"정상\t{text}"]
        return self._create_examples(lines, "pred")


class EthicProcessor(object):
    """Processor for the Korean Hate Speech data set """

    def __init__(self, args):
        self.args = args
        self.labels = ["True", "False"]

    def get_labels(self):
        return self.labels

    def get_plabels(self):
        #return ["비윤리", "정상"]
        return self.labels
    
    @classmethod
    def _read_file(cls, input_file):
        """Reads a tab separated value file."""
        with open(input_file, "r", encoding="utf-8") as f:
            lines = []
            for line in f:
                lines.append(line.strip())
            return lines

    def _create_examples(self, lines, set_type):
        """Creates examples for the training and dev sets."""
        examples = []
        if set_type == "pred":
            start = 0
        else:
            start = 1
        for (i, line) in enumerate(lines[start:]):
            line = line.split("\t")
            guid = "%s-%s" % (set_type, i)
            if set_type == "pred":
                text_a = line[1]
                label = line[0]
            else:
                if len(line) < 6:
                    continue
                text_a = line[5]
                label = line[1]
                if label not in self.labels:
                    continue
            if i % 1000 == 0:
                logger.info(line)
            examples.append(InputExample(guid=guid, text_a=text_a, text_b=None, label=label))
        return examples

    def get_examples(self, mode):
        """
        Args:
            mode: train, dev, test
        """
        file_to_read = None
        if mode == "train":
            file_to_read = self.args.train_file
        elif mode == "dev":
            file_to_read = self.args.dev_file
        elif mode == "test":
            file_to_read = self.args.test_file

        logger.info("LOOKING AT {}".format(os.path.join(self.args.data_dir, file_to_read)))
        return self._create_examples(
            self._read_file(os.path.join(self.args.data_dir, file_to_read)), mode
        )

    def make_examples(self, text):
        lines = [f"False\t{text}"]
        return self._create_examples(lines, "pred")


class EthClsProcessor(object):
    """Processor for the Korean Hate Speech data set """

    def __init__(self, args):
        self.args = args
        # multi
        self.labels = ['VIOLENCE','SEXUAL','ABUSE','DISCRIMINATION','IMMORAL_NONE']
        # multi4
        #self.labels = ['IMMORAL_NONE', 'CENSURE', 'HATE', 'DISCRIMINATION']
        # multi3
        #self.labels = ['IMMORAL_NONE', 'CENSURE', 'DISCRIMINATION']
        #self.labels = ['IMMORAL_NONE', 'HATE', 'DISCRIMINATION']
        #self.labels = ['IMMORAL_NONE', 'CENSURE', 'HATE']

    def get_labels(self):
        return self.labels

    def get_plabels(self):
        return ['폭력','선정','욕설','차별','정상']
        #return ['정상', '비난', '혐오', '차별']
        #return ['정상', '비난', '차별']
        #return ['정상', '혐오', '차별']
        #return ['정상', '비난', '혐오']

    @classmethod
    def _read_file(cls, input_file):
        """Reads a tab separated value file."""
        with open(input_file, "r", encoding="utf-8") as f:
            lines = []
            for line in f:
                lines.append(line.strip())
            return lines

    def _create_examples(self, lines, set_type):
        """Creates examples for the training and dev sets."""
        examples = []
        for (i, line) in enumerate(lines[1:]):
            line = line.split("\t")
            if len(line) < 6:
                continue
            guid = "%s-%s" % (set_type, i)
            text_a = line[5]
            label = line[4]
            if label not in self.labels:
                continue
            if i % 1000 == 0:
                logger.info(line)
            examples.append(InputExample(guid=guid, text_a=text_a, text_b=None, label=label))
        return examples

    def get_examples(self, mode):
        """
        Args:
            mode: train, dev, test
        """
        file_to_read = None
        if mode == "train":
            file_to_read = self.args.train_file
        elif mode == "dev":
            file_to_read = self.args.dev_file
        elif mode == "test":
            file_to_read = self.args.test_file

        logger.info("LOOKING AT {}".format(os.path.join(self.args.data_dir, self.args.task, file_to_read)))
        return self._create_examples(
            self._read_file(os.path.join(self.args.data_dir, file_to_read)), mode
        )

    def make_examples(self, text):
        lines = ["",f"0\t1\t2\t3\tIMMORAL_NONE\t{text}"]
        return self._create_examples(lines, "pred")


seq_cls_processors = {
    "class": EthicProcessor,
    "multi": EthClsProcessor,
    "kor": UnethicProcessor,
    "korm": UnethicMultiProcessor,
    "korm3": UnethicMultiProcessor,
    "korm3a": UnethicMultiProcessor,
    "korm3b": UnethicMultiProcessor,
    "korm4": UnethicMultiProcessor,
    "multi4": EthClsProcessor,
    "multi3": EthClsProcessor,
    "label": MultiProcessor,
}

seq_cls_tasks_num_labels = {
    "class": 2, 
    "multi": 5,
    "kor": 2,
    "korm": 4,
    "korm3": 3,
    "korm3a": 3,
    "korm3b": 3,
    "korm4": 3,
    "multi4": 4,
    "multi3": 3,
    "label": 2,
}

seq_cls_output_modes = {
    "class": "classification",
    "multi": "classification",
    "kor": "classification",
    "korm": "classification",
    "korm3": "classification",
    "korm3a": "classification",
    "korm3b": "classification",
    "korm4": "classification",
    "multi4": "classification",
    "multi3": "classification",
    "label": "classification",
}


def seq_cls_load_and_cache_examples(args, tokenizer, mode, line=None):
    processor = seq_cls_processors[args.task](args)
    output_mode = seq_cls_output_modes[args.task]
    # Load data features from cache or dataset file
    tasks = str(args.task)
    logger.info("Creating features from dataset file at %s", args.data_dir)
    if mode == "train":
        examples = processor.get_examples("train")
    elif mode == "dev":
        examples = processor.get_examples("dev")
    elif mode == "test":
        examples = processor.get_examples("test")
    elif mode == "predict":
        #label = processor.get_labels()
        #lines = [f"{label[0]}\t{line}"]
        #print(lines)
        examples = processor.make_examples(line)
        if len(examples) == 0:
            return None
    else:
        raise ValueError("For mode, only train, dev, test is avaiable")
    #print(examples)
    features = seq_cls_convert_examples_to_features(
        args, examples, tokenizer, max_length=args.max_seq_len, task=args.task
    )

    # Convert to Tensors and build dataset
    all_input_ids = torch.tensor([f.input_ids for f in features], dtype=torch.long)
    all_attention_mask = torch.tensor([f.attention_mask for f in features], dtype=torch.long)
    all_token_type_ids = torch.tensor([f.token_type_ids for f in features], dtype=torch.long)
    if output_mode == "classification":
        all_labels = torch.tensor([f.label for f in features], dtype=torch.long)
    elif output_mode == "regression":
        all_labels = torch.tensor([f.label for f in features], dtype=torch.float)

    dataset = TensorDataset(all_input_ids, all_attention_mask, all_token_type_ids, all_labels)
    return dataset
