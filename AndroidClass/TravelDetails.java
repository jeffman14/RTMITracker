package ph.busfinder.rtmitracker;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import ph.busfinder.rtmitracker.DriverLogin.SigninActivity;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TravelDetails  extends Activity {
	private EditText txtTravelNo, txtDriverIDUpdate,txtAideIDUpdate;
	Button btnUpdateEmpTravel;
	
	JSONParser jsonParser = new JSONParser();
	private ProgressDialog pDialog;
	int success;
	String message,driverno,aideno;
	
	// flag for Internet connection status
    Boolean isInternetPresent = false;
     
    // Connection detector class
    ConnectionDetector cd;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.travel_details);
        
        	Intent intent = getIntent();
        	driverno = intent.getStringExtra("driverno");
        	aideno = intent.getStringExtra("aideno");
        	Toast.makeText(getApplicationContext(), ""+driverno+"|"+aideno, Toast.LENGTH_SHORT).show();
        
       
	    
        btnUpdateEmpTravel=(Button)findViewById(R.id.btnUpdateEmpTravel2);
        txtTravelNo=(EditText)findViewById(R.id.txtTravelNo);
        txtDriverIDUpdate=(EditText)findViewById(R.id.txtDriverLogin);
        txtAideIDUpdate=(EditText)findViewById(R.id.txtAideIDUpdate);
        
        txtDriverIDUpdate.setText(driverno);
        txtAideIDUpdate.setText(aideno);
	    
	    cd = new ConnectionDetector(getApplicationContext());  
        btnUpdateEmpTravel.setOnClickListener(new Button.OnClickListener(){
        	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// get Internet status
	            isInternetPresent = cd.isConnectingToInternet();
	            // check for Internet status
	            if (isInternetPresent) {
	                // Internet Connection is Present
	                // make HTTP requests
	            	loginPost(v);
	            	
	            } else {
	                // Internet connection is not present
	                // Ask user to connect to Internet
	                showAlertDialog(TravelDetails.this, "Connection Error",
	                        "You don't have internet connection.", false);
	            }
				
        }
        });
        // get action bar  
        ActionBar actionBar = getActionBar();
        //showing action bar
        actionBar.show();
    }
    
    class SigninActivity  extends AsyncTask<String,Void,String>{

 	   public SigninActivity(Context context) {
 	   }

 	   @Override
 		protected void onPreExecute() {
 			super.onPreExecute();
 			pDialog = new ProgressDialog(TravelDetails.this);
 			pDialog.setMessage("Signing in... Please wait");
 			pDialog.setIndeterminate(false);
 			pDialog.setCancelable(true);
 			pDialog.show();
 		}
 	   
 	   @Override
 	   protected String doInBackground(String... arg0) {
 	      
 	         try{
 	            String driver = (String)arg0[0];
 	            String aide = (String)arg0[1];
 	            String travel = (String)arg0[2];
 	            String link="http://rtmitracker.16mb.com/insertUpdate_tblEmpTravel.php";
 	            
 	            String data  = URLEncoder.encode("driver", "UTF-8") 
 	            + "=" + URLEncoder.encode(driver, "UTF-8");
 	            
 	            data += "&" + URLEncoder.encode("aide", "UTF-8") 
 	            + "=" + URLEncoder.encode(aide, "UTF-8");
 	            
 	           data += "&" + URLEncoder.encode("travel", "UTF-8") 
 	   	            + "=" + URLEncoder.encode(travel, "UTF-8");
 	            
 	            URL url = new URL(link);
 	            URLConnection conn = url.openConnection(); 
 	            conn.setDoOutput(true); 
 	            OutputStreamWriter wr = new OutputStreamWriter
 	            (conn.getOutputStream()); 
 	            wr.write( data ); 
 	            wr.flush(); 
 	            BufferedReader reader = new BufferedReader
 	            (new InputStreamReader(conn.getInputStream()));
 	            StringBuilder sb = new StringBuilder();
 	            String line = null;
 	            
 	            // Read Server Response
 	            while((line = reader.readLine()) != null)
 	            {
 	               sb.append(line);
 	               break;
 	            }
 	            return sb.toString();
 	         }catch(Exception e){
 	            return new String("Exception: " + e.getMessage());
 	         }
 	      }
 	   @Override
 	   protected void onPostExecute(String result){
 	      pDialog.dismiss();
 	      
 	     if(result!=""){
 	    	 
 	    	 try{    	 
 	    	 
 	    	 JSONArray jArray=new JSONArray(result);
 	    	 JSONObject json_data=null;	    	 
 	    	 json_data=jArray.getJSONObject(0);
 	    	 
 	    	 message=json_data.getString("message");
 	    	 int success=json_data.getInt("success");	    	 
 	    	 
 	    	 if(success==1){
 	    		 Intent myIntent = new Intent(TravelDetails.this, LatLongActivity.class);
 	    		 //myIntent.putExtra("driverno", usernameField.getText().toString());
 				startActivity(myIntent);
 	    	 }else{
 	    		 displayDialog();
 	    	 }  	 
 	    	 
 	    	 }
 	    	 catch(Exception e){
 	    		 
 	    	 }	    	 
 	     }     
 	     
 	   }

	 @SuppressWarnings("deprecation")
   private void displayDialog(){
	   AlertDialog alertDialog = new AlertDialog.Builder(
				TravelDetails.this).create();

		// Setting Dialog Title
		alertDialog.setTitle("Warning!");

		// Setting Dialog Message
		alertDialog.setMessage(message);

		// Setting Icon to Dialog
		alertDialog.setIcon(R.drawable.ic_action_warning);

		// Setting OK Button
		alertDialog.setButton("OK",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog,
							int which) {
					}
				});

		// Showing Alert Message
		alertDialog.show();
   }

 	}
    
    public void loginPost(View view){
        String driver = txtDriverIDUpdate.getText().toString();
        String aide = txtAideIDUpdate.getText().toString();
        String travel = txtTravelNo.getText().toString();
        new SigninActivity(this).execute(driver,aide,travel);
        
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
}
