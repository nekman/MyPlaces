package se.nekman.places.service;

import java.util.List;

import se.nekman.places.entities.Place;

public interface IPlaceService {

	List<Place> getAll();

	Place getById(int id);

	long update(Place place);

	long deleteById(int id);

}
