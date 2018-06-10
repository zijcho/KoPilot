package monash.zi.kopilot;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


public class MapControlFragment extends android.support.v4.app.Fragment implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {
    private GoogleMap mMap;
    private OnMapClicked mListener;
    private ArrayList<Route> mSavedLocations;
    private LatLng mCurrentLoc;
    public MapControlFragment() {
        mSavedLocations = new ArrayList<>();
        mCurrentLoc = new LatLng(-37.8770, 145.0443);
    }
    public void initFragment(OnMapClicked listener,
                             ArrayList<Route> locations) {
        mListener = listener;
        mSavedLocations = locations;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map_control, container, false);

        SupportMapFragment mapFrag = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
        mapFrag.getMapAsync(this);
        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                mCurrentLoc, 15));
        updateMapMarkers();
    }
    public void updateMapMarkers() {
        if (mMap != null) {
            mMap.clear();
            for (Route location : mSavedLocations) {
                mMap.addMarker(new MarkerOptions()
                        .position(location.latLng)
                        .title(location.locationName));
            }
        }
    }
    public void setFocus(LatLng loc) {
        mCurrentLoc = loc;
        if(mMap != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));
        }
    }
    @Override
    public void onMapLongClick(final LatLng latLng) {
        final EditText inputText = new EditText(getContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setMessage("Please enter a name for the new location")
                .setView(inputText)
                .setPositiveButton("Create",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(mListener != null) {
                                    mListener.onMapClicked(inputText.getText().toString(),
                                            latLng);
                                }
                            }
                        })
                .setNegativeButton("Cancel", null);
        Dialog dialog = builder.create();
        dialog.show();
    }
    public interface OnMapClicked {
        void onMapClicked(String locName, LatLng position);
    }
}
