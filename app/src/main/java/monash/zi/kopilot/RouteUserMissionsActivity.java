package monash.zi.kopilot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class RouteUserMissionsActivity extends AppCompatActivity implements LocationListFragment.OnLocationSelectedListener,
        MapControlFragment.OnMapClicked {
    private boolean mIsTwoPane;
    private ArrayList<Route> mSavedLocations = new ArrayList<>();
    private LocationListFragment mListFragment;
    private MapControlFragment mMapFragment;
    private FrameLayout mPrimaryFrame;
    private FrameLayout mSecondaryFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_user_missions);
        mSavedLocations = loadRoutesFromFirebase();
        mListFragment = new LocationListFragment();
        mMapFragment = new MapControlFragment();
        mListFragment.initFragment(this, mSavedLocations);
        mMapFragment.initFragment(this, mSavedLocations);
        mPrimaryFrame = findViewById(R.id.listFrameLayout);
        mSecondaryFrame = findViewById(R.id.mapFrameLayout);
        getSupportFragmentManager().beginTransaction().add(mPrimaryFrame.getId(), mListFragment).commit();
        if (mSecondaryFrame != null) {
            mIsTwoPane = true;
            getSupportFragmentManager().beginTransaction()
                    .add(mSecondaryFrame.getId(),
                            mMapFragment).commit();
        } else {
            mIsTwoPane = false;
        }
    }

    private  ArrayList<Route> loadRoutesFromFirebase() {
        mSavedLocations = new ArrayList<>();

        // Set refs to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Routes");

        // Read from the database - load planet data
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                processHashMap((Map<String,Object>) dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


        return mSavedLocations;
    }

    private void processHashMap(Map<String,Object> routes) {

        for (Map.Entry<String, Object> entry : routes.entrySet()){
            // Route Level
            Map singleRoute = (Map) entry.getValue();
            // Metadata Level
            Map routeMetaData = (Map) singleRoute.get("metaData");
            // Lat/Lng Level
            Map routeLatLng = (Map) routeMetaData.get("createLocation");

            String routeName = singleRoute.get("routeName").toString();
            double latVal = Double.valueOf(routeLatLng.get("latitude").toString());
            double lngVal = Double.valueOf(routeLatLng.get("longitude").toString());

            Route routeObj = new Route(singleRoute);
            mSavedLocations.add(routeObj);
        }

        mMapFragment.updateMapMarkers();
        mListFragment.refreshList();
    }

    @Override
    public void onLocationSelected(LatLng loc) {
        if (!mIsTwoPane) {
            getSupportFragmentManager().beginTransaction()
                    .replace(mPrimaryFrame.getId(), mMapFragment)
                    .addToBackStack(null)
                    .commit();
        }
        mMapFragment.setFocus(loc);
    }

    @Override
    public void onMapClicked(String locName, LatLng position) {
        mSavedLocations.add(new Route(locName, position));
        mMapFragment.updateMapMarkers();
        mListFragment.refreshList();
    }
}
