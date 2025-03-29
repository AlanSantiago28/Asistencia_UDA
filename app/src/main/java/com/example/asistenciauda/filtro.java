package com.example.asistenciauda;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class filtro extends AppCompatActivity {
    private Button carrera, campus, turno ;

    @Override protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.filtro);
    }
}

