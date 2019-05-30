# upload-crashlytics-android
A tool that uses the Fabric Gradle plugin to upload an arbitrary APK to Fabric Beta

## Building

You can build the project with gradle:

```bash
./gradlew shadowJar
```

This will produce a standalone jar file `./build/libs/uploadcrashlyticsandroid-1.0-all.jar` that can be invoked on the command line.

## Usage

A command like follows:

```bash
java -jar 
  uploadcrashlyticsandroid-1.0-all.jar 
  --api-key XXXXX
  --build-secret YYYYY
  --apk-path app-release.apk
```

Will upload the specified APK to Fabric.

Additional options:

`--release-notes` - which will upload the specified text in plaintext format for the uploaded APK.

`--emails` - specify emails to invite to this version

`--groups` - specify groups to invite to this version

`--notify` - send notification
