# coding: utf-8

import random
import logging
from numpy.lib.function_base import average

import torch
import numpy as np
import os

from scipy.stats import pearsonr, spearmanr
from seqeval import metrics as seqeval_metrics
from sklearn import metrics as sklearn_metrics

from transformers import (
    BertConfig,
    ElectraConfig,
    BertTokenizer,
    ElectraTokenizer,
    BertForSequenceClassification,
    ElectraForSequenceClassification,
)

CONFIG_CLASSES = {
    "dbert": BertConfig,
    "delec": ElectraConfig,
}

TOKENIZER_CLASSES = {
    "dbert": BertTokenizer,
    "delec": ElectraTokenizer,
}

MODEL_FOR_SEQUENCE_CLASSIFICATION = {
    "dbert": BertForSequenceClassification,
    "delec": ElectraForSequenceClassification,
}


def init_logger():
    logging.basicConfig(
        format="%(asctime)s - %(levelname)s - %(name)s -   %(message)s",
        datefmt="%m/%d/%Y %H:%M:%S",
        level=logging.INFO,
    )


def set_seed(args):
    random.seed(args.seed)
    np.random.seed(args.seed)
    torch.manual_seed(args.seed)
    if not args.no_cuda and torch.cuda.is_available():
        torch.cuda.manual_seed_all(args.seed)