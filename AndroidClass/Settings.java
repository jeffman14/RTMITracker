package ph.busfinder.rtmitracker;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

public class Settings  extends Activity {
	EditText bodyno,driverid,touraideid;
	String driveridExtra,touraideidExtra;
	 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity); 
        // get action bar  
        ActionBar actionBar = getActionBar();
        //showing action bar
        actionBar.show();
        
        driverid=(EditText)findViewById(R.id.txtDriverIDUpdate);
        touraideid=(EditText)findViewById(R.id.txtDriverPassword);
        
        //getting driverID*******************************************************comment sa ang extra
	    //driveridExtra=getIntent().getExtras().getString("driverid");
	    //touraideidExtra=getIntent().getExtras().getString("empno");
	      
	    driverid.setText(driveridExtra);
	    touraideid.setText(touraideidExtra);
	    
	      Toast.makeText(getApplicationContext(), "Driver:"+driveridExtra+"|Aide:"+touraideidExtra, Toast.LENGTH_SHORT).show();
    }
        
    
}
