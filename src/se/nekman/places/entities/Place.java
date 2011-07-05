package se.nekman.places.entities;

import se.nekman.places.common.StringUtils;

import com.google.android.maps.GeoPoint;

public class Place {

	private final String name;
	private final double longitude;
	private final double latitude;
	private final int id;
	private String description;

	public Place(final String name, final double longitude, final double latitude, final String description, final int id) {
		this.name = name;
		this.longitude = longitude;
		this.latitude = latitude;
		this.id = id;
		this.description = description;
	}

	public Place(final String name, final double longitude, final double latitude, final String description) {
		this(name, longitude, latitude, description, -1);
	}

	public Place(final String name, final double longitude, final double latitude) {
		this(name, longitude, latitude, StringUtils.EMPTY, -1);
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public double getLongitude() {
		return longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public int getId() {
		return id;
	}

	public GeoPoint getGeoPoint() {
		return new GeoPoint((int) (latitude * 1E6), (int) (longitude * 1E6));
	}

	@Override
	public String toString() {
		return String.format(
				"[id=%s, longitude=%s, latitude=%s, description=%s, name=%s]",
				id, longitude, latitude, description, name);
	}
}
