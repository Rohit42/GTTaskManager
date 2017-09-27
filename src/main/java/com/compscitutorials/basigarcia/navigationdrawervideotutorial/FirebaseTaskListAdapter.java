package com.compscitutorials.basigarcia.navigationdrawervideotutorial;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.compscitutorials.basigarcia.navigationdrawervideotutorial.R;
import com.compscitutorials.basigarcia.navigationdrawervideotutorial.TaskJSON;
import com.compscitutorials.basigarcia.navigationdrawervideotutorial.ViewTasks;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;

public class FirebaseTaskListAdapter extends FirebaseRecyclerAdapter<TaskJSON, FirebaseTaskListAdapter.TaskViewHolder> implements ItemTouchHelperAdapter {
    private DatabaseReference mRef;
    private OnStartDragListener mOnStartDragListener;
    private Context mContext;
    private ChildEventListener mChildEventListener;

    public static ArrayList<TaskJSON> mTasks;
    public FirebaseTaskListAdapter(Class<TaskJSON> modelClass, int modelLayout,
                                   Class<TaskViewHolder> viewHolderClass,
                                   Query ref, OnStartDragListener onStartDragListener, Context context) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        mRef = ref.getRef();
        mOnStartDragListener = onStartDragListener;
        mContext = context;
        mTasks = new ArrayList<TaskJSON>();

        mChildEventListener = mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                mTasks.add(dataSnapshot.getValue(TaskJSON.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void populateViewHolder(final TaskViewHolder viewHolder, TaskJSON model, int position) {

        viewHolder.setName(model.getName());
        viewHolder.setDuration(model.getDuration());
        viewHolder.setAddress(model.getAddress());
        viewHolder.setCity(model.getCity());
        viewHolder.setTime(model.getDueDate());
        viewHolder.mDragImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mOnStartDragListener.onStartDrag(viewHolder);
                }
                return false;
            }
        });
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Add effect to if you touch it here
            }
        });
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mTasks, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return false;
    }

    @Override
    public void onItemDismiss(int position) {
        mTasks.remove(position);
        getRef(position).removeValue();
    }

    private void setIndexInFirebase() {
        for (TaskJSON task : mTasks) {
            int index = mTasks.indexOf(task);
            DatabaseReference ref = getRef(index);
            task.setIndex(Integer.toString(index));
            ref.setValue(task);
        }
    }

    @Override
    public void cleanup() {
        super.cleanup();
        setIndexInFirebase();
        mRef.removeEventListener(mChildEventListener);
    }
    public static class TaskViewHolder extends  RecyclerView.ViewHolder implements ItemTouchHelperViewHolder{

        View mView;
        public ImageView mDragImageView;

        public TaskViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mDragImageView = (ImageView) mView.findViewById(R.id.dragImageView);
        }

        public void setName(String name) {
            TextView nameView = (TextView) mView.findViewById(R.id.display_name);
            nameView.setText(name);
        }
        public void setDuration(String name) {
            TextView nameView = (TextView) mView.findViewById(R.id.display_duration);
            nameView.setText(name);
        }
        public void setAddress(String name) {
            TextView nameView = (TextView) mView.findViewById(R.id.display_address);
            nameView.setText(name);
        }
        public void setCity(String name) {
            TextView nameView = (TextView) mView.findViewById(R.id.display_city);
            nameView.setText(name);
        }
        public void setTime(Cal time) {

            GregorianCalendar due = new GregorianCalendar(time.getYear_int(),time.getMonth_int()-1,time.getDay_int(),time.getHour_int(),time.getMinute_int());
            GregorianCalendar today = new GregorianCalendar();
            long milliDif = due.getTimeInMillis()-today.getTimeInMillis();
            int days = (int) (milliDif/(1000*60*60*24.0));

            milliDif = milliDif - (1000*60*60*24)*days;
            int hours = (int) (milliDif/(1000*60*60.0));
            milliDif = milliDif - (1000*60*60)*hours;
            int minutes = (int) (milliDif/(1000*60.0));

            TextView dayView = (TextView) mView.findViewById(R.id.countDay);
            TextView hourView = (TextView) mView.findViewById(R.id.countHours);
            TextView minuteView = (TextView) mView.findViewById(R.id.countMin);

            dayView.setText(Integer.toString(days));
            hourView.setText(Integer.toString(hours));
            minuteView.setText(Integer.toString(minutes));


        }

        @Override
        public void onItemSelected() {
            itemView.animate()
                    .alpha(0.7f)
                    .scaleX(0.9f)
                    .scaleY(0.9f)
                    .setDuration(500);
        }

        @Override
        public void onItemClear() {
            itemView.animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .scaleY(1f);
        }
    }
}

