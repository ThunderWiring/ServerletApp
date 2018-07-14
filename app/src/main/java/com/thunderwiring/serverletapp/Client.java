package com.thunderwiring.serverletapp;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Creates a request to send to the server.
 */
public class Client {
    private Map<String, String> postData = new HashMap<>();
    private Context context;

    /**
     * @param requestContent data to be sent in the POST request
     * @param context App's main activity context
     */
    public Client(String requestContent, Context context) {
        postData.put("request_content", requestContent );
        this.context = context;
    }

    public void sendHttpRequest() {
        HttpRequestTask task = new HttpRequestTask(postData);
        task.execute("http://<change-this-with-your-host-ip>:3000/post-request-url");
    }

    void processRequest(String result) {
        TextView responseTextView = (TextView) ((Activity) context).findViewById(R.id.responseTextView);
        responseTextView.setText(result);
    }

    private final class HttpRequestTask extends AsyncTask<String, String, String>{
        private JSONObject postData;

        public HttpRequestTask(Map<String, String> data ) {
            if (data != null) {
                postData = new JSONObject(data);
            }
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            StringBuffer response = new StringBuffer();
            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestMethod("POST");
                urlConnection.setConnectTimeout(5000);

                // Send the post body
                DataOutputStream wr = new DataOutputStream (urlConnection.getOutputStream());
                wr.writeBytes(postData.toString());
                wr.flush ();
                wr.close ();

                if (urlConnection.getResponseCode() ==  HttpURLConnection.HTTP_OK) {
                    //Get Response
                    InputStream is = urlConnection.getInputStream();
                    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                    String line;

                    while((line = rd.readLine()) != null) {
                        response.append(line);
                        response.append('\n');
                    }
                    rd.close();
                } else {
                    response.append("HTTP Response code not OK - " + urlConnection.getResponseCode());
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return response.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            processRequest(s);
        }
    }
}
