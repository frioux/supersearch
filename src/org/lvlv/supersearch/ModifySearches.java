package org.lvlv.supersearch;

import static android.provider.BaseColumns._ID;
import static org.lvlv.supersearch.Constants.NAME;
import static org.lvlv.supersearch.Constants.TABLE_NAME;
import static org.lvlv.supersearch.Constants.TERM;
import static org.lvlv.supersearch.Constants.URL;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class ModifySearches extends Activity implements OnClickListener,OnKeyListener, OnItemSelectedListener{
	private Button add_search_button;
	private Button save_search_button;
	private Button delete_search_button;
	private EditText name_text;
	private EditText url_text;
	private EditText term_text;
	private SearchesData searches;
	private Spinner location;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modifysearches);
		
		searches = new SearchesData(this);
		add_search_button = (Button) findViewById(R.id.add_search_button);
		save_search_button = (Button) findViewById(R.id.save_search_button);
		delete_search_button = (Button) findViewById(R.id.delete_search_button);
		name_text = (EditText) findViewById(R.id.name_input);
		url_text = (EditText) findViewById(R.id.location_input);
		term_text = (EditText) findViewById(R.id.term_input);
		location = (Spinner) findViewById(R.id.location);
		
		add_search_button.setOnClickListener(this);
		save_search_button.setOnClickListener(this);
		delete_search_button.setOnClickListener(this);
		name_text.setOnKeyListener(this);
		url_text.setOnKeyListener(this);
		term_text.setOnKeyListener(this);
		location.setOnItemSelectedListener(this);
		
	}

	private void setupSearches() {
		searches = new SearchesData(this);
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
	
	protected void onStart() {
		super.onStart();
		setupSearches();
		populateFields();
	}
	
	private static String[] FROM = { _ID, NAME, URL, TERM};
	private static String ORDER_BY = NAME + " ASC" ;
	public Cursor getSearches() {
	   SQLiteDatabase db = searches.getReadableDatabase();
	   Cursor cursor = db.query(TABLE_NAME, FROM, null, null, null,
	         null, ORDER_BY);
	   startManagingCursor(cursor);
	   return cursor;
	}
	

	public void addSearch() {
		searches.addSearch(
				name_text.getText().toString(),
				url_text.getText().toString(),
				term_text.getText().toString()
				);
	
	}
	
	public void onClick(View v) {
		SQLiteCursor selection;
		switch (v.getId()) {
		
		case R.id.add_search_button:
			addSearch();
			break;
		case R.id.delete_search_button:
			selection =  (SQLiteCursor) location.getSelectedItem();
			searches.deleteSearch(selection.getInt(0));
			break;
		case R.id.save_search_button:
			selection =  (SQLiteCursor) location.getSelectedItem();
			searches.updateSearch(
					selection.getInt(0), 
					name_text.getText().toString(),
					url_text.getText().toString(), 
					term_text.getText().toString()
			);
			break;
		}
		setupSearches();
	}


	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_ENTER) {
			switch(v.getId()) {
			case R.id.name_input:
				break;
			case R.id.location_input:
				break;
			case R.id.term_input:
				addSearch();
				
				break;
			}
		}
		return false;
	}

	public void onItemSelected(AdapterView<?> adapterView, View view, int arg2,
			long arg3) {
		populateFields();

	}

	private void populateFields() {
		SQLiteCursor selection =  (SQLiteCursor) location.getSelectedItem();
		if (selection != null) {
			name_text.setText(selection.getString(1));
			url_text.setText(selection.getString(2));
			term_text.setText(selection.getString(3));
		}
			
	}
	
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	
	

}
