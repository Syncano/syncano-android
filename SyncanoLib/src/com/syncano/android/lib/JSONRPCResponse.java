package com.syncano.android.lib;

import java.lang.reflect.Type;

import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;
import com.syncano.android.lib.modules.Response;
import com.syncano.android.lib.modules.administrators.ResponseAdminGet;
import com.syncano.android.lib.modules.administrators.ResponseAdminGetOne;
import com.syncano.android.lib.modules.administrators.ResponseAdminUpdate;
import com.syncano.android.lib.modules.apikeys.ResponseApiKeyGet;
import com.syncano.android.lib.modules.apikeys.ResponseApiKeyGetOne;
import com.syncano.android.lib.modules.apikeys.ResponseApiKeyNew;
import com.syncano.android.lib.modules.apikeys.ResponseApiKeyStartSession;
import com.syncano.android.lib.modules.apikeys.ResponseApiKeyUpdateDescription;
import com.syncano.android.lib.modules.collections.ResponseCollectionGet;
import com.syncano.android.lib.modules.collections.ResponseCollectionGetOne;
import com.syncano.android.lib.modules.collections.ResponseCollectionNew;
import com.syncano.android.lib.modules.collections.ResponseCollectionUpdate;
import com.syncano.android.lib.modules.data.ResponseDataCopy;
import com.syncano.android.lib.modules.data.ResponseDataCount;
import com.syncano.android.lib.modules.data.ResponseDataGet;
import com.syncano.android.lib.modules.data.ResponseDataGetOne;
import com.syncano.android.lib.modules.data.ResponseDataNew;
import com.syncano.android.lib.modules.data.ResponseDataUpdate;
import com.syncano.android.lib.modules.folders.ResponseFolderGet;
import com.syncano.android.lib.modules.folders.ResponseFolderGetOne;
import com.syncano.android.lib.modules.folders.ResponseFolderNew;
import com.syncano.android.lib.modules.identities.ResponseIdentityGet;
import com.syncano.android.lib.modules.identities.ResponseIdentityUpdate;
import com.syncano.android.lib.modules.projects.ResponseProjectGet;
import com.syncano.android.lib.modules.projects.ResponseProjectGetOne;
import com.syncano.android.lib.modules.projects.ResponseProjectNew;
import com.syncano.android.lib.modules.projects.ResponseProjectUpdate;
import com.syncano.android.lib.modules.roles.ResponseRoleGet;
import com.syncano.android.lib.modules.subscriptions.ResponseSubscriptionGet;
import com.syncano.android.lib.modules.users.ResponseUserCount;
import com.syncano.android.lib.modules.users.ResponseUserGet;
import com.syncano.android.lib.modules.users.ResponseUserGetAll;
import com.syncano.android.lib.modules.users.ResponseUserGetOne;
import com.syncano.android.lib.modules.users.ResponseUserLogin;
import com.syncano.android.lib.modules.users.ResponseUserNew;
import com.syncano.android.lib.modules.users.ResponseUserUpdate;

public class JSONRPCResponse<T extends Response> {
	@Expose
	String jsonrpc;
	@Expose
	T result;
	@Expose
	String id;

	// All responses listed. Important for gson parser to properly create response type.
	public static Type getTypeForParser(Class<? extends Response> clazz) {
		if (Response.class.equals(clazz)) {
			return (new TypeToken<JSONRPCResponse<Response>>() {
			}).getType();
		} else if (ResponseAdminGet.class.equals(clazz)) {
			return (new TypeToken<JSONRPCResponse<ResponseAdminGet>>() {
			}).getType();
		} else if (ResponseAdminGetOne.class.equals(clazz)) {
			return (new TypeToken<JSONRPCResponse<ResponseAdminGetOne>>() {
			}).getType();
		}  else if (ResponseAdminUpdate.class.equals(clazz)) {
			return (new TypeToken<JSONRPCResponse<ResponseAdminUpdate>>() {
			}).getType();
		} else if (ResponseApiKeyGet.class.equals(clazz)) {
			return (new TypeToken<JSONRPCResponse<ResponseApiKeyGet>>() {
			}).getType();
		} else if (ResponseApiKeyGetOne.class.equals(clazz)) {
			return (new TypeToken<JSONRPCResponse<ResponseApiKeyGetOne>>() {
			}).getType();
		} else if (ResponseApiKeyNew.class.equals(clazz)) {
			return (new TypeToken<JSONRPCResponse<ResponseApiKeyNew>>() {
			}).getType();
		} else if (ResponseApiKeyStartSession.class.equals(clazz)) {
			return (new TypeToken<JSONRPCResponse<ResponseApiKeyStartSession>>() {
			}).getType();
		} else if (ResponseApiKeyUpdateDescription.class.equals(clazz)) {
			return (new TypeToken<JSONRPCResponse<ResponseApiKeyUpdateDescription>>() {
			}).getType();
		} else if (ResponseCollectionGet.class.equals(clazz)) {
			return (new TypeToken<JSONRPCResponse<ResponseCollectionGet>>() {
			}).getType();
		} else if (ResponseCollectionGetOne.class.equals(clazz)) {
			return (new TypeToken<JSONRPCResponse<ResponseCollectionGetOne>>() {
			}).getType();
		} else if (ResponseCollectionNew.class.equals(clazz)) {
			return (new TypeToken<JSONRPCResponse<ResponseCollectionNew>>() {
			}).getType();
		} else if (ResponseCollectionUpdate.class.equals(clazz)) {
			return (new TypeToken<JSONRPCResponse<ResponseCollectionUpdate>>() {
			}).getType();
		} else if (ResponseDataCopy.class.equals(clazz)) {
			return (new TypeToken<JSONRPCResponse<ResponseDataCopy>>() {
			}).getType();
		} else if (ResponseDataCount.class.equals(clazz)) {
			return (new TypeToken<JSONRPCResponse<ResponseDataCount>>() {
			}).getType();
		} else if (ResponseDataGet.class.equals(clazz)) {
			return (new TypeToken<JSONRPCResponse<ResponseDataGet>>() {
			}).getType();
		} else if (ResponseDataGetOne.class.equals(clazz)) {
			return (new TypeToken<JSONRPCResponse<ResponseDataGetOne>>() {
			}).getType();
		} else if (ResponseDataNew.class.equals(clazz)) {
			return (new TypeToken<JSONRPCResponse<ResponseDataNew>>() {
			}).getType();
		} else if (ResponseDataUpdate.class.equals(clazz)) {
			return (new TypeToken<JSONRPCResponse<ResponseDataUpdate>>() {
			}).getType();
		} else if (ResponseFolderGet.class.equals(clazz)) {
			return (new TypeToken<JSONRPCResponse<ResponseFolderGet>>() {
			}).getType();
		} else if (ResponseFolderGetOne.class.equals(clazz)) {
			return (new TypeToken<JSONRPCResponse<ResponseFolderGetOne>>() {
			}).getType();
		} else if (ResponseFolderNew.class.equals(clazz)) {
			return (new TypeToken<JSONRPCResponse<ResponseFolderNew>>() {
			}).getType();
		} else if (ResponseIdentityGet.class.equals(clazz)) {
			return (new TypeToken<JSONRPCResponse<ResponseIdentityGet>>() {
			}).getType();
		} else if (ResponseIdentityUpdate.class.equals(clazz)) {
			return (new TypeToken<JSONRPCResponse<ResponseIdentityUpdate>>() {
			}).getType();
		} else if (ResponseProjectGet.class.equals(clazz)) {
			return (new TypeToken<JSONRPCResponse<ResponseProjectGet>>() {
			}).getType();
		} else if (ResponseProjectGetOne.class.equals(clazz)) {
			return (new TypeToken<JSONRPCResponse<ResponseProjectGetOne>>() {
			}).getType();
		} else if (ResponseProjectNew.class.equals(clazz)) {
			return (new TypeToken<JSONRPCResponse<ResponseProjectNew>>() {
			}).getType();
		} else if (ResponseProjectUpdate.class.equals(clazz)) {
			return (new TypeToken<JSONRPCResponse<ResponseProjectUpdate>>() {
			}).getType();
		} else if (ResponseRoleGet.class.equals(clazz)) {
			return (new TypeToken<JSONRPCResponse<ResponseRoleGet>>() {
			}).getType();
		} else if (ResponseSubscriptionGet.class.equals(clazz)) {
			return (new TypeToken<JSONRPCResponse<ResponseSubscriptionGet>>() {
			}).getType();
		} else if (ResponseUserLogin.class.equals(clazz)) {
			return (new TypeToken<JSONRPCResponse<ResponseUserLogin>>() {
			}).getType();
		} else if (ResponseUserCount.class.equals(clazz)) {
			return (new TypeToken<JSONRPCResponse<ResponseUserCount>>() {
			}).getType();
		} else if (ResponseUserGet.class.equals(clazz)) {
			return (new TypeToken<JSONRPCResponse<ResponseUserGet>>() {
			}).getType();
		} else if (ResponseUserGetAll.class.equals(clazz)) {
			return (new TypeToken<JSONRPCResponse<ResponseUserGetAll>>() {
			}).getType();
		} else if (ResponseUserGetOne.class.equals(clazz)) {
			return (new TypeToken<JSONRPCResponse<ResponseUserGetOne>>() {
			}).getType();
		} else if (ResponseUserNew.class.equals(clazz)) {
			return (new TypeToken<JSONRPCResponse<ResponseUserNew>>() {
			}).getType();
		} else if (ResponseUserUpdate.class.equals(clazz)) {
			return (new TypeToken<JSONRPCResponse<ResponseUserUpdate>>() {
			}).getType();
		}
		return (new TypeToken<JSONRPCResponse<Response>>() {
		}).getType();
	}
}
