package net.numa08.kintaicollection.app.models.timeline;

import android.content.Context;
import android.text.format.DateUtils;

import java.util.Date;

public interface Kintai {

    public static final int TIME_FLAGS = DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE |
            DateUtils.FORMAT_SHOW_WEEKDAY | DateUtils.FORMAT_SHOW_YEAR |
            DateUtils.FORMAT_ABBREV_ALL;

    public Date getDate();

    public String toLogMessage(Context context);

    public static final class Syussya implements Kintai {
        final Date date;

        public Syussya(Date date) {
            this.date = date;
        }

        public Date getDate() {
            return date;
        }

        @Override
        public String toLogMessage(Context context) {
            return "ｼｭｯｼｬ @ " + DateUtils.formatDateTime(context, getDate().getTime(), TIME_FLAGS);
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

        @Override
        public String toLogMessage(Context context) {
            return "ﾀｲｼｬ @ " + DateUtils.formatDateTime(context, getDate().getTime(), TIME_FLAGS);
        }
    }
}
