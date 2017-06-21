package android.app.petsy.Classies;

/**
 * Created by Justinas on 2017-06-04.
 */

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

public class Utility {

public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public static boolean checkPermission(final Context context)
        {
                int currentAPIVersion = Build.VERSION.SDK_INT;
                if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
                {
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                                        alertBuilder.setCancelable(true);
                                        alertBuilder.setTitle("Permission necessary");
                                        alertBuilder.setMessage("External storage permission is necessary");
                                        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                                                public void onClick(DialogInterface dialog, int which) {
                                                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                                                }
                                        });
                                        AlertDialog alert = alertBuilder.create();
                                        alert.show();

                                } else {
                                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                                }
                                return false;
                        } else {
                                return true;
                        }
                } else {
                        return true;
                }
        }

        public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
                int width = bm.getWidth();
                int height = bm.getHeight();
                float scaleWidth = ((float) newWidth) / width;
                float scaleHeight = ((float) newHeight) / height;
                Matrix matrix = new Matrix();
// RESIZE THE BIT MAP
                matrix.postScale(scaleWidth, scaleHeight);
                // RECREATE THE NEW BITMAP
                Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
                        matrix, false);
                return resizedBitmap;
        }

}

