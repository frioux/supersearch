package org.lvlv.supersearch;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class SuperSearch extends Activity implements OnClickListener,OnKeyListener
{
	private Button goButton;
	private Button manageButton;
	private EditText location;
	private EditText inputText;
	private SearchesData searches;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		searches = new SearchesData(this);
		try {
		   Cursor cursor = searches.getSearches();
		   setupSearches(cursor);
		} finally {
		   searches.close();
		}

		goButton = (Button) findViewById(R.id.go_button);
		manageButton = (Button) findViewById(R.id.manage_button);
		location = (EditText) findViewById(R.id.search_location);
		inputText = (EditText) findViewById(R.id.search_input);
			
		goButton.setOnClickListener(this);
		manageButton.setOnClickListener(this);
		location.setOnKeyListener(this);
		inputText.setOnKeyListener(this);
	}
	private void setupSearches(Cursor cursor) {
		Log.d("foo", "bar");
		
	}
	private void doSearch() {
//		Uri uri = Uri.parse(location.getText().toString().replaceAll("%s", inputText.getText().toString()));
//		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//		startActivity(intent);
		
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