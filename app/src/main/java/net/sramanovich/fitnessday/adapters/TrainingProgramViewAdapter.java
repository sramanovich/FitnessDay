package net.sramanovich.fitnessday.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.sramanovich.fitnessday.R;
import net.sramanovich.fitnessday.db.TrainingSet;
import net.sramanovich.fitnessday.utils.PairSet;

import java.util.List;

public class TrainingProgramViewAdapter extends TrainingProgramListAdapter {

    public TrainingProgramViewAdapter(Context context, List<TrainingSet> objects) {
        super(context, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.training_program_view_item, parent, false);
        }

        TrainingSet set = getItem(position);

        TextView tvSeqNumber = (TextView) view.findViewById(R.id.textViewSeqNumber2);
        if(tvSeqNumber!=null) {
            tvSeqNumber.setText(new Integer(position+1).toString()+".");
        }

        TextView tvName = (TextView) view.findViewById(R.id.textViewProgramName);
        if(tvName!=null) {
            tvName.setText(set.exercise_name);
        }

        TextView tvExercises = (TextView) view.findViewById(R.id.textViewExercises);
        if(tvExercises!=null) {
            StringBuilder setExercise = new StringBuilder();
            for(PairSet pairSet: set.setList) {
                setExercise.append(pairSet.getReps());
                setExercise.append("(");
                setExercise.append(pairSet.getWeight());
                setExercise.append(" [kg]); ");
            }
            tvExercises.setText(setExercise.toString());
        }
        return view;
    }
}
