package net.numa08.kintaicollection.app.domein.api;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import net.numa08.kintaicollection.app.domein.api.request.FetchUserAccountRequest;
import net.numa08.kintaicollection.app.domein.api.request.KintaiCollectionApiRequest;
import net.numa08.kintaicollection.app.domein.api.request.KintaiTimelineLoadRequest;
import net.numa08.kintaicollection.app.domein.api.request.SyussyaRequest;
import net.numa08.kintaicollection.app.domein.api.request.TaisyaRequest;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class KintaiCollectionWebClient {

    public interface KintaiCollectionApiCallback {
        public void onSuccess(JSONObject json);
        public void onFailed(Throwable e);
        public void onFinish();
    }

    public final String apiUrl;
    private final RequestQueue queue;

    public KintaiCollectionWebClient(Context context) {
        final Properties properties = new Properties();
        try {
            final InputStream inputStream = context.getAssets().open("application.properties");
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.apiUrl = properties.getProperty("api_url","");
        queue = Volley.newRequestQueue(context);
    }

    public void fetchUserAccount(FetchUserAccountRequest request, FetchUserAccountRequest.FetchUserAccountClientCallback callback) {
        invokeAPI("fetch_account", request, callback, queue);
    }

    public void syussya(SyussyaRequest request, SyussyaRequest.SyussyaRequestCallback callback) {
        invokeAPI("syussya", request, callback, queue);
    }

    public void taisya(TaisyaRequest request, TaisyaRequest.TaisyaRequestCallback callback) {
        invokeAPI("taisya", request, callback, queue);
    }

    public void loadKintaiTimeline(KintaiTimelineLoadRequest request, KintaiTimelineLoadRequest.KintaiTimelineLoadCallback callback) {
        invokeAPI("timeline", request, callback, queue);
    }

    protected void invokeAPI(String endpoint, KintaiCollectionApiRequest request, final KintaiCollectionApiCallback callback, RequestQueue queue) {
        final String url = apiUrl + "/" + endpoint;
        queue.add(new JsonObjectRequest(url, request.request(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                callback.onSuccess(jsonObject);
                callback.onFinish();
            }
        },
        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                callback.onFailed(volleyError);
                callback.onFinish();
            }
        }
        ));
    }
}
