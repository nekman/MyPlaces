package se.nekman.places.test;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import se.nekman.places.common.StringUtils;
import se.nekman.places.entities.Place;
import se.nekman.places.repository.IPlaceRepository;
import se.nekman.places.repository.PlaceRepository;
import se.nekman.places.service.IPlaceService;
import se.nekman.places.service.PlaceService;
import static org.junit.Assert.*;

/**
 * 
 * /lib/android-2.1_r2.jar needs to be added to test configuration => classpath => User Entries
 * and BootStrap Entries needs to be removed. 
 *
 * Eclipse jUnit launcher should be used (if using Eclipse).
 */
public class PlaceServiceTest {

	private IPlaceService service;
	
	@Before
	public void setUp() throws Exception {
		
		// setup PlaceRepository with Mockito
		IPlaceRepository repository = mock(PlaceRepository.class);
		List<Place> places = Arrays.asList(getPlace("one",1,1,1), getPlace("two",2,2,2), getPlace("three",3,3,3));
		when(repository.getAll()).thenReturn(places);
		when(repository.getById(1)).thenReturn(places.get(0));
		when(repository.getById(2)).thenReturn(places.get(1));
		when(repository.getById(3)).thenReturn(places.get(2));

		service = new PlaceService(repository);	
	}
	
	@Test
	public void testGetAll() {
		List<Place> result = service.getAll();
		assertEquals("expected 3 items in the list", 3, result.size());
		assertEquals("two", result.get(1).getName());
	}
	
	@Test
	public void testGetByid() {
		Place p = service.getById(3);
		assertEquals("3.0", p.getLatitude() + "");
	}
	
	@Test
	public void testUpdateWithNullParameter() {
		try {
			service.update(null);
			fail("IllegalArgumentException was expected!");
		} catch(IllegalArgumentException e) {
		}
	}
	
	@Test
	public void testUpdate() {
		long result = service.update(getPlace("one",1,1,2));
		assertEquals(0, result);
		Place p = service.getById(2);
		assertEquals(2, p.getId());
	}
	
	private static Place getPlace(String name, long lng, long lat, int id) {
		return new Place(name,lng,lat, StringUtils.EMPTY, id);
	}

}
