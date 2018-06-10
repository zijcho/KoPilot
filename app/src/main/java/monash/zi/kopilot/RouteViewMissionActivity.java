package monash.zi.kopilot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class RouteViewMissionActivity extends AppCompatActivity {

    TextView routeTitleTextView;
    TextView routePlanetsTextView;
    TextView routeDVTextView;
    TextView routeDescriptionTextView;
    TextView routeCreatorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_view_mission);

        routeTitleTextView = findViewById(R.id.routeNameTextView);
        routePlanetsTextView = findViewById(R.id.planetsInRouteTextView);
        routeDVTextView = findViewById(R.id.routeDvTextView);
        routeDescriptionTextView = findViewById(R.id.routeDescriptionTextView);
        routeCreatorTextView = findViewById(R.id.routeCreatorTextView);

        LocDetails routeToView = (LocDetails) getIntent().getParcelableExtra("selectedRouteToView");
        assert routeToView != null;

        setTextViews(routeToView);
    }

    private void setTextViews(LocDetails intentObj) {
        routeTitleTextView.setText(intentObj.route.get("routeName").toString());
        routePlanetsTextView.setText(String.format("%s to %s", intentObj.route.get("startPlanet").toString(), intentObj.route.get("destPlanet").toString()));
        routeDescriptionTextView.setText(intentObj.route.get("routeDescription").toString());

        routeCreatorTextView.setText(intentObj.metaData.get("creator").toString());

        ArrayList<String> routeArray = new ArrayList<>();
        routeArray.add(intentObj.route.get("startPlanet").toString());
        routeArray.add(intentObj.route.get("destPlanet").toString());
        estimateRouteDeltaV(routeArray);
    }

    private void estimateRouteDeltaV(ArrayList<String> planetsInRoute) {
        boolean kerbinInRouteFlag = false;

        if (planetsInRoute.get(0).toLowerCase().equals("kerbin") || planetsInRoute.get(1).toLowerCase().equals("kerbin")) {
            kerbinInRouteFlag = true;
        }

        System.out.println("run1");
        System.out.println(routeDVTextView.getText().toString());
        getPlanetKDV(planetsInRoute.get(0), kerbinInRouteFlag);
        System.out.println(routeDVTextView.getText().toString());
        System.out.println("run2");
        getPlanetKDV(planetsInRoute.get(1), kerbinInRouteFlag);
    }

    private void getPlanetKDV(String planetNameStr, final boolean kerbinInRoute) {
        // Set refs to the database
        String planetarySystemReference = retrievePlanetSystem(planetNameStr);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Planets");
        DatabaseReference planetRef = myRef.child(planetarySystemReference).child(planetNameStr);

        // listener attachment
        planetRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                // Constant real-time changes would be rare however.
                HashMap value = (HashMap) dataSnapshot.getValue();

                // Logic for calculating the est DV.
                int currDv = Integer.valueOf(routeDVTextView.getText().toString());

                if (kerbinInRoute) {
                    currDv += Integer.valueOf(value.get("kDV").toString());
                } else {
                    currDv += Integer.valueOf(value.get("kDV").toString()) - 3400;
                }

                routeDVTextView.setText(String.valueOf(currDv));

                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    // Input a planet name, function will return it's planetary system that the planet resides in.
    private String retrievePlanetSystem(String inputPlanetName) {
        String[] planetarySystems = {"Dres System", "Duna System", "Eeloo System", "Eve System", "Jool System", "Kerbin System", "Moho System"};

        switch (inputPlanetName) {
            case "Dres":
                return planetarySystems[0];
            case "Duna":
            case "Ike":
                return planetarySystems[1];
            case "Eeloo":
                return planetarySystems[2];
            case "Eve":
            case "Gilly":
                return planetarySystems[3];
            case "Jool":
            case "Bop":
            case "Laythe":
            case "Pol":
            case "Tylo":
            case "Vall":
                return planetarySystems[4];
            case "Kerbin":
            case "Mun":
            case "Minmus":
                return planetarySystems[5];
            case "Moho":
                return planetarySystems[6];
            default:
                return "ERROR: Input planet name is invalid";
        }
    }
}
