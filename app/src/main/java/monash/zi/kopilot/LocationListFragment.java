package monash.zi.kopilot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class LocationListFragment extends Fragment implements AdapterView.OnItemClickListener {
    private ArrayList<Route> mSavedLocations;
    private OnLocationSelectedListener mListener;
    private ListView mListView;
    private ArrayAdapter<Route> mAdapter;
    public LocationListFragment() {
        mSavedLocations = new ArrayList<>();
    }
    public void initFragment(OnLocationSelectedListener listener, ArrayList<Route> locations) {
        mSavedLocations = locations;
        mListener = listener;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_location_list, container,
                false);
        mAdapter = new ArrayAdapter<>(inflater.getContext(),
                android.R.layout.simple_list_item_1,
                mSavedLocations);
        mListView = v.findViewById(R.id.locationListView);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        return v;
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        if(mListener != null) {
            mListener.onLocationSelected(mSavedLocations.get(position).latLng);

            Route intentObj = mSavedLocations.get(position);

            Intent newIntent = new Intent(getActivity(), RouteViewMissionActivity.class);
            newIntent.putExtra("selectedRouteToView", intentObj);

            startActivity(newIntent);
        }

    }
    public void refreshList() {
        mAdapter.notifyDataSetChanged();
    }

    public interface OnLocationSelectedListener {
        void onLocationSelected(LatLng loc);
    }
}
