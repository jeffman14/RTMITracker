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
	DBAdapter myDb;
	private EditText txtTravelNo, txtDriverIDUpdate,txtAideIDUpdate;
	Button btnUpdateEmpTravel;
	
	private static String url_insert_to_DB = "http://rtmitracker.16mb.com/insertUpdate_tblEmpTravel.php";
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
        
        //open DB
	    //openDB();
	    
        btnUpdateEmpTravel=(Button)findViewById(R.id.btnUpdateEmpTravel2);
        txtTravelNo=(EditText)findViewById(R.id.txtTravelNo);
        txtDriverIDUpdate=(EditText)findViewById(R.id.txtDriverLogin);
        txtAideIDUpdate=(EditText)findViewById(R.id.txtAideIDUpdate);
        
        //get driver id from local database	      
	   // Cursor cursor = myDb.getAllRows();
	    //displayRecordSet(cursor); 
        txtDriverIDUpdate.setText(driverno);
        txtAideIDUpdate.setText(aideno);
	    
	    cd = new ConnectionDetector(getApplicationContext());  
        btnUpdateEmpTravel.setOnClickListener(new Button.OnClickListener(){
        	
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub							
				// get Internet status
                isInternetPresent = cd.isConnectingToInternet();
                // check for Internet status
                if((txtTravelNo.getText().toString().matches(""))||(txtDriverIDUpdate.getText().toString().matches(""))||(txtAideIDUpdate.getText().toString().matches(""))) {
                	// Ask user to fill up all required field
                    showAlertDialog(TravelDetails.this, "Empty Fields",
                           "Fill up required fields.", false);                	
                }
                else if (isInternetPresent) {
                    // Internet Connection is Present
                	new insertToDB().execute();   	
                }       
                else{
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
    
    class insertToDB extends AsyncTask<String, String, String> {
    	
    	@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(TravelDetails.this);
			pDialog.setMessage("Saving... Please wait");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
    	
    	protected String doInBackground(String... args) {
    		    		
    		try{String driverno=txtDriverIDUpdate.getText().toString();
    		String aideno=txtAideIDUpdate.getText().toString();
    		String travelno=txtTravelNo.getText().toString();
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
		      
		     if(result!=""){		    	 
			    	 try{		    	 
			    	 
			    		 JSONArray jArray=new JSONArray(result);
				    	 JSONObject json_data=null;	    	 
				    	 json_data=jArray.getJSONObject(0);
				    	 
				    	 int success=json_data.getInt("success");
				    	 String message=json_data.getString("message");
				    	 
				    	 Toast.makeText(getApplicationContext(), ""+success+"|"+message, Toast.LENGTH_SHORT).show();
			    	 
			    	 String travelno=txtTravelNo.getText().toString();
			    	 updateTravelNo(1,travelno);
			    	 
			    	 
			    	 
			    	 }
			    	 catch(Exception e){
			    		 
			    	 }
		     }else{
		    	 Toast.makeText(getApplicationContext(), ""+success+"|"+message, Toast.LENGTH_SHORT).show();
		     }
    }  
    }
    
    public void DisplayRecords(View v) {		
		Cursor cursor = myDb.getAllRows();
		displayRecordSet(cursor);
	}
   private void openDB() {
		myDb = new DBAdapter(this);
		myDb.open();
	}
   private void displayRecordSet(Cursor cursor) {
		//reset the cursor from start
		if (cursor.moveToFirst()) {
			do {
				
				String driver = cursor.getString(DBAdapter.COL_DRIVERNO);
				String aide = cursor.getString(DBAdapter.COL_AIDENO);
				String travelno = cursor.getString(DBAdapter.COL_TRAVELNO);
				
				setData(driver,aide,travelno);
				Toast.makeText(getApplicationContext(), ""+driver+"|"+aide+"|"+travelno, Toast.LENGTH_SHORT).show();
			} while(cursor.moveToNext());
		}
		
		// Close the cursor to avoid a resource leak.
		cursor.close();
	}
   private void setData(String driver,String aide,String travelno) {
		// TODO Auto-generated method stub
	   txtDriverIDUpdate.setText(driver);
	   txtAideIDUpdate.setText(aide);
		
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
   
   public void updateTravelNo(long rowid,String travelno){
		myDb.updateTravelNo(rowid,travelno);
		}
   
}
