package net.numa08.kintaicollection.app;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.numa08.kintaicollection.app.domein.KintaiReportTask;
import net.numa08.kintaicollection.app.models.KintaiReportRequest;
import net.numa08.kintaicollection.app.models.SyussyaReportRequest;
import net.numa08.kintaicollection.app.models.TaisyaReportRequest;

public class KintaiReportFragment extends Fragment implements KintaiReportTask.KintaiReportTaskListener {

    private KintaiReportTask reportTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_kintai_report, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.syussyaButton).setOnClickListener(syussyaButtonClicked);
        view.findViewById(R.id.taisyaButton).setOnClickListener(tasyaButtonClicked);
    }


    private final View.OnClickListener syussyaButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final KintaiReportRequest request = new SyussyaReportRequest();
            reportTask = new KintaiReportTask(request);
            reportTask.registListener(KintaiReportFragment.this);
            reportTask.execute();
        }
    };

    private final View.OnClickListener tasyaButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final KintaiReportRequest request = new TaisyaReportRequest();
            reportTask = new KintaiReportTask(request);
            reportTask.registListener(KintaiReportFragment.this);
            reportTask.execute();
        }
    };

    @Override
    public void onPause() {
        if (reportTask != null) {
            reportTask.unRegitListener();
        }
    }

    @Override
    public void onSuccess() {
        reportTask.unRegitListener();
    }

    @Override
    public void onProgress(int progress) {

    }

    @Override
    public void onFailed(Throwable e) {
        reportTask.unRegitListener();
    }

    @Override
    public void onFinish() {}

}
