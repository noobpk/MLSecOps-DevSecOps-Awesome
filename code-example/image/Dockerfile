FROM ubuntu:18.04

ENV AUTH_KEY=
ENV ENABLE_KAFKA_STREAMING=
ENV KAFKA_BOOTSTRAP_SERVER=
ENV KAFKA_TOPIC=
ENV KAFKA_USERNAME=
ENV KAFKA_PASSWORD=
ENV KAFKA_SECURITY_PROTOCOL=

# Install Nginx & openssl
RUN apt-get update -q \
    && apt-get install --no-install-recommends -qy \
    nginx \
    openssl

RUN apt-get update -q \
  && apt-get install --no-install-recommends -qy \
    curl \
    software-properties-common \
    build-essential \
    libssl-dev \
    zlib1g-dev \
    libncurses5-dev \
    libncursesw5-dev \
    libreadline-dev \
    libsqlite3-dev \
    libgdbm-dev \
    libdb5.3-dev \
    libbz2-dev \
    libexpat1-dev \
    liblzma-dev \
    libffi-dev \
    && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Download and install Python 3.9 from source
RUN curl -O https://www.python.org/ftp/python/3.9.7/Python-3.9.7.tgz \
    && tar -xzf Python-3.9.7.tgz \
    && cd Python-3.9.7 \
    && ./configure --enable-optimizations \
    && make -j "$(nproc)" \
    && make install \
    && cd .. \
    && rm -rf Python-3.9.7 Python-3.9.7.tgz

# Set Python 3.9 as the default python3
RUN update-alternatives --install /usr/bin/python3 python3 /usr/local/bin/python3.9 1

# Install pip for Python 3.9
RUN curl https://bootstrap.pypa.io/get-pip.py -o get-pip.py \
    && python3.9 get-pip.py \
    && rm get-pip.py

# Clean up
RUN apt-get purge -y \
    curl \
    build-essential \
    libssl-dev \
    zlib1g-dev \
    libncurses5-dev \
    libncursesw5-dev \
    libreadline-dev \
    libsqlite3-dev \
    libgdbm-dev \
    libdb5.3-dev \
    libbz2-dev \
    libexpat1-dev \
    liblzma-dev \
    libffi-dev \
    && apt-get autoremove -y

# Set working directory
WORKDIR /api

COPY requirements.txt .

# Install Python dependencies
RUN pip3 --no-cache-dir install -r requirements.txt --no-cache-dir

# Generate self-signed certificate for Nginx (if needed)
RUN openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout /etc/nginx/cert.key -out /etc/nginx/cert.crt -subj "/CN=127.0.0.1"

# Configure Nginx
RUN rm /etc/nginx/sites-enabled/default
COPY nginx.conf /etc/nginx/sites-enabled/

# Expose port 443 for the Flask API
EXPOSE 443

COPY app.py .

COPY text_classification_cnn_model.h5 .
COPY tokenizer.pickle .
COPY label_encoder.pickle .

# Run the Flask API using Waitress and Nginx
CMD service nginx start && python3 app.py
