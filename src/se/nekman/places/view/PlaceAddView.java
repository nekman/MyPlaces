package se.nekman.places.view;

import static android.location.LocationManager.GPS_PROVIDER;
import se.nekman.places.R;
import se.nekman.places.app.ActivityBase;
import se.nekman.places.app.PlacesApplication;
import se.nekman.places.entities.Place;
import se.nekman.places.service.ILocationFinder;
import se.nekman.places.service.IPlaceService;
import se.nekman.places.service.LocationService;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PlaceAddView extends ActivityBase {

	private PlacesApplication app;
	private IPlaceService placeService;
	private LocationService locationListner;
	private Button btnOk;
	private LocationManager locationManager;
	private ProgressDialog progressDialog;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.placeaddview);
		app = getPlacesApplication();
		locationManager = getService(LOCATION_SERVICE);
		placeService = app.getPlaceService();

		btnOk = (Button)findViewById(R.id.btnOk);
		btnOk.setEnabled(false);
		setupLocationManager();
		init();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!locationManager.isProviderEnabled(GPS_PROVIDER)) {
			createGpsDisabledAlert();
			return;
		}

		locationManager.requestLocationUpdates(GPS_PROVIDER, 0, 0,locationListner);
	};

	@Override
	protected void onPause() {
		super.onPause();
		locationManager.removeUpdates(locationListner);
	}

	private void setupLocationManager() {

		progressDialog = ProgressDialog.show(PlaceAddView.this, "","Trying to find your location. Please wait...", true);

		locationListner = new LocationService(new ILocationFinder() {
			public void onFind() {
				btnOk.setEnabled(true);
				Toast.makeText(PlaceAddView.this, "Found your location!", Toast.LENGTH_SHORT).show();
				progressDialog.hide();
			}
		});
	}

	private void createGpsDisabledAlert() {
		new AlertDialog.Builder(this)
		.setMessage(
				"Your GPS is disabled! Would you like to enable it?")
		.setCancelable(false)
		.setPositiveButton("Enable GPS",new DialogInterface.OnClickListener() {
			public void onClick(final DialogInterface dialog,
					final int id) {
				showGpsOptions();
			}
		})
		.setNegativeButton("Do nothing", new DialogInterface.OnClickListener() {
			public void onClick(final DialogInterface dialog,
					final int id) {
				dialog.cancel();
			}
		}).create().show();
	}

	private void showGpsOptions() {
		startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
	}

	private Place getPlace() {
		final String desc = ((EditText) findViewById(R.id.txtDesc)).getText().toString();
		final String name = ((EditText) findViewById(R.id.txtName)).getText().toString();
		final Location loc = locationListner.getCurrentBestLocation();
		return new Place(name, loc.getLongitude(), loc.getLatitude(), desc);
	}

	private void init() {
		findViewById(R.id.btnOk).setOnClickListener(new OnClickListener() {
			public void onClick(final View v) {
				final Place p = getPlace();
				if (placeService.update(p) > 0) {
					Toast.makeText(PlaceAddView.this, "Saved " + p.getName(), Toast.LENGTH_SHORT).show();
					app.getTabHost().setCurrentTab(0);
					return;
				}

				Toast.makeText(PlaceAddView.this,"Unable to save " + p.getName(), Toast.LENGTH_SHORT).show();
			}
		});

		findViewById(R.id.btnCancel).setOnClickListener(new OnClickListener() {
			public void onClick(final View v) {
				app.getTabHost().setCurrentTab(0);
			}
		});
	}
}
