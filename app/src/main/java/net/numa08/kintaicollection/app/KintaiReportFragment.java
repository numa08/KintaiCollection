package net.numa08.kintaicollection.app;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import net.numa08.kintaicollection.app.domein.KintaiReportTask;
import net.numa08.kintaicollection.app.models.KintaiReportRequest;
import net.numa08.kintaicollection.app.models.SyussyaReportRequest;
import net.numa08.kintaicollection.app.models.TaisyaReportRequest;

import fj.Effect;
import fj.data.Option;

public class KintaiReportFragment extends Fragment implements KintaiReportTask.KintaiReportTaskListener {

    private Option<KintaiReportTask> reportTask;

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
            reportTask = Option.some(new KintaiReportTask(request));
            reportTask.foreach(new Effect<KintaiReportTask>() {
                @Override
                public void e(KintaiReportTask task) {
                    task.registListener(KintaiReportFragment.this);
                    task.execute();
                }
            });
        }
    };

    private final View.OnClickListener tasyaButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final KintaiReportRequest request = new TaisyaReportRequest();
            reportTask = Option.some(new KintaiReportTask(request));
            reportTask.foreach(new Effect<KintaiReportTask>() {
                @Override
                public void e(KintaiReportTask task) {
                    task.registListener(KintaiReportFragment.this);
                    task.execute();
                }
            });
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        reportTask.foreach(new Effect<KintaiReportTask>() {
            @Override
            public void e(KintaiReportTask task) {
                task.unRegitListener();
            }
        });
    }

    @Override
    public void onSuccess() {
        Option<Activity> activitiy = Option.some(getActivity());
        activitiy.foreach(new Effect<Activity>() {
            @Override
            public void e(Activity activity) {
                Toast.makeText(activity, "callback called", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onProgress(int progress) {

    }

    @Override
    public void onFailed(Throwable e) {
    }

    @Override
    public void onFinish() {
        reportTask.foreach(new Effect<KintaiReportTask>() {
            @Override
            public void e(KintaiReportTask task) {
                task.unRegitListener();
            }
        });
    }

}
