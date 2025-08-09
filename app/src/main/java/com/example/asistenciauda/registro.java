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
    private AutoCompleteTextView carrera ;
    private TextInputLayout tilNumeroControl, tilApaterno, tilAmaterno, tilNombre, tilSexo, fe, tilNumero, tilEmergencia, tilCarrera, tilTurno, tilObservaciones;
    private TextInputEditText NumeroControl, Apaterno, Amaterno, nombre, sexo, Fecha, numero, Emergencia, turno, observaciones;
    private Button crear;
    private String id = "0";
    final Calendar myCalendar = Calendar.getInstance();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.x);
        id = getIntent().getExtras().getString("id");// Asegúrate de que "layout" es el nombre correcto de tu XML

        // Inicializar las vistas usando los IDs del XML que proporcionaste
        imagen = findViewById(R.id.imagen);
        textView2 = findViewById(R.id.textView2);

        NumeroControl = findViewById(R.id.N2);
        Apaterno = findViewById(R.id.Apaterno);
        Amaterno = findViewById(R.id.Amaterno);
        nombre = findViewById(R.id.nom);
        sexo = findViewById(R.id.sex);
        numero = findViewById(R.id.num);
        Emergencia = findViewById(R.id.em);
        turno = findViewById(R.id.tur);
        observaciones = findViewById(R.id.ob);

        // Inicializar los TextInputLayout
        tilNumeroControl = findViewById(R.id.NC);
        tilApaterno = findViewById(R.id.AP);
        tilAmaterno = findViewById(R.id.AM);
        tilNombre = findViewById(R.id.Nombre);
        tilSexo = findViewById(R.id.Sexo);
        Fecha= findViewById(R.id.fe);
        fe = findViewById(R.id.Fecha);
        Fecha.setOnClickListener(new View.OnClickListener() {
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
                        Fecha.setText(dateFormat.format(myCalendar.getTime()));
                    }
                }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        tilNumero = findViewById(R.id.Tlfn);
        tilEmergencia = findViewById(R.id.NEmergencia);
        tilCarrera = findViewById(R.id.car);
        tilTurno = findViewById(R.id.turno);
        tilObservaciones = findViewById(R.id.Obs);
        carrera= findViewById(R.id.mas);

        String[] carreras = new String[]{
          "Lic. en Diseño Grafico",
          "Lic. en Derecho",
          "Lic. en Mercadotecnia",
          "Lic. en Comercio y Negocios Internacionales",
          "Lic. en Administracion de Empresas",
          "Lic. en Contador Publico",
          "Lic. en Psicologia Organizacional",
          "Lic. en Ciencias de la Comunicacion",
          "Ing. Electronica e instrumentacion",
                "Ing. Industrial",
                "Ing. en Sistemas"
        };
        ArrayAdapter<String> adapter =new ArrayAdapter<>(
          registro.this,
          R.layout.mas,
          carreras
        );
        carrera.setAdapter(adapter);

        crear = findViewById(R.id.Crear);

        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarEstudiante();
            }
        });

        if (getIntent().hasExtra("id")) {
            id = getIntent().getStringExtra("id");
            if (Integer.parseInt(id) > 0) {
                obtener_estudiantes("https://castalv.com/Clases/obtener_estudiante.php?ID=" + id);
            }
        }
    }

    private void guardarEstudiante() {
        String url = "https://castalv.com/Clases/guardar_estudiante.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                String message = jsonResponse.getString("message");
                Log.d("Resultado", "Respuesta del servidor: " + message);
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                listausuarios.obtener_estudiantes("https://castalv.com/Clases/obtener_estudiantes.php?Turno=0&Campus=0&Carrera=0");
                finish();
                // Puedes agregar aquí lógica para limpiar los campos después de guardar
            } catch (JSONException e) {
                Log.e("Resultado", "Error al procesar JSON: " + e.getMessage());
                Toast.makeText(this, "Ocurrio un error", Toast.LENGTH_SHORT).show();
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
                params.put("FechaNacimiento", Fecha.getText().toString());
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        if (jsonResponse.getBoolean("success")) {
                            JSONArray dataArray = jsonResponse.getJSONArray("data");
                            for (int i =0; i<dataArray.length(); i++){
                                JSONObject estudiante = dataArray.getJSONObject(i);
                            /*if (dataArray.length() > 0) {
                                JSONObject estudiante = dataArray.getJSONObject(0); */   //Suponiendo que solo se espera un estudiante

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
                                this.Fecha.setText(fecha);
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

        requestQueue.add(stringRequest);
    }

}