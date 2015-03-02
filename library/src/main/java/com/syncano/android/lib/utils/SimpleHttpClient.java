package com.syncano.android.lib.utils;

import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
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

import com.syncano.android.lib.BuildConfig;
import com.syncano.android.lib.Constants;
import com.syncano.android.lib.api.SyncanoException;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class SimpleHttpClient {
	private final static String LOG_TAG = SimpleHttpClient.class.getSimpleName();

    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";
    public static final String METHOD_PUT = "PUT";
    public static final String METHOD_PATCH = "PATCH";
    public static final String METHOD_DELETE = "DELETE";

	/** Timeout value */
	private final static int NOT_SET = -1;
	/** Recommended timeout value */
	private static final int TIMEOUT = 30000;
	/** Default socket timeout */
	private static final int SOCKET_TIMEOUT = 60000;
	/** Url where every call is sent */
	private String mUrl;
	/** Connection timeout value */
	private int mTimeout = NOT_SET;
	/** Post data */
	private StringEntity mPostData = null;
	/** Http client used to connect to API */
	private DefaultHttpClient mHttpclient;

	/**
	 * Default constructor

	 * @param url
	 *            Url where every call is sent
	 */
	SimpleHttpClient(String url) {
		mUrl = url;
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
	 * Sets post data which is sent later by send() method.
     * May be applied for POST, PUT, PATCH.
	 * 
	 * @param text
	 *            Text to send
	 */
	public void setPostData(String text) {
        if (text == null)
            return;

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
	 * @throws java.io.IOException
	 */
	public InputStream send(String apiKey, String requestMethod) throws IOException, SyncanoException {
		HttpUriRequest request;

        if (METHOD_GET.equals(requestMethod)) {
            request = new HttpGet(mUrl);
        } else if (METHOD_POST.equals(requestMethod)) {
            HttpPost httpPost = new HttpPost(mUrl);
            if (mPostData != null) {
                httpPost.setEntity(mPostData);
            }
            request = httpPost;
        } else if (METHOD_PUT.equals(requestMethod)) {
            HttpPut httpPut = new HttpPut(mUrl);
            if (mPostData != null) {
                httpPut.setEntity(mPostData);
            }
            request = httpPut;
        } else if (METHOD_PATCH.equals(requestMethod)) {
            HttpPatch httpPatch = new HttpPatch(mUrl);
            if (mPostData != null) {
                httpPatch.setEntity(mPostData);
            }
            request = httpPatch;
        } else if (METHOD_DELETE.equals(requestMethod)) {
            request = new HttpDelete(mUrl);
        } else {
            request = new HttpGet(mUrl);
        }

		request.setHeader("Content-Type", "application/json");
		request.setHeader("Accept-Encoding", "gzip");
        request.setHeader("Authorization", "Token " + apiKey);
		mHttpclient = getHttpClient();
		mHttpclient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, Constants.USER_AGENT);
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

            if (BuildConfig.DEBUG) {
                Log.d(LOG_TAG, "HTTP Response: " + response.getStatusLine().getStatusCode() + "  " + response.getStatusLine().getReasonPhrase());
            }

            if (response.getStatusLine().getStatusCode() >= 200 && response.getStatusLine().getStatusCode() < 400) {
                // For some requests it may be null (for example: DELETE).
                if (response.getEntity() != null) {
                    is = response.getEntity().getContent();
                    Header contentEncoding = response.getFirstHeader("Content-Encoding");
                    if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
                        is = new GZIPInputStream(is);
                    }
                }
            } else {
                SyncanoException exception = new SyncanoException();
                exception.setResultCode(SyncanoException.CODE_HTTP_ERROR);
                exception.setError("Http error occurred.");
                exception.setHttpResultCode(response.getStatusLine().getStatusCode());
                exception.setHttpError(response.getStatusLine().getReasonPhrase());
                throw exception;
            }
		} catch (IllegalStateException e) {
			Log.w(LOG_TAG, "IllegalStateException");
		}
		return is;
	}

	/**
	 * Method to get new http client with socket factory which allows to connect only to selected domains
     *
	 * @return new http client
	 */
	private static DefaultHttpClient getHttpClient() {
		try {
			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, "utf-8");
			HttpConnectionParams.setConnectionTimeout(params, TIMEOUT);
			HttpConnectionParams.setSoTimeout(params, SOCKET_TIMEOUT);
			params.setBooleanParameter("http.protocol.expect-continue", false);

			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			registry.register(new Scheme("https", newSslSocketFactory(), 443));

			ThreadSafeClientConnManager manager = new ThreadSafeClientConnManager(params, registry);
			return new DefaultHttpClient(manager, params);
		} catch (Exception e) {
			return new DefaultHttpClient();
		}
	}

	/**
	 * Method returns trusted socket factory with hostname verifying
	 *
	 * @return new trusted ssl factory
	 */
	private static SSLSocketFactory newSslSocketFactory() {
		try {
            KeyStore trusted = KeyStore.getInstance(KeyStore.getDefaultType());
            trusted.load(null, null);  //TODO Temporary solution. It has to be changed to verify Syncano's certificate instead of accepting the cert without verifying.

			SSLSocketFactory sf = new MySSLSocketFactory(trusted);
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
				Log.e(LOG_TAG, "Error while shutting down http client: " + e.toString());
			}
		}

	}

    //TODO Temporary solution. It has to be changed to verify Syncano's certificate instead of accepting the cert without verifying.
    public static class MySSLSocketFactory extends SSLSocketFactory {
        SSLContext sslContext = SSLContext.getInstance("SSL");

        public MySSLSocketFactory(KeyStore trustStore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
            super(trustStore);

            TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };

            sslContext.init(null, new TrustManager[] { tm }, null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }
    }
}
