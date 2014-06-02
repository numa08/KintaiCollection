package net.numa08.kintaicollection.app;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.numa08.kintaicollection.app.domein.api.KintaiCollectionWebClient;
import net.numa08.kintaicollection.app.domein.api.request.KintaiCollectionApiRequest;
import net.numa08.kintaicollection.app.domein.api.request.SyussyaRequest;
import net.numa08.kintaicollection.app.domein.api.request.TaisyaRequest;
import net.numa08.kintaicollection.app.models.timeline.User;

import java.util.Date;

import fj.Effect;
import fj.F;
import fj.P2;
import fj.P3;
import fj.data.Option;

public class KintaiReportFragment extends Fragment {

    private Option<KintaiCollectionWebClient> client = Option.none();

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
        client = Option.fromNull(getActivity())
                       .map(new F<Activity, KintaiCollectionWebClient>() {
                           @Override
                           public KintaiCollectionWebClient f(Activity activity) {
                               return new KintaiCollectionWebClient(activity.getApplicationContext());
                           }
                       });
    }

    private final View.OnClickListener syussyaButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final SyussyaRequest request = new SyussyaRequest(new Date(), fetchUser().id);
            client.bindProduct(Option.some(request), Option.some(syussyaRequestCallback)).foreach(new Effect<P3<KintaiCollectionWebClient, SyussyaRequest, SyussyaRequest.SyussyaRequestCallback>>() {
                @Override
                public void e(P3<KintaiCollectionWebClient, SyussyaRequest, SyussyaRequest.SyussyaRequestCallback> product) {
                    product._1().syussya(product._2(), product._3());
                }
            });
        }
    };

    private final View.OnClickListener tasyaButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final TaisyaRequest request = new TaisyaRequest(new Date(), fetchUser().id);
            client.bindProduct(Option.some(request), Option.some(taisyaRequestCallback)).foreach(new Effect<P3<KintaiCollectionWebClient, TaisyaRequest, TaisyaRequest.TaisyaRequestCallback>>() {
                @Override
                public void e(P3<KintaiCollectionWebClient, TaisyaRequest, TaisyaRequest.TaisyaRequestCallback> product) {
                    product._1().taisya(product._2(), product._3());
                }
            });
        }
    };


    private final SyussyaRequest.SyussyaRequestCallback syussyaRequestCallback = new SyussyaRequest.SyussyaRequestCallback() {
        @Override
        public void onSyussyaSuccess() {

        }
    };

    private final TaisyaRequest.TaisyaRequestCallback taisyaRequestCallback = new TaisyaRequest.TaisyaRequestCallback() {
        @Override
        public void onTaisyaSuccess() {

        }
    };

    private User fetchUser(){
        return null;
    }

}
