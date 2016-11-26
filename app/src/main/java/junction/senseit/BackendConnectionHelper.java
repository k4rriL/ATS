package junction.senseit;

/**
 * Created by Rakesh on 11/26/2016
 */

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by Rakesh on 10/14/2016
 */



public class BackendConnectionHelper extends Application {

    private static final String TAG = "BackendConnectionHelper";

    /**
     *  The access token required for subsequent access to backend
     *  after initial authentication using username & password
     */
    private static String ACCESS_TOKEN;

    /**
     *  These strings store the HTTPS URLs to communicate with the backend
     */
    private static String post_login_url, get_history_url, post_images_url, gen_img_info_url;

    /**
     *  Variables to store the backend IP address & port
     */
    private static String m_strBackendIP;

    /**
     *  The socket factory instance to be used with HTTPS connections
     *  It will be initialized in the beginning once
     */
    private static SSLSocketFactory m_sslSocketFactory = null;

    /**
     *  Variable that stores the amount of data exchanged with the server
     *  for different API calls. It is set in different APIs and can be read
     *  after the API call and can be reset to 0
     */
    private long m_dataInBytes = 0;

    public boolean initialize() {

        formURLsToConnect();
        getSSLSocketFactory();

        return (m_sslSocketFactory != null);
    }

    /**
     * Check if the network connection is available
     * @return - status of the operation
     */
    public boolean isNetworkAvailable () {

        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();

        // In case of no network available, networkInfo will be null
        return networkInfo != null && networkInfo.isConnected();
    }

    /**
     *  The certificate generated in getSSLSocketFactory contains the hostname.
     *  We connect and access the backend using IP addresses that is the alternative subject
     *  in the certificate. But, our certificate does not contain alternative subject so using
     *  overriding the default host name verification process as such.
     */
    static {
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {

                return hostname.equals(m_strBackendIP);
            }
        });
    }

    /**
     *  This method constructs the SSL socket after the self-signed server certificate
     *  verification. This is used to access the backend server using HTTPS requests.
     */
    private void getSSLSocketFactory() {

        try {

            // Create instance of the standard certificate class X509
            CertificateFactory cf = CertificateFactory.getInstance("X.509");

            // Use the cert.pem certificate of the server (present in the assets folder)
            // and create a X509 certificate
            Certificate serverCert;
            InputStream certInputStream = getResources().getAssets().open("cert.pem");
            try(BufferedInputStream caInput = new BufferedInputStream(certInputStream)) {

                serverCert = cf.generateCertificate(caInput);
            }

            // Create a KeyStore containing our server certificate
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("serverCert", serverCert);

            // Create a TrustManager that trusts the CAs in our KeyStore (server certificate)
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            // Create an SSLContext that uses our TrustManager
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);
            m_sslSocketFactory = context.getSocketFactory();

        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException | IOException | CertificateException e) {

            Log.e(TAG, "Exception while constructing ssl context");
            e.printStackTrace();
        }
    }

    /**
     * Parse the backend_ip file and form the URL strings
     * @return - status of the operation
     */
    private boolean formURLsToConnect() {

        boolean bStatus = false;

        try {
            InputStream file = getResources().getAssets().open("backend_ip.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(file));
            String line = reader.readLine();
            if (line != null) {

                String[] arrStrings = line.split(":");
                m_strBackendIP = arrStrings[1];
                String m_strPort = arrStrings[2];
                // URLs to be invoked for different functions
                post_login_url = "https://" + m_strBackendIP + ":" + m_strPort + "/api/authorize";
                get_history_url = "https://" + m_strBackendIP + ":" + m_strPort + "/api/image";
                post_images_url = "https://" + m_strBackendIP + ":" + m_strPort + "/api/image/add";
                gen_img_info_url = "https://" + m_strBackendIP + ":" + m_strPort + "/api/image/";

                bStatus = true;
            }
        } catch (IOException ex) {

            Log.e(TAG, "Exception while forming URLs for backend connection.");
        }

        return bStatus;
    }

    /**
     * This method encodes the URL parameters to be sent to the backend
     * in UTF-8 format  and returns the string format of it.
     * @param params - Key-Value pairs of various parameters
     * @return - String form of UTF-8 encoded parameters of URL
     */
    private String encodeParameters (HashMap<String, String> params) {

        final char PARAM_DELIMITER = '&';
        final char ASSIGN_VALUE_CHAR = '=';
        StringBuilder buffer = new StringBuilder();

        try {
            if (params != null) {

                boolean bFirstParam = true;
                for (String key : params.keySet()) {

                    // If not first parameter then concatenate with the delimiter character
                    if (!bFirstParam) {

                        buffer.append(PARAM_DELIMITER);
                    }

                    // Appends key=value for each parameter
                    buffer.append(key);
                    buffer.append(ASSIGN_VALUE_CHAR);
                    buffer.append(URLEncoder.encode(params.get(key), "UTF-8"));

                    bFirstParam = false;
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return buffer.toString();
    }

    /**
     * Reads the response of the backend on the supplied connection and
     * retrieves the JSON response and returns as a String
     * @param connection - Connection to the backend
     * @return - String format of the response
     */
    private String getJSONResponseString(HttpsURLConnection connection) {

        String respString = null;

        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder buffer = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {

                buffer.append(line);
            }

            if(buffer.length() > 0) {

                respString = buffer.toString();
            }

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return respString;
    }

    public boolean authenticateUser(String strUsername, String strPassword) {

        boolean bAuthenticated = false;

        // Establish HTTP connection and post data
        HttpsURLConnection urlConnection = null;
        try {

            // Get UTF-8 encoded URL parameters
            HashMap<String, String> mapParameters = new HashMap<>();
            mapParameters.put("username", strUsername);
            mapParameters.put("password", strPassword);
            String urlParams = encodeParameters(mapParameters);

            // Initialize URL object and Connect to the URL
            URL url = new URL(post_login_url);
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setSSLSocketFactory(m_sslSocketFactory);
            urlConnection.setDoOutput(true);
            urlConnection.setFixedLengthStreamingMode(urlParams.getBytes().length);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setConnectTimeout(30000);
            urlConnection.setRequestMethod("POST");

            // Post the username and password to the output stream
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
            writer.write(urlParams);
            writer.flush();
            writer.close();

            // Read the response
            if (urlConnection.getResponseCode() == HttpsURLConnection.HTTP_OK) {

                // Get JSON response and parse it
                String jsonRespString = getJSONResponseString(urlConnection);
                if(jsonRespString != null) {

                    JSONObject respObject = new JSONObject(jsonRespString);
                    ACCESS_TOKEN = respObject.getString("access_token").toLowerCase();
                    bAuthenticated = true;
                }
            }
        } catch (IOException | JSONException ex) {

            ex.printStackTrace();
        } finally {

            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return bAuthenticated;
    }
    /*
    public ArrayList<ImageInformation> getHistory() {

        // List of applications to return to the caller
        ArrayList<ImageInformation> arrImageInfo = null;

        // Establish HTTP connection and post data
        HttpsURLConnection urlConnection = null;
        try {

            // Initialize URL object and Connect to the URL
            URL url = new URL(get_history_url);
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setSSLSocketFactory(m_sslSocketFactory);
            urlConnection.setRequestProperty("Authorization", "Bearer " + ACCESS_TOKEN);
            urlConnection.setConnectTimeout(30000);
            urlConnection.setChunkedStreamingMode(0);
            urlConnection.setRequestMethod("GET");

            // Read the response
            if (urlConnection.getResponseCode() == HttpsURLConnection.HTTP_OK) {

                // Get JSON response and parse it
                String jsonRespString = getJSONResponseString(urlConnection);
                if(jsonRespString != null) {

                    arrImageInfo = new ArrayList<>();

                    // Get the JSON array of objects from the resultant JSON object
                    JSONObject respObject = new JSONObject(jsonRespString);
                    JSONArray arrImages = respObject.getJSONArray("images");
                    for (int index = 0; index < arrImages.length();  index++) {

                        ImageInformation imageInfo = new ImageInformation();

                        imageInfo.imageID = arrImages.getJSONObject(index).getString("id");
                        imageInfo.imageName = arrImages.getJSONObject(index).getString("name");
                        imageInfo.operationStatus = arrImages.getJSONObject(index).getString("status");
                        imageInfo.imageText = arrImages.getJSONObject(index).getString("text");

                        arrImageInfo.add(imageInfo);
                    }
                }
            }
        } catch (IOException | JSONException ex) {

            ex.printStackTrace();
        } finally {

            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return arrImageInfo;
    }

    public ImageInformation getImageInformation(String strImageID) {

        ImageInformation imageInfo = null;

        // Establish HTTP connection and get data
        HttpsURLConnection urlConnection = null;
        try {

            // Initialize URL object and Connect to the URL
            String strURL = gen_img_info_url + strImageID;
            URL url = new URL(strURL);
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setSSLSocketFactory(m_sslSocketFactory);
            urlConnection.setRequestProperty("Authorization", "Bearer " + ACCESS_TOKEN);
            urlConnection.setConnectTimeout(30000);
            urlConnection.setChunkedStreamingMode(0);
            urlConnection.setRequestMethod("GET");

            // Read the response
            if (urlConnection.getResponseCode() == HttpsURLConnection.HTTP_OK) {

                // Get the response length
                m_dataInBytes = urlConnection.getContentLength();

                // Get JSON response and parse it
                String jsonRespString = getJSONResponseString(urlConnection);
                if (jsonRespString != null) {

                    // Get the JSON array of objects from the resultant JSON object
                    JSONObject respObject = new JSONObject(jsonRespString);
                    imageInfo = new ImageInformation();

                    imageInfo.imageID = respObject.getString("id");
                    imageInfo.imageName = respObject.getString("name");
                    imageInfo.operationStatus = respObject.getString("status");
                    imageInfo.imageText = respObject.getString("text");
                }
            }
        } catch (IOException | JSONException ex) {

            ex.printStackTrace();
        } finally {

            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return imageInfo;
    }

    public String postImageToServer(String imagePath) {

        String imageID = null;
        HttpsURLConnection urlConnection = null;

        try {

            // Encode the images data to post to the server
            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

            // Add the JPEG image as an entity
            File file = new File(imagePath);
            ContentBody imageBody = new FileBody(file, "image/jpeg");
            entity.addPart("file", imageBody );

                Bitmap bitmap = BitmapFactory.decode
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                ByteArrayBody bab = new ByteArrayBody( bos.toByteArray(), ("img" + index + ".jpg") );
                entity.addPart( ("file" + index), bab);

            // Initialize URL object and Connect to the URL
           // URL url = new URL(post_images_url);
            //urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setSSLSocketFactory(m_sslSocketFactory);
            urlConnection.setRequestProperty("Connection", "Keep-Alive");
            urlConnection.addRequestProperty(entity.getContentType().getName(), entity.getContentType().getValue());
            urlConnection.addRequestProperty("Content-length", entity.getContentLength() + "");
            urlConnection.setRequestProperty("Authorization", "Bearer " + ACCESS_TOKEN);
            urlConnection.setConnectTimeout(300000);
            urlConnection.setRequestMethod("POST");

            // Get the response length
            m_dataInBytes = entity.getContentLength();

            // Write the data into the output stream
            entity.writeTo(urlConnection.getOutputStream());
            urlConnection.connect();

            // Read the response
            if (urlConnection.getResponseCode() == HttpsURLConnection.HTTP_OK) {

                // Get JSON response and parse it
                String jsonRespString = getJSONResponseString(urlConnection);
                if(jsonRespString != null) {

                    JSONObject respObject = new JSONObject(jsonRespString);
                    JSONArray jsonArrayOfIDs = respObject.getJSONArray("images");
                    for (int arrIndex = 0; arrIndex < jsonArrayOfIDs.length(); arrIndex++) {

                        imageID = jsonArrayOfIDs.getString(arrIndex);
                    }
                }
            }
        } catch (IOException | JSONException ex) {

            ex.printStackTrace();
        } finally {

            if (urlConnection != null) {

                urlConnection.disconnect();
            }
        }

        return imageID;
    }

    public Bitmap getImage(String strImageID, boolean bFullImage) {

        Bitmap bmImage = null;

        // Establish HTTP connection and get data
        HttpsURLConnection urlConnection = null;
        try {

            // Check if the caller requested for a full image or a preview
            String strURL;
            if(bFullImage) {

                strURL = gen_img_info_url + strImageID + "/raw";
            } else {

                strURL = gen_img_info_url + strImageID + "/preview";
            }

            // Initialize URL object and Connect to the URL
            URL url = new URL(strURL);
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setSSLSocketFactory(m_sslSocketFactory);
            urlConnection.setRequestProperty("Authorization", "Bearer " + ACCESS_TOKEN);
            urlConnection.setConnectTimeout(30000);
            urlConnection.setChunkedStreamingMode(0);
            urlConnection.setRequestMethod("GET");

            // Read the response
            if (urlConnection.getResponseCode() == HttpsURLConnection.HTTP_OK) {

                BufferedInputStream bis = new BufferedInputStream(urlConnection.getInputStream(), 8192);
                ByteArrayBuffer baf = new ByteArrayBuffer(50);
                int current;
                while ((current = bis.read()) != -1) {

                    baf.append((byte)current);
                }

                byte[] imageData = baf.toByteArray();
                bmImage = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
            }
        } catch (IOException ex) {

            ex.printStackTrace();
        } finally {

            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return bmImage;
    }

    public ImageProcessingStats doLocalOCR(String strImagePath) {

        ImageProcessingStats processingStats = new ImageProcessingStats();
        long startTime = System.currentTimeMillis();

        TessBaseAPI tessTwo = new TessBaseAPI();
        tessTwo.init(Environment.getExternalStorageDirectory().toString(), "eng");
        tessTwo.setPageSegMode(TessBaseAPI.PageSegMode.PSM_SINGLE_LINE);
        File file = new File(strImagePath);
        tessTwo.setImage(file);
        processingStats.imageText = tessTwo.getUTF8Text();
        tessTwo.end();

        long endTime = System.currentTimeMillis();
        processingStats.processingTime = (endTime - startTime);
        processingStats.dataExchangedInBytes = 0;

        return processingStats;
    }

    public ImageProcessingStats doRemoteOCR(String strImagePath) {

        ImageProcessingStats processingStats = new ImageProcessingStats();
        long startTime = System.currentTimeMillis();

        String imageID = postImageToServer(strImagePath);
        processingStats.dataExchangedInBytes = m_dataInBytes;

        ImageInformation imageInfo;
        do {
            // Sleep for sometime as it takes time to process the image
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ignored) {}

            imageInfo = getImageInformation(imageID);
        } while( !imageInfo.operationStatus.equalsIgnoreCase("processed"));

        // Get the image text
        processingStats.imageText = imageInfo.imageText;
        processingStats.dataExchangedInBytes += m_dataInBytes;

        long endTime = System.currentTimeMillis();
        processingStats.processingTime = (endTime - startTime);

        return processingStats;
    }*/
}


