package ph.busfinder.rtmitracker;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
	
	Button driverLogin;
	//ProgressBar pb;
	private ProgressDialog pDialog;
	TextView tvIndicator;
	ImageButton btnFix, btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        LocationListener ll = new myLocationListener();
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,ll);
        
        //pb=(ProgressBar)findViewById(R.id.progressBar1);
        tvIndicator=(TextView)findViewById(R.id.indicator);
        btnFix=(ImageButton)findViewById(R.id.btnFixLocation);        
        btnFix.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				displayDialog();
				
			}
        	
        });
        
        btnSignIn=(ImageButton)findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(MainActivity.this, DriverLogin.class);
				startActivity(i);
				
			}
        	
        });
        
        
        displayDialog();
        
    }

    private void displayDialog() {
		// TODO Auto-generated method stub
    	pDialog = new ProgressDialog(MainActivity.this);
		pDialog.setMessage("Fixing Location from GPS Satellites. The dialog will automatically dismiss after the fix.");
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(true);
		pDialog.show();
		
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    	MenuInflater mif = getMenuInflater();
    	mif.inflate(R.menu.main_activity_action, menu);
    	return super.onCreateOptionsMenu(menu);
    }
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
 
	private void settings() {
		// TODO Auto-generated method stub
    	Intent i = new Intent(MainActivity.this, SetBodyNumber.class);
		startActivity(i);	
	}

	private void AboutUs() {
    	Intent i = new Intent(MainActivity.this, AboutUs.class);
		startActivity(i);		
	}
	
	//Start of Inner Class for Location Listening
	class myLocationListener implements LocationListener {
	    	@Override
	    	public void onLocationChanged(Location location){
	    		if((location !=null)){
	    			int myAltitude=(int) location.getSpeed();	    			
	    		
	    		}	
	    	}	
	    	

			@Override
	    	public void onProviderDisabled(String provider){
	    		//to do here
	    		showAlertDialog(MainActivity.this, "GPS Disabled",
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
	    		Toast.makeText(getApplicationContext(), "Location has been fixed", Toast.LENGTH_SHORT).show();
	    		pDialog.dismiss();
	    		tvIndicator.setBackgroundColor(R.drawable.tv_indicator);
	    		tvIndicator.setText("Location has been fixed.");
	    		//pb.setVisibility(Gone);
	    	}
	    	
	    }//end of Location Listener Class
	
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
}




    
   


    
    

