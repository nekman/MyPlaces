package se.nekman.places.repository;

import static se.nekman.places.common.StringUtils.isEmpty;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Database extends SQLiteOpenHelper {

	private static final int databaseVersion = 1;

	private final String databaseCreate;
	private final String databaseUpgrade;

	/**
	 * 
	 * @param context
	 * @param databaseName
	 * @param databaseCreate
	 * @param databaseUpgrade
	 */
	public Database(final Context context, final String databaseName, final String databaseCreate, final String databaseUpgrade) {
		super(context, databaseName, null, databaseVersion);
		this.databaseCreate = databaseCreate;
		this.databaseUpgrade = databaseUpgrade;
	}

	/**
	 * 
	 * @param context
	 * @param databaseName
	 * @param databaseCreate
	 */
	public Database(final Context context, final String databaseName, final String databaseCreate) {
		this(context, databaseName, databaseCreate, null);
	}

	@Override
	public void onCreate(final SQLiteDatabase db) {
		try {
			db.execSQL(databaseCreate);
		} catch (final SQLException e) {
			Log.e("error", String.format("Error when trying to create %s", databaseCreate), e);
		}
	}

	@Override
	public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
		try {
			if (!isEmpty(databaseUpgrade)) {
				db.execSQL(databaseUpgrade);
			}
		} catch (final SQLException e) {
			Log.e("error",String.format("Error when running SQL %s", databaseUpgrade), e);
		}

		onCreate(db);
	}
}
