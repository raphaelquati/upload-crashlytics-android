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
  --app-name Myapp
  --package-name uk.lyptt.Myapp
  --version 1.0
  --build-version 1
```

Will upload the specified APK to Fabric.

You can also specify a `--release-notes` option, which will upload the specified text in markdown format for the uploaded APK.
