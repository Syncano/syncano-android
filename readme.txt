========== JAR ==========
To generate jar file call "makeJar" from command line:
./gradlew makeJar

Output will be generated in:
/library/build/outputs/jar/library-release-x.jar

========== JAR DISTRIBUTION ==========
If you want to use JAR in our project, you also weed to add gson library.
Version gson-2.3.1 is recommended.

========== TESTS ==========
To run tests call from command line:
./gradlew connectedAndroidTest

Remember to provide correct Api Key and Instance Name in:
/library/src/androidTest/java/com/syncano/library/Config.java