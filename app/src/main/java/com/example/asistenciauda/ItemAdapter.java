package com.example.asistenciauda;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {


    private ArrayList<usuario> itemList = new ArrayList<>();

    public ItemAdapter(ArrayList<usuario> itemList) {

        this.itemList = itemList;
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listaalumnos, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        usuario currentUser = itemList.get(position);

        holder.Name.setText(itemList.get(position).getNombre());
        holder.Apellido.setText(itemList.get(position).getApellidoPaterno());
        holder.Ncontrol.setText(itemList.get(position).getNoControl());

        holder.usuarios.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), registro.class);
            intent.putExtra("id", itemList.get(position).getID());
            holder.itemView.getContext().startActivity(intent);
        });


        holder.usuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), registro.class);
                intent.putExtra("id", itemList.get(position).getID());
                holder.itemView.getContext().startActivity(intent);
            }
        });

        holder.Crear.setOnClickListener(v -> {
            new AlertDialog.Builder(holder.itemView.getContext())
                    .setTitle("Crear PDF")
                    .setMessage("¿Deseas generar el PDF con los datos de este alumno?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        obtener_asistencia(currentUser, holder.itemView.getContext());
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private final Button usuarios;
        private Button eliminar;
        private final Button Crear;

        public TextView Name;
        public TextView Apellido, Ncontrol;
        public ImageView imageView;

        @SuppressLint("WrongViewCast")
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            Name = itemView.findViewById(R.id.name);
            Apellido = itemView.findViewById(R.id.Apellido);
            Ncontrol = itemView.findViewById(R.id.Ncontrol);
            imageView = itemView.findViewById(R.id.icon);
            usuarios = itemView.findViewById(R.id.editar);
            Crear = itemView.findViewById(R.id.PDF);


        }


    }


    public void generarPDF(Context context, ArrayList<modeloAsistencias> modeloAsistencias) {
        try {
            // Configurar el archivo en Downloads con MediaStore
            ContentValues values = new ContentValues();
            values.put(MediaStore.Downloads.DISPLAY_NAME, "asistencia.pdf");
            values.put(MediaStore.Downloads.MIME_TYPE, "application/pdf");
            values.put(MediaStore.Downloads.IS_PENDING, 1); // archivo en proceso

            ContentResolver resolver = context.getContentResolver();
            Uri collection = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                collection = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
            }
            Uri fileUri = resolver.insert(collection, values);

            if (fileUri == null) {
                Log.e("PDF", "No se pudo crear el archivo en MediaStore");
                return;
            }

            OutputStream outputStream = resolver.openOutputStream(fileUri);

            // Crear el PdfWriter usando el OutputStream
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Agregar título
            document.add(new Paragraph("Lista de Asistencias")
                    .setFontSize(16)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER));

            // Crear tabla
            Table table = new Table(3);

            // Encabezados
            String[] headers = {"ID", "NoControl", "Fecha Registro"};
            for (String header : headers) {
                Cell cell = new Cell().add(new Paragraph(header))
                        .setBackgroundColor(new DeviceRgb(76, 175, 80))
                        .setTextAlignment(TextAlignment.CENTER)
                        .setBold()
                        .setFontSize(12);
                table.addCell(cell);
            }

            // Filas con datos
            for (modeloAsistencias asistencia : modeloAsistencias) {
                table.addCell(new Cell().add(new Paragraph(asistencia.getID())).setTextAlignment(TextAlignment.CENTER));
                table.addCell(new Cell().add(new Paragraph(asistencia.getNoControl())).setTextAlignment(TextAlignment.CENTER));
                table.addCell(new Cell().add(new Paragraph(asistencia.getFechaRegistro())).setTextAlignment(TextAlignment.CENTER));
            }

            document.add(table);
            document.close();

            // Marcar como listo
            values.clear();
            values.put(MediaStore.Downloads.IS_PENDING, 0);
            resolver.update(fileUri, values, null, null);

            Log.d("PDF", "PDF creado correctamente: " + fileUri.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void obtener_asistencia(usuario usuarioPDF, Context contexto) {
        ArrayList<modeloAsistencias> modeloAsistencias = new ArrayList();
        RequestQueue requestQueue = Volley.newRequestQueue(contexto);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://castalv.com/Clases/consultar_asistencias.php",
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);

                        if (jsonResponse.getBoolean("succes")) {
                            JSONArray asistenciaArray = jsonResponse.getJSONArray("asistencia");

                            for (int i = 0; i < asistenciaArray.length(); i++) {
                                JSONObject asistenciaObj = asistenciaArray.getJSONObject(i);

                                modeloAsistencias.add(new modeloAsistencias(
                                        asistenciaObj.getString("ID"),
                                        asistenciaObj.getString("NoControl"),
                                        asistenciaObj.getString("FechaRegistro"),
                                        asistenciaObj.getString("nombre"),
                                        asistenciaObj.getString("Apellido")

                                ));
                            }

                            for (modeloAsistencias asistencia : modeloAsistencias) {
                                Log.e("Asistencia", asistencia.getID() + "_" +
                                        asistencia.getNoControl() + "_" +
                                        asistencia.getFechaRegistro());
                            }

                            generarPDF(contexto,modeloAsistencias);

                        } else {
                            Log.e("resultado", "La respuesta indica un fallo.");
                        }
                    } catch (JSONException e) {
                        Log.e("Resultado", "Error al proceso JSON" + e.getMessage());
                    }

                }, error -> Log.e("Resultado", "Error en la solicitud" + (error.getMessage() != null ? error.getMessage() : "Desconocido"))) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("NoControl", usuarioPDF.getNoControl());

                return params;
            }

        };


        requestQueue.add(stringRequest);











    }


}
