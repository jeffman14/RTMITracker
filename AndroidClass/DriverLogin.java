package ph.busfinder.rtmitracker;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class DriverLogin  extends Activity {
	
		private ProgressDialog pDialog;
		String driverid;
		private EditText usernameField,passwordField;
		private Button btnLogin;
		
		// flag for Internet connection status
	    Boolean isInternetPresent = false;
	     
	    // Connection detector class
	    ConnectionDetector cd;

	   @Override 
	   protected void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      setContentView(R.layout.driver_login);
	      
	      usernameField = (EditText)findViewById(R.id.txtDriverIDUpdate);
	      passwordField = (EditText)findViewById(R.id.txtDriverPassword);
	      btnLogin=(Button)findViewById(R.id.btnAideLogin);
	      
	      cd = new ConnectionDetector(getApplicationContext());
	      btnLogin.setOnClickListener(new Button.OnClickListener(){

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
                    showAlertDialog(DriverLogin.this, "Connection Error",
                            "You don't have internet connection.", false);
                }
				
			}
	    	  
	      });
	   }
	   
	   
	   @Override
	   public boolean onCreateOptionsMenu(Menu menu) {
	      // Inflate the menu; this adds items to the action bar if it is present.
	      getMenuInflater().inflate(R.menu.main, menu);
	      return true;
	   }
	   public void loginPost(View view){
	      String username = usernameField.getText().toString();
	      String password = passwordField.getText().toString();
	      new SigninActivity(this).execute(username,password);
	      
	   }
	   
	   @SuppressWarnings("deprecation")
	   public void displayDialog(){
		   AlertDialog alertDialog = new AlertDialog.Builder(
					DriverLogin.this).create();

			// Setting Dialog Title
			alertDialog.setTitle("Warning!");

			// Setting Dialog Message
			alertDialog.setMessage("Invalid ID or Password");

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
     
	   
	   
	   class SigninActivity  extends AsyncTask<String,Void,String>{

		   public SigninActivity(Context context) {
		   }

		   @Override
			protected void onPreExecute() {
				super.onPreExecute();
				pDialog = new ProgressDialog(DriverLogin.this);
				pDialog.setMessage("Signing in... Please wait");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(true);
				pDialog.show();
			}
		   
		   @Override
		   protected String doInBackground(String... arg0) {
		      
		         try{
		            String username = (String)arg0[0];
		            String password = (String)arg0[1];
		            String link="http://rtmitracker.16mb.com/driver_login.php";
		            
		            String data  = URLEncoder.encode("username", "UTF-8") 
		            + "=" + URLEncoder.encode(username, "UTF-8");
		            
		            data += "&" + URLEncoder.encode("password", "UTF-8") 
		            + "=" + URLEncoder.encode(password, "UTF-8");
		            
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
		    	 
		    	 driverid=json_data.getString("driverno");
		    	 Intent myIntent = new Intent(DriverLogin.this, TourAideLogin.class);
		    	 	myIntent.putExtra("driverid", driverid);
					startActivity(myIntent);
		    	 }
		    	 catch(Exception e){
		    		 
		    	 }
		    	 
		     }
		     else{
		    	 	displayDialog();
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
    
}

