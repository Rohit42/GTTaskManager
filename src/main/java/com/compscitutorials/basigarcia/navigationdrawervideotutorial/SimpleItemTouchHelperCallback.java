package com.compscitutorials.basigarcia.navigationdrawervideotutorial;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ArrayAdapter;

import com.compscitutorials.basigarcia.navigationdrawervideotutorial.ItemTouchHelperAdapter;

import java.util.ArrayList;

public class SimpleItemTouchHelperCallback extends ItemTouchHelper.SimpleCallback {
    private final ItemTouchHelperAdapter mAdapter;

    private Paint p = new Paint();
    private Context mContext;
    //  This constructor takes an ItemTouchHelperAdapter parameter. When implemented in
    //  FirebaseRestaurantListAdapter, the ItemTouchHelperAdapter instance will pass the gesture event back to the
    //  Firebase adapter where we will define what occurs when an item is moved or dismissed.

    public SimpleItemTouchHelperCallback(ItemTouchHelperAdapter adapter,Context context) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        mAdapter = adapter;
        mContext = context;

    }

    //  The method below informs the ItemTouchHelperAdapter that drag gestures are enabled.
    //  We could also disable drag gestures by returning 'false'.

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    //  The method below informs the ItemTouchHelperAdapter that swipe gestures are enabled.
    //  We could also disable them by returning 'false'.

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    //  getMovementFlags informs the ItemTouchHelper which movement directions are supported.
    // For example, when a user drags a list item, they press 'Down' to begin the drag and lift their finger, 'Up',  to end the drag.



    //  The method below notifies the adapter that an item has moved.
    //  This triggers the onItemMove override in our Firebase adapter,
    //  which will eventually handle updating the restaurants ArrayList to reflect the item's new position.

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source,
                          RecyclerView.ViewHolder target) {
        if (source.getItemViewType() != target.getItemViewType()) {
            return false;
        }
        mAdapter.onItemMove(source.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    //  The method below notifies the adapter that an item was dismissed.
    //  This triggers the onItemDismiss override in our Firebase adapter
    //  which will eventually handle 55deleting this item from the user's "Saved Restaurants" in Firebase.

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        int position = viewHolder.getAdapterPosition();

        if (direction == ItemTouchHelper.LEFT){
            mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
        } else {
            Intent intent = new Intent(mContext,OneActivity.class);
            intent.putExtra("position", viewHolder.getAdapterPosition());
            mContext.startActivity(intent);

        }

    }


    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        Bitmap icon;
        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

            View itemView = viewHolder.itemView;
            float height = (float) itemView.getBottom() - (float) itemView.getTop();
            float width = height / 3;

            if(dX > 0){
                p.setColor(Color.parseColor("#388E3C"));
                RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                c.drawRect(background,p);
                icon = BitmapFactory.decodeResource(itemView.getResources(), R.drawable.ic_edit_white);
                RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                c.drawBitmap(icon,null,icon_dest,p);
            } else {
                p.setColor(Color.parseColor("#D32F2F"));
                RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                c.drawRect(background,p);
                icon = BitmapFactory.decodeResource(itemView.getResources(), R.drawable.ic_delete_white);
                RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                c.drawBitmap(icon,null,icon_dest,p);
            }
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {

        //  This conditional ensures we only change appearance of active items:

        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder instanceof ItemTouchHelperViewHolder) {

                //  This tells the viewHolder that an item is being moved or dragged:

                ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
                itemViewHolder.onItemSelected();
            }
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    //  This triggers the callback in the ItemTouchHelperViewHolder which will be sent to our RestaurantListViewHolder.
    //  Then, in the clearView override in RestaurantListViewHolder, we will remove the animations attached
    //   to 'selected' items, since this item will no longer be actively selected.

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if (viewHolder instanceof ItemTouchHelperViewHolder) {

            //  Tells the view holder to return the item back to its normal appearance:

            ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
            itemViewHolder.onItemClear();
        }
    }
}