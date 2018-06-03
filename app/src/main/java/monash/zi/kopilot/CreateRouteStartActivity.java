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

    Route newRoute;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_route_start);
        setTitle("Select a start point:");

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager)
                findViewById(R.id.c_r_start_vp);
        mViewPager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager()));
        mViewPager.setCurrentItem(3);

        mViewPager.setClipToPadding(false);
        mViewPager.setPadding(100,0,100,0);


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

    private ArrayList<Fragment> setupPlanetFragments() {
        // Todo: loop/get data (planet names) from firebase instead
        ArrayList<Fragment> fragmentList = new ArrayList<>();
        String[] planetNameList = {"Moho", "Eve", "Gilly", "Kerbin", "Mun", "Minmus", "Duna", "Ike", "Jool", "Laythe", "Vall", "Tylo", "Bop", "Eeloo"};

        // SelectPlanetFragment.newInstance("")

        for (String aPlanetNameList : planetNameList) {
            Fragment newFrag = SelectPlanetFragment.newInstance(aPlanetNameList);
            fragmentList.add(newFrag);
        }

        return fragmentList;
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> mFragments = setupPlanetFragments();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }
}
