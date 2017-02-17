package net.sramanovich.fitnessday.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import net.sramanovich.fitnessday.Constants;
import net.sramanovich.fitnessday.ExerciseSetActivity;
import net.sramanovich.fitnessday.R;
import net.sramanovich.fitnessday.db.TrainingSet;

import java.util.List;

public class TrainingProgramListAdapter extends ArrayAdapter<TrainingSet> {

    Context context;

    List<TrainingSet> objects;

    LayoutInflater lInflater;

    public TrainingProgramListAdapter(Context context, List<TrainingSet> objects) {
        super(context, 0, objects);
        this.context = context;
        this.objects = objects;
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

    class ButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int position = (int)v.getTag();
            onOpenExerciseSetActivity(position);
        }
    }
}
