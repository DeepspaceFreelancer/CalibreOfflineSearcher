package com.calibreofflinesearcher.utils;

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
        final String mobiMimeType = mimeTypeMap.getMimeTypeFromExtension("mobi");
        this.mimeTypeMap.put(".mobi", mobiMimeType);
        final String pdfMimeType = mimeTypeMap.getMimeTypeFromExtension("pdf");
        this.mimeTypeMap.put(".pdf", pdfMimeType);
        final String txtMimeType = mimeTypeMap.getMimeTypeFromExtension("txt");
        this.mimeTypeMap.put(".txt", txtMimeType);

    }

    public String openBook(final Context context, final String path) {

        final File directory = new File(path);
        if(directory.isDirectory()) {
            final File[] files = directory.listFiles();
            for (int i = 0; i < files.length; i++) {
            	final File file = files[i];
                final String fileName = file.getName();
                String extension = fileName.substring(fileName.lastIndexOf("."));
                if (extension.toLowerCase().equals(".epub")) {                	
                    openFile(context, extension, file);
                    return fileName;
                }
                if (extension.toLowerCase().equals(".pdf")) {                	
                    openFile(context, extension, file);
                    return fileName;
                }
                if (extension.toLowerCase().equals(".mobi")) {                	
                    openFile(context, extension, file);
                    return fileName;
                }
            }
            return "DirectoryNotFound";
        }
        return "BookNotFound";
    }

	private void openFile(final Context context, String extension, final File file) {
		final Intent newIntent = new Intent(Intent.ACTION_VIEW);
		newIntent.setDataAndType(Uri.fromFile(file.getAbsoluteFile()), mimeTypeMap.get(extension));
		newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try {
		    context.startActivity(newIntent);
		} catch (ActivityNotFoundException e) {
		    Toast.makeText(context, "No handler for this type of file.", Toast.LENGTH_LONG).show();
		}
	}
}
