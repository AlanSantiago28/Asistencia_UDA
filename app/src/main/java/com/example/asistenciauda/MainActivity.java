package com.example.asistenciauda;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.health.connect.datatypes.units.Length;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private PieChart pieChart;
    private ImageButton user;
    private  ImageButton  libro;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio);

        pieChart = findViewById(R.id.Michart);
        user = findViewById(R.id.usuario);
        libro = findViewById(R.id.libro);

        obtener_asistencia();



        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirRegistro();
            }
        });

        libro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {abrirLibro();}
        });
    }
    private boolean isDarkModeActive(){
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
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://asmit.com.mx/uda_wbs/consultar_asistencias_por_hora.php?FechaHoraReferencia=" + fechaFormateada + " " + Hora,
                response -> {
                    try {
                        // Convertir la respuesta en un objeto JSON
                        JSONObject jsonResponse = new JSONObject(response);

                        // Verificar si la solicitud fue exitosa
                        if (jsonResponse.getBoolean("success")) {
                            // Obtener el objeto "resultados" en lugar de un array
                            JSONObject jsonObject = jsonResponse.getJSONObject("resultados");

                            // Extraer los datos
                            int Entraron_Temprano = jsonObject.getInt("Entraron Temprano");
                            int Entraron_Tarde = jsonObject.getInt("Entraron Tarde");
                            int No_Han_Llegado = jsonObject.getInt("No Han Llegado");

                            llenarGrafica( Entraron_Temprano, Entraron_Tarde, No_Han_Llegado);

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

    private void llenarGrafica( int temprano, int tarde, int noLlegado){
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(temprano, "Temprano"));
        entries.add(new PieEntry(tarde, "Tarde"));
        entries.add(new PieEntry(noLlegado, "No llegaron"));

        PieDataSet dataSet = new PieDataSet(entries, "Asistencia");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

       int textColor = isDarkModeActive() ? R.color.textColorDark : R.color.textColorLight;
       dataSet.setValueTextColor(ContextCompat.getColor(this, textColor));
        Legend legend =pieChart.getLegend();
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



    private void abrirRegistro(){
        Intent open = new Intent(this, listausuarios.class);
        startActivity(open);
    }

    private void abrirLibro(){
        Intent open = new Intent(this, grafica.class);
        startActivity(open);
    }
}
