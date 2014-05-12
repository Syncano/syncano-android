# Syncano Android library

## Overview
---

syncano-android is a library written in Java that provides communication with Syncano via HTTP RESTful interface and TCP sokects.

Its full source code can be found on [GitHub](https://github.com/Syncano/syncano-android). 

## Installation:
---

Get source code from [Syncano Android Library on GitHub](https://github.com/Syncano/syncano-android) or [download the archive from GitHub](https://github.com/Syncano/syncano-android/archive/master.zip).

Then, add the library to your project under Eclipse IDE using `File -> Import` and choose `Android -> Existing Android Code Into Workspace`.

Right click you project under IDE and choose `Properties`. Under `Android` group, you can add our library using `Add` button in `Library` section.

That's it, now you can use it in your own project!

## Required permissions
---

To use the library, you have to add following permissions to your `AndroidManifest.xml` file.

```	
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```
## Usage examples
---

Here are few examples of how to use android-syncano library.

#### Get syncano object instance

(This will be used to send requests to your instance of Syncano via HTTP interface. Pass your own Syncano domain here, along with your generated API Key):

```
Syncano syncano = new Syncano(getContext(), Constants.INSTANCE_NAME, Constants.API_KEY);
```

#### Create a new object with text content

(Use a project ID and collection ID from your admin GUI, i.e. YourDomain.syncano.com, for the below. You can use the “default” values or create your own.):

```
ParamsDataNew newObject = new ParamsDataNew(Constants.PROJECT_ID, Constants.COLLECTION_ID, null, "Moderated");
String text = "Test content";
newObject.setText(text);
ResponseDataNew responseNew = syncano.dataNew(newObject);
if(responseNew.getResultCode() == Response.CODE_SUCCESS) {
	// data object successfully created
}
```

#### Download created object

```
String createdObjectId = responseNew.getData().getId();
ParamsDataGet getObject = new ParamsDataGet(Constants.PROJECT_ID, Constants.COLLECTION_ID, null);
getObject.setDataIds(new String[] {
	createdObjectId
});
ResponseDataGet responseGet = syncano.dataGet(getObject);
if(responseGet.getResultCode() == Response.CODE_SUCCESS) {
	// response success
}
if(text.equals(responseGet.getData()[0].getText())) {
	// downloaded item has the same text content as sent before
}
```

#### Remove an object

```
ParamsDataDelete delete = new ParamsDataDelete(Constants.PROJECT_ID, Constants.COLLECTION_ID, null);
delete.setDataIds(new String[] {
	createdObjectId
});
Response responseDelete = syncano.dataDelete(delete);
if(responseDelete.getResultCode() == Response.CODE_SUCCESS) {
	// item successfully deleted
}
```

#### Sync Server usage example

(This will be used to send and receive notifications from Syncano Sync Server. You can also use it to send requests to your instance of Syncano via TCP interface. Pass your own Syncano domain here, along with your generated API Key):

```
SyncServerConnection conn = new SyncServerConnection(getContext(), Constants.INSTANCE_NAME, Constants.API_KEY,
	new SyncServerListener() {
		@Override
		public void message(String object, String message) {
			Log.d(TAG, "message " + object + " " + message);
		}

		@Override
		public void error(String why) {
			Log.d(TAG, "error: " + why);
		}

		@Override
		public void disconnected() {
			Log.d(TAG, "disconnected");
		}

		@Override
		public void connected() {
			Log.d(TAG, "connected");
		}
	}, new SubscriptionListener() {
		@Override
		public void deleted(int[] ids) {
			Log.d(TAG, "deleted " + ids);
		}

		@Override
		public void changed(ArrayList<DataChanges> changes) {
			Log.d(TAG, "changed");
		}

		@Override
		public void added(Data data) {
			Log.d(TAG, "added");
		}

});

conn.start();
```

