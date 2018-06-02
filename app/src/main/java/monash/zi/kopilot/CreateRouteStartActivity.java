package monash.zi.kopilot;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class CreateRouteStartActivity extends AppCompatActivity {
    // for the purposes of the demo, I'll hard code in the start and end points, but it is important to note
    // that since we're demonstrating the fire-base interaction, it's stressed that little to no data of the planets
    // themselves are stored locally.

    Button planetSelectButton;

    ArrayList<String> planetList;
    Route newRoute;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_route_start);
        setTitle("Select a start point:");

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager)
                findViewById(R.id.c_r_start_vp);
        mViewPager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager()));
        mViewPager.setCurrentItem(0);


        // set listeners
        // Button: Routes and user missions
//        planetImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent newIntent = new Intent(CreateRouteStartActivity.this, PlanetInfoActivity.class);
////                newIntent.putExtra("planetToView", tempPlanetSelection);
//
//                startActivity(newIntent);
//            }
//        });
//
//        // Button: Select destination
//        planetSelectButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent newIntent = new Intent(CreateRouteStartActivity.this, CreateRouteDestActivity.class);
////                newIntent.putExtra("planetStartPoint", tempPlanetSelection);
//                startActivity(newIntent);
//            }
//        });
    }

    private void setupPlanetList() {
        // Todo: loop/get data (planet names) from firebase instead

        planetList.add("Kerbin");
        planetList.add("Mun");
        planetList.add("Minmus");
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        private String[] mPageTitles = {}; // For tab?
        // Fragment not showing on screen

        Fragment newFrag1 = SelectPlanetFragment.newInstance("Kerbin");
        Fragment newFrag2 = SelectPlanetFragment.newInstance("Mun");

        private Fragment[] mFragments = {newFrag1, newFrag2};

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments[position];
        }

        @Override
        public int getCount() {
            return mFragments.length;
        }
    }
}
