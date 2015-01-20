package ph.busfinder.rtmitracker;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateDriverNoAideNo  extends Activity {
	private EditText travelNo, txtDriverIDUpdate,txtAideIDUpdate;
	String driveridExtra, aideExtra;
	Button btnUpdateEmpTravel;
	
	private static String url_insert_to_DB = "http://rtmitracker.16mb.com/insert_tblEmpTravel.php";
	JSONParser jsonParser = new JSONParser();
	private ProgressDialog pDialog;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_driverno_aideno);
        
        btnUpdateEmpTravel=(Button)findViewById(R.id.btnUpdateEmpTravel2);
        travelNo=(EditText)findViewById(R.id.txtTravelNo);
        txtDriverIDUpdate=(EditText)findViewById(R.id.txtDriverIDUpdate);
        txtAideIDUpdate=(EditText)findViewById(R.id.txtAideIDUpdate);
        
        driveridExtra=getIntent().getExtras().getString("driverid");
        aideExtra=getIntent().getExtras().getString("aideid");
        
        txtDriverIDUpdate.setText(driveridExtra);
        txtAideIDUpdate.setText(aideExtra);
        
        btnUpdateEmpTravel.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "Click", Toast.LENGTH_SHORT).show();
				new insertToDB().execute();
			}
        	
        });
        // get action bar  
        ActionBar actionBar = getActionBar();
        //showing action bar
        actionBar.show();
    }
    
    class insertToDB extends AsyncTask<String, String, String> {
    	
    	@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(UpdateDriverNoAideNo.this);
			pDialog.setMessage("Saving... Please wait");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
    	
    	protected String doInBackground(String... args) {
    		
    		try{
    			
    		String driverno=txtDriverIDUpdate.getText().toString();
    		String aideno=txtAideIDUpdate.getText().toString();
    		String travelno=travelNo.getText().toString();
    		//Building Parameters
    		List<NameValuePair> params = new ArrayList<NameValuePair>();
    		params.add(new BasicNameValuePair("driverid", driverno));
    		params.add(new BasicNameValuePair("aideid", aideno));
    		params.add(new BasicNameValuePair("travelno", travelno));
    		
    		JSONObject json = jsonParser.makeHttpRequest(url_insert_to_DB,
    				"POST", params);
    		return json.getString(driverno);
    		}catch(Exception e){
                return new String("Exception: " + e.getMessage());
             }
    	}
    	
    	@Override
		   protected void onPostExecute(String result){
		      pDialog.dismiss();
		      
		     String travelnumber = travelNo.getText().toString();
		     Intent myIntent = new Intent(UpdateDriverNoAideNo.this, LatLongActivity.class);
		     myIntent.putExtra("travelno", travelnumber);
		     startActivity(myIntent);
		    	 
    }  
}
}
