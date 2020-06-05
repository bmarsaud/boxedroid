# boxedroid

[![Build status](https://github.com/bmarsaud/boxedroid/workflows/CI%20Maven%20build/badge.svg)](https://github.com/bmarsaud/boxedroid/actions?query=workflow%3A%22CI+Maven+build%22)
[![Code coverage percentage](https://codecov.io/gh/bmarsaud/boxedroid/branch/master/graph/badge.svg)](https://codecov.io/gh/bmarsaud/boxedroid/branch/master)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=boxedroid&metric=alert_status)](https://sonarcloud.io/dashboard?id=boxedroid)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=boxedroid&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=boxedroid)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=boxedroid&metric=security_rating)](https://sonarcloud.io/dashboard?id=boxedroid)

*Work In Progress project!*

:package: boxedroid is a cross-plateform packaging of the official Android SDK emulator.

## Why boxedroid ?
Dealing with Android emulators has always been a tricky task for an end-user.
You ends up using a weird software from an obscure company that integrates an odd software overlay, trackers and under-average mobile games that you will never launch.

If you are a company and your clients need to use an emulator for certain tasks, recommending one of these software can... hurt your brand image.

From a developer perspective, dealing with emulators is way easier. You just launch the official Android emulator from the Android SDK tools and that's it!
But, you probably know that what is easy for a tech guy, is definitely not for the ordinary mortal.

That's why boxedroid comes in! boxedroid provides a user-friendly an cross-platform way to launch the official Android emulator from the Android SDK.

## Usage
At the moment boxedroid is only available as a CLI.

You can execute it using Java 8+ with the following command :

```bash
java -jar boxedroid.jar --sdk <SDK_PATH> --android <ANDROID_VERSION> [options]
```

|Option|Description|Available values|
|---|---|---|
|`--sdk` (required)|The absolute path to the Android SDK||
|`--android` (required)|The Android version of the emulator|`5`, `5.0`, `5.1`, `5.1.1`, `6`, `6.0`, `7`, `7.0`, `7.1`, `7.1.1`, `7.1.2`, `8`, `8.0`, `8.1`, `9`, `9.0`, `10`, `10.0`|
|`--abi`|The emulator architecture|`x86` (default), `x86_64`, `arm`, `arm_64`|
|`--variant`|The image variant|`google_apis` (default), `default`, `google_apis_playstore`, `android_tv`, `android_wear`, `android_wear_cn`|
|`--device`|The emulator device name|`pixel_2` (default), `pixel_3`, `Nexus 8`, `Nexus 9`, `Nexus 10` and way more according to your installation|