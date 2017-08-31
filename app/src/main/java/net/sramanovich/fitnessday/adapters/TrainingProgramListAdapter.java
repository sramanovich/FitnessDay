package net.sramanovich.fitnessday.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.sramanovich.fitnessday.Constants;
import net.sramanovich.fitnessday.ExerciseSetActivity;
import net.sramanovich.fitnessday.R;
import net.sramanovich.fitnessday.db.TrainingSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

public class TrainingProgramListAdapter extends ArrayAdapter<TrainingSet> {

    Context context;

    List<TrainingSet> objects;

    LayoutInflater lInflater;

    public TrainingProgramListAdapter(Context context, List<TrainingSet> objects) {
        super(context, 0, objects);
        this.context = context;
        this.objects = new ArrayList<>(objects);
        this.lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public TrainingSet getItem(int position) {
        return objects.get(position);
    }

    public boolean setItem(int position, TrainingSet object) {
        if(position<0||position>=getCount()) {
            return false;
        }

        objects.set(position, object);

        return true;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.training_program_item, parent, false);
        }

        TrainingSet set = getItem(position);

        TextView tvSeqNumber = (TextView) view.findViewById(R.id.textViewSeqNumber);
        if(tvSeqNumber!=null) {
            tvSeqNumber.setText(new Integer(position+1).toString()+".");
        }

        TextView tvName = (TextView) view.findViewById(R.id.textViewName);
        if(tvName!=null) {
            tvName.setText(set.exercise_name);
        }

        Button btnStart = (Button) view.findViewById(R.id.buttonStart);
        if(btnStart!=null) {
            btnStart.setTag(position);
            btnStart.setOnClickListener(new ButtonClickListener());
            if (set.setList.isEmpty()) {
                btnStart.setText("Start");
            } else {
                btnStart.setText("Continue");
            }
        }

        LinearLayout layoutItemColor = (LinearLayout) view.findViewById(R.id.program_item_layout);
        if (layoutItemColor != null ) {

            switch (set.split_nr) {
                case 1:
                    layoutItemColor.setBackgroundColor(view.getResources().getColor(R.color.colorExerciseListBackground1));
                    break;
                case 2:
                    layoutItemColor.setBackgroundColor(view.getResources().getColor(R.color.colorExerciseListBackground2));
                    break;
                case 3:
                    layoutItemColor.setBackgroundColor(view.getResources().getColor(R.color.colorExerciseListBackground3));
                    break;
                case 4:
                    layoutItemColor.setBackgroundColor(view.getResources().getColor(R.color.colorExerciseListBackground4));
                    break;
                case 5:
                    layoutItemColor.setBackgroundColor(view.getResources().getColor(R.color.colorExerciseListBackground5));
                    break;
                case 6:
                    layoutItemColor.setBackgroundColor(view.getResources().getColor(R.color.colorExerciseListBackground6));
                    break;
                case 7:
                    layoutItemColor.setBackgroundColor(view.getResources().getColor(R.color.colorExerciseListBackground7));
                    break;
                default:
                    layoutItemColor.setBackgroundColor(view.getResources().getColor(R.color.colorExerciseListBackgroundDef));
                    break;
            }
        }

        return view;
    }

    public void onOpenExerciseSetActivity(int position) {
        TrainingSet trainingSet = getItem(position);
        if(trainingSet==null)
            return;

        Intent intent = new Intent(context, ExerciseSetActivity.class);

        intent.putExtra(Constants.INTENT_PARAM_NAME, trainingSet.exercise_name);
        intent.putExtra(Constants.INTENT_PARAM_POSITION, position);
        intent.putParcelableArrayListExtra(Constants.INTENT_PARAM_SET_LIST, trainingSet.setList);

        Activity activity = (Activity)context;
        activity.startActivityForResult(intent, 1);
    }

    public void setSplitFilter(int split_nr) {
        ListIterator<TrainingSet> iter = objects.listIterator();
        while(iter.hasNext()) {
            TrainingSet set = iter.next();
            if(split_nr >0 && set.split_nr != split_nr ) {
                iter.remove();
            }
        }
    }

    public void sortList(){
        Collections.sort(objects, new Comparator<TrainingSet>() {
            @Override
            public int compare(TrainingSet lhs, TrainingSet rhs) {
                if (lhs.split_nr > rhs.split_nr) {
                    return 1;
                }
                else if (lhs.split_nr < rhs.split_nr) {
                    return -1;
                }

                return 0;
            }
        });
    }
    class ButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int position = (int)v.getTag();
            onOpenExerciseSetActivity(position);
        }
    }
}
