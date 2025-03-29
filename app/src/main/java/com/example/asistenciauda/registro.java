package com.example.asistenciauda;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class registro extends AppCompatActivity {

    String[] items = {"Masculino", "Femenino"};

    AutoCompleteTextView autoCompleteTextView;

    ArrayAdapter<String> adapterItems;

    private EditText NumeroControl, Apaterno, Amaterno, nombre, sexo, fecha, numero, Emergencia, carrera, turno, observaciones;

    final Calendar myCalendar = Calendar.getInstance();
    private Button crear;
    private String id="0";

    @SuppressLint("MissingInflatedId")
    @Override
    //@SuppressLint()
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
        id = getIntent().getExtras().getString("id");

        NumeroControl = findViewById(R.id.control);
        Apaterno = findViewById(R.id.AP);
        Amaterno = findViewById(R.id.AM);
        nombre = findViewById(R.id.Nombre);
        sexo = findViewById(R.id.Sexo);
        fecha = findViewById(R.id.Fecha);
        fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(registro.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, month);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        String myFormat = "dd,MMM,yyyy";
                        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
                        fecha.setText(dateFormat.format(myCalendar.getTime()));
                    }
                }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        numero = findViewById(R.id.Telefono);
        Emergencia = findViewById(R.id.emergencia);
        carrera = findViewById(R.id.Carrera);
        turno = findViewById(R.id.Turno);
        observaciones = findViewById(R.id.Observaciones);
        crear = findViewById(R.id.Crear);


        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                guardarEstudiante();

            }
        });

        if(Integer.parseInt(id)>0){
            obtener_estudiantes("https://asmit.com.mx/uda_wbs/obtener_estudiante.php?ID="+id);
        }
    }

    private void guardarEstudiante() {
        String url = "https://asmit.com.mx/uda_wbs/guardar_estudiante.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                String message = jsonResponse.getString("message");
                Log.d("Resultado", "Respuesta del servidor: " + message);
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                Log.e("Resultado", "Error al procesar JSON: " + e.getMessage());
            }
        }, error -> {
            Log.e("Resultado", "Error en la solicitud: " + (error.getMessage() != null ? error.getMessage() : "Desconocido"));
            Toast.makeText(this, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("ID", id);
                params.put("NoControl", NumeroControl.getText().toString());
                params.put("ApellidoPaterno", Apaterno.getText().toString());
                params.put("ApellidoMaterno", Amaterno.getText().toString());
                params.put("Nombre", nombre.getText().toString());
                params.put("Sexo", sexo.getText().toString());
                params.put("FechaNacimiento", fecha.getText().toString());
                params.put("Telefono", numero.getText().toString());
                params.put("TelefonoEmergencia", Emergencia.getText().toString());
                params.put("Carrera", carrera.getText().toString());
                params.put("Turno", turno.getText().toString());
                params.put("Campus", "3");
                params.put("Observaciones", observaciones.getText().toString());
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void obtener_estudiantes(String url) {
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

                                this.nombre.setText(nombre);
                                Apaterno.setText(apellidoPaterno);
                                Amaterno.setText(apellidoMaterno);
                                this.NumeroControl.setText(NumeroControl);
                                this.fecha.setText(fecha);
                                this.Emergencia.setText(Emergencia);
                                this.turno.setText(turno);
                                this.carrera.setText(carrera);
                                this.numero.setText(numero);
                                observaciones.setText(Observaciones);
                            }
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
}