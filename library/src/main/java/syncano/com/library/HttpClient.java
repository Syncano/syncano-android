package syncano.com.library;

import android.util.Log;
import com.google.gson.Gson;

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

    private String apiKey;

    public HttpClient(String apiKey) {
        this.apiKey = apiKey;
    }

    // requestMethod - GET / POST / PUT / PATCH / DELETE
    // method - Syncano API method to call
    public <T> T sendRequest(String requestMethod, String method,Class<T> classOfT, LinkedHashMap<String, String> parameters)
    {
        // prepare connection to be sent
        HttpsURLConnection connection = prepareConnection(Constatnts.SERVER_URL + method, requestMethod, parameters);
        T response = receive(connection, classOfT);

       return response;
    }

    public <T> T sendRequest(String requestMethod, String method,Class<T> classOfT)
    {
        // prepare connection to be sent
        HttpsURLConnection connection = prepareConnection(Constatnts.SERVER_URL + method, requestMethod, null);
        T response = receive(connection, classOfT);

        return response;
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
            connection.setRequestProperty(CoreProtocolPNames.USER_AGENT, Constatnts.USER_AGENT);

            if (parameters != null)
            {
                /*
                for (Map.Entry<String, String> entry : parameters.entrySet())
                {
                    if (entry.getKey().isEmpty() == false) {
                        connection.setRequestProperty(entry.getKey(), entry.getValue());
                    }
                }*/


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

    private <T> T receive (HttpURLConnection connection, Class<T> classOfT)
    {
        StringBuffer response = new StringBuffer();
        try {
            // read answer
            int connectionResponseCode = connection.getResponseCode();

            if (connectionResponseCode >= 200 && connectionResponseCode < 400)
            {
                InputStream is = connection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\n');
                }
                rd.close();
            }

            else
            {
                InputStream is = connection.getErrorStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\n');
                }
                rd.close();
                Log.e("HttpClient", response.toString());
                return null;
            }


        }
        catch (IOException e) {
            e.printStackTrace();
        }

       return new Gson().fromJson(response.toString(), classOfT);
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
