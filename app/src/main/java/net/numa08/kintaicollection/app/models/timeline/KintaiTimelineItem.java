package net.numa08.kintaicollection.app.models.timeline;

import java.net.URL;

public class KintaiTimelineItem {
    private final URL iconUrl;
    private final User user;
    private final Kintai kintai;


    public KintaiTimelineItem(URL iconUrl, User user, Kintai kintai) {
        this.iconUrl = iconUrl;
        this.user = user;
        this.kintai = kintai;
    }

    public URL getIconUrl() {
        return iconUrl;
    }

    public User getUser() {
        return user;
    }

    public Kintai getKintai() {
        return kintai;
    }
}
