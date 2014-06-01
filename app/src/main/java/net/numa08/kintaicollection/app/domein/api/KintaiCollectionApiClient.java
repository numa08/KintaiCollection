package net.numa08.kintaicollection.app.domein.api;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

abstract public class KintaiCollectionApiClient {

    public interface KintaiCollectionApiCallback {
        public void onSuccess(JSONObject json);
        public void onFailed(Throwable e);
        public void onFinish();
    }

    public final String apiUrl;

    protected KintaiCollectionApiClient(String apiUrl) {
        this.apiUrl = apiUrl;
    }


    public void invokeAPI(String endpoint, KintaiCollectionApiRequest request, final KintaiCollectionApiCallback callback, RequestQueue queue) {
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
