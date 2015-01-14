# Syncano Android library

## Overview
---

Syncano's Android library is written in Java and provides communication with Syncano via HTTP RESTful interface and TCP sokects.

Click here to learn more about [Syncano](http://www.syncano.com) or [create an account!](https://login.syncano.com/sign_up)

## Installation
---

You can obtain Syncano’s Android Library in two forms:

1. Git repository
2. Maven repository

Below you can find instructions on how to install the library on Eclipse and Android Studio.

## Eclipse
---

If you don’t have Eclipse with Android SDK plugin already, you can grab it from [Android website.](http://developer.android.com/sdk/index.html)

Create New Android Project
=====================

1. Open Eclipse IDE
2. In Eclipse menu select **File -> New -> Android Application Project**
3. Type Application name, project name and package name. For the example below, we will use **SyncanoProject** as app and project name, and **com.syncano.syncanoproject** for package name
4. You don’t have to change anything in the SDK settings, so press **Next** button
5. It’s not needed to change anything on this page as well, so press **Next** again
6. You can choose custom graphics for your project, for this example app, we will just leave it as it is
7. In the **Create Activity** screen choose one matching your needs, or leave it on **Blank Activity**
8. Name created activity and the layout, or leave the default values and press **Finish**


Add syncano-android from the source code
=================================

1. Download (or clone) the source code from [GitHub](https://github.com/Syncano/syncano-android)
2. Import project into Eclipse
    1. In the Eclipse menu choose **File -> Import -> Android -> Existing Android Code Into Workspace**
    2. You should see **Browse** button next to the **Root Directory** text field . Click it, and navigate into library’s folder, making sure you choose **SyncanoLib** directory. Press **Open** to apply
    3. Right click on your project in the package explorer in left menu
    4. Choose **Properties**
    5. In the window find **Android preferences**
    6. Click **Add** button in the Library area and choose **SyncanoLib** project
    7. Press **OK** to apply changes

Our android library is now added to your project and you can freely use it inside your application!

## Android Studio
---

If you don’t have Android Studio already, you can find the newest version [here](http://developer.android.com/sdk/index.html)

Create New Android Project
=====================

1. Open Android Studio IDE
2. Choose **File -> New Project...**
3. Type in application name and company domain and press **Next**. For examples below, we’ll use **SyncanoProject** as an app name, and **com.syncano** as company domain
4. Choose proper platform and SDK for your project, we’ll jus stick with **Phone and Tablet** and selected by default SDK version, then click **Next**
5. Select an activity. We’ll use **Blank activity** for the example project
6. Name your activity and layout or leave the default ones and press **Finish**

Add syncano-android from Maven
==========================

1. Make sure your project is open
2. In the navigation menu on the left find **build.gradle** file and double-click it to open. You should remember not to use main .gradle file, but the one for the app, so in our example it would be in the here: **SyncanoProject -> app -> build.gradle**
3. Add this code:
        repositories {
          mavenCentral()
        }
or if **repositories** block is already present in the file, just add:
        mavenCentral()
inside the **repositories** block
4.  Add the following line under dependencies to get the newest version of our library
        compile 'com.syncano:SyncanoLib:latest.release@aar'
5. Now your **build.gradle** file should look more less like this:
        apply plugin: 'com.android.application'

        android {
          compileSdkVersion 19
          buildToolsVersion '20.0.0'

          defaultConfig {
            applicationId "syncano.com.syncanoproject"
            minSdkVersion 15
            targetSdkVersion 19
            versionCode 1
            versionName "1.0"
          }

          buildTypes {
            release {
              runProguard false
              proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
            }
          }
        }

        dependencies {
          compile fileTree(dir: 'libs', include: ['*.jar'])
          compile 'com.syncano:SyncanoLib:1.0.0@aar'
        }

        repositories {
          mavenCentral()
        }
6. Sync your project with gradle files. To do it, choose from menu **Tools -> Android -> Sync Project with Gradle Files**.

That’s it. Now you can use Syncano library in your project.

Add syncano-android from the source code
=================================

1. Download or clone source code from our GitHub repository
2. In IDE right click on the project and choose **Open Module Settings**
3. Click **+** button on the left, choose **Import Existing Project** and press **Next**
4. Click the **...** button on the right to the empty text field and navigate into Syncano’s Android library directory, making sure you choose **SyncanoLib** folder and press **OK**, and then **Finish**
5. You should be now back in module settings page. Choose module that should use our library (by default it would be **app** module), choose **Dependencies** tab on the top, click the **+** button and choose **Module dependency**.
6. Choose **SyncanoLib** and press **OK**
7. Choose **OK** again to close **Module Settings** page and apply changes

### Next Steps
---

Further instructions and tips apply regardless of whether you are using Eclipse or Android Studio:

Permissions
=========

You'll need to add certain permissions to your project settings. Using menu on the left navigate to **AndroidManifest.xml** file. 
+ If project was created in **Eclipse**, it should be visible when you unfold your project
+ In **Android Studio**, it should be in **app -> src -> main**. 

Open the file and paste these two lines inside:

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

Importing Syncano Library
====================

While writing the code, when you use Syncano objects, IDE will suggest proper modules to be imported in your file. In general everything is placed under **com.syncano.android.lib.*** package, but you should rely on IDE suggestions to provide proper imports.

Using Syncano Library
=================

**Initializing Syncano Objects**

+ Enter **.java class** file to which you want to add Syncano support.
+ Add **Syncano** and **SyncServer** fields to your class, by pasting:

```Java
private Syncano syncano;
private SyncServerConnection syncServer;
```

+ Inside **onCreate** method initialize Syncano objects by adding:

```Java
syncano = new Syncano(this, "YOUR INSTANCE", "YOUR API KEY");
        // Next to passing your instance name and your API Key, you have to pass two listeners
        // as well
syncServer = new SyncServerConnection(this, "YOUR INSTANCE", "YOUR API KEY",
                // This listener will be notified when connection state with Sync Server will be
                // changed or when notification message was received
                new SyncServerConnection.SyncServerListener() {
                    @Override
                    public void onMessage(String object, JsonObject message) {
                        Log.d("SyncServerTag", "Received notification message: " + message);
                    }

                    @Override
                    public void onError(String why) {
                        Log.d("Error",why);
                    }

                    @Override
                    public void onDisconnected() {
                        Log.d("SyncServer","Disconnected");
                    }

                    @Override
                    public void onConnected() {
                        Log.d("SyncServer","Connected");
                    }
                    // This listener will be notified when new subscription event will be received.
                    // It can be either newly created object, info about the change in existing one,
                    // or deletion info
                }, new SyncServerConnection.SubscriptionListener() {
            @Override
            public void onDeleted(String[] ids, Channel channel) {
            }

            @Override
            public void onChanged(ArrayList<DataChanges> changes, Channel channel) {
            }

            @Override
            public void onAdded(Data data, Channel channel) {
            }
        });
```

Please also remember to start the SyncServer!

```Java
syncServer.start();
```

**Creating Data Object**

+ Add a new method that will create a new data object in Syncano:

```Java
private void sendData() {
  // You have to pass project id and collection id, which you can obtain e.g. from Admin GUI.
  // Instead of collection id, you use collection key, which would be passed instead of null.
  final ParamsDataNew paramsDataNew = new ParamsDataNew("PROJECT_ID", "COLLECTION_ID", null, Data.PENDING);
  // Sending parameters to Syncano backend
  syncano.sendAsyncRequest(paramsDataNew, new SyncanoBase.Callback() {
    // Handling response from the server
    @Override
    public void finished(Response response) {
      Data data = ((ResponseDataNew) response).getData();
      if (response.getResultCode() != Response.CODE_SUCCESS) {
        // Handle error gracefully
      } else {
        // Handle create object, you can use 'data' object now
      }
    }
  });
}
```

**Downloading newest Data Object**

Add following method to your class implementation:

```Java
private void getData() {
  // Create data.get() params object, passing project and collection id
  final ParamsDataGet paramsDataGet = new ParamsDataGet("PROJECT_ID"," COLLECTION_ID", null);
  // Get only one data object, since we want only the newest one
  paramsDataGet.setLimit(1);
  // Set order to descending to download the newest one (default is ASC, which would download oldest one)
  paramsDataGet.setOrder("DESC");
  // Send params to Syncano server
  syncano.sendAsyncRequest(paramsDataGet, new SyncanoBase.Callback() {
    // Response handling
    @Override
    public void finished(Response response) {
      Data[] data = ((ResponseDataGet) response).getData();
      if (response.getResultCode() != Response.CODE_SUCCESS) {
        // Error handling code
      } else if (data.length == 0) {
        // There was no data to be downloaded from server
      } else {
        // Received response, you can use 'data' object now
      }
    }
  });
}
```

**Deleting selected Data Object**

Add a function to delete desired data object. You can obtain ID of an object to be deleted through Admin GUI, or using ID of previously downloaded object.

```Java
// As a parameter functions takes ID of an object you want to delete
private void deleteData(final String dataId) {
  // Create parameters object to be sent to Syncano and define project and collection id
  final ParamsDataDelete paramsDataDelete = new ParamsDataDelete("PROJECT_ID", "COLLECTION_ID", null);
  // You can delete all objects, or pass ID of an object to be deleted
  paramsDataDelete.setDataIds(new String[]{dataId});
  // Pass object parameters for deleting an object to Syncano
  syncano.sendAsyncRequest(paramsDataDelete, new SyncanoBase.Callback() {
    // Handle the response
    @Override
    public void finished(Response response) {
      if (response.getResultCode() != Response.CODE_SUCCESS) {
        // Handle error
      }  else {
        // Handle success response, object(s) was(were) properly deleted
      }
    }
  });
}
```

**Receiving notifications**

+ When you crated **syncServer** object in **onCreate** method, you passed two listeners to it. One of them, **SyncServerListener**, implements method:

```Java
public void onMessage(String object, JsonObject message) {
}
```


+ To handle receiving notifications, you have to put code that handles received notification, To i.e. log it, just this one line inside:

```Java
Log.d("SyncServerTag", "Received notification message: " + message);
```

+ It should now log all the messages incoming through Sync Server. Here’s how a complete method looks like:

```Java
public void onMessage(String object, JsonObject message) {
  Log.d("SyncServerTag", "Received notification message: " + message);
}
```


**Sending notifications**

Add this method to send notifications:

```Java
private  void sendNotification() {
  // Create parameters object and fill it with notification content
  ParamsNotificationSend paramsNotificationSend = new ParamsNotificationSend(null);
  String messageToBeSent = "Some example message";
  paramsNotificationSend.addParam("message", messageToBeSent);
  // Send notification by passing created parameters to Sync Server
  syncServer.call(paramsNotificationSend, new SyncServerConnection.SyncServerCallback() {
    // Handle the response
    @Override
    public void result(Response response) {
      if (response.getResultCode() != Response.CODE_SUCCESS) {
        // Handle the error
      } else {
        // Handle success message, notification was sent to server
      }
    }
  });
}
```

Support
======

Now you’re ready to use Syncano in your Android project. We really hope you will enjoy working with our platform. If you have any issues, just let us know at [support@syncano.com](support@syncano.com)

License
======

Syncano’s Android Library (syncano-android) is available under the MIT license. See the LICENSE file inside library sources for more info.

