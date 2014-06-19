package net.numa08.kintaicollection.app.models.timeline;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.text.ParseException;
import java.util.Comparator;
import java.util.Date;

public class KintaiTimelineItem implements Comparable<KintaiTimelineItem>{
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

    @Override
    public int compareTo(KintaiTimelineItem another) {
        final Date lDate = this.getKintai().getDate();
        final Date rDate = another.getKintai().getDate();
        return -1 * lDate.compareTo(rDate);
    }

    @Override
    public String toString() {
        return "KintaiTimelineItem{" +
                "user=" + user +
                ", kintai=" + kintai +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KintaiTimelineItem that = (KintaiTimelineItem) o;

        if (!kintai.equals(that.kintai)) return false;
        if (!user.equals(that.user)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = user.hashCode();
        result = 31 * result + kintai.hashCode();
        return result;
    }

    public static KintaiTimelineItem parseJson(JsonElement element) throws ParseException {
        final JsonObject object = element.getAsJsonObject();
        final User user = new User(object.get("name").getAsString(), object.get("id").getAsString(), object.get("profileimageurl").getAsString());
        final String status = object.get("status").getAsString();
        final Date createAt = new Date(object.get("createdAt").getAsLong());
        final Kintai kintai;
        if (Kintai.Status.Syussya.equals(status)) {
            kintai = new Kintai.Syussya(createAt);
        } else  {
            kintai = new Kintai.Taisya(createAt);
        }

        final KintaiTimelineItem item = new KintaiTimelineItem(user, kintai);
        return item;
    }

    public static final Comparator<KintaiTimelineItem> COMPARATOR = new Comparator<KintaiTimelineItem>() {
        @Override
        public int compare(KintaiTimelineItem lhs, KintaiTimelineItem rhs) {
            final Date lDate = lhs.getKintai().getDate();
            final Date rDate = rhs.getKintai().getDate();
            return -1 * lDate.compareTo(rDate);
        }
    };
}
