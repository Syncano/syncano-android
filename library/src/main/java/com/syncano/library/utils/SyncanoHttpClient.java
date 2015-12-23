package com.syncano.library.utils;

import com.syncano.library.BuildConfig;
import com.syncano.library.Constants;
import com.syncano.library.api.HttpRequest;
import com.syncano.library.api.Response;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

public class SyncanoHttpClient {
    private final static String LOG_TAG = SyncanoHttpClient.class.getSimpleName();

    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";
    public static final String METHOD_PUT = "PUT";
    public static final String METHOD_PATCH = "PATCH";
    public static final String METHOD_DELETE = "DELETE";

    /**
     * Timeout value
     */
    private final static int NOT_SET = -1;

    /**
     * Connection timeout value
     */
    private int timeout = NOT_SET;

    /**
     * Http client used to connect to API
     */
    private DefaultHttpClient httpclient;

    /**
     * Default constructor
     */
    public SyncanoHttpClient() {
    }

    /**
     * Sets new timeout value
     *
     * @param millis timeout value in milliseconds
     */
    public void setTimeout(int millis) {
        timeout = millis;
    }

    /**
     * Method to send post data contained in postData field
     *
     * @return Response with data
     */
    public <T> Response<T> send(String serverUrl, HttpRequest<T> syncanoRequest) {
        HttpEntity parameters = syncanoRequest.prepareParams();

        String url;
        if (syncanoRequest.getCompleteCustomUrl() != null) {
            url = syncanoRequest.getCompleteCustomUrl() + syncanoRequest.getUrlParams();
        } else {
            url = serverUrl + syncanoRequest.getUrl() + syncanoRequest.getUrlParams();
        }

        if (BuildConfig.DEBUG) {
            SyncanoLog.d(LOG_TAG, "Request: " + syncanoRequest.getRequestMethod() + "  " + url);
            SyncanoLog.d(LOG_TAG, "Request params: " + parameters);
        }

        HttpUriRequest request = getHttpUriRequest(syncanoRequest.getRequestMethod(), url, parameters);
        request.setHeader("Content-Type", syncanoRequest.getContentType());
        request.setHeader("Accept-Encoding", "gzip");

        for (NameValuePair header : syncanoRequest.getHttpHeaders()) {
            request.setHeader(header.getName(), header.getValue());
        }

        httpclient = Encryption.getHttpClient();
        httpclient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, Constants.USER_AGENT);
        HttpParams httpParameters = httpclient.getParams();
        if (timeout != NOT_SET) {
            // Set the timeout in milliseconds until a connection is established.
            // The default value is zero, that means the timeout is not used.
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeout * 1000);
            // Set the default socket timeout (SO_TIMEOUT)
            // in milliseconds which is the timeout for waiting for data.
            HttpConnectionParams.setSoTimeout(httpParameters, timeout * 1000);
        }

        Response<T> syncanoResponse = syncanoRequest.instantiateResponse();
        HttpResponse response;

        try {
            response = httpclient.execute(request);
        } catch (ClientProtocolException e) {
            SyncanoLog.w(LOG_TAG, "ClientProtocolException");
            syncanoResponse.setResultCode(Response.CODE_CLIENT_PROTOCOL_EXCEPTION);
            syncanoResponse.setError(e.toString());
            return syncanoResponse;
        } catch (IOException e) {
            SyncanoLog.w(LOG_TAG, "IOException");
            syncanoResponse.setResultCode(Response.CODE_ILLEGAL_IO_EXCEPTION);
            syncanoResponse.setError(e.toString());
            return syncanoResponse;
        }


        try {
            syncanoResponse.setHttpResultCode(response.getStatusLine().getStatusCode());
            syncanoResponse.setHttpReasonPhrase(response.getStatusLine().getReasonPhrase());

            if (BuildConfig.DEBUG) {
                SyncanoLog.d(LOG_TAG, "HTTP Response: " + response.getStatusLine().getStatusCode() + "  " + response.getStatusLine().getReasonPhrase());
            }

            // download response data
            String json = null;
            if (response.getEntity() != null) {
                InputStream is = response.getEntity().getContent();
                Header contentEncoding = response.getFirstHeader("Content-Encoding");
                if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
                    is = new GZIPInputStream(is);
                }

                byte[] data = readToByteArray(is);
                if (data != null) {
                    json = new String(data);
                    if (BuildConfig.DEBUG) {
                        SyncanoLog.d(LOG_TAG, "Received: " + json);
                    }
                }
            }

            // when request succeeded parse data, otherwise set error flags
            if (syncanoRequest.isCorrectHttpResponseCode(response.getStatusLine().getStatusCode())) {
                // For some requests it may be null (for example: DELETE).
                syncanoResponse.setData(syncanoRequest.parseResult(syncanoResponse, json));
            } else {
                syncanoResponse.setResultCode(Response.CODE_HTTP_ERROR);
                syncanoResponse.setError("Http error.");
                if (json != null) {
                    syncanoResponse.setError(json);
                }
            }
        } catch (IllegalStateException e) {
            SyncanoLog.w(LOG_TAG, "IllegalStateException");
            syncanoResponse.setResultCode(Response.CODE_ILLEGAL_STATE_EXCEPTION);
            syncanoResponse.setError(e.toString());
            return syncanoResponse;
        } catch (IOException e) {
            SyncanoLog.w(LOG_TAG, "IOException");
            syncanoResponse.setResultCode(Response.CODE_ILLEGAL_IO_EXCEPTION);
            syncanoResponse.setError(e.toString());
            return syncanoResponse;
        }

        return syncanoResponse;
    }

    private HttpUriRequest getHttpUriRequest(String requestMethod, String url, HttpEntity postData) {
        if (METHOD_GET.equals(requestMethod)) {
            return new HttpGet(url);
        } else if (METHOD_POST.equals(requestMethod)) {
            HttpPost httpPost = new HttpPost(url);
            if (postData != null) {
                httpPost.setEntity(postData);
            }
            return httpPost;
        } else if (METHOD_PUT.equals(requestMethod)) {
            HttpPut httpPut = new HttpPut(url);
            if (postData != null) {
                httpPut.setEntity(postData);
            }
            return httpPut;
        } else if (METHOD_PATCH.equals(requestMethod)) {
            HttpPatch httpPatch = new HttpPatch(url);
            if (postData != null) {
                httpPatch.setEntity(postData);
            }
            return httpPatch;
        } else if (METHOD_DELETE.equals(requestMethod)) {
            return new HttpDelete(url);
        } else {
            return new HttpGet(url);
        }
    }

    /**
     * Copies whole InputStream to byte array
     *
     * @param is InputStream to copy
     * @return byte[] containing copied InputStream
     */
    private static byte[] readToByteArray(InputStream is) throws IOException {
        if (is == null)
            return null;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte buffer[] = new byte[1024];
        for (int s; (s = is.read(buffer)) != -1; ) {
            baos.write(buffer, 0, s);
        }
        return baos.toByteArray();
    }
}
