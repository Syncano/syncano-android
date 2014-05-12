# Syncano Android library

## Overview
---

Syncano's Android library is written in Java and provides communication with Syncano via HTTP RESTful interface and TCP sokects.

## Installation
---

Get [the full source code](https://github.com/Syncano/syncano-android) or [download the archive](https://github.com/Syncano/syncano-android/archive/master.zip)  from GitHub.

Then, add the library to an Eclipse IDE project using `File -> Import`. Make sure to choose `Android -> Existing Android Code Into Workspace`.

Under IDE, right click your project and choose `Properties`. Add Syncano's Android library by clicking `Add` buton in the `Library` section under the `Android` group.

## Required permissions
---

To use the library, you have to add following permissions to your `AndroidManifest.xml` file.

```	
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```
## Usage examples
---

Here are few examples of how to use Syncano's Android library.

### Create Syncano object instance

(This will be used to send requests to your instance of Syncano via HTTP interface. Pass your own Syncano domain here, along with your generated API Key):

```java
Syncano syncano = new Syncano(getContext(), Constants.INSTANCE_NAME, Constants.API_KEY);
```

### Create a new object with text

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

### Download a created object

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

### Remove an object

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

### Use the Sync Server

(This will be used to send and receive notifications from Syncano's Sync Server. You can also use it to send requests to your instance of Syncano via TCP interface. Pass your own Syncano domain here, along with your generated API Key):

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

