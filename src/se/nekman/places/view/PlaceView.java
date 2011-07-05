package se.nekman.places.view;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import java.util.List;

import se.nekman.places.R;
import se.nekman.places.app.ActivityBase.PlaceMapActivity;
import se.nekman.places.app.PlacesApplication;
import se.nekman.places.entities.Place;
import se.nekman.places.service.IPlaceService;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class PlaceView extends PlaceMapActivity {

	private MapView mapView;
	private PlacesApplication app;
	private Place currentPlace;
	private IPlaceService placeService;

	private final PlaceMapActivity activity;

	public PlaceView() {
		activity = this;
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.placeview);
		// init 
		mapView = (MapView)findViewById(R.id.mapView);
		app = getPlacesApplication();
		currentPlace = app.getCurrentPlace();
		placeService = app.getPlaceService();
		
		// set header text
		((TextView) findViewById(R.id.txtHeader)).setText(currentPlace.getName());
		// add button click events and setup the map
		addClickEvents();
		setupMap();
	}

	@Override
	public void onResume() {
		super.onResume();
		currentPlace = app.getCurrentPlace();
		// (re)init map
		initMap();
		// force portrait orientation
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	
	@Override
	public void onConfigurationChanged(final Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    // force portrait orientation
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	
	@Override
	public void onBackPressed() {
		// display listview on system back
	   app.getTabHost().setCurrentTab(0);
	   return;
	}
	
	@Override
	public boolean onKeyDown(final int keyCode, final KeyEvent event) {
		final MapController mc = mapView.getController();
		switch (keyCode) {
			case KeyEvent.KEYCODE_3:
				mc.zoomIn();
				break;
			case KeyEvent.KEYCODE_1:
				mc.zoomOut();
				break;
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@SuppressWarnings("deprecation")
	private void setupMap() {
		final LinearLayout zoomLayout = (LinearLayout) findViewById(R.id.zoom);
		zoomLayout.addView(mapView.getZoomControls(), new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
		mapView.displayZoomControls(true);
	}

	private void initMap() {
		final MapController mc = mapView.getController();
		final GeoPoint p = currentPlace.getGeoPoint();
		mc.animateTo(p);
		mc.setZoom(17);
		// Add a location marker
		final List<Overlay> listOfOverlays = mapView.getOverlays();
		listOfOverlays.clear();
		listOfOverlays.add(new PlaceMapOverlay(p));
		mapView.invalidate();
	}

	private void addClickEvents() {
		findViewById(R.id.btnDelete).setOnClickListener(new OnClickListener() {
			public void onClick(final View v) {
				app.confirm(activity, "Delete?", "Yes",new DialogInterface.OnClickListener() {
					public void onClick(final DialogInterface dialog, final int which) {
						placeService.deleteById(currentPlace.getId());
						// activity.finish();
						app.getTabHost().setCurrentTab(0);
					}
				});
			}
		});

		findViewById(R.id.btnEdit).setOnClickListener(new OnClickListener() {
			public void onClick(final View v) {
				Toast.makeText(activity, "Not implemented... ", Toast.LENGTH_SHORT).show();
			}
		});
	}

	private final class PlaceMapOverlay extends Overlay {

		private final GeoPoint point;

		public PlaceMapOverlay(final GeoPoint p) {
			point = p;
		}

		@Override
		public boolean draw(final Canvas canvas, final MapView mapView, final boolean shadow, final long when) {
			super.draw(canvas, mapView, shadow);

			// ---translate the GeoPoint to screen pixels---
			final Point screenPts = new Point();
			mapView.getProjection().toPixels(point, screenPts);

			// ---add the marker---
			final Bitmap bmp = BitmapFactory.decodeResource(getResources(),R.drawable.pin);
			canvas.drawBitmap(bmp, screenPts.x, screenPts.y - 50, null);
			return true;
		}
	}
}