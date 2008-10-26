package org.lvlv.supersearch;

import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import static android.provider.BaseColumns._ID;
import static org.lvlv.supersearch.Constants.TABLE_NAME;
import static org.lvlv.supersearch.Constants.NAME;
import static org.lvlv.supersearch.Constants.URL;
import static org.lvlv.supersearch.Constants.TERM;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


public class SearchesData extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "searches.db";
	private static final int DATABASE_VERSION = 1;
	
	public SearchesData(Context ctx) {
		super(ctx, DATABASE_NAME, null, DATABASE_VERSION );
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE "+ TABLE_NAME + " (" 
		    + _ID  + " INTEGER PRIMARY KEY AUTOINCREMENT, "
		    + NAME + " VARCHAR(25) NOT NULL, "
		    + URL  + " VARCHAR(255) NOT NULL, "
		    + TERM + " VARCHAR(25) DEFAULT 'Search');"
		);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}
	
	public void prepopulate() {
		addSearch("Answers.com", "http://answers.com/%s", "Search");
		addSearch("Google", "http://www.google.com/#/search?q=%s", "Search");
		addSearch("Merriam-Webster", "http://www.merriam-webster.com/dictionary/%s", "Word");
	}
	
	public void addSearch(String name, String url, String term) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(NAME, name);
		values.put(URL, url);
		values.put(TERM, term);
		db.insertOrThrow(TABLE_NAME, null, values);
	}
	
	

}