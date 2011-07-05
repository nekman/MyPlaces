package se.nekman.places.app;

import se.nekman.places.common.StringUtils;
import se.nekman.places.entities.Place;
import se.nekman.places.repository.PlaceRepository;
import se.nekman.places.service.IPlaceService;
import se.nekman.places.service.PlaceService;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.TabHost;

/**
 * Helper class for retrieving Application context, service implementations and
 * <code>Place</code> object that needs to be passed between activities.
 */
public class PlacesApplication extends Application {

	private static PlacesApplication instance;
	private static IPlaceService placeService;
	private final static Place EMPTY_PLACE = new Place(StringUtils.EMPTY, 0, 0);

	private Place currentPlace;
	private TabHost tabHost;

	public PlacesApplication() {
		instance = this;
	}

	public IPlaceService getPlaceService() {
		if (placeService == null) {
			// this setter where at first executed in ctor, but resulted in NP exception...
			// probably due to that the context was not ready.
			placeService = new PlaceService(new PlaceRepository());
		}

		return placeService;
	}

	public static Context getContext() {
		return instance;
	}

	public void setCurrentPlace(final Place p) {
		currentPlace = p;
	}

	public TabHost getTabHost() {
		return tabHost;
	}

	public void setTabHost(final TabHost tabHost) {
		this.tabHost = tabHost;
	}

	public Place getCurrentPlace() {
		if (currentPlace == null) {
			return EMPTY_PLACE;
		}

		return currentPlace;
	}
	
    public void confirm(final Activity activity, final String message, final String okText, final DialogInterface.OnClickListener okListner) {
    	new AlertDialog.Builder(activity)
    	.setMessage(message)
        .setPositiveButton(okText, okListner)
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(final DialogInterface dialog, int which) {
				dialog.cancel();
			}
		})
        .create()
        .show();
    }
}