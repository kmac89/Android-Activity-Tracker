package edu.berkeley.security.eventtracker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ServerActivity extends EventActivity {
	public static final String PREFERENCE_FILENAME = "ServerPrefs";
	public static final String isServerRunning = "isServerRunning";
	private Button serverButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		serverButton = (Button) findViewById(R.id.serverButton);
		serverButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				boolean isServerRunning = !isServerRunning();
				updateServerStatus(isServerRunning);
				serverButton.setText(isServerRunning ? R.string.stopEventServer
						: R.string.startEventServer);
				// what if not able to connect to internet??
			}
		});
	}

	@Override
	protected int getLayoutResource() {
		return R.layout.server;
	}

	/**
	 * Returns whether or not the web server is currently running
	 */
	public static boolean isServerRunning() {
		return settings.getBoolean(isServerRunning, false);

	}

	/**
	 * Called whenever a change to the status of the web server has been made by
	 * clicking on the serverButton. Updates preferences and starts/stops the
	 * service that begins the web server
	 * 
	 * @param serverRunning
	 */
	private void updateServerStatus(boolean serverRunning) {
		SharedPreferences.Editor prefEditor = serverSettings.edit();
		prefEditor.putBoolean(isServerRunning, serverRunning);
		prefEditor.commit();
		if (serverRunning) {
			startService(serverServiceIntent);
		} else {
			stopService(serverServiceIntent);
		}
	}

}