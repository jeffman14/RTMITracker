package ph.busfinder.rtmitracker;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;

public class AboutUs  extends Activity {
	 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
 
        // get action bar  
        ActionBar actionBar = getActionBar();
        //showing action bar
        actionBar.show();
    }
    
    
}
