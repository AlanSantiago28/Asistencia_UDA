package com.example.asistenciauda;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class listausuarios extends AppCompatActivity {

    private Button crear;
    private  ItemAdapter itemAdapter;

    private final ArrayList<usuario> obtener_estudiantes = new ArrayList<>();
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usuarios);

        crear = findViewById(R.id.Crear);
        recyclerView = findViewById(R.id.R);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        itemAdapter = new ItemAdapter(obtener_estudiantes);

        itemAdapter.notifyDataSetChanged();

        recyclerView.setAdapter(itemAdapter);

        crear.setOnClickListener(v -> {
          Intent intent = new Intent(this,registro.class);
            intent.putExtra("id","0");
          startActivity(intent);
        });

        obtener_estudiantes("https://asmit.com.mx/uda_wbs/obtener_estudiantes.php?Turno=0&Campus=0&Carrera=0");

    }

    private void obtener_estudiantes(String url) {
        obtener_estudiantes.clear();
        // Crear una cola de solicitudes
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Crear la solicitud GET
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        // Convertir la respuesta en un objeto JSON
                        JSONObject jsonResponse = new JSONObject(response);

                        // Verificar si la solicitud fue exitosa
                        if (jsonResponse.getBoolean("success")) {
                            // Obtener el array "data"
                            JSONArray dataArray = jsonResponse.getJSONArray("data");

                            // Recorrer el array de estudiantes
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject estudiante = dataArray.getJSONObject(i);

                                // Extraer los datos
                                String id = estudiante.getString("ID");
                                String NumeroControl = estudiante.getString("NoControl");
                                String apellidoPaterno = estudiante.getString("ApellidoPaterno");
                                String apellidoMaterno = estudiante.getString("ApellidoMaterno");
                                String nombre = estudiante.getString("Nombre");
                                String fecha = estudiante.getString("FechaNacimiento");
                                String numero = estudiante.getString("Telefono");
                                String Emergencia = estudiante.getString("TelefonoEmergencia");
                                String carrera = estudiante.getString("Carrera");
                                String turno = estudiante.getString("Turno");
                                String campus = estudiante.getString("Campus");
                                String Observaciones = estudiante.getString("Observaciones");

                                obtener_estudiantes.add(new usuario(
                                        id,NumeroControl,apellidoPaterno,apellidoMaterno,nombre,"Hombre",
                                        fecha,numero,Emergencia,carrera,turno,campus,Observaciones
                                ));

                            }
                        } else {
                            Log.e("Resultado", "La respuesta indica un fallo.");
                        }
                    } catch (JSONException e) {
                        Log.e("Resultado", "Error al procesar JSON: " + e.getMessage());
                    }
                    itemAdapter.notifyDataSetChanged();
                },
                error -> Log.e("Resultado", "Error en la solicitud: " + (error.getMessage() != null ? error.getMessage() : "Desconocido")));

        // Agregar la solicitud a la cola
        requestQueue.add(stringRequest);
    }
}