package com.example.cliqv1;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
// abstract, da kein Objekt von der Klasse erstellt werden soll, sondern nur die Methoden interessant sind
public abstract class Util {

    // diese Methode holt ein Bild aus dem drawable Ordner und wandelt es in ein Bitmap um
    // drawableResId übergibt die id des gewünschten Bildes im ressourcen Bereich
    public static Bitmap getBitmapFromDrawable(Activity activity, int drawableResId){
        // activity.getApplicationContext() sorgt dafür, dass die Methode die richtige Activity verwendet
        Bitmap bitmap = BitmapFactory.decodeResource(activity.getApplicationContext().getResources(), drawableResId);
        return bitmap;
    }
    // wandelt das gegebene Bitmap in ein Bytearray um
    public static byte[] getBitmapData(Bitmap bitmap) {
        ByteArrayOutputStream blob = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, blob);

        byte[] bitmapdata = blob.toByteArray();

        try {
            blob.close();
            blob = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmapdata;
    }
    // wandelt eine Bitmap in einen Base64 String um, um Bilder so in der Datenbank als Longtext zu speichern
    public static String getBase64StringFromBitmap(Bitmap bitmap) {
        String base64StringOfBitmap = Base64.encodeToString(getBitmapData(bitmap), 1);
        return base64StringOfBitmap;
    }
    //wandelt dinen Base64 String in ein Bitmap um
    public static Bitmap getBitmapFromBase64String(String base64String){
        byte[] decoded = Base64.decode(base64String,1);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decoded, 0, decoded.length);
        return bitmap;
    }

}
