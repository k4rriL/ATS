package junction.senseit;

/**
 * Created by Rakesh on 11/26/2016
 */

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.HashMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

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
    private static String post_login_url, get_tickets_url, post_location_url, gen_img_info_url;

    /**
     *  Variables to store the backend IP address & port
     */
    private static String m_strBackendIP;

    public boolean initialize() {

        formURLsToConnect();
        return true;
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
     * Parse the backend_ip file and form the URL strings
     */
    private void formURLsToConnect() {

        m_strBackendIP = getResources().getString(R.string.str_server_ip);
        String m_strPort = getResources().getString(R.string.str_server_port);

        // URLs to be invoked for different functions
        post_login_url = "http://" + m_strBackendIP + ":" + m_strPort + "/api/users/token";
        get_tickets_url = "http://" + m_strBackendIP + ":" + m_strPort + "/api/workers/view/";
        post_location_url = "http://" + m_strBackendIP + ":" + m_strPort + "/api/workers/updatelocation/";
        gen_img_info_url = "http://" + m_strBackendIP + ":" + m_strPort + "/api/image/";
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
    private String getJSONResponseString(HttpURLConnection connection) {

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

    public int authenticateUser(String strUsername, String strPassword) {

        int nWorkerID = -1;

        // Establish HTTP connection and post data
        HttpURLConnection urlConnection = null;
        try {

            // Get UTF-8 encoded URL parameters
            HashMap<String, String> mapParameters = new HashMap<>();
            mapParameters.put("username", strUsername);
            mapParameters.put("password", strPassword);
            String urlParams = encodeParameters(mapParameters);

            // Initialize URL object and Connect to the URL
            URL url = new URL(post_login_url);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setFixedLengthStreamingMode(urlParams.getBytes().length);
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
                    respObject = respObject.getJSONObject("data");
                    ACCESS_TOKEN = respObject.getString("token").toLowerCase();
                    nWorkerID = respObject.getInt("id");
                }
            }
        } catch (IOException | JSONException ex) {

            ex.printStackTrace();
        } finally {

            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return nWorkerID;
    }

    public String getTicketsForWorker(int workerID) {

        // String containing the json string
        String strJSONResponse = null;

        // Establish HTTP connection and post data
        HttpURLConnection urlConnection = null;
        try {

            // Initialize URL object and Connect to the URL
            URL url = new URL(get_tickets_url + workerID);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Authorization", "Bearer " + ACCESS_TOKEN);
            urlConnection.setConnectTimeout(30000);
            urlConnection.setChunkedStreamingMode(0);
            urlConnection.setRequestMethod("GET");

            // Read the response
            if (urlConnection.getResponseCode() == HttpsURLConnection.HTTP_OK) {

                // Get JSON response and parse it
                String jsonRespString = getJSONResponseString(urlConnection);
                if(jsonRespString != null) {

                    strJSONResponse = jsonRespString;
                }
            }
        } catch (IOException ex) {

            ex.printStackTrace();
        } finally {

            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return strJSONResponse;
    }

   
}


