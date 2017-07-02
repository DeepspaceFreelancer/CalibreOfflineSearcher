package com.calibreofflinesearcher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.calibreofflinesearcher.dbutil.QueryExecuter;
import com.calibreofflinesearcher.pojo.BookInfo;
import com.calibreofflinesearcher.pojo.FullBookInfo;
import com.calibreofflinesearcher.utils.FileOpener;
import com.calibreofflinesearcher.utils.Settings;

public class MainActivity extends Activity {

	private enum DISPLAY_MODES {
		SEARCH, HISTORY
	}

	public static MainActivity INSTANCE = null;

	private DISPLAY_MODES displayModes = DISPLAY_MODES.SEARCH;
	private final List<String> listItems = new ArrayList<>();

	private List<BookInfo> bookInfos;
	private ArrayAdapter<String> listViewAdapter;

	private LinkedList<FullBookInfo> bookHistory;

	private QueryExecuter queryExecuter = null;
	private final FileOpener fileOpener = new FileOpener();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		try {

			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main);

			queryExecuter = QueryExecuter.getInstance();
			loadPreferences();

			listViewAdapter = new ArrayAdapter<String>(this, R.layout.list_black_text, R.id.list_content, listItems);
			ListView listView = (ListView) findViewById(R.id.lstEredmeny);
			listView.setAdapter(listViewAdapter);
			final MainActivity that = this;
			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					final int bookId = displayModes == DISPLAY_MODES.SEARCH ? bookInfos.get(position).id
							: bookHistory.get(position).id;
					final String relativePath = displayModes == DISPLAY_MODES.SEARCH ? bookInfos.get(position).path
							: bookHistory.get(position).path;
					that.openBook(getApplicationContext(), bookId, relativePath);
				}
			});
			listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
					final int bookId = displayModes == DISPLAY_MODES.SEARCH ? bookInfos.get(position).id
							: bookHistory.get(position).id;
					that.openDetails(getApplicationContext(), bookId);
					return true;
				}
			});

		} catch (Exception e) {
			Toast.makeText(this, String.format("onCreate Error: %s", e.getMessage()), Toast.LENGTH_LONG).show();
		}
	}

	private LinkedList<FullBookInfo> getHistory(final String serializedHistory) {
		LinkedList<FullBookInfo> returnValue = new LinkedList<FullBookInfo>();
		String[] historyStrings = serializedHistory.split(",");
		for (String history : historyStrings) {
			history = history.trim();
			if (history != "") {
				int bookId = Integer.parseInt(history);
				FullBookInfo fullBookInfo = queryExecuter.getFullBookInfo(bookId);
				returnValue.add(fullBookInfo);
			}
		}
		return returnValue;
	}

	private void add2History(final int bookId) {
		loadBookHistory();
		while (bookHistory.size() > 50) {
			bookHistory.removeLast();
		}

		for (Iterator<FullBookInfo> iter = bookHistory.iterator(); iter.hasNext();) {
			FullBookInfo data = iter.next();
			if (data.id == bookId) {
				iter.remove();
			}
		}

		final FullBookInfo fullBookInfo = queryExecuter.getFullBookInfo(bookId);
		bookHistory.addFirst(fullBookInfo);
		saveHistory();
	}

	private void saveHistory() {
		StringBuilder bookHistoryString = new StringBuilder();
		for (FullBookInfo fullBookInfo : bookHistory) {
			if (fullBookInfo != null) {
				bookHistoryString.append(fullBookInfo.id);
				bookHistoryString.append(",");
			}
		}

		SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("CalibreOfflineSearcher",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(Constants.HISTORY_KEY, bookHistoryString.toString());
		editor.commit();
	}

	@Override
	protected void onResume() {
		super.onResume();
		try {
			queryExecuter = QueryExecuter.getInstance();
			if (queryExecuter != null) {
				if (!queryExecuter.isOpen()) {
					loadPreferences();
					queryExecuter.connectQueryDatabase(Settings.getInstance().getLibraryLocation());
				}
			} else {
				Toast.makeText(this, "queryExecuter is null", Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			Toast.makeText(this, String.format("onResume Error: %s", e.getMessage()), Toast.LENGTH_LONG).show();
		}
	}

	private void openDetails(final Context applicationContext, int id) {
		final Intent intent = new Intent(this, ItemDetailsActivity.class);
		final Bundle bundle = new Bundle();
		bundle.putInt(Constants.id, id);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	private void openBook(final Context applicationContext, final int id, final String path) {
		try {
			add2History(id);
			final String fullPath = Settings.getInstance().getLibraryRootLocation() + "/" + path;
			final String problem = fileOpener.openBook(getApplicationContext(), fullPath);
			// Toast.makeText(this, String.format("%s %s", problem, fullPath),
			// Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			Toast.makeText(this, String.format("openBook Error: %s", e.getMessage()), Toast.LENGTH_LONG).show();
		}
	}

	public void sendMessage(View view) {
		displayModes = DISPLAY_MODES.SEARCH;
		final String queryText = ((EditText) findViewById(R.id.txbSzo)).getText().toString();
		final boolean accentsOn = ((ToggleButton) findViewById(R.id.tgbEkezet)).isChecked();
		final boolean titleOn = ((ToggleButton) findViewById(R.id.tgbCim)).isChecked();
		final boolean authorOn = ((ToggleButton) findViewById(R.id.tgbSzerzo)).isChecked();
		final boolean tagsOn = ((ToggleButton) findViewById(R.id.tgbCimke)).isChecked();
		final boolean descriptionOn = ((ToggleButton) findViewById(R.id.tgbLeiras)).isChecked();

		listItems.clear();

		try {
			QueryExecuter.QueryControlParams queryControlParams = new QueryExecuter.QueryControlParams(accentsOn,
					titleOn, authorOn, tagsOn, descriptionOn);
			bookInfos = queryExecuter.execute(queryControlParams, queryText);
			for (BookInfo bookInfo : bookInfos) {
				listItems.add(bookInfo.publishDate + " - " + bookInfo.author + " - " + bookInfo.title);
			}
		} catch (Exception e) {
			Toast.makeText(this, String.format("sendMessage Error: %s", e.getMessage()), Toast.LENGTH_LONG).show();
		}

		listViewAdapter.notifyDataSetChanged();
	}

	public void sendHistory(View view) {
		displayModes = DISPLAY_MODES.HISTORY;
		listItems.clear();
		try {
			loadBookHistory();
			for (FullBookInfo fullbookInfo : bookHistory) {
				if (fullbookInfo != null) {
					listItems.add(fullbookInfo.publishDate + " - " + fullbookInfo.author + " - " + fullbookInfo.title);
				}

			}
		} catch (Exception e) {
			Toast.makeText(this, String.format("sendHistory Error: %s", e.getMessage()), Toast.LENGTH_LONG).show();
		}

		listViewAdapter.notifyDataSetChanged();
	}

	public void navigateToSettings(View view) {
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}

	public void loadPreferences() {

		SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("CalibreOfflineSearcher",
				Context.MODE_PRIVATE);
		Settings.getInstance().setCalibreLibraryLocation(sharedPreferences.getString(
				Constants.CALIBRE_DATABASE_LOCATION_KEY, Settings.getInstance().getCalibreLibraryLocation()));
		Settings.getInstance().setLibraryLocation(sharedPreferences
				.getString(Constants.NORMALIZED_DATABASE_LOCATION_KEY, Settings.getInstance().getLibraryLocation()));
	}

	public void loadBookHistory() {
		if (bookHistory == null) {
			SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("CalibreOfflineSearcher",
					Context.MODE_PRIVATE);
			bookHistory = getHistory(sharedPreferences.getString(Constants.HISTORY_KEY, ""));
		}
	}
}
