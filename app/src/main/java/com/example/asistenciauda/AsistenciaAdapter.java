package com.example.asistenciauda;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class AsistenciaAdapter extends RecyclerView.Adapter<AsistenciaAdapter.AsistenciaViewHolder> {

    private Context context;
    private ArrayList<modeloAsistencias> listaAsistencias;

    public AsistenciaAdapter(Context context, ArrayList<modeloAsistencias> listaAsistencias) {
        this.context = context;
        this.listaAsistencias = listaAsistencias;
    }

    @NonNull
    @Override
    public AsistenciaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.listaalumnos, parent, false);
        return new AsistenciaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AsistenciaViewHolder holder, int position) {
        modeloAsistencias asistencia = listaAsistencias.get(position);
        holder.txtNombre.setText(asistencia.getNombre());
        holder.txtApellido.setText(asistencia.getApellido());
        holder.txtNoControl.setText(asistencia.getNoControl());

        holder.btnPDF.setOnClickListener(v -> {
            new AlertDialog.Builder(holder.itemView.getContext())
                    .setTitle("Crear PDF")
                    .setMessage("¿Deseas generar el PDF con los datos de esta asistencia?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        generarPDF(holder.itemView.getContext(), asistencia);
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return listaAsistencias.size();
    }

    public static class AsistenciaViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtApellido, txtNoControl;
        Button btnPDF;

        public AsistenciaViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.name);
            txtApellido = itemView.findViewById(R.id.Apellido);
            txtNoControl = itemView.findViewById(R.id.Ncontrol);
            btnPDF = itemView.findViewById(R.id.Crear);
        }
    }



    private void generarPDF(Context context, modeloAsistencias asistencia) {
        try {
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            if (!dir.exists()) dir.mkdirs();
            File file = new File(dir, "asistencia_" + asistencia.getNoControl() + ".pdf");

            PdfWriter writer = new PdfWriter(new FileOutputStream(file));
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("Asistencia de Alumno").setBold().setFontSize(18).setTextAlignment(TextAlignment.CENTER));

            Table table = new Table(3);
            table.addCell(new Cell().add(new Paragraph("ID")).setBackgroundColor(new DeviceRgb(76, 175, 80)).setTextAlignment(TextAlignment.CENTER));
            table.addCell(new Cell().add(new Paragraph("NoControl")).setBackgroundColor(new DeviceRgb(76, 175, 80)).setTextAlignment(TextAlignment.CENTER));
            table.addCell(new Cell().add(new Paragraph("Fecha Registro")).setBackgroundColor(new DeviceRgb(76, 175, 80)).setTextAlignment(TextAlignment.CENTER));

            table.addCell(new Cell().add(new Paragraph(asistencia.getID())).setTextAlignment(TextAlignment.CENTER));
            table.addCell(new Cell().add(new Paragraph(asistencia.getNoControl())).setTextAlignment(TextAlignment.CENTER));
            table.addCell(new Cell().add(new Paragraph(asistencia.getFechaRegistro())).setTextAlignment(TextAlignment.CENTER));

            document.add(table);
            document.close();

            Log.d("PDF", "PDF creado correctamente: " + file.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
