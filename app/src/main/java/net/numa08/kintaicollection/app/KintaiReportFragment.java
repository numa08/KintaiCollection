package net.numa08.kintaicollection.app;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class KintaiReportFragment extends Fragment {


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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private final View.OnClickListener syussyaButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        }
    };

    private final View.OnClickListener tasyaButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        }
    };


}
