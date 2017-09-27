package com.compscitutorials.basigarcia.navigationdrawervideotutorial;

import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rohit-Blade on 9/24/2017.
 */

public class FindCombinations {

    DatabaseReference mDatabase;
    FirebaseUser mUser;
    int hours;
    int minutes;
    static int time;
    static ArrayList<ArrayList<TaskJSON>> permutations;
    private static ArrayList<TaskJSON> allTasks = new ArrayList<TaskJSON>();
    public FindCombinations(int hours, int minutes){
        this.hours = hours;
        this.minutes = minutes;
        time = 60*hours + minutes;
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference(mUser.getUid());


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allTasks = new ArrayList<TaskJSON>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    TaskJSON task = noteDataSnapshot.getValue(TaskJSON.class);
                    if(task!=null){
                        allTasks.add(task);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public ArrayList<ArrayList<TaskJSON>> getPermutations() {

        permutations = new ArrayList<ArrayList<TaskJSON>>();
        buildPowerSet(allTasks,allTasks.size());
        return permutations;
    }

    private static void buildPowerSet( ArrayList<TaskJSON> list, int count)
    {
        double sum = 0;
        for(TaskJSON task : list){
            sum = sum + Double.parseDouble(task.getDuration());
        }
        if(sum<=time && sum != 0 && !permutations.contains(list)){
            permutations.add(list);
        }
        for(int i=0; i<list.size(); i++)
        {
            ArrayList<TaskJSON> temp = new ArrayList<TaskJSON>(list);
            temp.remove(i);
            buildPowerSet(temp, temp.size());
        }
    }

    public ArrayList<TaskJSON> getAllTasks() {

        return allTasks;
    }
}
