package com.compscitutorials.basigarcia.navigationdrawervideotutorial;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Rohit-Blade on 9/24/2017.
 */

public class SwipeAdapter extends FragmentStatePagerAdapter {


    private View v;
    public SwipeAdapter(FragmentManager fm, View v) {

        super(fm);

        this.v = v;
    }


    @Override
    public Fragment getItem(int position) {


        scheduleOption fragment = new scheduleOption();
        ArrayList<String> taskAddresses = new ArrayList<>();
        ArrayList<TaskJSON> task = MainFragment.combinations.get(position);
        for(TaskJSON t : task){
            taskAddresses.add(t.getFirebaseaddress());
        }
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("count",taskAddresses);
        fragment.setArguments(bundle);

        return fragment;



    }

    @Override
    public int getCount() {

        if(MainFragment.combinations == null){
            return 0;
        }
        return MainFragment.combinations.size();
    }
}
