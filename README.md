# Syncano Android Library

## Overview
---

Syncano's Android library is written in pure Java and provides communication with Syncano ([www.syncano.io](http://www.syncano.io/?utm_source=github&utm_medium=readme&utm_campaign=syncano-android)) via HTTPS RESTful interface.

## Android QuickStart Guide
---
Syncano library is now available on jCenter. To install it, add one line inside your project's gradle dependencies and you will be able to use it out of the box without the `jar` file.

```json
dependencies {
  compile 'io.syncano:library:4.0.5'
}
```

You can find quick start on installing and using Syncano's Android library in our [documentation](http://docs.syncano.com/docs/android/?utm_source=github&utm_medium=readme&utm_campaign=syncano-android).

For more detailed information on how to use Syncano and its features - our [Developer Manual](http://docs.syncano.com/docs/getting-started-with-syncano/?utm_source=github&utm_medium=readme&utm_campaign=syncano-android) should be very helpful.

In case you need help working with the library - email us at [libraries@syncano.com](mailto:libraries@syncano.com) - we're always happy to help!

## Contributing
We love contributions! Those who want to help us improve our Android library -- contribute to code, documentation, adding tests or making any other improvements -- please [create a Pull Request] (https://github.com/Syncano/syncano-android/pulls) with proposed changes.

Happy coding!

## Use library inside a pure Java project without Android
Syncano library is written in pure Java, so you can use it in your Spring or Java SE project. 
The only difference is that you have to provide two additional libraries: `Apache HTTP` and `Bouncycastle`.

```json
dependencies {
  compile 'io.syncano:library:4.0.5'
  compile 'org.apache.httpcomponents:httpclient:4.0.1'
  compile 'org.apache.httpcomponents:httpcore:4.0.1'
  compile 'org.bouncycastle:bcprov-jdk15on:1.53'
}
```

##JAR DISTRIBUTION
If you want to use JAR file in your project, you need to add gson library.
We recommend using version **gson-2.5**.

## JAR
To generate a jar file, call "makeJar" from command line:

```bash
$ ./gradlew makeJar
```

Output will be generated in:
```
library/build/outputs/jar/library-release-x.jar
```

## TESTS
To run tests, call from a command line:

```bash
$ ./gradlew testDebug --continue --info
```

Before running tests, remember to provide a correct API Key and your Instance Name in:

```json
library/gradle.properties
```

or pass them in `gradlew` command:

```bash
$ ./gradlew testDebug --continue --info -Pinstance_name="\"YOUR_INSTANCE_NAME\"" -Papi_key="\"YOUR_API_KEY\"" -Papi_key_users="\"YOUR_API_KEY_FOR_RESGISTERING_USERS\""
```

## License
---
Syncano's Android library is available under the MIT license. 
See the [LICENSE](https://github.com/Syncano/syncano-android/blob/master/LICENSE) file for more info.
