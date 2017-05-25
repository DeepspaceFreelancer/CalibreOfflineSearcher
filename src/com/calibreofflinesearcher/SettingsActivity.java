package com.calibreofflinesearcher;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.net.URISyntaxException;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        refreshText();
    }

    private void refreshText() {
        EditText text = (EditText)findViewById(R.id.editText);
        text.setText(Settings.getInstance().getLibraryLocation());
    }

    private static final int FILE_SELECT_CODE = 0;
    private static final String TAG = "SettingsActivity";

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Adatbazis helye"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }

//        Intent intent = new Intent()
//                .setType("*/*")
//                .setAction(Intent.ACTION_GET_CONTENT);
//
//        startActivityForResult(Intent.createChooser(intent, "Select a file"), 123);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    Log.d(TAG, "File Uri: " + uri.toString());
                    // Get the path
                    String path = null;
                    try {
                        path = SettingsActivity.getPath(this, uri);
                        setPath(path);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Cannot retrieve path...", Toast.LENGTH_LONG).show();
                    }
                    Log.d(TAG, "File Path: " + path);
                    // Get the file instance
                    // File file = new File(path);
                    // Initiate the upload
                }
                break;
//            case 123:
//                if(resultCode == RESULT_OK){
//                    setPath(getPath(this, data.getData()));
//                }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @author paulburke
     */
//    public static String getPath(final Context context, final Uri uri) {
//
////        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
////
////        // DocumentProvider
////        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
////            // ExternalStorageProvider
////            if (isExternalStorageDocument(uri)) {
////                final String docId = DocumentsContract.getDocumentId(uri);
////                final String[] split = docId.split(":");
////                final String type = split[0];
////
////                if ("primary".equalsIgnoreCase(type)) {
////                    return Environment.getExternalStorageDirectory() + "/" + split[1];
////                }
////                else
////                {
////                    return Environment.getExternalStoragePublicDirectory(type).getAbsolutePath() + "/" + split[1];
////                }
////
////                // TODO handle non-primary volumes
////            }
////            // DownloadsProvider
////            else if (isDownloadsDocument(uri)) {
////
////                final String id = DocumentsContract.getDocumentId(uri);
////                final Uri contentUri = ContentUris.withAppendedId(
////                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
////
////                return getDataColumn(context, contentUri, null, null);
////            }
////            // MediaProvider
////            else if (isMediaDocument(uri)) {
////                final String docId = DocumentsContract.getDocumentId(uri);
////                final String[] split = docId.split(":");
////                final String type = split[0];
////
////                Uri contentUri = null;
////                if ("image".equals(type)) {
////                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
////                } else if ("video".equals(type)) {
////                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
////                } else if ("audio".equals(type)) {
////                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
////                }
////
////                final String selection = "_id=?";
////                final String[] selectionArgs = new String[] {
////                        split[1]
////                };
////
////                return getDataColumn(context, contentUri, selection, selectionArgs);
////            }
////        } else
//        // MediaStore (and general)
//        
//        if ("content".equalsIgnoreCase(uri.getScheme())) {
//            return getDataColumn(context, uri, null, null);
//        }
//        // File
//        else if ("file".equalsIgnoreCase(uri.getScheme())) {
//            return uri.getPath();
//        }
//
//        return null;
//    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
                e.printStackTrace();
                Toast.makeText(context, "Hmm something went wrong....", Toast.LENGTH_SHORT).show();
            }
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public void sendBackButton(View view) {
        finish();
    }

    public void searchFile(View view) {
        showFileChooser();
    }

    public void setPath(String path) {
        Settings.getInstance().setLibraryLocation(path);
        refreshText();
    }
}
