# Syncano Android Library

## Overview
---

Syncano's Android library is written in Java and provides communication with Syncano ([www.syncano.io](http://www.syncano.io/?utm_source=github&utm_medium=readme&utm_campaign=syncano-android)) via HTTPS RESTful interface.

The full source code can be found on [Github](https://github.com/Syncano/syncano-android) - feel free to browse or contribute.

## Android QuickStart Guide
---

You can find quick start on installing and using Syncano's Android library in our [documentation](http://docs.syncano.com/docs/android/?utm_source=github&utm_medium=readme&utm_campaign=syncano-android).

For more detailed information on how to use Syncano and its features - our [Developer Manual](http://docs.syncano.com/docs/getting-started-with-syncano/?utm_source=github&utm_medium=readme&utm_campaign=syncano-android) should be very helpful.

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
/library/build/outputs/jar/library-release-x.jar
```

##JAR DISTRIBUTION
If you want to use JAR in our project, you also weed to add gson library.
Version **gson-2.3.1** is recommended.

## TESTS
To run tests call from command line:
```bash
$ ./gradlew connectedAndroidTest
```

Remember to provide correct Api Key and Instance Name in:
```
/library/src/androidTest/java/com/syncano/library/Config.java
```

## TESTS COVERAGE
To generate test coverage call "createDebugCoverageReport" from command line:
```bash
$ ./gradlew createDebugCoverageReport
```

Coverage report will be located in:
```
/library/build/outputs/reports/coverage/
```
