package syncano.com.library;

import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class HttpClient {

    private String apiKey;
    private String authKey;

    public HttpClient(String apiKey, String authKey) {
        this.apiKey = apiKey;
        this.authKey = authKey;
    }

    protected HttpURLConnection prepareConnection(URL url, String requestMethod) throws IOException {
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(requestMethod);
        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("user-agent", "syncano-android-4.0");

        /* !!!!!!!!!!!!!!!!!!!!!!!!
        ToDo Check connection params in syncano 3.0 android library.
        */

        return connection;
    }

    // requestMethod - GET / POST / PUT / PATCH / DELETE
    // method - Syncano API method to call
    public void sendRequest(String requestMethod, String method /*, params*/)
    {
        Log.d("HttpClient", "Response: " + send(requestMethod, method));
    }

    private JsonElement send(String requestMethod, String method) {
        HttpURLConnection connection = null;

        try {
            connection = prepareConnection(new URL(Constatnts.SERVER_URL + method), requestMethod);

            // read answer
            InputStream is = null;
            is = connection.getInputStream();

            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer response = new StringBuffer();
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\n');
                }

            rd.close();
            JsonParser parser = new JsonParser();
            return parser.parse(response.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return  null;
    }
}
