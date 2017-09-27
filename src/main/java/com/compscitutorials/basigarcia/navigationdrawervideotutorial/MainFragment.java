package com.compscitutorials.basigarcia.navigationdrawervideotutorial;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    ViewPager viewPager;
    int hour;
    int minute;
    FindCombinations findCombinations;
    Button scheduleButton;
    public static ArrayList<ArrayList<TaskJSON>> combinations = new ArrayList<>();

    public MainFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        //Find Duration
        final NumberPicker nHour = (NumberPicker) rootView.findViewById(R.id.hoursScroll);
        final NumberPicker nMinute = (NumberPicker) rootView.findViewById(R.id.minutesScroll);
        nHour.setMaxValue(24);
        nHour.setMinValue(0);
        nMinute.setMaxValue(60);
        nMinute.setMinValue(0);
        viewPager = (ViewPager) rootView.findViewById(R.id.viewPager);

        scheduleButton = (Button) rootView.findViewById(R.id.scheduleButton);
        scheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(nHour == null){
                    hour = 0;
                }
                else{
                    hour = nHour.getValue();
                }
                if(nMinute == null){
                    minute = 0;
                }
                else{
                    minute = nMinute.getValue();
                }
                //Find Combinations
                findCombinations = new FindCombinations(hour,minute);

                combinations = findCombinations.getPermutations();



                SwipeAdapter swipeAdapter = new SwipeAdapter(getActivity().getSupportFragmentManager(),v);

                viewPager.setAdapter(swipeAdapter);

            }
        });


        return rootView;
    }


}
