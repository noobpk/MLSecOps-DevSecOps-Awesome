import numpy as np
import pandas as pd
from keras.preprocessing.sequence import pad_sequences
from keras.models import load_model
from sklearn.preprocessing import LabelBinarizer
from keras.utils import to_categorical

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

# Convert labels to one-hot encoding (ensure consistency with training)
# Get the list of original classes from the encoder
original_classes = encoder.classes_

# Map evaluation labels to integers based on the original classes
label_map = {label: idx for idx, label in enumerate(original_classes)}
eval_labels_int = [label_map[label] for label in eval_labels if label in label_map]

# Convert to one-hot encoding
eval_labels_one_hot = to_categorical(eval_labels_int, num_classes=len(original_classes))

# Load the saved model
model = load_model('/tmp/model/text_classification_cnn_model.h5')

# Evaluate the model on the new dataset
loss, accuracy = model.evaluate(eval_data, eval_labels_one_hot)
print(f'Evaluation Accuracy: {accuracy:.4f}')
