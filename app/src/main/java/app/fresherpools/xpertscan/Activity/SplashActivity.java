package app.fresherpools.xpertscan.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import app.fresherpools.xpertscan.R;
import com.google.android.gms.ads.MobileAds;

import static app.fresherpools.xpertscan.Utils.Code.PERMISSION_REQUEST_CODE2;
import static app.fresherpools.xpertscan.Utils.Methods.checkpermission;
import static app.fresherpools.xpertscan.Utils.Methods.requestPermission;

public class SplashActivity extends AppCompatActivity {

    private TextView gradientText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        MobileAds.initialize(this);

        gradientText = findViewById(R.id.gradient_text);
        Shader shader = new LinearGradient(0,0,0,gradientText.getLineHeight(),getColor(R.color.colorPrimary),getColor(R.color.colorAccent),Shader.TileMode.REPEAT);
        gradientText.getPaint().setShader(shader);

        if (checkpermission(SplashActivity.this)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            }, 3000);
        } else {
            requestPermission(SplashActivity.this);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 10 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this, Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE2);
            }
        }
        if (requestCode == 20 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            }, 3000);
        }
    }


}