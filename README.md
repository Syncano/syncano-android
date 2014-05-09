# Syncano Android library

## Installation:
---

Get source code from [Syncano Android Library on GitHub](https://github.com/Syncano/syncano-android) and connect as an Android Library to your project.

## Required permissions
---

	<uses-permission android:name="android.permission.INTERNET" />
	<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

## Usage examples
---

##### Get syncano object instance
	Syncano syncano = new Syncano(getContext(), Constants.INSTANCE_NAME, Constants.API_KEY);

##### Create a new object with text content
	ParamsDataNew newObject = new ParamsDataNew(Constants.PROJECT_ID, Constants.COLLECTION_ID, null, "Moderated");
	String text = "Test content";
	newObject.setText(text);
	ResponseDataNew responseNew = syncano.dataNew(newObject);
	if(responseNew.getResultCode() == Response.CODE_SUCCESS) {
		// data object successfully created
	}

##### Download created object
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

##### Remove an object
	ParamsDataDelete delete = new ParamsDataDelete(Constants.PROJECT_ID, Constants.COLLECTION_ID, null);
	delete.setDataIds(new String[] {
		createdObjectId
	});
	Response responseDelete = syncano.dataDelete(delete);
	if(responseDelete.getResultCode() == Response.CODE_SUCCESS) {
		// item successfully deleted
	}

##### Sync Server usage example
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

