package com.syncano.android.lib.syncserver;

import android.content.Context;
import android.util.Log;


import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import com.syncano.android.lib.Constants;
import com.syncano.android.lib.utils.SimpleHttpClient;

public class SocketCreator {

	private final static String LOG_TAG = SocketCreator.class.getSimpleName();

	/**
	 * Method to create trusted socket
	 * 
	 * @param ctx
	 *            context
	 * @return new socket secured with ssl
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public static Socket createSocket(Context ctx) throws UnknownHostException, IOException {
		TrustManagerFactory trustManagerFactory;
		try {
			trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			trustManagerFactory.init(getKeyStore(ctx));

			// get context
			SSLContext sslContext = SSLContext.getInstance("TLS");

			// init context
			sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
			Socket sslsocket = sslContext.getSocketFactory().createSocket(Constants.SOCKET_ADDRESS,
					Constants.SOCKET_PORT);
			return sslsocket;
		} catch (NoSuchAlgorithmException e) {
			Log.e(LOG_TAG, "No such algorithm :" + e.toString());
		} catch (KeyManagementException e) {
			Log.e(LOG_TAG, "Key management problem :" + e.toString());
		} catch (KeyStoreException e) {
			Log.e(LOG_TAG, "Key store error :" + e.toString());
		}
		return null;

	}

	/**
	 * Gets keystore to secure socket
	 * 
	 * @param ctx
	 *            context
	 * @return KeyStore with certificates
	 */
	private static final KeyStore getKeyStore(Context ctx) {
		return SimpleHttpClient.getSslKey(ctx);
	}
}
