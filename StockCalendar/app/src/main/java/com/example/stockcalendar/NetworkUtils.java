package com.example.stockcalendar;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtils {
    private static final String LOG_TAG =
            NetworkUtils.class.getSimpleName();

    // Base URL for Books API.
    private static final String BOOK_BASE_URL = "http://140.116.215.236/bba753951/stock/calendar_fun";
    // Parameter for the search string.
    private static final String QUERY_START = "show_cal_start";
    // Parameter that limits search results.
    private static final String MAX_END = "show_cal_end";


    static String getBookInfo(String[] queryString) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String bookJSONString = null;


        try {
            //...
//            Uri builtURI = Uri.parse(BOOK_BASE_URL).buildUpon()
//                    .appendQueryParameter(QUERY_START, "2019-01-2")
//                    .appendQueryParameter(MAX_END,"2019-03-02")
//                    .build();
//            URL requestURL = new URL(builtURI.toString());
//            Log.d("aaa",builtURI.toString());
            URL requestURL = new URL(BOOK_BASE_URL);


            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
//            urlConnection.setRequestProperty("contentType", "utf-8");
//            urlConnection.setUseCaches(false);
//            urlConnection.connect();

//            String body = "show_cal_start=2018-1-30&show_cal_end=2019-03-03";
            String body = String.format("show_cal_start=%s&show_cal_end=%s",queryString[0],queryString[1]);
            Log.d("search", body);

            OutputStream os = urlConnection.getOutputStream();
            os.write(body.getBytes("utf-8"));
            os.flush();
            os.close();

            // Get the InputStream.
            InputStream inputStream = urlConnection.getInputStream();

// Create a buffered reader from that input stream.
            reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

// Use a StringBuilder to hold the incoming response.
            StringBuilder builder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                // Since it's JSON, adding a newline isn't necessary (it won't
                // affect parsing) but it does make debugging a *lot* easier
                // if you print out the completed buffer for debugging.
                builder.append("\n");
            }


            if (builder.length() == 0) {
                // Stream was empty. No point in parsing.
                return null;
            }


            bookJSONString = builder.toString();
            Log.d("aaa", bookJSONString);
//            String a = new String(bookJSONString.getBytes("UTF-8"), "UTF-8");
//            a="你好";
//            Log.d("aaa", getEncoding(a));


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //...
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        return bookJSONString;
    }


//    public static String getEncoding(String str) {
//        String encode;
//
//        encode = "UTF-16";
//        try {
//            if (str.equals(new String(str.getBytes(), encode))) {
//                return encode;
//            }
//        } catch (Exception ex) {
//        }
//
//        encode = "ASCII";
//        try {
//            if (str.equals(new String(str.getBytes(), encode))) {
//                return "字符串<< " + str + " >>中仅由数字和英文字母组成，无法识别其编码格式";
//            }
//        } catch (Exception ex) {
//        }
//
//        encode = "ISO-8859-1";
//        try {
//            if (str.equals(new String(str.getBytes(), encode))) {
//                return encode;
//            }
//        } catch (Exception ex) {
//        }
//
//        encode = "GB2312";
//        try {
//            if (str.equals(new String(str.getBytes(), encode))) {
//                return encode;
//            }
//        } catch (Exception ex) {
//        }
//
//        encode = "UTF-8";
//        try {
//            if (str.equals(new String(str.getBytes(), encode))) {
//                return encode;
//            }
//        } catch (Exception ex) {
//        }
//
//        /*
//         *......待完善
//         */
//
//        return "未识别编码格式";
//
//    }
}


