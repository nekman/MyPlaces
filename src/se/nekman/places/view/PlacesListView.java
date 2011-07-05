package se.nekman.places.view;

import se.nekman.places.R;
import se.nekman.places.app.PlacesApplication;
import se.nekman.places.app.ActivityBase.PlaceListActivity;
import se.nekman.places.entities.Place;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class PlacesListView extends PlaceListActivity {
	private Place[] places;
	private PlacesApplication app;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview);
		app = getPlacesApplication();

		initListView();
	}

	@Override
	public void onListItemClick(final ListView parent, final View v, final int position, final long id) {
		app.setCurrentPlace(places[position]);
		app.getTabHost().setCurrentTab(2);
	}

	@Override
	public void onResume() {
		super.onResume();
		initListView();
	}

	private void initListView() {
		places = app.getPlaceService().getAll().toArray(new Place[] {});
		setListAdapter(new ArrayAdapter<Place>(this, android.R.layout.simple_list_item_1, places) {
			@Override
			public View getView(final int position, final View convertView, final ViewGroup parent) {
				final TextView view = (TextView) super.getView(position, convertView, parent);
				view.setText(places[position].getName());
				return view;
			}
		});
	}
}
