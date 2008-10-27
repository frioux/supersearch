package org.lvlv.supersearch;

import static android.provider.BaseColumns._ID;
import static org.lvlv.supersearch.Constants.NAME;
import static org.lvlv.supersearch.Constants.TABLE_NAME;
import static org.lvlv.supersearch.Constants.TERM;
import static org.lvlv.supersearch.Constants.URL;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

public class ModifySearches extends Activity implements OnClickListener,OnKeyListener{
	private Button add_search_button;
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
		name_text = (EditText) findViewById(R.id.name_input);
		url_text = (EditText) findViewById(R.id.location_input);
		term_text = (EditText) findViewById(R.id.term_input);
		location = (Spinner) findViewById(R.id.location);
		
		add_search_button.setOnClickListener(this);
		name_text.setOnKeyListener(this);
		url_text.setOnKeyListener(this);
		term_text.setOnKeyListener(this);
		
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
	}
	
	private static String[] FROM = { _ID, NAME, URL, TERM};
	private static String ORDER_BY = NAME + " ASC" ;
	public Cursor getSearches() {
	   // Perform a managed query. The Activity will handle closing
	   // and re-querying the cursor when needed.
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
		addSearch();
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
	
	

}
