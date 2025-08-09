package com.example.asistenciauda;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.StrictMode;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Scaner extends AppCompatActivity {

    Button btnScan;
    EditText txtResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scaner);

        btnScan = findViewById(R.id.btnScan);
        txtResultado = findViewById(R.id.txtResultado);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IntentIntegrator integrador = new IntentIntegrator(Scaner.this);
                integrador.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrador.setPrompt("Lector - CDP");
                integrador.setCameraId(0);
                integrador.setBeepEnabled(true);
                integrador.setBarcodeImageEnabled(true);
                integrador.initiateScan();
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result !=null){
            if (result.getContents()==null){
                Toast.makeText(this, "Lectora cancelada", Toast.LENGTH_LONG).show();
            } else {
                String noControl = result.getContents().substring(5,result.getContents().length());
                txtResultado.setText(noControl);

                // Llamar a la función para registrar la asistencia vía GET
                registrarAsistenciaRemota(noControl);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    private void registrarAsistenciaRemota(String noControl) {
        String baseUrl = "https://castalv.com/Clases/registrar_asistencia.php";
        try {
            // Codificar el número de control para la URL
            String encodedNoControl = URLEncoder.encode(noControl, "UTF-8");
            String urlString = baseUrl + "?NoControl=" + encodedNoControl;

            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            try {
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000); // Tiempo máximo de conexión (ms)
                connection.setReadTimeout(5000);    // Tiempo máximo de lectura (ms)

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // La petición fue exitosa
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    // Mostrar la respuesta del servidor
                    runOnUiThread(() -> Toast.makeText(this, "Asistencia registrada. Servidor responde: " + response.toString(), Toast.LENGTH_LONG).show());
                    runOnUiThread(() -> txtResultado.setText("")); // Limpiar el EditText
                } else {
                    // Hubo un error en la petición
                    runOnUiThread(() -> Toast.makeText(this, "Error al registrar asistencia. Código de respuesta: " + responseCode, Toast.LENGTH_LONG).show());
                }
            } finally {
                connection.disconnect();
            }

        } catch (IOException e) {
            runOnUiThread(() -> Toast.makeText(this, "Error de conexión: " + e.getMessage(), Toast.LENGTH_LONG).show());
            e.printStackTrace();
        }
    }
}