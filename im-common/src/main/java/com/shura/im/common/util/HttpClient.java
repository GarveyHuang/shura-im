package com.shura.im.common.util;

import okhttp3.*;

import java.io.IOException;

/**
 * @Author: Garvey
 * @Created: 2021/10/23
 * @Description:
 */
public class HttpClient {

    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json");

    public static Response call(OkHttpClient okHttpClient, String params, String url) throws IOException {
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE, params);

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        Response response = okHttpClient.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }

        return response;
    }
}
