package com.syncano.android.lib.utils;

import android.content.Context;
import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import com.syncano.android.lib.Constants;
import com.syncano.android.lib.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.util.zip.GZIPInputStream;

public class SimpleHttpClient {
	private final static String LOG_TAG = SimpleHttpClient.class.getSimpleName();
	/** Default user agent name */
	private final static String USER_AGENT = "syncano-android-" + Constants.VERSION_NAME;
	/** Timeout value */
	private final static int NOT_SET = -1;
	/** Recommended timeout value */
	private static final int TIMEOUT = 30000;
	/** Default socket timeout */
	private static final int SOCKET_TIMEOUT = 60000;
	/** Url where every call is sent */
	private String mUrl;
	/** Context for http client */
	private Context mCtx;
	/** Connection timeout value */
	private int mTimeout = NOT_SET;
	/** Post data */
	private StringEntity mPostData = null;
	/** Http client used to connect to API */
	private DefaultHttpClient mHttpclient;

	/**
	 * Default constructor
	 * 
	 * @param ctx
	 *            Context
	 * @param url
	 *            Url where every call is sent
	 */
	SimpleHttpClient(Context ctx, String url) {
		mUrl = url;
		mCtx = ctx;
	}

	/**
	 * Sets new timeout value
	 * 
	 * @param millis
	 *            timeout value in milliseconds
	 */
	public void setTimeout(int millis) {
		mTimeout = millis;
	}

	/**
	 * Sets post data which is sent later by send() method
	 * 
	 * @param text
	 *            Text to send
	 */
	public void setPostData(String text) {
		try {
			mPostData = new StringEntity(text, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			Log.e(LOG_TAG, "Unsupported encoding: " + e.toString());
		}
	}

	/**
	 * Method to send post data contained in mPostData field
	 * 
	 * @return input stream with response
	 * @throws IOException
	 */
	public InputStream send() throws IOException {
		HttpUriRequest request;
		if (mPostData != null) {
			HttpPost httppost = new HttpPost(mUrl);
			httppost.setEntity(mPostData);
			request = httppost;
		} else {
			request = new HttpGet(mUrl);
		}
		request.setHeader("Content-Type", "application/json-rpc");
		request.setHeader("Accept-Encoding", "gzip");
		mHttpclient = getHttpClient(mCtx);
		mHttpclient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, USER_AGENT);
		HttpParams httpParameters = mHttpclient.getParams();
		if (mTimeout != NOT_SET) {
			// Set the timeout in milliseconds until a connection is established.
			// The default value is zero, that means the timeout is not used.
			HttpConnectionParams.setConnectionTimeout(httpParameters, mTimeout * 1000);
			// Set the default socket timeout (SO_TIMEOUT)
			// in milliseconds which is the timeout for waiting for data.
			HttpConnectionParams.setSoTimeout(httpParameters, mTimeout * 1000);
		}

		HttpResponse response = null;
		try {
			response = mHttpclient.execute(request);
		} catch (ClientProtocolException e) {
			Log.w(LOG_TAG, "ClientProtocolException");
		}
		if (response == null) return null;

		InputStream is = null;
		try {
			is = response.getEntity().getContent();
			Header contentEncoding = response.getFirstHeader("Content-Encoding");
			if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
				is = new GZIPInputStream(is);
			}
		} catch (IllegalStateException e) {
			Log.w(LOG_TAG, "IllegalStateException");
		}
		return is;
	}

	/**
	 * Method to get new http client with socket factory which allows to connect only to selected domains
	 * 
	 * @param ctx
	 *            Context needed to create http client
	 * @return new http client
	 */
	private static DefaultHttpClient getHttpClient(Context ctx) {
		try {
			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, "utf-8");
			HttpConnectionParams.setConnectionTimeout(params, TIMEOUT);
			HttpConnectionParams.setSoTimeout(params, SOCKET_TIMEOUT);
			params.setBooleanParameter("http.protocol.expect-continue", false);

			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			registry.register(new Scheme("https", newSslSocketFactory(ctx), 443));

			ThreadSafeClientConnManager manager = new ThreadSafeClientConnManager(params, registry);
			return new DefaultHttpClient(manager, params);
		} catch (Exception e) {
			return new DefaultHttpClient();
		}
	}

	/**
	 * Method to load existing ssl key needed to authorize every call
	 * 
	 * @param ctx
	 *            context needed to load ssl key
	 * @return KeyStore with ssl keys
	 */
	public static KeyStore getSslKey(Context ctx) {
		try {
			KeyStore trusted = KeyStore.getInstance("BKS");
			// Get the raw resource, which contains the keystore with
			// your trusted certificates (root and any intermediate certs)

			InputStream in;
			in = ctx.getResources().openRawResource(R.raw.syncano);

			try {
				// Initialize the keystore with the provided trusted certificates
				// Also provide the password of the keystore
				trusted.load(in, "appnrollhydra".toCharArray());
			} finally {
				in.close();
			}
			return trusted;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Method returns trusted socket factory with hostname verifying
	 * 
	 * @param ctx
	 *            context needed to load ssl key
	 * @return new trusted ssl factory
	 */
	private static SSLSocketFactory newSslSocketFactory(Context ctx) {
		try {
			// Get an instance of the Bouncy Castle KeyStore format
			KeyStore trusted = getSslKey(ctx);
			// Pass the keystore to the SSLSocketFactory. The factory is responsible
			// for the verification of the server certificate.
			SSLSocketFactory sf = new SSLSocketFactory(trusted);
			// Hostname verification from certificate
			// http://hc.apache.org/httpcomponents-client-ga/tutorial/html/connmgmt.html#d4e506
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
//			sf.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
			return sf;
		} catch (Exception e) {
			throw new AssertionError(e);
		}
	}

	/**
	 * Method to close connection with server
	 */
	public void close() {
		if (mHttpclient != null) {
			try {
				mHttpclient.getConnectionManager().shutdown();
			} catch (Exception e) {
				Log.e(LOG_TAG, "Error while shuttting down http client: " + e.toString());
			}
		}

	}

}
