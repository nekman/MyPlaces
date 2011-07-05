package se.nekman.places.app;

import android.app.Activity;
import android.app.ListActivity;

import com.google.android.maps.MapActivity;

//Experiment...
public class ActivityBase extends Activity {

	private static final ActivityBase INSTANCE = new ActivityBase();
	
	@SuppressWarnings("unchecked")
	public <T> T getService(final String name) {
		return (T) getSystemService(name);
	}

	public PlacesApplication getPlacesApplication() {
		return (PlacesApplication)PlacesApplication.getContext();
	}

	public static class PlaceListActivity extends ListActivity {
		public PlacesApplication getPlacesApplication() {
			return INSTANCE.getPlacesApplication();
		}	
	}

	public static class PlaceMapActivity extends MapActivity {
		public PlacesApplication getPlacesApplication() {
			return INSTANCE.getPlacesApplication();
		}

		public <T> T getService(final String name) {
			return INSTANCE.getService(name);
		}
		
		@Override
		protected boolean isRouteDisplayed() {
			return false;
		}
	}
}
