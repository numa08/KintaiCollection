package net.numa08.kintaicollection.app.domein.api.request;

import net.numa08.kintaicollection.app.domein.api.KintaiCollectionWebClient;

import org.json.JSONObject;

import java.util.Date;

public class SyussyaRequest implements KintaiCollectionApiRequest {

    private final Date date;

    public SyussyaRequest(Date date) {
        this.date = date;
    }


    @Override
    public JSONObject request() {
        return null;
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
