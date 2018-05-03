/*
 *    Copyright (C) 2015 Haruki Hasegawa
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.rescribe.doctor.adapters.waiting_list;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultAction;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionDefault;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionMoveToSwipedDirection;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableSwipeableItemViewHolder;
import com.h6ah4i.android.widget.advrecyclerview.utils.RecyclerViewAdapterUtils;
import com.rescribe.doctor.R;
import com.rescribe.doctor.model.waiting_list.AbstractDataProvider;
import com.rescribe.doctor.model.waiting_list.Active;
import com.rescribe.doctor.model.waiting_list.PatientDataActiveWaitingListProvider;
import com.rescribe.doctor.util.CommonMethods;
import com.rescribe.doctor.util.RescribeConstants;
import com.rescribe.doctor.util.dragable_swipable.DrawableUtils;
import com.rescribe.doctor.util.dragable_swipable.ViewUtils;

import java.util.ArrayList;

import static com.rescribe.doctor.adapters.waiting_list.DraggableSwipeableViewAllWaitingListAdapter.CONFIRMED;
import static com.rescribe.doctor.adapters.waiting_list.DraggableSwipeableViewAllWaitingListAdapter.IN_QUEUE;
import static com.rescribe.doctor.util.RescribeConstants.SALUTATION;

public class DraggableSwipeableActiveWaitingListAdapter
        extends RecyclerView.Adapter<DraggableSwipeableActiveWaitingListAdapter.MyViewHolder>
        implements DraggableItemAdapter<DraggableSwipeableActiveWaitingListAdapter.MyViewHolder>,
        SwipeableItemAdapter<DraggableSwipeableActiveWaitingListAdapter.MyViewHolder> {
    private static final String TAG = "MyDSItemAdapter";

    private int mPreLeftSwipePosition = RecyclerView.NO_POSITION;

    public ArrayList<Active> getAllItems() {

        ArrayList<Active> activeAll = new ArrayList<>();
        for (PatientDataActiveWaitingListProvider.ConcreteActiveData concreteData : mProvider.getActiveData())
            activeAll.add(concreteData.getActiveAll());
        return activeAll;

    }

    public void removeItem(int index) {
        mProvider.removeItem(index);
        notifyItemRemoved(index);
    }

    // NOTE: Make accessible with short name
    private interface Draggable extends DraggableItemConstants {
    }

    private interface Swipeable extends SwipeableItemConstants {
    }

    private AbstractDataProvider mProvider;
    private EventListener mEventListener;
    private View.OnClickListener mItemViewOnClickListener;
    private View.OnClickListener mSwipeableViewContainerOnClickListener;

    public interface EventListener {
        void onDeleteClick(int position, Active viewAll);

        void onPhoneClick(String phoneNumber);

        void onItemPinned(int position);

        void onItemViewClicked(View v, boolean pinned);

        void onItemMoved(int fromPosition, int toPosition);
    }

    public static class MyViewHolder extends AbstractDraggableSwipeableItemViewHolder {
        FrameLayout mContainer;
        ImageView mDragHandle;

        LinearLayout mBehindViews;
        ImageButton deleteButton;

        LinearLayout mCardView;
        ImageView mBluelineImageView;
        TextView mPatientIdTextView;
        TextView mAppointmentTime;
        ImageView mPatientImageView;
        TextView mPatientNameTextView;
        LinearLayout mPatientDetailsLinearLayout;
        TextView mStatusTextView;
        TextView mTypeStatus;
        LinearLayout mAppointmentDetailsLinearLayout;
        TextView mAppointmentLabelTextView;
        TextView mAppointmentTimeTextView;
        TextView mPatientPhoneNumber;
        View mSeparatorView;
        TextView mTokenLabelTextView;
        TextView mTokenNumber;


        MyViewHolder(View v) {
            super(v);
            mContainer = v.findViewById(R.id.container);
            mDragHandle = v.findViewById(R.id.dragHandle);

            mBehindViews = v.findViewById(R.id.behind_views);
            deleteButton = v.findViewById(R.id.deleteButton);

            mCardView = v.findViewById(R.id.cardView);
            mBluelineImageView = v.findViewById(R.id.bluelineImageView);
            mPatientIdTextView = v.findViewById(R.id.patientIdTextView);
            mAppointmentTime = v.findViewById(R.id.appointmentTime);
            mPatientImageView = v.findViewById(R.id.patientImageView);
            mPatientNameTextView = v.findViewById(R.id.patientNameTextView);
            mPatientDetailsLinearLayout = v.findViewById(R.id.patientDetailsLinearLayout);
            mStatusTextView = v.findViewById(R.id.statusTextView);
            mTypeStatus = v.findViewById(R.id.typeStatus);
            mAppointmentDetailsLinearLayout = v.findViewById(R.id.appointmentDetailsLinearLayout);
            mAppointmentLabelTextView = v.findViewById(R.id.appointmentLabelTextView);
            mAppointmentTimeTextView = v.findViewById(R.id.appointmentTimeTextView);
            mPatientPhoneNumber = v.findViewById(R.id.patientPhoneNumber);
            mSeparatorView = v.findViewById(R.id.separatorView);
            mTokenLabelTextView = v.findViewById(R.id.tokenLabelTextView);
            mTokenNumber = v.findViewById(R.id.tokenNumber);
        }

        @Override
        public View getSwipeableContainerView() {
            return mContainer;
        }
    }

    public DraggableSwipeableActiveWaitingListAdapter(AbstractDataProvider dataProvider) {
        mProvider = dataProvider;
        mItemViewOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemViewClick(v);
            }
        };
        mSwipeableViewContainerOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSwipeableViewContainerClick(v);
            }
        };

        // DraggableItemAdapter and SwipeableItemAdapter require stable ID, and also
        // have to implement the getItemId() method appropriately.
        setHasStableIds(true);
    }

    private void onItemViewClick(View v) {
        if (mEventListener != null) {
            mEventListener.onItemViewClicked(v, true); // true --- pinned
        }
    }

    private void onSwipeableViewContainerClick(View v) {
        if (mEventListener != null) {
            mEventListener.onItemViewClicked(RecyclerViewAdapterUtils.getParentViewHolderItemView(v), false);  // false --- not pinned
        }
    }

    @Override
    public long getItemId(int position) {
        return mProvider.getItem(position).getId();
    }

    @Override
    public int getItemViewType(int position) {
        return mProvider.getItem(position).getViewType();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.list_item_draggable, parent, false);
        return new MyViewHolder(v);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final AbstractDataProvider.Data item = mProvider.getItem(position);

        // set listeners
        // (if the item is *pinned*, click event comes to the itemView)
        holder.itemView.setOnClickListener(mItemViewOnClickListener);
        // (if the item is *not pinned*, click event comes to the mContainer)
        holder.mContainer.setOnClickListener(mSwipeableViewContainerOnClickListener);

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // call delete api
                mEventListener.onDeleteClick(position, item.getActiveAll());
            }
        });
        holder.mPatientPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEventListener.onPhoneClick(item.getActiveAll().getPatientPhone());
            }
        });


        //---- START: Setting of hospitalID or referecne ID, reference is IS high priority than hospitalID.-----
        String dataToShowInPatientID = String.valueOf(item.getActiveAll().getReferenceID());

        if (dataToShowInPatientID == null || RescribeConstants.BLANK.equalsIgnoreCase(dataToShowInPatientID)) {
            dataToShowInPatientID = String.valueOf(item.getActiveAll().getHospitalPatId());
        }
        //---- END------

        holder.mPatientIdTextView.setText(holder.mPatientIdTextView.getResources().getString(R.string.id) + " " + dataToShowInPatientID);
        if (!item.getActiveAll().getWaitingInTime().equals("")) {
            holder.mAppointmentTime.setVisibility(View.VISIBLE);
            String waitingTime = CommonMethods.formatDateTime(item.getActiveAll().getWaitingInTime(), RescribeConstants.DATE_PATTERN.hh_mm_a, RescribeConstants.DATE_PATTERN.HH_mm_ss, RescribeConstants.TIME).toLowerCase();
            holder.mAppointmentTime.setText(holder.mPatientIdTextView.getResources().getString(R.string.in_time) + " - " + waitingTime);
        } else {
            holder.mAppointmentTime.setVisibility(View.INVISIBLE);
        }
        if (!item.getActiveAll().getAppointmentTime().equals("")) {
            holder.mAppointmentTimeTextView.setVisibility(View.VISIBLE);
            holder.mAppointmentLabelTextView.setVisibility(View.VISIBLE);
            String appointmentScheduleTime = CommonMethods.formatDateTime(item.getActiveAll().getAppointmentTime(), RescribeConstants.DATE_PATTERN.hh_mm_a, RescribeConstants.DATE_PATTERN.HH_mm_ss, RescribeConstants.TIME).toLowerCase();
            holder.mAppointmentTimeTextView.setText(appointmentScheduleTime);

        } else {
            holder.mAppointmentTimeTextView.setVisibility(View.INVISIBLE);
            holder.mAppointmentLabelTextView.setVisibility(View.INVISIBLE);
        }

        holder.mPatientPhoneNumber.setText(item.getActiveAll().getPatientPhone());
        holder.mTokenNumber.setText(item.getActiveAll().getTokenNumber());

        String salutation;
        if (item.getActiveAll().getSalutation() != 0)
            salutation = SALUTATION[item.getActiveAll().getSalutation() - 1];
        else salutation = "";


        holder.mPatientNameTextView.setText(salutation + CommonMethods.toCamelCase(item.getActiveAll().getPatientName()));
        holder.mTypeStatus.setText(" " + item.getActiveAll().getWaitingStatus());
        TextDrawable textDrawable = CommonMethods.getTextDrawable(holder.mPatientImageView.getContext(), item.getActiveAll().getPatientName());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.dontAnimate();
        requestOptions.override(holder.mPatientImageView.getResources().getDimensionPixelSize(R.dimen.dp67));
        requestOptions.circleCrop();
        requestOptions.placeholder(textDrawable);
        requestOptions.error(textDrawable);

        Glide.with(holder.mPatientImageView.getContext())
                .load(item.getActiveAll().getPatientImageUrl())
                .apply(requestOptions)
                .into(holder.mPatientImageView);

        // set background resource (target view ID: container)
        final int dragState = holder.getDragStateFlags();
        final int swipeState = holder.getSwipeStateFlags();

        if (((dragState & Draggable.STATE_FLAG_IS_UPDATED) != 0) ||
                ((swipeState & Swipeable.STATE_FLAG_IS_UPDATED) != 0)) {
            int bgResId;

            if ((dragState & Draggable.STATE_FLAG_IS_ACTIVE) != 0) {
                bgResId = R.drawable.bg_item_dragging_active_state;

                // need to clear drawable state here to get correct appearance of the dragging item.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    DrawableUtils.clearState(holder.mContainer.getForeground());
                }
            } else if ((dragState & Draggable.STATE_FLAG_DRAGGING) != 0) {
                bgResId = R.drawable.bg_item_dragging_state;
            } else if ((swipeState & Swipeable.STATE_FLAG_IS_ACTIVE) != 0) {
                bgResId = R.drawable.bg_item_swiping_active_state;
            } else if ((swipeState & Swipeable.STATE_FLAG_SWIPING) != 0) {
                bgResId = R.drawable.bg_item_swiping_state;
            } else {
                bgResId = R.drawable.bg_item_normal_state;
            }

            holder.mContainer.setBackgroundResource(bgResId);
        }

        if (item.getActiveAll().getWaitingStatusId().equals(IN_QUEUE) || item.getActiveAll().getWaitingStatusId().equals(CONFIRMED)) {
            if (getAllItems().size() == 1)
                holder.mDragHandle.setVisibility(View.GONE);
            else holder.mDragHandle.setVisibility(View.VISIBLE);

            holder.setMaxLeftSwipeAmount(-0.4f);
            holder.setSwipeItemHorizontalSlideAmount(item.isPinned() ? -0.4f : 0);
        } else {
            holder.mDragHandle.setVisibility(View.GONE);
            holder.setMaxLeftSwipeAmount(0);
            holder.setSwipeItemHorizontalSlideAmount(item.isPinned() ? 0 : 0);
        }


        holder.setMaxRightSwipeAmount(0);

        // set height programmatically

        ViewTreeObserver vto = holder.mContainer.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                holder.mContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                ViewGroup.LayoutParams layoutParams = holder.mBehindViews.getLayoutParams();
                layoutParams.height = holder.mContainer.getMeasuredHeight();
                layoutParams.width = holder.mContainer.getMeasuredWidth();
                holder.mBehindViews.setLayoutParams(layoutParams);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mProvider.getCount();
    }

    @Override
    public void onMoveItem(int fromPosition, int toPosition) {
        Log.d(TAG, "onMoveItem(fromPosition = " + fromPosition + ", toPosition = " + toPosition + ")");

        move(fromPosition, toPosition);
        if ((toPosition + 1) < mProvider.getCount()) {
            Active active = mProvider.getItem(toPosition + 1).getActiveAll();
            if (active.getWaitingStatusId().equals(IN_QUEUE) || active.getWaitingStatusId().equals(CONFIRMED))
                mEventListener.onItemMoved(fromPosition, toPosition);
            else
                move(toPosition, fromPosition);
        } else mEventListener.onItemMoved(fromPosition, toPosition);
    }

    private void move(int fromPosition, int toPosition) {
        mProvider.moveItem(fromPosition, toPosition);
        if (fromPosition == mPreLeftSwipePosition)
            mPreLeftSwipePosition = toPosition;
    }

    @Override
    public boolean onCheckCanStartDrag(MyViewHolder holder, int position, int x, int y) {
        // x, y --- relative from the itemView's top-left
        final View containerView = holder.mContainer;
        final View dragHandleView = holder.mDragHandle;

        final int offsetX = containerView.getLeft() + (int) (containerView.getTranslationX() + 0.5f);
        final int offsetY = containerView.getTop() + (int) (containerView.getTranslationY() + 0.5f);

        return ViewUtils.hitTest(dragHandleView, x - offsetX, y - offsetY);
    }

    @Override
    public ItemDraggableRange onGetItemDraggableRange(MyViewHolder holder, int position) {
        // no drag-sortable range specified
        return null;
    }

    @Override
    public boolean onCheckCanDrop(int draggingPosition, int dropPosition) {
        return true;
    }

    @Override
    public void onItemDragStarted(int position) {
        notifyDataSetChanged();
    }

    @Override
    public void onItemDragFinished(int fromPosition, int toPosition, boolean result) {
        notifyDataSetChanged();
    }

    @Override
    public int onGetSwipeReactionType(MyViewHolder holder, int position, int x, int y) {
        if (onCheckCanStartDrag(holder, position, x, y)) {
            return Swipeable.REACTION_CAN_NOT_SWIPE_BOTH_H;
        } else {
            return Swipeable.REACTION_CAN_SWIPE_BOTH_H;
        }
    }

    @Override
    public void onSwipeItemStarted(MyViewHolder holder, int position) {
        notifyDataSetChanged();
    }

    @Override
    public void onSetSwipeBackground(MyViewHolder holder, int position, int type) {
        if (type == Swipeable.DRAWABLE_SWIPE_NEUTRAL_BACKGROUND) {
            holder.mBehindViews.setVisibility(View.GONE);
        } else {
            holder.mBehindViews.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("SwitchIntDef")
    @Override
    public SwipeResultAction onSwipeItem(MyViewHolder holder, final int position, int result) {
        Log.d(TAG, "onSwipeItem(position = " + position + ", result = " + result + ")");

        switch (result) {
            case Swipeable.RESULT_SWIPED_LEFT:
                SwipeLeftResultAction swipeLeftResultAction = new SwipeLeftResultAction(this, position);
                if (mPreLeftSwipePosition != RecyclerView.NO_POSITION) {
                    mProvider.getItem(mPreLeftSwipePosition).setPinned(false);
                    notifyItemChanged(mPreLeftSwipePosition);
                }
                mPreLeftSwipePosition = position;
                return swipeLeftResultAction;

            case Swipeable.RESULT_CANCELED:
            default:
                if (position != RecyclerView.NO_POSITION) {
                    return new UnpinResultAction(this, position);
                } else {
                    return null;
                }
        }
    }

    public EventListener getEventListener() {
        return mEventListener;
    }

    public void setEventListener(EventListener eventListener) {
        mEventListener = eventListener;
    }


    private static class SwipeLeftResultAction extends SwipeResultActionMoveToSwipedDirection {
        private DraggableSwipeableActiveWaitingListAdapter mAdapter;
        private final int mPosition;
        private boolean mSetPinned;

        SwipeLeftResultAction(DraggableSwipeableActiveWaitingListAdapter adapter, int position) {
            mAdapter = adapter;
            mPosition = position;
        }

        @Override
        protected void onPerformAction() {
            super.onPerformAction();

            AbstractDataProvider.Data item = mAdapter.mProvider.getItem(mPosition);

            if (!item.isPinned()) {
                item.setPinned(true);
                mAdapter.notifyItemChanged(mPosition);
                mSetPinned = true;
            }
        }

        @Override
        protected void onSlideAnimationEnd() {
            super.onSlideAnimationEnd();

            if (mSetPinned && mAdapter.mEventListener != null) {
                mAdapter.mEventListener.onItemPinned(mPosition);
            }
        }

        @Override
        protected void onCleanUp() {
            super.onCleanUp();
            // clear the references
            mAdapter = null;
        }
    }

    private static class UnpinResultAction extends SwipeResultActionDefault {
        private DraggableSwipeableActiveWaitingListAdapter mAdapter;
        private final int mPosition;

        UnpinResultAction(DraggableSwipeableActiveWaitingListAdapter adapter, int position) {
            mAdapter = adapter;
            mPosition = position;
        }

        @Override
        protected void onPerformAction() {
            super.onPerformAction();

            AbstractDataProvider.Data item = mAdapter.mProvider.getItem(mPosition);
            if (item.isPinned()) {
                item.setPinned(false);
                mAdapter.notifyItemChanged(mPosition);
            }
        }

        @Override
        protected void onCleanUp() {
            super.onCleanUp();
            // clear the references
            mAdapter = null;
        }
    }

}
