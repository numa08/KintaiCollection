package net.numa08.kintaicollection.app.domein;

import android.os.AsyncTask;

import net.numa08.kintaicollection.app.models.KintaiReportRequest;

import java.lang.ref.WeakReference;

public class KintaiReportTask extends AsyncTask<Void, Integer, Boolean>{

    public interface KintaiReportTaskListener {
        public void onSuccess();
        public void onFinish();
        public void onProgress(int progress);
        public void onFailed(Throwable e);
    }

    private final KintaiReportRequest request;
    private WeakReference<KintaiReportTaskListener> listener;

    public KintaiReportTask(KintaiReportRequest request) {
        this.request = request;
    }

    public void registListener(KintaiReportTaskListener listener) {
        this.listener = new WeakReference<>(listener);
    }

    public void unRegitListener() {
        if (listener == null) {
            return;
        }
        if (listener.get() == null) {
            return;
        }
        listener.clear();
    }


    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        return null;
    }

    @Override
    protected void onCancelled(Boolean aBoolean) {
        super.onCancelled(aBoolean);
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
    }
}
