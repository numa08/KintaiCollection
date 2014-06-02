package net.numa08.kintaicollection.app.domein.api.request;

import net.numa08.kintaicollection.app.domein.api.KintaiCollectionWebClient;

import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;

public class SyussyaRequest implements KintaiCollectionApiRequest {

    private final Date date;
    private final String id;

    public SyussyaRequest(Date date, String id) {
        this.date = date;
        this.id = id;
    }


    @Override
    public JSONObject request() {
        return new JSONObject(new HashMap<String, String>(){{
            put("id", id);
            put("time", Long.toString(date.getTime()));
        }});
    }

    public static abstract class SyussyaRequestCallback implements KintaiCollectionWebClient.KintaiCollectionApiCallback{

        public abstract void onSyussyaSuccess();

        @Override
        public void onSuccess(JSONObject json) {
            onSyussyaSuccess();
        }

        @Override
        public void onFailed(Throwable e) {}

        @Override
        public void onFinish() {}
    }
}
