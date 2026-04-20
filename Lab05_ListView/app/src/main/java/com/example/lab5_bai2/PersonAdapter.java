package com.example.lab5_bai2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class PersonAdapter extends BaseAdapter {
    private final Context context;
    private final List<Person> people;

    public PersonAdapter(Context context, List<Person> people) {
        this.context = context;
        this.people = people;
    }

    @Override
    public int getCount() {
        return people.size();
    }

    @Override
    public Object getItem(int position) {
        return people.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_person, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Person person = people.get(position);
        holder.txtName.setText(person.getName());
        holder.imgAvatar.setImageResource(person.getAvatarResId());
        return convertView;
    }

    private static class ViewHolder {
        private final ImageView imgAvatar;
        private final TextView txtName;

        private ViewHolder(View itemView) {
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            txtName = itemView.findViewById(R.id.txtName);
        }
    }
}
