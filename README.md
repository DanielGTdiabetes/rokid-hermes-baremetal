# Rokid Hermes Bare Metal

Native Android Kotlin MVP for Rokid Glasses running Android 12. The app talks directly to Hermes over the local network without AIUI, Lingzhu, CXR, Bluetooth, camera, microphone, or an iPhone bridge.

## Project purpose

`rokid-hermes-baremetal` is a minimal bare-metal Android APK for validating direct Hermes connectivity from Rokid Glasses. The MVP provides one screen and one health-check button that calls the local Hermes endpoint.

## Build instructions

Requirements:

- JDK 17 or newer
- Android SDK with platform 35 installed
- Gradle 8.x or newer, or run with an IDE-provided Gradle installation

Build the debug APK:

```bash
gradle :app:assembleDebug
```

## Expected APK path

```text
app/build/outputs/apk/debug/app-debug.apk
```

## Install on Rokid Glasses

```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

## Launch on Rokid Glasses

```bash
adb shell monkey -p com.intek.rokidhermes 1
```

## Test endpoint

```text
http://192.168.0.234:8080/health
```

Tap **Probar Hermes** in the app. A successful HTTP 200 response shows **Hermes conectado**. Any other HTTP response or network failure shows a clear error message.

## Current MVP scope

- Native Kotlin Android app
- Package: `com.intek.rokidhermes`
- Minimum SDK: 29
- Target SDK: 35
- 480x480-friendly black UI
- Direct HTTP GET to Hermes health endpoint
- INTERNET permission and cleartext HTTP enabled for local testing
- Logcat tag: `RokidHermes`

## Next milestones

- MVP-02 Nightscout current glucose
- MVP-03 Hermes text query
- MVP-04 microphone input
- MVP-05 camera/OCR
