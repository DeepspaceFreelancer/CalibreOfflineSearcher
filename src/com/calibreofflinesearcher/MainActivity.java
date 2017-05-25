package com.calibreofflinesearcher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private final List<String> listItems = new ArrayList<>();

    private List<BookInfo> bookInfos;
    private ArrayAdapter<String> listViewAdapter;

    private final QueryExecuter queryExecuter = new QueryExecuter();
    private final FileOpener fileOpener = new FileOpener();

    //private final static String LIBRARY_LOCATION = "/mnt/sdcard/EkartLaszlo/Magyar2/";
    //private final static String LIBRARY_LOCATION = "/mnt/external_sd/Magyar/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewAdapter = new ArrayAdapter<String>(this, R.layout.list_black_text, R.id.list_content, listItems);
        ListView listView = (ListView) findViewById(R.id.lstEredmeny);
        listView.setAdapter(listViewAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fileOpener.openBook(getApplicationContext(), bookInfos.get(position).path);
            }
        });
        final MainActivity that = this;
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                that.openDetails(getApplicationContext(), bookInfos.get(position));
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!queryExecuter.isOpen()) {
            try {
                queryExecuter.connectQueryDatabase(Settings.getInstance().getLibraryLocation());
            } catch (Exception e) {
                Toast.makeText(this, String.format("Error: %s", e.getMessage()), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void openDetails(Context applicationContext, BookInfo bookInfo) {
        Intent intent = new Intent(this, ItemDetails.class);
        startActivity(intent);
    }

    public void sendMessage(View view) {

        final String queryText = ((EditText) findViewById(R.id.txbSzo)).getText().toString();
        final boolean accentsOn = ((ToggleButton) findViewById(R.id.tgbEkezet)).isChecked();
        final boolean titleOn = ((ToggleButton) findViewById(R.id.tgbCim)).isChecked();
        final boolean authorOn = ((ToggleButton) findViewById(R.id.tgbSzerzo)).isChecked();
        final boolean tagsOn = ((ToggleButton) findViewById(R.id.tgbCimke)).isChecked();
        final boolean descriptionOn = ((ToggleButton) findViewById(R.id.tgbLeiras)).isChecked();

        listItems.clear();

        try {
            QueryExecuter.QueryControlParams queryControlParams = new QueryExecuter.QueryControlParams(accentsOn, titleOn, authorOn, tagsOn, descriptionOn);
            bookInfos = queryExecuter.execute(queryControlParams, queryText);
            for (BookInfo bookInfo : bookInfos) {
                listItems.add(bookInfo.author + " - " + bookInfo.title);
            }
        } catch (Exception e) {
            Toast.makeText(this, String.format("Error: %s", e.getMessage()), Toast.LENGTH_LONG).show();
        }

        listViewAdapter.notifyDataSetChanged();
    }

    public void navigateToSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
