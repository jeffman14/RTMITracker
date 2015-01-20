package ph.busfinder.rtmitracker;

import android.app.ActionBar;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SetBodyNumber  extends Activity {
	DBAdapter myDb;
	EditText txtBodyNo, txtPlateNo,txtBusType;
	Button btnSaveBodyNo;
	String body, plate,Type;
	 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.travel_details); 
        // get action bar  
        ActionBar actionBar = getActionBar();
        //showing action bar
        actionBar.show();        
        //open DB
        openDB();        
        
        txtBodyNo=(EditText)findViewById(R.id.txtBodyNo);
        txtPlateNo=(EditText)findViewById(R.id.txtPlateNo);
        txtBusType=(EditText)findViewById(R.id.txtBusType);
        
        Cursor cursor = myDb.getAllRows();
		displayRecordSet(cursor);
		
        btnSaveBodyNo=(Button)findViewById(R.id.btnSaveBodyNo);
        btnSaveBodyNo.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				body=txtBodyNo.getText().toString();
				plate=txtPlateNo.getText().toString();
				Type=txtBusType.getText().toString();
				update(1,body,Type,plate);
			}
        	
        });
       
    }
 
public void update(long rowid,String bodyNo, String busType, String plateNo){
	myDb.updateRow(rowid,bodyNo,busType, plateNo);
	Toast.makeText(getApplicationContext(), "Changes Saved!", Toast.LENGTH_SHORT).show();
	}

public void AddRecord(String bodyno, String plateno, String bustype) {	
	
	long newId = myDb.insertRow(bodyno, plateno, bustype);
	Cursor cursor = myDb.getRow(newId);
	displayRecordSet(cursor);
}
    
    public void DisplayRecords(View v) {		
		Cursor cursor = myDb.getAllRows();
		displayRecordSet(cursor);
	}

    @Override
	protected void onDestroy() {
		super.onDestroy();	
		closeDB();
	}

	private void openDB() {
		myDb = new DBAdapter(this);
		myDb.open();
	}
	
	private void closeDB() {
		myDb.close();
	}
	 
	private void displayRecordSet(Cursor cursor) {
				//reset the cursor from start
				if (cursor.moveToFirst()) {
					do {
						
						String bodyno = cursor.getString(DBAdapter.COL_BODYNO);
						String plateno = cursor.getString(DBAdapter.COL_PLATENO);
						String bustype = cursor.getString(DBAdapter.COL_BUSTYPE);
						
						setData(bodyno,plateno,bustype);						
					} while(cursor.moveToNext());
				}
				
				// Close the cursor to avoid a resource leak.
				cursor.close();
			}

			private void setData(String bodyno, String plateno, String bustype) {
				// TODO Auto-generated method stub
				txtBodyNo.setText(bodyno);
				txtPlateNo.setText(plateno);
				txtBusType.setText(bustype);
				
			}
}
