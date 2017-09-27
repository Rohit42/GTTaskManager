package com.compscitutorials.basigarcia.navigationdrawervideotutorial;


import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewTasks extends Fragment implements OnStartDragListener {


    private static final String FIREBASE_QUERY_INDEX = "index";

    public ViewTasks() {
    }
    private Toolbar mToolBar;
    private RecyclerView mTaskList;
    private FirebaseTaskListAdapter mFirebaseAdapter ;
    private DatabaseReference mDatabase;
    private FirebaseUser mUser;
    private ItemTouchHelper mItemTouchHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_tasks, container, false);
        //List View
        //Database
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference(mUser.getUid());
        mTaskList = (RecyclerView) view.findViewById(R.id.list_task);
        mTaskList.setLayoutManager(new LinearLayoutManager(view.getContext()));

//        mToolBar = (Toolbar) view.findViewById(R.id.tasks_appBar);
//        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolBar);
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("View Tasks");
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        return view;

    }

    public void onStart(){
        super.onStart();
        setUpFirebaseAdapter();
    }
//        mTaskList.setAdapter(firebaseRecyclerAdapter);

    private void setUpFirebaseAdapter() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child(uid);
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child(uid)
                .orderByChild(FIREBASE_QUERY_INDEX);

        mFirebaseAdapter = new FirebaseTaskListAdapter(TaskJSON.class,
                R.layout.task_view, FirebaseTaskListAdapter.TaskViewHolder.class,
                mDatabase, this, getContext());

        mTaskList.setHasFixedSize(true);
        mTaskList.setLayoutManager(new LinearLayoutManager(getContext()));
        mTaskList.setAdapter(mFirebaseAdapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mFirebaseAdapter,getContext());
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mTaskList);
    }

    public void onDestroy() {
        super.onDestroy();
        mFirebaseAdapter.cleanup();
    }
    @Override
    public void onResume(){
        super.onResume();
        mFirebaseAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}
