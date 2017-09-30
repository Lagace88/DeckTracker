package com.tool.dirtytgaming.decktrackerpro;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {

	private static final String TAG = "DBAdapter"; //used for logging database version changes
			
	// Field Names
	public static final String KEY_DECKID = "_id";
	public static final String KEY_NAME = "name";
	public static final String KEY_TRED = "tred";
	public static final String KEY_TGREEN = "tgreen";
	public static final String KEY_TBLUE = "tblue";
	public static final String KEY_BRED = "bred";
	public static final String KEY_BGREEN = "bgreen";
	public static final String KEY_BBLUE = "bblue";
	public static final String KEY_WINS = "wins";
	public static final String KEY_LOSES = "losses";
	public static final String KEY_WINRATE = "winrate";
	
	public static final String[] USERDECK_KEYS = new String[] {KEY_DECKID, KEY_NAME, KEY_TRED, KEY_TGREEN, KEY_TBLUE,
			KEY_BRED, KEY_BGREEN, KEY_BBLUE, KEY_WINRATE};
	public static final String[] FACEDDECK_KEYS = new String[] {KEY_DECKID, KEY_NAME, KEY_TRED, KEY_TGREEN, KEY_TBLUE,
			KEY_BRED, KEY_BGREEN, KEY_BBLUE};
	public static final String[] WIN_LOSS_KEYS = new String[] {KEY_DECKID, KEY_WINS, KEY_LOSES};
	public static final String[] RECOMMENDED_KEYS = new String[] {"_id", "deck"};

	// DataBase info:
	public static final String DATABASE_NAME = "decks";
	public static final String DATABASE_TABLE_USERDECKS = "USERDECKS";
	public static final int DATABASE_VERSION = 1; // The version number must be incremented each time a change to DB structure occurs.
	
	private final Context context;
	private DatabaseHelper myDBHelper;
	private SQLiteDatabase db;


	public DBAdapter(Context ctx) {
		this.context = ctx;
		myDBHelper = new DatabaseHelper(context);
	}

	// Open the database connection.
	public DBAdapter open() {
		db = myDBHelper.getWritableDatabase();
		return this;
	}
	
	// Close the database connection.
	public void close() {
		myDBHelper.close();
	}

	// Combine info from FACEDDECKS and selected table.
	public Cursor combine(String table) {
		Cursor c = db.rawQuery("SELECT FACEDDECKS._id, FACEDDECKS.name, FACEDDECKS.tred, FACEDDECKS.tgreen, FACEDDECKS.tblue, " +
				"FACEDDECKS.bred, FACEDDECKS.bgreen, FACEDDECKS.bblue, " + table + ".wins, " + table + ".losses FROM FACEDDECKS JOIN " + table + " " +
				"ON FACEDDECKS._id = " + table + "._id", null);
		c.moveToFirst();
		return c;
	}

	// Add a new set of values to be inserted into the database.
	public void insertRow(String name, int[] tColor, int[] bColor, int winRate, String table, int deck, int tableType) {
		// tableType 0 = User
		// 			 1 = Faced
		//			 2 = Win/Loss
		//			 3 = Recommended

		if (tableType == 0) {
			ContentValues initialValues = new ContentValues();
			initialValues.put("name", name);
			initialValues.put("tred", tColor[0]);
			initialValues.put("tgreen", tColor[1]);
			initialValues.put("tblue", tColor[2]);
			initialValues.put("bred", bColor[0]);
			initialValues.put("bgreen", bColor[1]);
			initialValues.put("bblue", bColor[2]);
			initialValues.put("winrate", winRate);

			db.insert(table, null, initialValues);
		}else if (tableType == 1) {
			ContentValues initialValues = new ContentValues();
			initialValues.put("name", name);
			initialValues.put("tred", tColor[0]);
			initialValues.put("tgreen", tColor[1]);
			initialValues.put("tblue", tColor[2]);
			initialValues.put("bred", bColor[0]);
			initialValues.put("bgreen", bColor[1]);
			initialValues.put("bblue", bColor[2]);

			db.insert(table, null, initialValues);
		}else if (tableType == 2) {
			ContentValues initialValues = new ContentValues();
			initialValues.put("wins", 0);
			initialValues.put("losses", 0);

			db.insert(table, null, initialValues);
		}else if (tableType == 3) {
			ContentValues initialValues = new ContentValues();
			initialValues.put("deck", deck);

			db.insert(table, null, initialValues);
		}
	}
	
	// Delete a row from the database, by rowId (primary key)
	public boolean deleteRow(long rowId, String table) {
		String where = KEY_DECKID + "=" + rowId;
		return db.delete(table, where, null) != 0;
	}

	// Return all data in the database.
	public Cursor getAllRows(String table, int TableType) {
		// 0 == USEERDECK
		// 1 == FACEDDECK
		// 2 == WIN/Loss
		// 3 == RECOMMENDED

		Cursor c;
		if (TableType == 0) {
			c = db.query(true, table, USERDECK_KEYS, null, null, null, null, null, null);
			if (c != null) {
				c.moveToFirst();
			}
			return c;
		} else if (TableType == 1){
			c = db.query(true, table, FACEDDECK_KEYS, null, null, null, null, null, null);
			if (c != null) {
				c.moveToFirst();
			}
			return c;
		} else if (TableType == 2) {
			c = db.query(true, table, WIN_LOSS_KEYS, null, null, null, null, null, null);
			if (c != null) {
				c.moveToFirst();
			}
			return c;
		} else if (TableType == 3) {
			c = db.query(true, table, RECOMMENDED_KEYS, null, null, null, null, null, null);
			if (c != null) {
				c.moveToFirst();
			}
			return c;
		}
		return c = null;
	}

	// Get a specific row (by rowId)
	public Cursor getRow(long rowId, String table, int DeckType) {
		// DeckType 0 = USERDECKS
		// 			1 = FACEDDECKS
		// 			2 = Win/loss deck

		String where = "";
		Cursor c;

		if (DeckType == 0) {
			where = KEY_DECKID + "=" + rowId;
			c = db.query(true, table, USERDECK_KEYS,
					where, null, null, null, null, null);
			if (c != null) {
				c.moveToFirst();
			}
			return c;
		} else if (DeckType == 1) {
			where = KEY_DECKID + "=" + rowId;
			c = db.query(true, table, FACEDDECK_KEYS,
					where, null, null, null, null, null);
			if (c != null) {
				c.moveToFirst();
			}
		} else {
			where = KEY_DECKID + "=" + rowId;
			c = db.query(true, table, WIN_LOSS_KEYS,
					where,  null, null, null, null, null);
			if (c != null) {
				c.moveToFirst();
			}
		}
		return c;
	}
	
	// Change name value of a row
	public boolean updateName(long rowId, String name, String table) {
		String where = KEY_DECKID + "=" + rowId;
		ContentValues newValues = new ContentValues();
		newValues.put(KEY_NAME, name);

		// Insert it into the database.
		return db.update(table, newValues, where, null) != 0;
	}

	public boolean updateTextColor(long rowId, int[] tColor, String table) {
		String where = KEY_DECKID + "=" + rowId;
		ContentValues newValues = new ContentValues();
		newValues.put("tred", tColor[0]);
		newValues.put("tgreen", tColor[1]);
		newValues.put("tblue", tColor[2]);

		// Insert it to db
		return db.update(table, newValues, where, null) != 0;
	}

	public boolean updateBGColor(long rowId, int[] bColor, String table) {
		String where = KEY_DECKID + "=" + rowId;
		ContentValues newValues = new ContentValues();
		newValues.put("bred", bColor[0]);
		newValues.put("bgreen", bColor[1]);
		newValues.put("bblue", bColor[2]);

		// Insert into db
		return db.update(table, newValues, where, null) != 0;
	}

	public boolean updateWins(long rowId, int wins, String table) {
		String where = KEY_DECKID + "=" + rowId;
		ContentValues newValues = new ContentValues();
		newValues.put(KEY_WINS, wins);

		// Insert into db
		return db.update(table, newValues, where, null) != 0;
	}

	public boolean updateLoses(long rowId, int loses, String table) {
		String where = KEY_DECKID + "=" + rowId;
		ContentValues newValues = new ContentValues();
		newValues.put(KEY_LOSES, loses);

		// Insert into db
		return db.update(table, newValues, where, null) != 0;
	}

	public boolean updateID(long rowId, int newID, String table) {
		String where = KEY_DECKID + "=" + rowId;
		ContentValues newValue = new ContentValues();
		newValue.put(KEY_DECKID, newID);

		// Insert into db
		return db.update(table, newValue, where, null) !=0;
	}

	public boolean updateWinRate(int newWinRate, int rowID) {
		String where = KEY_DECKID + "=" + rowID;
		ContentValues newValue = new ContentValues();
		newValue.put(KEY_WINRATE, newWinRate);

		//Insert into db
		return db.update("USERDECKS", newValue, where, null) != 0;
	}

	public boolean updateRecommended (int deck, int rowID) {
		String where = KEY_DECKID + "=" + rowID;
		ContentValues newValue = new ContentValues();
		newValue.put("deck", deck);

		// Insert into db
		return db.update("RECOMMENDED", newValue, where, null) != 0;
	}

	public boolean addToRecommended (int deck, int size) {
		ContentValues flag = new ContentValues();
		flag.put("deck", 0);

		// Add new deck to flag position.
		updateRecommended(deck, size);
		// Add flag.
		return db.insert("RECOMMENDED", null, flag) != 0;
	}

	public void updateWinRateName (String oldName, String newName) {
		String changeName =
				"ALTER TABLE " + oldName
				+ " RENAME TO " + newName + ";";

		db.execSQL(changeName);
	}

	public void deleteTable (String tableName) {
        String delete = "DROP TABLE IF EXISTS [" + tableName + ']';

        db.execSQL(delete);
    }

	public void createTable(String tableName, int tableType) {
		// tableType == 0 USERDECKS
		// tableType == 1 FACEDDECKS
		// tableType == 2 WinLoss
		// tableType == 3 RECOMMENDED

		if (tableType == 0) {
			String createTable =
					"CREATE TABLE IF NOT EXISTS " + tableName
							+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
							+ "name TEXT,"
							+ "tred INTEGER,"
							+ "tgreen INTEGER,"
							+ "tblue INTEGER,"
							+ "bred INTEGER,"
							+ "bgreen INTEGER,"
							+ "bblue INTEGER,"
							+ "winrate INTEGER"
							+ ");";
			db.execSQL(createTable);
		}else if (tableType == 1) {
			String createTable =
					"CREATE TABLE IF NOT EXISTS " + tableName
							+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
							+ "name TEXT,"
							+ "tred INTEGER,"
							+ "tgreen INTEGER,"
							+ "tblue INTEGER,"
							+ "bred INTEGER,"
							+ "bgreen INTEGER,"
							+ "bblue INTEGER"
							+ ");";
			db.execSQL(createTable);
		}else if (tableType == 2){
				String createTable =
						"CREATE TABLE IF NOT EXISTS " + tableName
								+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
								+ "wins INTEGER, "
								+ "losses INTEGER"
								+ ");";
				db.execSQL(createTable);
		}else if (tableType == 3) {
			String createTable =
					"CREATE TABLE IF NOT EXISTS " + tableName
						+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
						+ "deck INTEGER"
						+ ");";
			db.execSQL(createTable);
		}
	}

	private static class DatabaseHelper extends SQLiteOpenHelper
	{
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase _db) {
			//SQL statement to create database
			String DATABASE_CREATE_SQL =
					"CREATE TABLE " + DATABASE_TABLE_USERDECKS
							+ " (" + KEY_DECKID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
							+ KEY_NAME + " TEXT,"
							+ "tred INTEGER,"
							+ "tgreen INTEGER,"
							+ "tblue INTEGER,"
							+ "bred INTEGER,"
							+ "bgreen INTEGER,"
							+ "bblue INTEGER,"
							+ KEY_WINRATE + " INTEGER"
							+ ");";
			_db.execSQL(DATABASE_CREATE_SQL);
		}

		@Override
		public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading application's database from version " + oldVersion
					+ " to " + newVersion + ", which will destroy all old data!");
			
			// Destroy old database:
			_db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_USERDECKS);
			
			// Recreate new database:
			onCreate(_db);
		}
	}
}

