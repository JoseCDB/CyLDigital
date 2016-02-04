package es.jcyl.cee.cylford.app.cyldigital.backend.ws;

import android.content.Context;
import android.net.NetworkInfo;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.security.cert.Certificate;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import es.jcyl.cee.cylford.app.cyldigital.R;
import es.jcyl.cee.cylford.app.cyldigital.backend.App;
import es.jcyl.cee.cylford.app.cyldigital.ui.OnlineActivity;

/**
 * Created by josecarlos.delbarrio on 13/10/2015.
 */
public abstract class FormCylDigWS {

    /**
     * Comprueba si la red está accesible
     */
    protected static boolean estaRedAccesible() {
        //ConnectivityManager: Class that answers queries about the state of network connectivity
        NetworkInfo ni = App.getConnectivityManager().getActiveNetworkInfo();
        return ni != null && ni.isConnected() && ni.isAvailable();
    }

    /**
     * Genera el objeto HttpURLConnection que se utilizará
     * en el InputStream que recupera las actividades.
     * */
    protected static HttpsURLConnection enviarRequestA(String sUrl, int connectTimeOut, int readTimeOut, OnlineActivity listener) throws IOException {
        HttpsURLConnection cnt = null;
        URL url = null;
        try {

            // Load CAs from an InputStream
            // (could be from a resource or ByteArrayInputStream or ...)
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            // From https://www.washington.edu/itconnect/security/ca/load-der.crt
            //InputStream caInput = new BufferedInputStream(new FileInputStream("android.crt"));
            Context context = listener;
            InputStream caInput = new BufferedInputStream(context.getResources().openRawResource(R.raw.google_cert));
            Certificate ca;
            try {
                ca = cf.generateCertificate(caInput);
                System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
            } finally {
                caInput.close();
            }

            // Create a KeyStore containing our trusted CAs
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            // Create a TrustManager that trusts the CAs in our KeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            // Let us create the factory where we can set some parameters for the connection
            SSLContext sslContext = SSLContext.getInstance("SSL");;
            sslContext.init(null, tmf.getTrustManagers(), null);
            //Now that we have the KeyStore containing the client certificate, we can use it to build an SSLContext:
           /* KeyManagerFactory kmf = KeyManagerFactory.getInstance(algorithm);
            kmf.init(keyStore);
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(kmf.getKeyManagers(), null, null);*/

            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

            url = new URL(sUrl);
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("192.168.10.53", 8080));
            cnt = (HttpsURLConnection) url.openConnection(proxy);
            cnt.setRequestMethod("GET");
            //cnt.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.9; rv:26.0) Gecko/20100101 Firefox/26.0");
            cnt.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            //cnt.setRequestProperty("Accept", "application/json");

            cnt.setConnectTimeout(connectTimeOut);
            cnt.setReadTimeout(readTimeOut);
            cnt.setSSLSocketFactory(sslContext.getSocketFactory());

        } catch (KeyStoreException keyStoreException) {

        } catch (NoSuchAlgorithmException nsae){

        } catch (KeyManagementException keyManagmentException){

        } catch (CertificateException ce){

        }
        return cnt;
    }

}
