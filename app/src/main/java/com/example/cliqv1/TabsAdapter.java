package com.example.cliqv1;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsAdapter extends FragmentPagerAdapter {

    public TabsAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {

       switch (i) {
           case 0:
               GroupsFragment groupsFragment = new GroupsFragment();
               return groupsFragment;

           case 1:
               ProfileFragment profileFragment = new ProfileFragment();
               return profileFragment;

           default:
               return null;
       }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Gruppenchats";

            case 1:
                return "Profil";

            default:
                return null;
        }

    }
}
