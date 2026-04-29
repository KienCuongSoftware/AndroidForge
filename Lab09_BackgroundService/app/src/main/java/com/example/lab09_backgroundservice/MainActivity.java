package com.example.lab09_backgroundservice;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

public class MainActivity extends AppCompatActivity {
    private String selectedAudioUri;
    private TextView textViewSelectedFile;

    private final ActivityResultLauncher<Intent> pickAudioLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    if (uri != null) {
                        try {
                            final int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION;
                            getContentResolver().takePersistableUriPermission(uri, takeFlags);
                        } catch (SecurityException ignored) {
                            // Persisted permission is not always available depending on provider.
                        }
                        selectedAudioUri = uri.toString();
                        textViewSelectedFile.setText(getString(R.string.selected_audio, uri.getLastPathSegment()));
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonPickAudio = findViewById(R.id.buttonPickAudio);
        Button buttonStart = findViewById(R.id.buttonStart);
        Button buttonStop = findViewById(R.id.buttonStop);
        textViewSelectedFile = findViewById(R.id.textViewSelectedFile);

        buttonPickAudio.setOnClickListener(v -> openAudioPicker());

        buttonStart.setOnClickListener(v -> {
            if (selectedAudioUri == null || selectedAudioUri.isEmpty()) {
                Toast.makeText(this, R.string.pick_audio_first, Toast.LENGTH_SHORT).show();
                return;
            }
            Intent serviceIntent = new Intent(this, MusicService.class);
            serviceIntent.setAction(MusicService.ACTION_START);
            serviceIntent.putExtra(MusicService.EXTRA_AUDIO_URI, selectedAudioUri);
            startService(serviceIntent);
        });

        buttonStop.setOnClickListener(v -> {
            Intent serviceIntent = new Intent(this, MusicService.class);
            serviceIntent.setAction(MusicService.ACTION_STOP);
            startService(serviceIntent);
        });
    }

    private void openAudioPicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("audio/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        pickAudioLauncher.launch(intent);
    }
}