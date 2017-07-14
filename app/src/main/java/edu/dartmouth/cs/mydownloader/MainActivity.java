package edu.dartmouth.cs.mydownloader;

import android.app.Activity;
import android.os.Bundle;

// Like this example:
// IntentService (with background thread
// Activity, fragment and IntentService
// file handling (write)
// HTTP client to server
// Broadcast receiver
// You can show the students the "Network" TAB in the Android Monitor stats

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getFragmentManager().findFragmentById(android.R.id.content) == null) {

            getFragmentManager().beginTransaction().add(android.R.id.content,
                    new DownloaderFragment()).commit();
        }

    }

}
