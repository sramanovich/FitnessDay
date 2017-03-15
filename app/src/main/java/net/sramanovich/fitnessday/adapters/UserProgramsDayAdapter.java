package net.sramanovich.fitnessday.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import net.sramanovich.fitnessday.R;
import net.sramanovich.fitnessday.utils.ProgramData;

import java.util.List;

public class UserProgramsDayAdapter extends ArrayAdapter<ProgramData> {

    Context context;

    List<ProgramData> objects;

    LayoutInflater lInflater;

    public UserProgramsDayAdapter(Context context, List<ProgramData> objects) {
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
    public ProgramData getItem(int position) {
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
            view = lInflater.inflate(R.layout.user_programs_content_item2, parent, false);
        }

        ProgramData data = getItem(position);

        TextView tvName = (TextView) view.findViewById(R.id.textDayProgramName);
        if(tvName!=null) {
            tvName.setText(data.getmName());
        }
        TextView tvNote = (TextView) view.findViewById(R.id.textDayProgramNote);
        if(tvNote!=null) {
            tvNote.setText(data.getmNote());
        }

        return view;
    }
}

