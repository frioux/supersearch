package org.lvlv.supersearch;

import android.provider.BaseColumns;

public interface Constants extends BaseColumns {
	public static final String PREFS_NAME = "SuperSearch";
	public static final String FIRST_RUN = "firstrun";
	
	public static final String TABLE_NAME = "searches";
	
		//Columns
	public static final String NAME = "name";
	public static final String URL = "url";
	public static final String TERM = "term";

}
