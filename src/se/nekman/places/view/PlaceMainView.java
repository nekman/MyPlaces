package se.nekman.places.view;

import se.nekman.places.R;
import se.nekman.places.app.PlacesApplication;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;

public class PlaceMainView extends TabActivity {

	private PlacesApplication app;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		app = (PlacesApplication) getApplication();

		final Resources res = getResources(); // Resource object to get
												// Drawables
		final TabHost tabHost = getTabHost(); // The activity TabHost
		TabHost.TabSpec spec; // Resusable TabSpec for each tab
		Intent intent; // Reusable Intent for each tab

		// Create an Intent to launch an Activity for the tab (to be reused)
		intent = new Intent().setClass(this, PlacesListView.class);

		// Initialize a TabSpec for each tab and add it to the TabHost
		spec = tabHost
			.newTabSpec("tab1")
			.setIndicator("Saved places", res.getDrawable(R.drawable.ic_tab_marker))
			.setContent(intent);

		tabHost.addTab(spec);

		intent = new Intent().setClass(this, PlaceAddView.class);
		spec = tabHost
			.newTabSpec("tab2")
			.setIndicator("Add new", res.getDrawable(R.drawable.ic_tab_marker))
			.setContent(intent);

		tabHost.addTab(spec);

		intent = new Intent().setClass(this, PlaceView.class);
		
		spec = tabHost.newTabSpec("tab3").setIndicator("Edit")
				.setContent(intent);
		tabHost.addTab(spec);

		tabHost.setCurrentTab(0);
		tabHost.getTabWidget().getChildAt(2).setVisibility(View.INVISIBLE);

		app.setTabHost(tabHost);

	}

}