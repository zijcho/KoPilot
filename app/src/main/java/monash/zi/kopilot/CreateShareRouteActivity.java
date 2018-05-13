package monash.zi.kopilot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class CreateShareRouteActivity extends AppCompatActivity {

    Button gotoChecklist;
    Button gotoDVCalculator;
    Button createUserMission;

    TextView routeDescription;
    TextView DvEstimate;


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
            }
        });
    }

    private void setTextViewsFromRouteDetails() {

    }

    private int estimateRouteDeltaV(ArrayList<String> planetsInRoute) {
      //todo: Stubbed the (actual) value for the DV required to get to the mun from Kerbin, need implement deltaV reqs from the 'subway map' of values in the Solar System.
        return 5150;
    };
}
