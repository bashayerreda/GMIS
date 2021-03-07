package com.example.gmisproject.user;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.gmisproject.MainActivity;


public class UserFragmentAdapter extends FragmentPagerAdapter {


    public UserFragmentAdapter(MainActivity mainActivity, FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new UserBinFragment();
        } else if (position == 1) {
            return new UserRequestFragment();
        } else {
            return new UserMsgFragment();
        }
    }


}
