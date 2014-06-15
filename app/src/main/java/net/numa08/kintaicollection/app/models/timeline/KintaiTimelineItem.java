package net.numa08.kintaicollection.app.models.timeline;

import com.google.gson.JsonElement;

public class KintaiTimelineItem {
    private final User user;
    private final Kintai kintai;


    public KintaiTimelineItem(User user, Kintai kintai) {
        this.user = user;
        this.kintai = kintai;
    }
    public User getUser() {
        return user;
    }

    public Kintai getKintai() {
        return kintai;
    }

    public static KintaiTimelineItem parseJson(JsonElement element) {
        return null;
    }
}
