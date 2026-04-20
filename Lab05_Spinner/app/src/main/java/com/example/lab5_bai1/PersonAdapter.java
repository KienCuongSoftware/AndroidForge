package com.example.lab5_bai1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class PersonAdapter extends ArrayAdapter<Person> {

    public PersonAdapter(@NonNull Context context, int resource, @NonNull List<Person> listPerson) {
        super(context, resource, listPerson);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    private View createView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_item, parent, false);
        }

        ImageView imageViewPerson = convertView.findViewById(R.id.imageViewSpinner);
        TextView textViewPerson = convertView.findViewById(R.id.textViewSpinner);

        Person person = getItem(position);
        if (person != null) {
            imageViewPerson.setImageResource(person.getImagePerson());
            textViewPerson.setText(person.getNamePerson());
        }

        return convertView;
    }
}
