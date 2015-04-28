You can find docs [here](http://docs.syncano.com/v4.0/docs/android).
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
