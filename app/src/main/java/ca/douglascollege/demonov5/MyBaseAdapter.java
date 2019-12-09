package ca.douglascollege.demonov5;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MyBaseAdapter extends BaseAdapter {

    List<String[]> adapterData = new ArrayList();

    public MyBaseAdapter (List<String[]> anyList){
        adapterData = anyList;
    }

    @Override
    public int getCount() {
        return adapterData.size();
    }

    @Override
    public String[] getItem(int position) {
        return adapterData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            convertView = layoutInflater.inflate(R.layout.externallayout, parent,false);
        }

        TextView textViewId = convertView.findViewById(R.id.textViewOne);
        TextView textViewName = convertView.findViewById(R.id.textViewTwo);
        TextView textViewDept = convertView.findViewById(R.id.textViewThree);

        String[] eachRecord = getItem(position);

        if (eachRecord[0].equals("Id")){
            textViewId.setTextColor(Color.RED);
            textViewName.setTextColor(Color.RED);
            textViewDept.setTextColor(Color.RED);
        }
        textViewId.setText(eachRecord[0]);
        textViewName.setText(eachRecord[1]);
        textViewDept.setText(eachRecord[2]);

        textViewId.setGravity(Gravity.LEFT);
        textViewName.setGravity(Gravity.LEFT);
        textViewDept.setGravity(Gravity.LEFT);

        return convertView;

    }
}
