package net.sramanovich.fitnessday.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.sramanovich.fitnessday.R;
import net.sramanovich.fitnessday.db.ExercisesTable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BodyPartRecycleViewAdapter extends RecyclerView.Adapter<BodyPartRecycleViewAdapter.ViewHolder> {

    private List<ExercisesTable.BodyPart> mData = new ArrayList<>();

    LayoutInflater lInflater;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ImageView mImageView;

        public ViewHolder(View v) {
            super(v);
            //mTextView = (TextView) v.findViewById(R.id.textViewBodyPartName);
            mImageView = (ImageView) v;
            //mImageView = (ImageView) v.findViewById(R.id.imgViewBodyPart);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public BodyPartRecycleViewAdapter(Context context) {
        initData();
        this.lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private void initData() {
        mData.clear();
        mData = Arrays.asList(ExercisesTable.BodyPart.values());
    }

    // Create new views (invoked by the layout manager)
    @Override
    public BodyPartRecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                    int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout)LayoutInflater.from(parent.getContext())
                .inflate(R.layout.body_types_recycle_view_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        //View viewName = v.findViewById(R.id.textViewBodyPartName);
        View viewImage = v.findViewById(R.id.imgViewBodyPart);
        v.removeView(viewImage);
        ViewHolder vh = new ViewHolder(viewImage);
        //v.removeView(viewImage);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        if (position < 0 || position >= mData.size()) {
            return;
        }

        //holder.mTextView.setText(mData.get(position).getName());

        switch (mData.get(position)) {
            case BODY_PART_ALL:
                holder.mImageView.setImageResource(R.drawable.body_0_full);
                break;
            case BODY_PART_ABDOMINALS:
                holder.mImageView.setImageResource(R.drawable.body_1_abdominals);
                break;
            case BODY_PART_BICEPS:
                holder.mImageView.setImageResource(R.drawable.body_2_biceps);
                break;
            case BODY_PART_CALVES:
                holder.mImageView.setImageResource(R.drawable.body_3_calves);
                break;
            case BODY_PART_CHEST:
                holder.mImageView.setImageResource(R.drawable.body_4_chest);
                break;
            case BODY_PART_FOREARMS:
                holder.mImageView.setImageResource(R.drawable.body_5_forearms);
                break;
            case BODY_PART_GLUTES:
                holder.mImageView.setImageResource(R.drawable.body_6_glutes);
                break;
            case BODY_PART_HAMSTRINGS:
                holder.mImageView.setImageResource(R.drawable.body_7_hamstrings);
                break;
            case BODY_PART_LATS:
                holder.mImageView.setImageResource(R.drawable.body_8_lats);
                break;
            case BODY_PART_LOWER_BACK:
                holder.mImageView.setImageResource(R.drawable.body_9_lower_back);
                break;
            case BODY_PART_QUADS:
                holder.mImageView.setImageResource(R.drawable.body_10_quads);
                break;
            case BODY_PART_SHOULDERS:
                holder.mImageView.setImageResource(R.drawable.body_11_shoulders);
                break;
            case BODY_PART_TRAPS:
                holder.mImageView.setImageResource(R.drawable.body_12_traps);
                break;
            case BODY_PART_TRICEPS:
                holder.mImageView.setImageResource(R.drawable.body_13_triceps);
                break;
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mData.size();

    }
}
