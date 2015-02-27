package com.syncano.android.lib;

import android.util.Log;

import com.syncano.android.lib.api.SyncanoException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;

import org.apache.http.params.CoreProtocolPNames;
import java.net.HttpURLConnection;
import java.security.cert.X509Certificate;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.X509TrustManager;
import javax.net.ssl.HttpsURLConnection;

public class HttpClient {

    private static final String TAG = HttpClient.class.getSimpleName();

    public String sendRequest(String apiKey, String requestMethod, String url, String jsonParameters) throws SyncanoException {
        // prepare connection to be sent
        HttpsURLConnection connection = prepareConnection(apiKey, url, requestMethod, jsonParameters);
        return receive(connection);
    }

    public String sendRequest(String apiKey, String requestMethod, String url) throws SyncanoException {
        // prepare connection to be sent
        HttpsURLConnection connection = prepareConnection(apiKey, url, requestMethod, null);
        return receive(connection);
    }


    private HttpsURLConnection prepareConnection(String apiKey, String url, String requestMethod, String jsonParameters) {

        // TODO change this to verify Syncano's certificate once it is provided.
        trustAllCertificates ();

        try {
            final HttpsURLConnection connection;

            if (BuildConfig.DEBUG) {
                Log.d(TAG, "Request Method: " + requestMethod + " URL: " + url);
                Log.d(TAG, "Request Params: " + jsonParameters);
            }

            URL destinationURL = new URL (url);
            connection = (HttpsURLConnection) destinationURL.openConnection();
            connection.setRequestMethod(requestMethod);
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Token " + apiKey);
            connection.setRequestProperty(CoreProtocolPNames.USER_AGENT, Constants.USER_AGENT);

            if (jsonParameters != null)
            {
                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(jsonParameters);
                writer.flush();
                writer.close();

                os.close();
            }

            return connection;
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String receive(HttpURLConnection connection) throws SyncanoException {
        try {
            // read answer
            int connectionResponseCode = connection.getResponseCode();

            if (connectionResponseCode >= 200 && connectionResponseCode < 400)
            {
                return readResponse (connection.getInputStream());
            }
            else
            {
                SyncanoException exception = new SyncanoException();
                exception.setResultCode(SyncanoException.CODE_HTTP_ERROR);
                exception.setError("Http error occurred.");
                exception.setHttpResultCode(connectionResponseCode);
                exception.setHttpError(readResponse(connection.getErrorStream()));
                throw exception;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String readResponse(InputStream stream) throws IOException {
        StringBuffer responseString = new StringBuffer();

        BufferedReader rd = new BufferedReader(new InputStreamReader(stream));
        String line;
        while ((line = rd.readLine()) != null) {
            responseString.append(line);
            responseString.append('\n');
        }
        rd.close();

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Response: " + responseString.toString());
        }

        return responseString.toString();
    }

    // Temporary solution. It has to be changed to verify Syncano's certificate instead of accepting the cert without verifying.
    private void trustAllCertificates() {
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }

    // Temporary solution. It has to be removed when Syncano provides valid SSL certificate.
    private TrustManager[] trustAllCerts;
    {
        trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};
    }

    // Temporary solution. It has to be removed when Syncano provides valid SSL certificate. Create all-trusting host name verifier.
    private HostnameVerifier allHostsValid;
    {
        allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
    }
}
