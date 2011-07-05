package se.nekman.places.service;

import java.util.List;

import se.nekman.places.entities.Place;
import se.nekman.places.repository.IPlaceRepository;

public class PlaceService implements IPlaceService {

	private final IPlaceRepository repository;

	public PlaceService(final IPlaceRepository repository) {
		this.repository = repository;
	}

	public List<Place> getAll() {
		return repository.getAll();
	}

	public Place getById(final int id) {
		return repository.getById(id);
	}

	public long update(final Place place) {
		// validate input
		validate(place);

		// if place id equals 0, then create it
		if (place.getId() == -1) {
			return repository.create(place);
		}

		// update existing place
		return repository.update(place);
	}

	public long deleteById(final int id) {
		return repository.deleteById(id);
	}

	private void validate(final Place place) {
		if (place == null) {
			throw new IllegalArgumentException("place == null");
		}
	}

}
