package com.appclima.appclimanavigation.presentation.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.appclima.appclimanavigation.R;
import com.appclima.appclimanavigation.utilities.Font_icons;

public class WelcomeActivity extends AppCompatActivity {

    // Importa la clase de iconos (como fuentes)
    Font_icons font_icons;

    // La pantalla de inicio tarda 4 segundos en cambiarse (pantalla de presentación)
    private static int WELCOME_SCREEN_TIME_OUT = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getSupportActionBar().hide();

        // Define new icon
        font_icons = new Font_icons();


        // Importa el icono del path definido en strings.xml (iconFontPath)
        // Para el texto llamado icon en la vista MainActivity.xml
        ((TextView) findViewById(R.id.icon)).setTypeface(font_icons.get_icons(
                getResources().getString(R.string.iconFontPath), this));

        // Pantalla de inicio durante 4 segundos, y luego lanza la pantalla principal:
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent welcomeIntent = new Intent (WelcomeActivity.this, MainActivity.class);
                startActivity(welcomeIntent);
                finish();
            }
        }, WELCOME_SCREEN_TIME_OUT);



    }
}
