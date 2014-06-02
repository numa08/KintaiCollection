package net.numa08.kintaicollection.app.domein.api.request;

import net.numa08.kintaicollection.app.domein.api.KintaiCollectionWebClient;

import org.json.JSONObject;

import java.util.Date;

public class TaisyaRequest implements KintaiCollectionApiRequest {

    private final Date date;

    public TaisyaRequest(Date date) {
        this.date = date;
    }

    @Override
    public JSONObject request() {
        return null;
    }

    public static abstract class TaisyaRequestCallback implements KintaiCollectionWebClient.KintaiCollectionApiCallback {
        public abstract void onTaisyaSuccess();

        @Override
        public void onSuccess(JSONObject json) {
            onTaisyaSuccess();
        }

        @Override
        public void onFailed(Throwable e) {}

        @Override
        public void onFinish() {}
    }
}
