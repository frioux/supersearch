package org.lvlv.supersearch;

import static android.provider.BaseColumns._ID;
import static org.lvlv.supersearch.Constants.*;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

public class SuperSearch extends Activity implements OnClickListener,OnKeyListener
{
	private Button goButton;
	private Button manageButton;
	private Button aboutButton;
	private Spinner location;
	private EditText inputText;
	private SearchesData searches;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		goButton = (Button) findViewById(R.id.go_button);
		manageButton = (Button) findViewById(R.id.manage_button);
		location = (Spinner) findViewById(R.id.search_location);
		inputText = (EditText) findViewById(R.id.search_input);
			
		goButton.setOnClickListener(this);
		manageButton.setOnClickListener(this);
		location.setOnKeyListener(this);
		inputText.setOnKeyListener(this);
		
		searches = new SearchesData(this);
		if (!isFirstRun()) {
			firstRunSetup();
		}
	}
	
	protected void onSaveInstanceState(Bundle outState) {
	   super.onSaveInstanceState(outState);
	}
	protected void onRestoreInstanceState(Bundle inState) {
		super.onRestoreInstanceState(inState);
	}

	protected void onResume() {
		super.onResume();
	}
	protected void onStart() {
		super.onStart();
		setupSearches();
	}

	private void setupSearches() {
		try {
			Cursor cursor = getSearches();
			String[] from = new String[] { Constants.NAME, Constants.URL };
			int[] to = new int[] { R.id.name, R.id.url };
	        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.spinnerrow, cursor, from, to  );
			adapter.setDropDownViewResource(R.layout.spinnerrow);
	        location.setAdapter(adapter);
		} finally {
		   searches.close();
		}
	}
	
	private void doSearch() {
		SQLiteCursor selection =  (SQLiteCursor) location.getSelectedItem();
		Uri uri = Uri.parse(selection.getString(2).replaceAll("%s", inputText.getText().toString()));
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);	
	}
	
	private void firstRunSetup() {
		searches.addSearch("Answers.com", "http://answers.com/%s", "Search");
		searches.addSearch("Google", "http://google.com/search?q=%s", "Search");
		searches.addSearch("Wikipedia", "http://en.wikipedia.org/wiki/Special:Search?search=%s", "Search");
		searches.addSearch("Merriam-Webster", "http://www.merriam-webster.com/dictionary/%s", "Define");
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(FIRST_RUN, false);
		editor.commit();
	}

	private boolean isFirstRun() {
		SharedPreferences preferences = getSharedPreferences(PREFS_NAME, 0);
		return preferences.getBoolean(FIRST_RUN, true);
	}

	
	private static String[] FROM = { _ID, NAME, URL, TERM};
	private static String ORDER_BY = NAME + " ASC" ;
	private Cursor getSearches() {
	   SQLiteDatabase db = searches.getReadableDatabase();
	   Cursor cursor = db.query(TABLE_NAME, FROM, null, null, null,
	         null, ORDER_BY);
	   startManagingCursor(cursor);
	   return cursor;
	}
	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.go_button: 
			doSearch();
			break;
		case R.id.manage_button:
			Intent i = new Intent(this, ModifySearches.class);
			startActivity(i);
			break;
		}
	}

	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_ENTER) {
			doSearch();
			return true;
		}
		return false;
	}
}