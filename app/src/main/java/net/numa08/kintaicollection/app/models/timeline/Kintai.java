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

    public Status status();

    public enum Status {
        Syussya("ｼｭｯｼｬ"),
        Taisya("ﾀｲｼｬ");

        String status;

        Status(String s) {
            this.status = s;
        }

        public boolean equals(String status) {
            if (status == null) {
                return false;
            }
            return this.status.equals(status);
        }
    }

    public static final class Syussya implements Kintai {
        final Date date;

        public Syussya(Date date) {
            this.date = date;
        }

        public Date getDate() {
            return date;
        }

        @Override
        public Status status() {
            return Status.Syussya;
        }

        @Override
        public String toLogMessage(Context context) {
            return "ｼｭｯｼｬ @ " + DateUtils.formatDateTime(context, getDate().getTime(), TIME_FLAGS);
        }

        @Override
        public String toString() {
            return "Syussya{" +
                    "date=" + date +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Syussya)) return false;

            Syussya syussya = (Syussya) o;

            if (date != null ? !date.equals(syussya.date) : syussya.date != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return date != null ? date.hashCode() : 0;
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
        public Status status() {
            return Status.Taisya;
        }

        @Override
        public String toLogMessage(Context context) {
            return "ﾀｲｼｬ @ " + DateUtils.formatDateTime(context, getDate().getTime(), TIME_FLAGS);
        }

        @Override
        public String toString() {
            return "Taisya{" +
                    "date=" + date +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Taisya)) return false;

            Taisya taisya = (Taisya) o;

            if (date != null ? !date.equals(taisya.date) : taisya.date != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return date != null ? date.hashCode() : 0;
        }
    }
}
