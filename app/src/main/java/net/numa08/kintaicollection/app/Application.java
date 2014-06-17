package net.numa08.kintaicollection.app;

import com.deploygate.sdk.DeployGate;

public class Application extends android.app.Application{

    @Override
    public void onCreate() {
        super.onCreate();
        DeployGate.install(this);
    }
}
