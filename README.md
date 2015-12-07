# Syncano Android Library

## Overview
---

Syncano's Android library is written in Java and provides communication with Syncano ([www.syncano.io](http://www.syncano.io/)) via HTTPS RESTful interface.

The full source code can be found on [Github](https://github.com/Syncano/syncano-android) - feel free to browse or contribute.

## Android QuickStart Guide
---

You can find quick start on installing and using Syncano's Android library in our [documentation](http://docs.syncano.com/docs/android).

For more detailed information on how to use Syncano and its features - our [Developer Manual](http://docs.syncano.com/docs/getting-started-with-syncano) should be very helpful.

In case you need help working with the library - email us at libraries@syncano.com - we will be happy to help!

## License
---

Syncano's Android library (syncano-android) is available under the MIT license. See the LICENSE file for more info.

Happy coding!

# Contributing

## JAR
To generate jar file call "makeJar" from command line:
```bash
$ ./gradlew makeJar
```

Output will be generated in:
```
library/build/outputs/jar/library-release-x.jar
```

##JAR DISTRIBUTION
If you want to use JAR in our project, you also need to add gson library.
Version **gson-2.5** is recommended.

## TESTS
To run tests call from command line:
```bash
$ ./gradlew connectedAndroidTest
```

To run tests, remember to provide correct Api Key and Instance Name in:
```
library/gradle.properties
```
or pass them in gradlew command:
```bash
$ ./gradlew connectedCheck -Pinstance_name="\"your_instance_name\"" -Papi_key="\"your_api_key\"" -Papi_key_users="\"your_api_key_for_registering users\""
```
