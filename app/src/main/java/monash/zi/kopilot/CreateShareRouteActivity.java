package monash.zi.kopilot;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class CreateShareRouteActivity extends AppCompatActivity {

    Button gotoChecklist;
    Button gotoDVCalculator;
    Button createUserMission;

    TextView routeDescription;
    TextView DvEstimate;


    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_share_route);

        // init
        gotoChecklist = findViewById(R.id.gotoChecklistButton);
        gotoDVCalculator = findViewById(R.id.gotoDVCalculatorButton);
        createUserMission = findViewById(R.id.createMissionButton);

        routeDescription = findViewById(R.id.routeDetailTextView);
        DvEstimate = findViewById(R.id.routeDetailDVTextView);

        // Route details from previous intent
        final ArrayList<String> plannedRoute = getIntent().getStringArrayListExtra("plannedRoute");
        // Set text views to display planet route info
        setTextViewsFromRouteDetails(plannedRoute);


        // set listeners
        // Button: Checklist
        gotoChecklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateShareRouteActivity.this, ChecklistListViewActivity.class));
            }
        });

        // Button: DV Calculator
        gotoDVCalculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateShareRouteActivity.this, DvCalculatorActivity.class));
            }
        });

        // Button: Create Mission
        createUserMission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent newIntent = new Intent(CreateShareRouteActivity.this, UserMissionCreateActivity.class);
//                newIntent.putExtra("planetToView", tempPlanetSelection);
//                startActivity(newIntent);
                finish();
            }
        });
    }

    private void setTextViewsFromRouteDetails(ArrayList<String> planetsInRoute) {
        routeDescription.setText(String.format("Approximate ∆v required from %s to %s:", planetsInRoute.get(0), planetsInRoute.get(1)));
        DvEstimate.setText("0");

        estimateRouteDeltaV(planetsInRoute);
    }

    private void estimateRouteDeltaV(ArrayList<String> planetsInRoute) {
        boolean kerbinInRouteFlag = false;

        if (planetsInRoute.get(0).toLowerCase().equals("kerbin") || planetsInRoute.get(1).toLowerCase().equals("kerbin")) {
            kerbinInRouteFlag = true;
        }

        getPlanetKDV(planetsInRoute.get(0), kerbinInRouteFlag);
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
                int currDv = Integer.valueOf(DvEstimate.getText().toString());

                if (kerbinInRoute) {
                    currDv += Integer.valueOf(value.get("kDV").toString());
                }
                else {
                    currDv += Integer.valueOf(value.get("kDV").toString()) - 3400;
                }

                DvEstimate.setText(String.valueOf(currDv));

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
