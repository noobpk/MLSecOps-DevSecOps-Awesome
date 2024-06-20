from flask import Flask, request, jsonify
import numpy as np
from keras.preprocessing.sequence import pad_sequences
from keras.models import load_model
import pickle
from waitress import serve

app = Flask(__name__)

# Load the saved model
model = load_model("text_classification_cnn_model.h5")

# Load the tokenizer and label encoder
with open("tokenizer.pickle", "rb") as handle:
    tokenizer = pickle.load(handle)

with open("label_encoder.pickle", "rb") as handle:
    encoder = pickle.load(handle)

max_len = 100  # Use the same max_len as in training


@app.route("/predict", methods=["POST"])
def predict():
    try:
        # Get the data from the POST request
        data = request.json
        text = data["text"]

        # Tokenize and pad the input text
        sequences = tokenizer.texts_to_sequences([text])
        padded_sequences = pad_sequences(sequences, maxlen=max_len)

        # Make a prediction
        prediction = model.predict(padded_sequences)
        predicted_label = encoder.inverse_transform(prediction)[0]

        # Return the prediction as a JSON response
        return jsonify({"prediction": predicted_label})

    except Exception as e:
        return jsonify({"error": str(e)})


if __name__ == "__main__":
    # Serve the application with Waitress
    serve(app, host="0.0.0.0", port=5000)
