package app.fresherpools.xpertscan.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import app.fresherpools.xpertscan.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;

public class Methods {

    public static Bitmap rotate(String path) {
        File file = new File(path);
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        Bitmap rotated = null;
        ExifInterface ei = null;
        try {
            ei = new ExifInterface(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(270);
                break;
            default:
                matrix.setRotate(0);
        }
        rotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return rotated;
    }

    public static void startCorp(@NonNull Uri uri, Activity activity) {
        UCrop uCrop = UCrop.of(uri, uri);
        uCrop.useSourceImageAspectRatio();
        uCrop.withOptions(getCropOptions(activity));
        uCrop.start(activity);
    }

    public static void Corp(@NonNull Uri uri, Activity activity) {
        String ImageName = "images.jpeg";
        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(activity.getCacheDir(), ImageName)));
        uCrop.useSourceImageAspectRatio();
        uCrop.withOptions(getCropOptions(activity));
        uCrop.start(activity);
    }

    public static UCrop.Options getCropOptions(Activity activity) {
        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(100);
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setStatusBarColor(activity.getResources().getColor(R.color.colorAccent));
        options.setToolbarColor(activity.getResources().getColor(R.color.colorAccent));
        options.setToolbarTitle("Crop Image");
        options.setActiveControlsWidgetColor(activity.getResources().getColor(R.color.colorAccent));
        options.setRootViewBackgroundColor(activity.getResources().getColor(R.color.colorBlack));
        options.setToolbarWidgetColor(activity.getResources().getColor(R.color.colorWhite));
        options.setCropFrameColor(activity.getResources().getColor(R.color.colorAccent));
        return options;
    }

    public static void requestPermission(Activity activity) {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Code.PERMISSION_REQUEST_CODE1);
        }
    }

    public static boolean checkpermission(Activity activity) {
        int result1 = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        if (result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public static void collapse(FloatingActionButton floatingMenu, LinearLayout animate) {
        floatingMenu.setImageResource(R.drawable.floating_expand);
        animate.animate().translationY(800);
        Code.ISCOLLAPSED = true;
    }

    public static void expand(FloatingActionButton floatingMenu, LinearLayout animate) {
        floatingMenu.setImageResource(R.drawable.floating_collapse);
        animate.animate().translationY(0);
        Code.ISCOLLAPSED = false;
    }

}
