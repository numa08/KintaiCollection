package net.numa08.kintaicollection.app.models.timeline;

import java.util.Date;

public interface Kintai {

    public Date getDate();

    public static final class Syussya implements Kintai {
        final Date date;

        public Syussya(Date date) {
            this.date = date;
        }

        public Date getDate() {
            return date;
        }
    }

    public static final class Taisya implements Kintai {
        final Date date;

        public Taisya(Date date) {
            this.date = date;
        }

        public Date getDate() {
            return date;
        }
    }
}
