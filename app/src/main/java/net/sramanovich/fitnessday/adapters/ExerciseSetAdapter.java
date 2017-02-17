package net.sramanovich.fitnessday.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import net.sramanovich.fitnessday.R;
import net.sramanovich.fitnessday.utils.PairSet;

import java.util.List;

public class ExerciseSetAdapter extends ArrayAdapter<PairSet>{

    Context context;

    List<PairSet> objects;

    LayoutInflater lInflater;

    public ExerciseSetAdapter(Context context, List<PairSet> objects) {
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
    public PairSet getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.exrcise_set_item, parent, false);
        }

        PairSet pair = getItem(position);

        TextView tvReps = (TextView) view.findViewById(R.id.textViewReps);
        if(tvReps!=null) {
            tvReps.setText(pair.getReps().toString());
        }

        TextView tvWeight = (TextView) view.findViewById(R.id.textViewWeight);
        if(tvWeight!=null) {
            tvWeight.setText(pair.getWeight().toString());
        }

        Button btnRemoveRep = (Button) view.findViewById(R.id.buttonRemove);
        if(btnRemoveRep!=null) {
            btnRemoveRep.setTag(position);
            btnRemoveRep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int)v.getTag();
                    objects.remove(position);
                    notifyDataSetChanged();
                }
            });
        }

        return view;
    }
}
