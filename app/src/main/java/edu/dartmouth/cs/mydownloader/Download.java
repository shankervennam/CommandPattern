package edu.dartmouth.cs.mydownloader;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class Download extends IntentService {

    public static final String ACTION_COMPLETE =
            "edu.dartmouth.cs.mydownloader.action.COMPLETE";

    // pass the name of the worker thread. Need this constructor
    public Download() {
        super("Download");
    }

    @Override
    public void onHandleIntent(Intent i) {

        // this is a background thread

        Log.e(getClass().getName(), "Background thread active: onHandleIntent");

        try {

            // create a downloads directory if doesn't exist
            File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            root.mkdirs();

            // getLastPathSegment() gets the filename potion of the URI. e.g., 2014_orc.pdf
            File output = new File(root, i.getData().getLastPathSegment());

            if (output.exists()) {
                output.delete();
            }

            URL url = new URL(i.getData().toString());
            HttpURLConnection c = (HttpURLConnection)url.openConnection();

            FileOutputStream fos = new FileOutputStream(output.getPath());
            BufferedOutputStream out = new BufferedOutputStream(fos);

            try {
                InputStream in = c.getInputStream();
                byte[] buffer = new byte[8192];
                int len = 0;

                // read until the end of the input stream
                while ((len=in.read(buffer)) >= 0) {
                    Log.d(getClass().getName(), "Background thread active: len is " + len + " bytes");
                    out.write(buffer, 0, len);
                }
                out.flush();
            }
            finally {

                fos.getFD().sync();
                out.close();
                c.disconnect();
            }

            // inform the UI we are done
            LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(ACTION_COMPLETE));
            Log.d(getClass().getName(), "Background thread finished: broadcast sent" );

        } catch (IOException e2) {
            Log.e(getClass().getName(), "Exception in download", e2);
        }

    }
}
