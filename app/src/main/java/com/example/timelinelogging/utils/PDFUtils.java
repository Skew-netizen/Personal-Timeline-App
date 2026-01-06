package com.example.timelinelogging.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import com.example.timelinelogging.data.entity.Post;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class PDFUtils {

    public static void exportPostsToPDF(Context context, List<Post> posts, String fileName) {
        String pdfFileName = fileName + ".pdf";

        try {
            OutputStream outputStream;
            String toastMessage;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContentResolver resolver = context.getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, pdfFileName);
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

                Uri uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues);
                if (uri == null) {
                    Toast.makeText(context, "Error creating PDF: Failed to get URI", Toast.LENGTH_SHORT).show();
                    return;
                }
                outputStream = resolver.openOutputStream(uri);
                toastMessage = "PDF saved to Downloads folder.";

            } else {
                // For older versions, save to the app's private documents directory.
                File dir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
                if (dir == null) {
                    Toast.makeText(context, "Error creating PDF: Storage directory not available", Toast.LENGTH_SHORT).show();
                    return;
                }
                File file = new File(dir, pdfFileName);
                outputStream = new FileOutputStream(file);
                toastMessage = "PDF saved: " + file.getAbsolutePath();
            }

            if (outputStream == null) {
                Toast.makeText(context, "Error creating PDF: Output stream is null", Toast.LENGTH_SHORT).show();
                return;
            }

            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            for (Post post : posts) {
                document.add(new Paragraph("Time: " + post.getTime()));
                document.add(new Paragraph("Tag: " + post.getTag()));
                document.add(new Paragraph("Content: " + post.getContent()));
                document.add(new Paragraph("--------------------------------------------------"));
            }

            document.close();
            Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error creating PDF", Toast.LENGTH_SHORT).show();
        }
    }
}
