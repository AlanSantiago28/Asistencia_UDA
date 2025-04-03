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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class registro extends AppCompatActivity {

    private ImageView imagen;
    private TextView textView2;
    private AutoCompleteTextView etCarrera ;
    private TextInputLayout tilNumeroControl, tilApaterno, tilAmaterno, tilNombre, tilSexo, fecha, tilNumero, tilEmergencia, tilCarrera, tilTurno, tilObservaciones;
    private TextInputEditText etNumeroControl, etApaterno, etAmaterno, etNombre, etSexo, Fe, etNumero, etEmergencia, etTurno, etObservaciones;
    private Button crear;
    private String id = "0";
    final Calendar myCalendar = Calendar.getInstance();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.x); // Asegúrate de que "layout" es el nombre correcto de tu XML

        // Inicializar las vistas usando los IDs del XML que proporcionaste
        imagen = findViewById(R.id.imagen);
        textView2 = findViewById(R.id.textView2);

        // Inicializar los TextInputLayout
        tilNumeroControl = findViewById(R.id.NC);
        tilApaterno = findViewById(R.id.AP);
        tilAmaterno = findViewById(R.id.AM);
        tilNombre = findViewById(R.id.Nombre);
        tilSexo = findViewById(R.id.Sexo);
        Fe= findViewById(R.id.fe);
        fecha = findViewById(R.id.Fecha);
        Fe.setOnClickListener(new View.OnClickListener() {
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
                        Fe.setText(dateFormat.format(myCalendar.getTime()));
                    }
                }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        tilNumero = findViewById(R.id.Tlfn);
        tilEmergencia = findViewById(R.id.NEmergencia);
        tilCarrera = findViewById(R.id.car);
        tilTurno = findViewById(R.id.turno);
        tilObservaciones = findViewById(R.id.Obs);
        etCarrera= findViewById(R.id.mas);

        String[] carreras = new String[]{
          "Licenciado en Diseño Grafico",
          "Licenciado en Derecho",
          "Licenciado en Mercadotecnia",
          "Licenciado en Comercio y Negocios Internacionales",
          "Licenciado en Administracion de Empresas",
          "Licenciado en Contador Publico",
          "Licenciado en Psicologia Organizacional",
          "Licenciado en Ciencias de la Comunicacion",
          "Ingenieria Electronica e instrumentacion",
                "Ingenieria Industrial",
                "Ingenieria en Sistemas"
        };
        ArrayAdapter<String> adapter =new ArrayAdapter<>(
          registro.this,
          R.layout.mas,
          carreras
        );
        etCarrera.setAdapter(adapter);


        crear = findViewById(R.id.Crear);

        // Configurar el DatePickerDialog para el campo de fecha

        // Configurar el listener para el botón Crear
        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarEstudiante();
            }
        });

        // Si se pasa un ID, obtener los datos del estudiante (esto ya estaba en tu código)
        // Asegúrate de que la Activity que llama a esta le pase el ID correcto
        if (getIntent().hasExtra("id")) {
            id = getIntent().getStringExtra("id");
            if (Integer.parseInt(id) > 0) {
                obtener_estudiantes("https://asmit.com.mx/uda_wbs/obtener_estudiante.php?ID=" + id);
            }
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
                // Puedes agregar aquí lógica para limpiar los campos después de guardar
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
                params.put("NoControl", etNumeroControl.getText().toString());
                params.put("ApellidoPaterno", etApaterno.getText().toString());
                params.put("ApellidoMaterno", etAmaterno.getText().toString());
                params.put("Nombre", etNombre.getText().toString());
                params.put("Sexo", etSexo.getText().toString());
                params.put("FechaNacimiento", Fe.getText().toString());
                params.put("Telefono", etNumero.getText().toString());
                params.put("TelefonoEmergencia", etEmergencia.getText().toString());
                params.put("Carrera", etCarrera.getText().toString());
                params.put("Turno", etTurno.getText().toString());
                params.put("Campus", "3");
                params.put("Observaciones", etObservaciones.getText().toString());
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void obtener_estudiantes(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        if (jsonResponse.getBoolean("success")) {
                            JSONArray dataArray = jsonResponse.getJSONArray("data");
                            if (dataArray.length() > 0) {
                                JSONObject estudiante = dataArray.getJSONObject(0); // Suponiendo que solo se espera un estudiante

                                etNumeroControl.setText(estudiante.getString("NoControl"));
                                etApaterno.setText(estudiante.getString("ApellidoPaterno"));
                                etAmaterno.setText(estudiante.getString("ApellidoMaterno"));
                                etNombre.setText(estudiante.getString("Nombre"));
                                Fe.setText(estudiante.getString("FechaNacimiento"));
                                etNumero.setText(estudiante.getString("Telefono"));
                                etEmergencia.setText(estudiante.getString("TelefonoEmergencia"));
                                etCarrera.setText(estudiante.getString("Carrera"));
                                etTurno.setText(estudiante.getString("Turno"));
                                etObservaciones.setText(estudiante.getString("Observaciones"));
                            }
                        } else {
                            Log.e("Resultado", "La respuesta indica un fallo.");
                        }
                    } catch (JSONException e) {
                        Log.e("Resultado", "Error al procesar JSON: " + e.getMessage());
                    }
                },
                error -> Log.e("Resultado", "Error en la solicitud: " + (error.getMessage() != null ? error.getMessage() : "Desconocido")));

        requestQueue.add(stringRequest);
    }
}