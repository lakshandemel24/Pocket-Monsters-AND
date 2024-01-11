package com.example.pocketmonsters.ui.profile;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;

public class FileUtils {

    public static long getImageSizeInKB(Context context, Uri uri) {
        long sizeInBytes = 0;

        // Check if the Uri is a content Uri
        if ("content".equals(uri.getScheme())) {
            String[] projection = {MediaStore.Images.Media.SIZE};
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE);
                    sizeInBytes = cursor.getLong(columnIndex);
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        // If the Uri is a file Uri
        else if ("file".equals(uri.getScheme())) {
            String filePath = uri.getPath();
            File file = new File(filePath);
            sizeInBytes = file.length();
        }

        // Convert bytes to kilobytes
        long sizeInKB = sizeInBytes / 1024;
        return sizeInKB;
    }
}

