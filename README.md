# DroidMind OS 🧠
### The Ultimate On-Device AI Operating Layer for Android (API 31+)

[![Continuous Integration](https://github.com/abdulraheemnohri/DroidMind-OS/actions/workflows/ci.yml/badge.svg)](https://github.com/abdulraheemnohri/DroidMind-OS/actions/workflows/ci.yml)
[![Android CI & Release](https://github.com/abdulraheemnohri/DroidMind-OS/actions/workflows/android.yml/badge.svg)](https://github.com/abdulraheemnohri/DroidMind-OS/actions/workflows/android.yml)
[![CodeQL Security Scan](https://github.com/abdulraheemnohri/DroidMind-OS/actions/workflows/codeql.yml/badge.svg)](https://github.com/abdulraheemnohri/DroidMind-OS/actions/workflows/codeql.yml)
![Version](https://img.shields.io/github/v/release/abdulraheemnohri/DroidMind-OS)
![License](https://img.shields.io/github/license/abdulraheemnohri/DroidMind-OS)

DroidMind OS is a privacy-first, autonomous cognitive layer for Android 12+. It transforms your device into an intelligent, self-optimizing system by replacing cloud-based dependencies with high-performance, on-device AI models.

---

## 🚀 Complete Feature Highlights

### 🛡️ Privacy & Behavioral Security (PrivacyShield)
- **Deep Log Analysis:** Monitors system logs (`READ_LOGS`) to detect background data exfiltration, credential leaks, or suspicious API calls in real-time.
- **AI Clipboard Sanitizer:** Automatically identifies and masks sensitive data like credit card numbers, passwords, or SSNs when copied.
- **Decoy Mode:** Allows users to switch to a "guest" state with obfuscated data for temporary privacy.
- **Permission Anomaly Detection:** Flags apps that request high-risk permissions (e.g., Microphone/Location) without a clear user-facing utility.

### 🚫 Advanced Ad & Tracker Blocking (AdShield)
- **Network-Level Interception:** Operates a local DNS-filtering VPN that drops ad/tracker requests before they leave the device.
- **Aggressiveness Profiles:** Choose between Minimal (basic ads), Standard (balanced), and Maximum (paranoid tracker blocking).
- **CNN-Based Detection:** A local neural network identifies obfuscated ad domains that bypass traditional hosts-based lists.
- **WebView Cosmetic Filtering:** Injects scripts into browser views to remove empty ad placeholders and banners.

### 🔋 Intelligent Power Management (PowerForge)
- **Predictive Hibernation:** Learns your usage cycles to hibernate "energy-vampire" apps before they drain the battery.
- **Stepped Adaptive Charging:** Controls power inflow based on temperature and usage patterns to maximize battery longevity.
- **Granular Drain Analysis:** Uses `BATTERY_STATS` to provide a human-readable breakdown of exactly which system component or app is consuming power.

### 🧠 Cognitive Engine (AI Core)
- **Usage Prediction:** A local LSTM model predicts your next app launch to pre-warm resources and optimize RAM.
- **On-Device LLM:** Integration with **SmolLM2-360M** (GGUF) for secure notification summarization, smart replies, and NL automation.
- **Anomaly Detection:** An Autoencoder-based monitor that learns "normal" device behavior and alerts you to hardware or network irregularities.

---

## ⚙️ How It Works (Working Mechanism)

DroidMind OS functions as a "Reasoning Layer" between the Android framework and the user:

1. **Signal Collection:** The `ContextEngine` aggregates data from motion sensors, FusedLocation, battery state, and active network interfaces.
2. **Vectorization:** These signals are converted into high-dimensional vectors and stored in the `VectorMemory`.
3. **Pattern Matching:** The system continuously compares the current vector against historical "Golden Patterns" using Cosine Similarity.
4. **Autonomous Decision:** When a pattern matches (e.g., "Arriving at Office"), the `ConfidenceGateway` evaluates if the predicted action (e.g., "Enable FocusFlow") meets the user-defined autonomy threshold.
5. **Secure Execution:** Authorized actions are executed across modules, while all data remains encrypted via Android Keystore (AES/GCM) and SQLCipher.

---

## ⚡ Setup & First-Run Flow

### 📦 Installation
1. Download the latest signed APK from the [Releases](https://github.com/abdulraheemnohri/DroidMind-OS/releases) page.
2. Install on an Android 12+ (API 31) device.
3. Upon launch, grant the necessary permissions (Accessibility, VPN, and Usage Stats) to enable core features.

### 🤖 AI Model Initialization
DroidMind OS is designed for offline operation. On the first run:
1. The `ModelManager` checks if the core AI assets exist in local storage.
2. If missing, it fetches the authorized models (SmolLM2, MobileNetV3, etc.) from the official registry.
3. Once downloaded, the system enters **Strictly Offline Mode**, where no AI data ever leaves the device.

---

## 🛠 Project Architecture

Built with a strict **Clean Architecture (MVVM)** approach across 17 modules:
- **`:app`:** UI Orchestration and Launcher.
- **`:core`:** AI Engine, Contextual Reasoning, and Vector Persistence.
- **`:modules`:** Independent feature logic (AdShield, PrivacyShield, PowerForge).
- **`:services`:** Hardware-level integrations (VPN, Accessibility, Shizuku Bridge).
- **`:ui`:** Premium Material 3 components with Glassmorphic design.

---

## 🛠 CI/CD & DevOps
- **Auto-Releases:** Every push to `main` triggers a GitHub Action that increments the version (v0.1.0 -> v0.2.0) and publishes a signed APK.
- **Security Audits:** Automated CodeQL scanning and Dependency Review.
- **AI-Powered Review:** Every PR is vetted by an LLM-based coding agent for logic and security.

## 📦 Download
Get the production blueprint from the [Releases](https://github.com/abdulraheemnohri/DroidMind-OS/releases) page.
