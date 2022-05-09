package com.lineage.service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class NotificationService {

    private static final String BASE_URL = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";
    private static final String API_TOKEN = "5220961603:AAFZI7jwRjyH7lv3cfBRoqgJP8qeDkO0EkI";
    private static final String CHAT_ID = "333228448";

    public void sendMessage(String text) {
        try {
            URL url = new URL(String.format(BASE_URL, API_TOKEN, CHAT_ID, text));
            URLConnection conn = url.openConnection();
            InputStream is = new BufferedInputStream(conn.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
