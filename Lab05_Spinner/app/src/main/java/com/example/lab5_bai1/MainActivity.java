package com.example.lab5_bai1;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Spinner spinnerPerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        spinnerPerson = findViewById(R.id.spinnerPerson);

        List<Person> listPerson = new ArrayList<>();
        listPerson.add(new Person(R.drawable.person_1, getString(R.string.person_1)));
        listPerson.add(new Person(R.drawable.person_2, getString(R.string.person_2)));
        listPerson.add(new Person(R.drawable.person_3, getString(R.string.person_3)));
        listPerson.add(new Person(R.drawable.person_4, getString(R.string.person_4)));

        PersonAdapter personAdapter = new PersonAdapter(this, R.layout.spinner_item, listPerson);
        spinnerPerson.setAdapter(personAdapter);

        spinnerPerson.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Person selectedPerson = personAdapter.getItem(position);
                if (selectedPerson != null) {
                    String message = getString(R.string.selected_person, selectedPerson.getNamePerson());
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No-op.
            }
        });
    }
}