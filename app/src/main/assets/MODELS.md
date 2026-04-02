# DroidMind OS v1.4 - AI Models Registry

This file contains the registry and direct download links for the on-device AI models used in DroidMind OS.

## 1. On-Device LLM (GGUF)
- **Model:** SmolLM2-360M-Instruct Q4_K_M
- **File:** `SmolLM2-360M-Instruct-Q4_K_M.gguf`
- **Link:** [Download from Hugging Face](https://huggingface.co/bartowski/SmolLM2-360M-Instruct-GGUF/resolve/main/SmolLM2-360M-Instruct-Q4_K_M.gguf)
- **Size:** ~271 MB
- **Use Case:** Summarization & Replies

## 2. Usage Classifier (TFLite)
- **Model:** TFLite Recommendation Model
- **File:** `recommendation.tflite`
- **Link:** [Download from Google Storage](https://storage.googleapis.com/download.tensorflow.org/models/tflite/task_library/recommendation/android/recommendation.tflite)
- **Size:** ~2-5 MB
- **Use Case:** Next app prediction

## 3. Anomaly Detector (Autoencoder TFLite)
- **Model:** TFLite Anomaly Detection
- **File:** `anomaly_detection.tflite`
- **Link:** [Download from Google Storage](https://storage.googleapis.com/download.tensorflow.org/models/tflite/anomaly_detection/model.tflite)
- **Size:** ~1-3 MB
- **Use Case:** Abnormal network/permission detection

## 4. Image Classifier (MobileNet TFLite)
- **Model:** MobileNetV3 small
- **File:** `mobilenet_v3_small_100_224.tflite`
- **Link:** [Download from Google Storage](https://storage.googleapis.com/download.tensorflow.org/models/tflite/task_library/image_classification/android/mobilenet_v3_small_100_224.tflite)
- **Size:** ~4 MB
- **Use Case:** Screenshot privacy & categorization

## 5. Ad Detection (Object Detection CNN)
- **Model:** SSD MobileNet V2
- **File:** `ssd_mobilenet_v2.tflite`
- **Link:** [Download from Google Storage](https://storage.googleapis.com/download.tensorflow.org/models/tflite/task_library/object_detection/android/ssd_mobilenet_v2.tflite)
- **Size:** ~17 MB
- **Use Case:** Banner & Popup ad detection
