# Syncano Android Library

## Overview
---

Syncano's Android library is written in pure Java and provides communication with Syncano ([www.syncano.io](http://www.syncano.io/)) via HTTPS RESTful interface.


## Android QuickStart Guide
---
Syncano library is now avaiable on Jcenter just add one line inside your project gradle dependencies and you can use it out of box without Jar
```
dependencies {
  compile 'io.syncano:library:4.0.5'
}
```

You can find quick start on installing and using Syncano's Android library in our [documentation](http://docs.syncano.com/docs/android).

For more detailed information on how to use Syncano and its features - our [Developer Manual](http://docs.syncano.com/docs/getting-started-with-syncano) should be very helpful.

In case you need help working with the library - email us at libraries@syncano.com - we will be happy to help!

## Contributing
We love contributions with want create better Syncano with us. If you'd like to contribute code, documentation, add test or any other improvements, please [create a Pull Request] (https://github.com/Syncano/syncano-android/pulls) on our GitHub repository.
Happy coding!

## Use library inside your pure Java project without Android
Syncano library as mentioned above is written in pure java so you can use it in your Spring or Java SE project. The only difference is you should provide two library Apache HTTP and Bouncycastle.
```
dependencies {
  compile 'io.syncano:library:4.0.5'
  compile 'org.apache.httpcomponents:httpclient:4.0.1'
  compile 'org.apache.httpcomponents:httpcore:4.0.1'
  compile 'org.bouncycastle:bcprov-jdk15on:1.53'
}
```

##JAR DISTRIBUTION
If you want to use JAR in our project, you also need to add gson library.
Version **gson-2.5** is recommended.

## JAR
To generate jar file call "makeJar" from command line:
```bash
$ ./gradlew makeJar
```

Output will be generated in:
```
library/build/outputs/jar/library-release-x.jar
```

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


## License
---
Syncano's Android library is available under the MIT license. 
See the [LICENSE](https://github.com/Syncano/syncano-android/blob/master/LICENSE) file for more info.
