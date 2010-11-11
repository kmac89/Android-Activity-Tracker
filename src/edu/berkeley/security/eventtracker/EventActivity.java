package edu.berkeley.security.eventtracker;

import edu.berkeley.security.eventtracker.eventdata.EventEntry;
import edu.berkeley.security.eventtracker.eventdata.EventManager;
import edu.berkeley.security.eventtracker.eventdata.GPSCoordinates;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * The main activity that all event-related activities extend. It houses a
 * toolbar and a global EventManager.
 */
abstract public class EventActivity extends Activity {
	private static final int trackingStringID = R.string.toolbarTracking;
	private static final int notTrackingStringID = R.string.toolbarNotTracking;
	private TextView textViewIsTracking;
	protected EventManager mEventManager;
	private static SampleLocationListener mLocationListener;
	private static LocationManager m_location_manager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		ViewStub v = (ViewStub) findViewById(R.id.content_view);
		v.setLayoutResource(getLayoutResource());
		v.inflate();

		initializeToolbar();

		mEventManager = EventManager.getManager(this);
		if (mLocationListener == null) {
			mLocationListener = new SampleLocationListener();
		}
		if (m_location_manager == null) {
			m_location_manager = (LocationManager) getSystemService(LOCATION_SERVICE);
			try {

				m_location_manager.requestLocationUpdates(
						LocationManager.GPS_PROVIDER, 60000, 100,
						mLocationListener);
				Boolean gpsEnabled = m_location_manager
						.isProviderEnabled(LocationManager.GPS_PROVIDER);
				Log.d("onCreateonCreate", String.valueOf(gpsEnabled));
			} catch (Exception e) {

			}
		}

	}

	/**
	 * Initializes the toolbar onClickListeners and intializes references to
	 * toolbar views. The left button is initialized to the edit activity
	 * button.
	 */
	protected void initializeToolbar() {
		textViewIsTracking = (TextView) findViewById(R.id.toolbar_center);

		findViewById(R.id.toolbar_right_option).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						startSettingsActivity();
					}
				});
		findViewById(R.id.toolbar_left_option).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						startEditEventActivity();
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.event_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.settings_option:
			startSettingsActivity();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		refreshState();
		updateTrackingUI();
	}

	/**
	 * Refreshes the state from the database. Called prior to any calls to
	 * isTracking() and updateTrackingUI().
	 */
	protected void refreshState() {
	}

	/**
	 * Launches the ListEvents activity.
	 */
	protected void startListEventsActivity() {
		Intent listIntent = new Intent(this, ListEvents.class);
		startActivity(listIntent);
	}

	/**
	 * Launches the Settings activity.
	 */
	protected void startSettingsActivity() {
		Intent settingsIntent = new Intent(this, Settings.class);
		startActivity(settingsIntent);
	}

	/**
	 * Launches the EditEvent activity.
	 */
	protected void startEditEventActivity() {
		Intent settingsIntent = new Intent(this, EditEvent.class);
		startActivity(settingsIntent);
	}

	/**
	 * Calls isTracking() and updates the UI accordingly.
	 * 
	 * @return True if it an event is tracked, false otherwise.
	 */
	protected boolean updateTrackingUI() {
		boolean isTracking = isTracking();
		updateTrackingUI(isTracking);
		return isTracking;
	}

	/**
	 * Overrides calling isTracking() with the given boolean.
	 * 
	 * @param isTracking
	 *            Whether or not an event is being tracked.
	 */
	protected void updateTrackingUI(boolean isTracking) {
		textViewIsTracking.setText(isTracking ? trackingStringID
				: notTrackingStringID);
	}

	/**
	 * @return Whether or not an activity is being tracking. Should be preceded
	 *         by a call to refresh state, if not already refreshed.
	 */
	protected boolean isTracking() {
		return mEventManager.isTracking();
	}

	/**
	 * @return the current event
	 */
	public EventEntry getCurrentEvent(){
		return mEventManager.getCurrentEvent();
	}
	
	/**
	 * @return The layout resource to inflate in onCreate.
	 */
	abstract protected int getLayoutResource();

	// GPS
	private class SampleLocationListener implements LocationListener {
		public void onLocationChanged(Location location) {
			EventEntry currentEvent = getCurrentEvent();
			if (location != null && currentEvent != null) {

				mEventManager.addGPSCoordinates(new GPSCoordinates(location
						.getLatitude(), location.getLongitude()),
						currentEvent.mDbRowID);

			}
		}

		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			Log.d("SampleLocationListener onProviderDisabled", provider);
		}

		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			Log.d("SampleLocationListener onProviderEnabled", provider);
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			Log.d("SampleLocationListener onStatusChanged", provider);
		}
	}

}
