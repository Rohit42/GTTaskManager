package com.compscitutorials.basigarcia.navigationdrawervideotutorial;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class scheduleOption extends android.support.v4.app.Fragment {

    TextView textView;
    RecyclerView scheduleRecycler;
    private DatabaseReference mDatabase;
    private FirebaseUser mUser;

    public scheduleOption() {

        // Required empty public constructor
    }

    private static final String FIREBASE_QUERY_INDEX = "index";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //Firebase & Recycler

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference(mUser.getUid());

        Bundle bundle = getArguments();
        ArrayList<String> taskList = bundle.getStringArrayList("count");
        View view = inflater.inflate(R.layout.fragment_schedule_option, container, false);
        scheduleRecycler = (RecyclerView) view.findViewById(R.id.schedule_option_recycler);
        scheduleRecycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        scheduleRecycler.setHasFixedSize(true);

        scheduleRecycler.setAdapter(new scheduleRecycleAdapter(taskList));

        return view;
    }

    public class scheduleRecycleAdapter extends RecyclerView.Adapter<FirebaseTaskListAdapter.TaskViewHolder> {

        private int mNumberItems;
        private ArrayList<TaskJSON> chosenTaskList = new ArrayList<TaskJSON>();

        public scheduleRecycleAdapter(ArrayList<String> strings) {
            chosenTaskList = new ArrayList<TaskJSON>();

            for(int i = 0; i< FirebaseTaskListAdapter.mTasks.size();i++){
                for(int j = 0; j < strings.size(); j++ ){
                    if(FirebaseTaskListAdapter.mTasks.get(i).getFirebaseaddress().equals(strings.get(j))){
                        chosenTaskList.add(FirebaseTaskListAdapter.mTasks.get(i));
                    }

                }

            }

        }


        @Override
        public FirebaseTaskListAdapter.TaskViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View itemView = LayoutInflater.
                    from(viewGroup.getContext()).
                    inflate(R.layout.task_view, viewGroup, false);

            return new FirebaseTaskListAdapter.TaskViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(FirebaseTaskListAdapter.TaskViewHolder holder, int position) {

            TaskJSON model = chosenTaskList.get(position);
            holder.setName(model.getName());
            holder.setDuration(model.getDuration());
            holder.setAddress(model.getAddress());
            holder.setCity(model.getCity());
            holder.setTime(model.getDueDate());
        }

        @Override
        public int getItemCount() {

            return chosenTaskList.size();
        }
    }
}
