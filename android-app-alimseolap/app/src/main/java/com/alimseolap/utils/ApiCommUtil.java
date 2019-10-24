package com.alimseolap.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;


public class ApiCommUtil {

    public String requestJson(String _url, String _jsonBody){

        HttpURLConnection urlConn = null;

        try {
            urlConn = request(_url, _jsonBody);

            if(urlConn == null)
                return "{\"msg\", \"failed\"}";


            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));

            String result = "";
            String tmp;

            while((tmp = reader.readLine()) != null)
                result += tmp;

            return result;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(urlConn != null)
                urlConn.disconnect();
        }
        return "{\"msg\", \"failed\"}";
    }



    // ImgProcess
    public Bitmap requestImg(String _url, String _jsonBody){
        HttpURLConnection urlConn = null;
        Bitmap bit = null;

        try {
            urlConn = request(_url, _jsonBody);
            if(urlConn == null)
                return null;

            bit = BitmapFactory.decodeStream(urlConn.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(urlConn != null)
                urlConn.disconnect();
        }
        return bit;
    }



    private HttpURLConnection request(String _url, String _jsonBody) throws IOException {

        URL url = new URL(_url);

        HttpURLConnection connection =  (HttpURLConnection)url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");

        String strParams = _jsonBody;
        OutputStream os = connection.getOutputStream();

        os.write(strParams.getBytes("UTF-8"));
        os.flush();
        os.close();

        if(connection.getResponseCode() != HttpURLConnection.HTTP_OK)
            return null;
        return connection;
    }
}
