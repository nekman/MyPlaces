package se.nekman.places.repository;

import java.util.List;

import se.nekman.places.entities.Place;

public interface IPlaceRepository {

	List<Place> getAll();

	Place getById(int id);

	long create(Place place);

	long update(Place place);

	long deleteById(int id);
}
