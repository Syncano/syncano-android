package com.syncano.library.utils;

import org.apache.http.HttpVersion;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import java.io.InputStream;
import java.security.KeyStore;

public class Encryption {

    private static final int TIMEOUT = 30000;
    private static final int SOCKET_TIMEOUT = 60000;

    /**
     * Method to get new http client with socket factory which allows to connect only to selected domains
     *
     * @return new http client
     */
    public static DefaultHttpClient getHttpClient() {
        try {
            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, "utf-8");
            HttpConnectionParams.setConnectionTimeout(params, TIMEOUT);
            HttpConnectionParams.setSoTimeout(params, SOCKET_TIMEOUT);
            params.setBooleanParameter("http.protocol.expect-continue", false);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("https", Encryption.newSslSocketFactory(), 443));

            ThreadSafeClientConnManager manager = new ThreadSafeClientConnManager(params, registry);
            return new DefaultHttpClient(manager, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }

    private static SSLSocketFactory newSslSocketFactory() {
        try {
            // Get an instance of the Bouncy Castle KeyStore format
            KeyStore trusted = getSslKey();
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

    private static KeyStore getSslKey() {
        try {
            KeyStore trusted = KeyStore.getInstance("BKS");
            // Get the raw resource, which contains the keystore with
            // your trusted certificates (root and any intermediate certs)
            InputStream in = Encryption.class.getResourceAsStream("/com/syncano/library/utils/keystore.bks");

            try {
                // Initialize the keystore with the provided trusted certificates
                // Also provide the password of the keystore
                trusted.load(in, new char[]{});
            } finally {
                in.close();
            }
            return trusted;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
