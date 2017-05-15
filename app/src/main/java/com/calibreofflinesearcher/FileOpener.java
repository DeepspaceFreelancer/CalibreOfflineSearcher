package com.calibreofflinesearcher;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FileOpener {

    private final Map<String, String> mimeTypeMap;

    public FileOpener() {

        this.mimeTypeMap = new HashMap<>();

        final MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        final String epubMimeType = mimeTypeMap.getMimeTypeFromExtension("epub");
        this.mimeTypeMap.put(".epub", epubMimeType);
        final String txtMimeType = mimeTypeMap.getMimeTypeFromExtension("txt");
        this.mimeTypeMap.put(".txt", txtMimeType);

    }

    public String openBook(final Context context, final String path) {

        final File directory = new File(path);
        if(directory.isDirectory()) {
            final File[] files = directory.listFiles();
            for (int i = 0; i < files.length; i++) {
                final String fileName = files[i].getName();
                String extension = fileName.substring(fileName.lastIndexOf("."));
                if (extension.toLowerCase().equals(".txt")) {

                    final Intent newIntent = new Intent(Intent.ACTION_VIEW);
                    newIntent.setDataAndType(Uri.fromFile(files[i].getAbsoluteFile()), mimeTypeMap.get(extension));
                    newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    try {
                        context.startActivity(newIntent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(context, "No handler for this type of file.", Toast.LENGTH_LONG).show();
                    }
                    return fileName;
                }
            }
        }
        return "BookNotFound";
    }
}
