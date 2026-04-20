package com.example.lab5_bai2;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ViewCompat.setOnApplyWindowInsetsListener(toolbar, (view, insets) -> {
            Insets statusBarInsets = insets.getInsets(WindowInsetsCompat.Type.statusBars());
            view.setPadding(
                    view.getPaddingLeft(),
                    statusBarInsets.top,
                    view.getPaddingRight(),
                    view.getPaddingBottom()
            );
            return insets;
        });

        ListView lvPeople = findViewById(R.id.lvPeople);
        List<Person> people = new ArrayList<>();
        people.add(new Person("Người thứ 1", R.drawable.person_1));
        people.add(new Person("Người thứ 2", R.drawable.person_2));
        people.add(new Person("Người thứ 3", R.drawable.person_3));
        people.add(new Person("Người thứ 4", R.drawable.person_4));

        PersonAdapter adapter = new PersonAdapter(this, people);
        lvPeople.setAdapter(adapter);
        lvPeople.setOnItemClickListener((parent, view, position, id) -> {
            String message = "Bạn vừa chọn " + people.get(position).getName();
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
        });
    }
}