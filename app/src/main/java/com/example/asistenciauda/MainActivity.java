package com.example.asistenciauda;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.health.connect.datatypes.units.Length;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import android.Manifest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private PieChart pieChart;
    private ImageButton user;
    private ImageButton libro;
    private ImageButton scaner;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio);

        //hola  //adios

        pieChart = findViewById(R.id.Michart);
        user = findViewById(R.id.usuario);
        libro = findViewById(R.id.libro);
        scaner = findViewById(R.id.scaner);

        scaner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirScanner();
            }
        });
        Timer timer = new Timer();

        TimerTask tarea = new TimerTask() {
            @Override
            public void run() {
                obtener_asistencia();
            }
        };

        // Ejecutar después de 2 segundos (2000 ms) y repetir cada 5 segundos (5000 ms)
        timer.schedule(tarea, 30000);




        user.setOnClickListener(v -> abrirRegistro());

        libro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirLibro();
            }
        });
        if (!arePermissionsGranted()) {
            requestPermissions();
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

    }

    private boolean isDarkModeActive() {
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void obtener_asistencia() {
        // Crear una cola de solicitudes
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        LocalDate fecha = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String fechaFormateada = fecha.format(dateFormatter);
        String Hora = "08:00";

        // Crear la solicitud GET
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://castalv.com/Clases/consultar_asistencias_por_hora.php?FechaHoraReferencia=" + fechaFormateada + " " + Hora,
                response -> {
                    try {
                        // Convertir la respuesta en un objeto JSON
                        JSONObject jsonResponse = new JSONObject(response);

                        // Verificar si la solicitud fue exitosa
                        if (jsonResponse.getBoolean("success")) {
                            // Obtener el objeto "resultados" en lugar de un array
                            JSONObject jsonObject = jsonResponse.getJSONObject("resumen");

                            // Extraer los datos
                            int Entraron_Temprano = jsonObject.getInt("Asistencia");
                            int Entraron_Tarde = jsonObject.getInt("Tarde");
                            int No_Han_Llegado = jsonObject.getInt("Faltantes");

                            llenarGrafica(Entraron_Temprano, Entraron_Tarde, No_Han_Llegado);

                            // Mostrar en log
                            Log.d("Resultado", "Entraron Temprano: " + Entraron_Temprano);
                            Log.d("Resultado", "Entraron Tarde: " + Entraron_Tarde);
                            Log.d("Resultado", "No Han Llegado: " + No_Han_Llegado);
                        } else {
                            Log.e("Resultado", "La respuesta indica un fallo.");
                        }
                    } catch (JSONException e) {
                        Log.e("Resultado", "Error al procesar JSON: " + e.getMessage());
                    }
                },
                error -> Log.e("Resultado", "Error en la solicitud: " + (error.getMessage() != null ? error.getMessage() : "Desconocido")));

        // Agregar la solicitud a la cola
        requestQueue.add(stringRequest);
    }

    private void llenarGrafica(int temprano, int tarde, int noLlegado) {
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(temprano, "Temprano"));
        entries.add(new PieEntry(tarde, "Tarde"));
        entries.add(new PieEntry(noLlegado, "No llegaron"));

        PieDataSet dataSet = new PieDataSet(entries, "Asistencia");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        int textColor = isDarkModeActive() ? R.color.textColorDark : R.color.textColorLight;
        dataSet.setValueTextColor(ContextCompat.getColor(this, textColor));
        Legend legend = pieChart.getLegend();
        legend.setTextColor(ContextCompat.getColor(this, textColor));
        pieChart.setCenterTextColor(ContextCompat.getColor(this, textColor));

        PieData data = new PieData(dataSet);
        pieChart.setData(data);


        pieChart.animateY(1400);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Asistencia");
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setHoleRadius(58f);
        pieChart.setTransparentCircleRadius(61f);
        pieChart.invalidate();

    }


    private void abrirRegistro() {
        Intent open = new Intent(this, listausuarios.class);
        startActivity(open);
    }

    private void abrirLibro() {
        Intent open = new Intent(this, grafica.class);
        startActivity(open);
    }

    private void abrirScanner() {
        Intent open = new Intent(this, Scaner.class);
        startActivity(open);
    }

    private boolean arePermissionsGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.INTERNET}, 100);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    // Inform the user that permission was denied
                }
            }
        }
    }
}
