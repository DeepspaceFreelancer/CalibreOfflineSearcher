package com.calibreofflinesearcher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.net.URISyntaxException;

import com.calibreofflinesearcher.dbutil.DBNormalizer;
import com.calibreofflinesearcher.utils.Settings;

public class SettingsActivity extends Activity {

	private static final int FILE_SELECT_CODE = 0;
	private static final String TAG = "SettingsActivity";

	enum FileChoiceMode {
		UNDEFINED, CALIBRE_DB, NORMALIZED_DB
	}

	private FileChoiceMode fileChoiceMode = FileChoiceMode.UNDEFINED;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		refreshText();
	}

	private void refreshText() {
		EditText editCalibreDB = (EditText) findViewById(R.id.editCalibreDB);
		editCalibreDB.setText(Settings.getInstance().getCalibreLibraryLocation());
		EditText editNMDB = (EditText) findViewById(R.id.editNMDB);
		editNMDB.setText(Settings.getInstance().getLibraryLocation());
	}

	public void sendBackButton(final View view) {
		finish();
	}

	public void sendCalibreDBSearch(final View view) {
		fileChoiceMode = FileChoiceMode.CALIBRE_DB;
		showFileChooser();
	}

	public void sendNMDBSearch(final View view) {
		fileChoiceMode = FileChoiceMode.NORMALIZED_DB;
		showFileChooser();
	}

	public void sendRefreshNMBD(final View view) {
		DBNormalizer dbNormalizer = new DBNormalizer();
		try {
			dbNormalizer.doConversion(Settings.getInstance().getCalibreLibraryLocation(),
					Settings.getInstance().getLibraryLocation());
		} catch (Exception e) {
			Toast.makeText(this, String.format("Error: %s", e.getMessage()), Toast.LENGTH_SHORT).show();
		}
	}
	
	public void sendClearHistory(final View view) {
		SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("CalibreOfflineSearcher", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(Constants.HISTORY_KEY, "");
		editor.commit();
		Toast.makeText(this, "Történet törléshez azonali olvasó újraindítás szükséges...", Toast.LENGTH_SHORT).show();
	}

	private void showFileChooser() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*");
		intent.addCategory(Intent.CATEGORY_OPENABLE);

		try {
			startActivityForResult(Intent.createChooser(intent, "Adatbázis helye"), FILE_SELECT_CODE);
		} catch (android.content.ActivityNotFoundException ex) {
			// Potentially direct the user to the Market with a Dialog
			Toast.makeText(this, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case FILE_SELECT_CODE:
			if (resultCode == RESULT_OK) {
				// Get the Uri of the selected file
				Uri uri = data.getData();
				Log.d(TAG, "File Uri: " + uri.toString());
				// Get the path
				String path = null;
				try {
					path = SettingsActivity.getPath(this, uri);
					setPath(path);
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(this, "Cannot retrieve path...", Toast.LENGTH_LONG).show();
				}
				Log.d(TAG, "File Path: " + path);
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public static String getPath(Context context, Uri uri) throws URISyntaxException {
		if ("content".equalsIgnoreCase(uri.getScheme())) {
			String[] projection = { "_data" };
			Cursor cursor = null;

			try {
				cursor = context.getContentResolver().query(uri, projection, null, null, null);
				int column_index = cursor.getColumnIndexOrThrow("_data");
				if (cursor.moveToFirst()) {
					return cursor.getString(column_index);
				}
			} catch (Exception e) {
				// Eat it
				e.printStackTrace();
				Toast.makeText(context, "Hmm something went wrong....", Toast.LENGTH_SHORT).show();
			}
		} else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	public void setPath(final String path) {
		switch (fileChoiceMode) {
		case CALIBRE_DB:
			Settings.getInstance().setCalibreLibraryLocation(path);
			break;
		case NORMALIZED_DB:
			Settings.getInstance().setLibraryLocation(path);
			break;
		default:
			break;
		}
		setPreferences();
		refreshText();
	}
	
	public void setPreferences() {		
		SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("CalibreOfflineSearcher", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(Constants.CALIBRE_DATABASE_LOCATION_KEY, Settings.getInstance().getCalibreLibraryLocation());
		editor.putString(Constants.NORMALIZED_DATABASE_LOCATION_KEY, Settings.getInstance().getLibraryLocation());
		editor.commit();
	}
}
