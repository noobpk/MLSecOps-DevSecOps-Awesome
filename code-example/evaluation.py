import numpy as np
import pandas as pd
from keras.preprocessing.sequence import pad_sequences
from keras.models import load_model
from sklearn.preprocessing import LabelBinarizer

# Load evaluation dataset from CSV file
eval_data = pd.read_csv('/tmp/dataset/dataset-evaluation.csv')

# Assuming the CSV file has columns named 'Sentence' and 'Label'
eval_texts = eval_data['Sentence'].astype(str).tolist()
eval_labels = eval_data['Label'].astype(str).tolist()

# Load the tokenizer and label encoder
import pickle
with open('/tmp/model/tokenizer.pickle', 'rb') as handle:
    tokenizer = pickle.load(handle)

with open('/tmp/model/label_encoder.pickle', 'rb') as handle:
    encoder = pickle.load(handle)

# Tokenize the text (using the same tokenizer fitted on the training data)
eval_sequences = tokenizer.texts_to_sequences(eval_texts)

# Pad sequences
max_len = 100  # Use the same max_len as in training
eval_data = pad_sequences(eval_sequences, maxlen=max_len)

# Convert labels to binary format (using the same encoder fitted on the training data)
eval_labels = encoder.transform(eval_labels)

# Load the saved model
model = load_model('/tmp/model/text_classification_cnn_model.h5')

# Evaluate the model on the new dataset
loss, accuracy = model.evaluate(eval_data, eval_labels)
print(f'Evaluation Accuracy: {accuracy:.4f}')
