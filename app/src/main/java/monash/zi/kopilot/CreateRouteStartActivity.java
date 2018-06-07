package monash.zi.kopilot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class CreateRouteStartActivity extends AppCompatActivity {

    ViewPager mViewPager;
    Button planetSelectButton;
    Button planetInfoButton;

    private String[] planetNameList = {"Moho", "Eve", "Gilly", "Kerbin", "Mun", "Minmus", "Duna", "Ike","Dres", "Jool", "Laythe", "Vall", "Tylo", "Bop", "Eeloo"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_route_start);
        setTitle("Select a start point:");

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.startPlanetViewPager);
        mViewPager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager(), setupPlanetFragments()));
        mViewPager.setCurrentItem(3);

        mViewPager.setClipToPadding(false);
        mViewPager.setPadding(100,0,100,0);

        planetSelectButton = findViewById(R.id.startPlanetButton);
        planetInfoButton = findViewById(R.id.startPlanetInfoButton);


        // set listeners
        // Button: Select destination
        planetSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(CreateRouteStartActivity.this, CreateRouteDestActivity.class);
                newIntent.putExtra("planetStartPoint", planetNameList[mViewPager.getCurrentItem()]);
                startActivity(newIntent);
                finish();
            }
        });

        // Button: View planet info
        planetInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(CreateRouteStartActivity.this, PlanetInfoActivity.class);
                newIntent.putExtra("planetToView",  planetNameList[mViewPager.getCurrentItem()]);
                startActivity(newIntent);
            }
        });
    }

    private ArrayList<Fragment> setupPlanetFragments() {
        ArrayList<Fragment> fragmentList = new ArrayList<>();


        for (String aPlanetNameList : planetNameList) {
            Fragment newFrag = SelectPlanetFragment.newInstance(aPlanetNameList);
            fragmentList.add(newFrag);
        }

        return fragmentList;
    }
}
