package se.nekman.places.repository;

import static se.nekman.places.app.PlacesApplication.getContext;

import java.util.List;

import se.nekman.android.dbrepository.IEntityMapper;
import se.nekman.android.dbrepository.RepositoryBase;
import se.nekman.places.entities.Place;
import android.content.ContentValues;
import android.database.Cursor;

public class PlaceRepository extends RepositoryBase<Place> implements IPlaceRepository {

	private final static Place EMPTY = new Place(null, 0, 0);
	private final static String DATABASE_NAME = "places.db";
	private final static String DATABASE_TABLE = "places";
	private final static String DATABASE_UPGRADE = "DROP TABLE IF EXISTS places";
	private final static String DATABASE_CREATE = "CREATE TABLE places "
			+ "(id INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ "name TEXT NOT NULL, " + "description TEXT NOT NULL, "
			+ "longitude REAL NOT NULL, " + "latitude REAL NOT NULL);";

	private final static String SQL_GET_ALL = "SELECT name, longitude, latitude, description, id FROM places";
	private final static String SQL_GET_BY_ID = "SELECT name, longitude, latitude, description, id FROM places WHERE id = ?";

	public PlaceRepository() {
		super(new Database(getContext(), DATABASE_NAME, DATABASE_CREATE, DATABASE_UPGRADE).getReadableDatabase(), Mapper.INSTANCE);
	}

	public List<Place> getAll() {
		return queryForList(SQL_GET_ALL, null);
	}

	public Place getById(final int id) {
		final Place p = queryForObject(SQL_GET_BY_ID, new String[] { String.valueOf(id) });
		if (p == null) {
			return EMPTY;
		}

		return p;
	}

	public long create(final Place place) {
		return insert(DATABASE_TABLE, toContentValues(place));
	}

	public long update(final Place place) {
		return update(DATABASE_TABLE, toContentValues(place), "id=?",
				new String[] { place.getId() + "" });
	}

	public long deleteById(final int id) {
		return delete(DATABASE_TABLE, "id=?", new String[] { id + "" });
	}

	private ContentValues toContentValues(final Place p) {
		final ContentValues v = new ContentValues();
		v.put("name", p.getName());
		v.put("longitude", p.getLongitude());
		v.put("latitude", p.getLatitude());
		v.put("description", p.getDescription());

		// only add id if greater than -1. If id = -1 we should insert a row
		// instead.
		final int id = p.getId();
		if (id > -1) {
			v.put("id", id);
		}

		return v;
	}

	private static final class Mapper implements IEntityMapper<Place> {

		public static final IEntityMapper<Place> INSTANCE = new Mapper();

		public Place toEntity(final Cursor c) {
			return new Place(c.getString(0), // name
					c.getDouble(1), // longitude
					c.getDouble(2), // latitude
					c.getString(3), // description
					c.getInt(4) // id
			);
		}
	}
}
