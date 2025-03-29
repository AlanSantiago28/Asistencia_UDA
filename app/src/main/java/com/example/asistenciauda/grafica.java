package com.example.asistenciauda;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class grafica extends AppCompatActivity {

private PieChart pieChart;



    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.grafica);

        pieChart = findViewById(R.id.chart);

        if (pieChart == null) {
            android.util.Log.e("Grafica", "PieChart es nulo después de findViewById");
        } else {
            android.util.Log.d("Grafica", "PieChart encontrado correctamente");
            llenarGrafica();
        }
    }

    private void llenarGrafica(){
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(30f, "Dato1"));
        entries.add(new PieEntry(50f, "Dato2"));
        entries.add(new PieEntry(20f, "dato3"));

        PieDataSet dataSet = new PieDataSet(entries, "Datos");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData data = new PieData(dataSet);

        if (data != null && data.getDataSets().size() > 0) {
            android.util.Log.d("Grafica", "Datos creados correctamente. Número de entradas: " + entries.size());
            pieChart.setData(data);
            pieChart.animateY(1400, Easing.EaseInOutQuad);
            pieChart.getDescription().setEnabled(false);
            pieChart.setCenterText("datos");
            pieChart.setDrawHoleEnabled(true);
            pieChart.setHoleColor(Color.WHITE);
            pieChart.setHoleRadius(58f);
            pieChart.setTransparentCircleRadius(61f);
            pieChart.invalidate();
        } else {
            android.util.Log.e("Grafica", "No se pudieron crear los datos para el gráfico.");
        }
    }
}
