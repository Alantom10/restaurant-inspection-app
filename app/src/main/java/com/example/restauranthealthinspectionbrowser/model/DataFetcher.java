package com.example.restauranthealthinspectionbrowser.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A helper class that converts Date objects to formatted strings for display
 * on UI.
 */
public class DataFetcher {
    private static final String TAG = "DataFetcher";

    public String fetchLastModifiedRestaurants() throws JSONException {
        String metaDataUrl = "https://data.surrey.ca/api/3/action/package_show?id=restaurants";
        return fetchResourceJsonObject(metaDataUrl).getString("last_modified");
    }

    public String fetchLastModifiedInspections() throws JSONException {
        String metaDataUrl = "https://data.surrey.ca/api/3/action/package_show?id=fraser-health-restaurant-inspection-reports";
        return fetchResourceJsonObject(metaDataUrl).getString("last_modified");
    }

    public byte[] fetchRestaurantData() throws IOException, JSONException {
        String metaDataUrl = "https://data.surrey.ca/api/3/action/package_show?id=restaurants";
        String packageUrl = fetchResourceJsonObject(metaDataUrl).getString("url");
//        Log.i(TAG, "Received restaurant package url: " + packageUrl);
        return getUrlBytes(packageUrl);
    }

    public byte[] fetchInspectionData() throws IOException, JSONException {
        String metaDataUrl = "https://data.surrey.ca/api/3/action/package_show?id=fraser-health-restaurant-inspection-reports";
        String packageUrl = fetchResourceJsonObject(metaDataUrl).getString("url");
//        Log.i(TAG, "Received inspection package url: " + packageUrl);
        return getUrlBytes(packageUrl);
    }

    private JSONObject fetchResourceJsonObject(String urlSpec) {
        JSONObject result = null;
        try {
            String jsonString = getUrlString(urlSpec);
//            Log.i(TAG, "Received JSON: " + jsonString);
            JSONObject jsonBody = new JSONObject(jsonString);
            JSONArray resourceJsonArray = jsonBody.getJSONObject("result").getJSONArray("resources");
            result = resourceJsonArray.getJSONObject(0);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        } catch (JSONException je){
            Log.e(TAG, "Failed to parse JSON", je);
        }
        return result;
    }

    private byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() +
                        ": with " +
                        urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    private String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }
}
