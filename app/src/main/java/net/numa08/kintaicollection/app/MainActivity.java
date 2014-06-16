package net.numa08.kintaicollection.app;

import android.os.Bundle;

import net.numa08.kintaicollection.app.domein.RegisterUserData;
import net.numa08.kintaicollection.app.views.ProgressActivity;


public class MainActivity extends ProgressActivity {

    public static class Action {
        public static final String UPDATE_KINTAI_TIMELINE = MainActivity.class.getName() + ".UPDATE_KINTAIME_TIMELINE";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final RegisterUserData registerUserData = new RegisterUserData();
        getFragmentManager().beginTransaction()
                            .add(registerUserData, "registerUserData")
                            .commit();
    }
}
