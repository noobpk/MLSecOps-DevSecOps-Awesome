import numpy as np
import pandas as pd
from keras.preprocessing.text import Tokenizer
from keras.preprocessing.sequence import pad_sequences
from keras.models import Sequential
from keras.layers import Embedding, Conv1D, GlobalMaxPooling1D, Dense, Dropout
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import LabelBinarizer

# Load dataset from CSV file
data = pd.read_csv("/tmp/dataset/dataset-train.csv")

# Assuming the CSV file has columns named 'Sentence' and 'Label'
texts = data["Sentence"].astype(str).tolist()
labels = data["Label"].astype(str).tolist()

# Tokenize the text
max_words = 10000
tokenizer = Tokenizer(num_words=max_words)
tokenizer.fit_on_texts(texts)
sequences = tokenizer.texts_to_sequences(texts)

# Pad sequences
max_len = 100
data = pad_sequences(sequences, maxlen=max_len)

# Convert labels to binary format
encoder = LabelBinarizer()
labels = encoder.fit_transform(labels)

# Split the data into training and test sets
X_train, X_test, y_train, y_test = train_test_split(
    data, labels, test_size=0.2, random_state=42
)

# Create the CNN model
embedding_dim = 100

model = Sequential()
model.add(
    Embedding(input_dim=max_words, output_dim=embedding_dim, input_length=max_len)
)
model.add(Conv1D(filters=128, kernel_size=5, activation="relu"))
model.add(GlobalMaxPooling1D())
model.add(Dense(128, activation="relu"))
model.add(Dropout(0.5))
model.add(Dense(len(encoder.classes_), activation="softmax"))

# Compile the model
model.compile(optimizer="adam", loss="categorical_crossentropy", metrics=["accuracy"])

# Train the model
history = model.fit(X_train, y_train, epochs=2, batch_size=32, validation_split=0.2)

# Save the model
model.save("/tmp/model/text_classification_cnn_model.h5")

# Save the tokenizer and label encoder
import pickle
with open('/tmp/model/tokenizer.pickle', 'wb') as handle:
    pickle.dump(tokenizer, handle, protocol=pickle.HIGHEST_PROTOCOL)

with open('/tmp/model/label_encoder.pickle', 'wb') as handle:
    pickle.dump(encoder, handle, protocol=pickle.HIGHEST_PROTOCOL)

print("DONE - MODEL SAVED")