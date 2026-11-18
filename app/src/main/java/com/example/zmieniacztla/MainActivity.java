package com.example.zmieniacztla;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    private ImageView bgImage;
    private TextView tvText;
    private final int[] backgrounds = {R.drawable.tlo1, R.drawable.tlo2};
    private final int[] colors = {R.color.text_color, R.color.text_opt2, R.color.text_opt3};
    private String[] texts;
    private int bgIndex = 0;
    private int textIndex = 0;
    private int colorIndex = 0;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable bgRunnable = new Runnable() {
        @Override
        public void run() {
            bgIndex = (bgIndex + 1) % backgrounds.length;
            bgImage.setImageResource(backgrounds[bgIndex]);
            handler.postDelayed(this, 3000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = getSharedPreferences("Prefs", MODE_PRIVATE);
        boolean isNight = prefs.getBoolean("night", false);
        colorIndex = prefs.getInt("color", 0);

        AppCompatDelegate.setDefaultNightMode(isNight ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bgImage = findViewById(R.id.bgImage);
        tvText = findViewById(R.id.tvText);
        Button btnText = findViewById(R.id.btnChangeText);
        Button btnColor = findViewById(R.id.btnChangeColor);
        Button btnBg = findViewById(R.id.btnChangeBg);
        SwitchCompat switchNight = findViewById(R.id.switchNight);
        texts = getResources().getStringArray(R.array.texts);
        bgImage.setImageResource(backgrounds[bgIndex]);
        tvText.setText(texts[textIndex]);
        tvText.setTextColor(ContextCompat.getColor(this, colors[colorIndex]));
        switchNight.setChecked(isNight);

        btnText.setOnClickListener(v -> {
            textIndex = (textIndex + 1) % texts.length;
            tvText.setText(texts[textIndex]);
        });

        btnColor.setOnClickListener(v -> {
            colorIndex = (colorIndex + 1) % colors.length;
            tvText.setTextColor(ContextCompat.getColor(this, colors[colorIndex]));
            prefs.edit().putInt("color", colorIndex).apply();
        });

        btnBg.setOnClickListener(v -> {
            handler.removeCallbacks(bgRunnable);
            bgIndex = (bgIndex + 1) % backgrounds.length;
            bgImage.setImageResource(backgrounds[bgIndex]);
            handler.postDelayed(bgRunnable, 3000);
        });

        switchNight.setOnCheckedChangeListener((btn, checked) -> {
            prefs.edit().putBoolean("night", checked).apply();
            AppCompatDelegate.setDefaultNightMode(checked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(bgRunnable, 3000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(bgRunnable);
    }
}