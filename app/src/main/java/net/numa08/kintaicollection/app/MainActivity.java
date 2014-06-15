package net.numa08.kintaicollection.app;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
