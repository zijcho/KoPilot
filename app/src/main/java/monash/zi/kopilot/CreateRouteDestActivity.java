package monash.zi.kopilot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class CreateRouteDestActivity extends AppCompatActivity {

    ViewPager mViewPager;
    Button planetSelectButton;

    ArrayList<String> planetsInRoute = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_route_dest);
        setTitle("Select a Destination:");

        // start point from previous intent
        final String startingPlanet = getIntent().getStringExtra("planetStartPoint");
        assert startingPlanet != null;

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.c_r_vp_dest);
        mViewPager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager(), setupPlanetFragments()));
        mViewPager.setCurrentItem(3);

        mViewPager.setClipToPadding(false);
        mViewPager.setPadding(100,0,100,0);

        planetSelectButton = findViewById(R.id.destPlanetButton);

        // set listeners
        // Button: Routes and user missions
//        planetImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent newIntent = new Intent(CreateRouteDestActivity.this, PlanetInfoActivity.class);
//                newIntent.putExtra("planetToView", tempPlanetSelection);
//                startActivity(newIntent);
//            }
//        });
//
//        // Button: Select destination, and finish route creation
        planetSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(CreateRouteDestActivity.this, CreateShareRouteActivity.class);

                // Whatever the selected destination planet is
                planetsInRoute.add(startingPlanet);
                planetsInRoute.add(""); // replace with get from fragment

                // Pass along the route array to the final activity in the route creation
                newIntent.putStringArrayListExtra("plannedRoute", planetsInRoute);
                startActivity(newIntent);
            }
        });
    }

    private ArrayList<Fragment> setupPlanetFragments() {
        ArrayList<Fragment> fragmentList = new ArrayList<>();
        String[] planetNameList = {"Moho", "Eve", "Gilly", "Kerbin", "Mun", "Minmus", "Duna", "Ike", "Jool", "Laythe", "Vall", "Tylo", "Bop", "Eeloo"};

        for (String aPlanetNameList : planetNameList) {
            Fragment newFrag = SelectPlanetFragment.newInstance(aPlanetNameList);
            fragmentList.add(newFrag);
        }

        return fragmentList;
    }
}
