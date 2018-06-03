package monash.zi.kopilot;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

class SectionsPagerAdapter extends FragmentPagerAdapter {
    // Setup an adapter with the passed in array of fragments.
    private ArrayList<Fragment> fragmentsArray = new ArrayList<>();

    SectionsPagerAdapter(FragmentManager fm, ArrayList<Fragment> setupFragments) {
        super(fm);
        fragmentsArray = setupFragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentsArray.get(position);
    }

    @Override
    public int getCount() {
        return fragmentsArray.size();
    }
}
