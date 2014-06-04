package net.numa08.kintaicollection.app.domein.api.request;

import net.numa08.kintaicollection.app.domein.api.KintaiCollectionWebClient;
import net.numa08.kintaicollection.app.models.timeline.KintaiTimelineItem;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class KintaiTimelineLoadRequest implements KintaiCollectionApiRequest {

    private final String id;

    public KintaiTimelineLoadRequest(String id) {
        this.id = id;
    }

    @Override
    public JSONObject request() {
        return new JSONObject(new HashMap<String, String>(){{
            put("id", id);
        }});
    }

    public static abstract class KintaiTimelineLoadCallback implements KintaiCollectionWebClient.KintaiCollectionApiCallback {
        public abstract void onLoaded(List<KintaiTimelineItem> items);

        @Override
        public void onSuccess(JSONObject json) {
        }
    }
}
