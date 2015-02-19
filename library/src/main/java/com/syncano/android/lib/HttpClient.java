package com.syncano.android.lib;

import android.util.Log;
import com.google.gson.Gson;
import com.syncano.android.lib.api.Response;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import org.apache.http.params.CoreProtocolPNames;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.security.cert.X509Certificate;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.X509TrustManager;
import javax.net.ssl.HttpsURLConnection;
import java.util.LinkedHashMap;
import java.util.Map;

public class HttpClient {

    private static final String TAG = HttpClient.class.getSimpleName();
    private String apiKey;

    public HttpClient(String apiKey) {
        this.apiKey = apiKey;
    }

    public String sendRequest(String requestMethod, String url, Response response, LinkedHashMap<String, String> parameters)
    {
        // prepare connection to be sent
        HttpsURLConnection connection = prepareConnection(url, requestMethod, parameters);
        return receive(connection, response);
    }

    public String sendRequest(String requestMethod, String url, Response response)
    {
        // prepare connection to be sent
        HttpsURLConnection connection = prepareConnection(url, requestMethod, null);
        return receive(connection, response);
    }


    private HttpsURLConnection prepareConnection(String url, String requestMethod, LinkedHashMap<String, String> parameters) {

        // TODO change this to verify Syncano's certificate once it is provided.
        trustAllCertificates ();

        try {
            final HttpsURLConnection connection;

            URL destinationURL = new URL (url);
            connection = (HttpsURLConnection) destinationURL.openConnection();
            connection.setRequestMethod(requestMethod);
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Authorization", "Token " + apiKey);
            connection.setRequestProperty(CoreProtocolPNames.USER_AGENT, Constants.USER_AGENT);

            if (parameters != null)
            {
                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(getQuery(parameters));
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

    private String getQuery(LinkedHashMap<String, String> parameters)
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (Map.Entry<String, String> entry : parameters.entrySet())
        {
            if (first)
                first = false;
            else
                result.append("&");

            try
            {
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
        }

        return result.toString();
    }

    private String receive(HttpURLConnection connection, Response response)
    {
        try {
            // read answer
            int connectionResponseCode = connection.getResponseCode();
            response.setResultCode(connectionResponseCode);

            if (connectionResponseCode >= 200 && connectionResponseCode < 400)
            {
                return readResponse (connection.getInputStream());
            }
            else
            {
                response.setError(readResponse (connection.getErrorStream()));
                return null;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String readResponse(InputStream stream) throws IOException
    {
        StringBuffer responseString = new StringBuffer();

        BufferedReader rd = new BufferedReader(new InputStreamReader(stream));
        String line;
        while ((line = rd.readLine()) != null) {
            responseString.append(line);
            responseString.append('\n');
        }
        rd.close();

        return responseString.toString();
    }

    // Temporary solution. It has to be changed to verify Syncano's certificate instead of accepting the cert without verifying.
    private void trustAllCertificates () {
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
