package com.calibreofflinesearcher;

import android.os.Bundle;
import android.view.View;
import android.app.Activity;

public class ItemDetails extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
    }
    
    public void sendBackButton(View view) {
        finish();
    }
}
