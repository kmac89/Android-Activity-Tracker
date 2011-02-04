package edu.berkeley.security.eventtracker.network;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import edu.berkeley.security.eventtracker.eventdata.EventEntry;

public class Synchronizer extends IntentService {

	public Synchronizer() {
		super("Synchronizer");
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
		
		Bundle bundle=intent.getExtras();
		
		EventEntry event;
		
		event = (EventEntry) bundle.getSerializable("EventData");
		
		if(event != null){
			Networking.sendData((EventEntry) event);
		}else{
			Networking.sendRegistration();
		}
		
		
	}

}
