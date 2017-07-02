package com.calibreofflinesearcher;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.calibreofflinesearcher.dbutil.QueryExecuter;
import com.calibreofflinesearcher.pojo.FullBookInfo;

import android.app.Activity;

public class ItemDetailsActivity extends Activity {

	private QueryExecuter queryExecuter; 
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        
        Bundle bundle = getIntent().getExtras();
        
        try {
        	
        	FullBookInfo fullBookInfo = QueryExecuter.getInstance().getFullBookInfo(bundle.getInt(Constants.id));
	        final TextView author = (TextView) findViewById(R.id.textAuthor);         
	        author.setText(fullBookInfo.author);        
	        final TextView title = (TextView) findViewById(R.id.textTitle);         
	        title.setText(fullBookInfo.title);
	        final TextView publishDate = (TextView) findViewById(R.id.textpublishDate);         
	        publishDate.setText(fullBookInfo.publishDate);
	        final TextView tags = (TextView) findViewById(R.id.textTags);         
	        tags.setText(fullBookInfo.tags);
	        final TextView description = (TextView) findViewById(R.id.textDescription);         
	        description.setText(fullBookInfo.description);
        
        } catch (Exception e) {
            Toast.makeText(this, String.format("Error: %s", e.getMessage()), Toast.LENGTH_LONG).show();
            throw e;
        }

    }
    
    public void sendBackButton(View view) {
        finish();
    }
}
