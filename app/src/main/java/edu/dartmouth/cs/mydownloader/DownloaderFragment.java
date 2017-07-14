package edu.dartmouth.cs.mydownloader;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * A placeholder fragment containing a simple view.
 *
 */
public class DownloaderFragment extends Fragment implements View.OnClickListener
{

    private Button b = null;
    private static final String DOCUMENT =
            "https://www.dartmouth.edu/~regarchive/documents/2014_orc.pdf";

    private BroadcastReceiver onEvent = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent i) {
            b.setEnabled(true);
            Log.d(getClass().getName(), "Broadcast RX: onReceive() download completed enable download button");
            Toast.makeText(getActivity(), "We are done!", Toast.LENGTH_LONG).show();

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(getClass().getName(), "onCreateView()");
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        b = (Button) v.findViewById(R.id.button);
        b.setOnClickListener(this);
        return (v);
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d(getClass().getName(), "onResume(): register broadcast RX");
        // setting up the filter for the intent based on action
        IntentFilter f = new IntentFilter(Download.ACTION_COMPLETE);
        LocalBroadcastManager bm = LocalBroadcastManager.getInstance(getActivity());
        // bind the action (intentFilter) to the event (broadcast RXr)
        bm.registerReceiver(onEvent, f); // the broadcast receiver, the event ACTION_COMPLETE
    }

    @Override
    public void onPause() {

        Log.d(getClass().getName(), "onPause(): UNregister broadcast RX");
        LocalBroadcastManager bm = LocalBroadcastManager.getInstance(getActivity());
        bm.unregisterReceiver(onEvent);

        super.onPause();
    }

    @Override
    public void onClick(View v) {

        b.setEnabled(false); // disable button
        Intent i = new Intent(getActivity(), Download.class);
        i.setData(Uri.parse(DOCUMENT));
        getActivity().startService(i);
    }

}
