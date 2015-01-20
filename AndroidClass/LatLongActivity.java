package ph.busfinder.rtmitracker;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LatLongActivity  extends Activity {
	private static final long MINIMUM_DISTANCECHANGE_FOR_UPDATE = 1; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATE = 3000; // in Milliseconds

    // flag for Internet connection status
    Boolean isInternetPresent = false;
     
    // Connection detector class
    ConnectionDetector cd;
    private ProgressDialog pDialog;
	private final int REQ_CODE_SPEECH_INPUT = 100;
	
	TextView tvVacancy,tvDisplay;	
	Button btnPlus,btnMinus,btnMic;
	
	JSONParser jsonParser = new JSONParser();
	
		private static String url_update_travel_status = "http://rtmitracker.16mb.com/update_travel_status.php";

		public static String lat = null;
		public static String vacancyIntChecker = null;
		public static String lng = null;
		public static String speed = null;
		public static String placename = null;
		String Vacancy,travelNumber;
		
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.latlng_activity);
        btnMic = (Button) findViewById(R.id.btnMic);
        tvDisplay=(TextView)findViewById(R.id.tvDisplay);
        tvVacancy=(TextView)findViewById(R.id.tvVacancy);
        btnPlus=(Button)findViewById(R.id.btnPlusOne);
        btnMinus=(Button)findViewById(R.id.btnMinusOne); 
        
        travelNumber=getIntent().getExtras().getString("travelno");  
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        LocationListener ll = new myLocationListener();
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATE, MINIMUM_DISTANCECHANGE_FOR_UPDATE,ll);
        
        btnMic.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				promptSpeechInput();
			}
		});
        
        
        btnPlus.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {				
				// TODO Auto-generated method stub
				String vacant=tvVacancy.getText().toString();
				int finalVacant=Integer.parseInt(vacant)+1;							
				tvVacancy.setText(Integer.toString(finalVacant));
			}
        	
        });
        
        btnMinus.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {				
				// TODO Auto-generated method stub
				String vacant=tvVacancy.getText().toString();
				if((Integer.parseInt(vacant)==0)){
					tvVacancy.setText("0");
				}else{
				int finalVacant=Integer.parseInt(vacant)-1;							
				tvVacancy.setText(Integer.toString(finalVacant));
				}
			}
        	
        });
    }//end of onCreateMethod
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
        case R.id.settings_icon:
        	settings();
            return true;
        case R.id.action_about:
        	AboutUs();
        	return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
 
//Start of Inner Class for Location Listening
class myLocationListener implements LocationListener {
    	@Override
    	public void onLocationChanged(Location location){
    		if((location !=null)){
    			double plong=location.getLongitude();
    			double plat=location.getLatitude();
    			int curSpeed=(int) location.getSpeed();
    			DecimalFormat df=new DecimalFormat("#.000000");
    			df.format(plong);
    			df.format(plat);
    			  
    			lat=Double.toString(plat);
    			lng=Double.toString(plong);
    			speed=Integer.toString(curSpeed);
    			tvDisplay.setText("Bearing: "+location.getBearing()+"|Speed: "+speed+"Place:"+placename);
    			new saveToDB().execute();
    		
    		}	
    	}
    	
    	

		@Override
    	public void onProviderDisabled(String provider){
    		//to do here
    		showAlertDialog(LatLongActivity.this, "GPS Disabled",
                    "Please Enable GPS.", false);
    	}
    	

    	



		@Override
    	public void onProviderEnabled(String provider){
    		//to do here
    		Toast.makeText(getApplicationContext(), "GPS Enabled", Toast.LENGTH_SHORT).show();
    	}

    	@Override
    	public void onStatusChanged(String provider, int status, Bundle extras) {
    		// TODO Auto-generated method stub
    		Toast.makeText(getApplicationContext(), "Status Changed", Toast.LENGTH_SHORT).show();
    	}
    	
    }//end of Location Listener Class

//Start of saveToDB Class wich runs in background via AsynchTask  
class saveToDB extends AsyncTask<String, String, String> {
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		pDialog = new ProgressDialog(LatLongActivity.this);
		pDialog.setMessage("Saving... Please wait");
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(true);
		pDialog.show();
	}
	/*
	protected String doInBackground(String... args) {
		
		try{
			
		String vacancy=(String) tvVacancy.getText();
		String travelno="travel1";
		//Toast.makeText(getApplicationContext(), ""+travelno, Toast.LENGTH_SHORT).show();
		//Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("lat", lat));
		params.add(new BasicNameValuePair("lng", lng));
		params.add(new BasicNameValuePair("speed", speed));
		params.add(new BasicNameValuePair("vacant", vacancy));
		params.add(new BasicNameValuePair("travelno", travelno));
		
		JSONObject json = jsonParser.makeHttpRequest(url_update_travel_status,
				"POST", params);
		return json.getString(lat);
		
		}catch(Exception e){
            return new String("Exception: " + e.getMessage());
         }
	}
	*/
	
	protected String doInBackground(String... args) {
		
		try{
		Vacancy	=tvVacancy.getText().toString();
		//Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("lat", lat));
		params.add(new BasicNameValuePair("lng", lng));
		params.add(new BasicNameValuePair("travelno", travelNumber));
		params.add(new BasicNameValuePair("vacancy", Vacancy));
		params.add(new BasicNameValuePair("speed", speed));
		
		JSONObject json = jsonParser.makeHttpRequest(url_update_travel_status,
				"POST", params);
		return json.getString(lng);
		}catch(Exception e){
            return new String("Exception: " + e.getMessage());
         }
	}
	
	@Override
	   protected void onPostExecute(String result){
	      pDialog.dismiss();
	      
	    	 
} 
}

//Private Method which uses Google Voice prompt
private void promptSpeechInput() {
	Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
	intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
			RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
	intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
	intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.vacancy));
	try {
		startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
	} catch (ActivityNotFoundException a) {
		Toast.makeText(getApplicationContext(),	getString(R.string.vacancy),Toast.LENGTH_SHORT).show();
	}
}

/**
 * Receiving speech input
 * */
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);

	switch (requestCode) {
	case REQ_CODE_SPEECH_INPUT: {
		if (resultCode == RESULT_OK && null != data) {

			ArrayList<String> result = data
					.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
				
				tvVacancy.setText(result.get(0));
				
		}
		break;
	}

	}
}

@SuppressWarnings("deprecation")
public void showAlertDialog(Context context, String title, String message, Boolean status) {
    AlertDialog alertDialog = new AlertDialog.Builder(context).create();

    // Setting Dialog Title
    alertDialog.setTitle(title);

    // Setting Dialog Message
    alertDialog.setMessage(message);
     
    // Setting alert dialog icon
    alertDialog.setIcon((status) ? R.drawable.ic_action_accept : R.drawable.ic_action_warning);

    // Setting OK Button
    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
        }
    });

    // Showing Alert Message
    alertDialog.show();
}


private void settings() {
	// TODO Auto-generated method stub
	Intent i = new Intent(LatLongActivity.this, SetBodyNumber.class);
	startActivity(i);	
}

private void AboutUs() {
	Intent i = new Intent(LatLongActivity.this, AboutUs.class);
	startActivity(i);		
}

}//end of main class
