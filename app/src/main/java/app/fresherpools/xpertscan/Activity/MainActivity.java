package app.fresherpools.xpertscan.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import app.fresherpools.xpertscan.Adapter.ListAdapter;
import app.fresherpools.xpertscan.Data.ListData;

import app.fresherpools.xpertscan.R;
import app.fresherpools.xpertscan.Override.Refresh;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;

import app.fresherpools.xpertscan.Utils.Code;
import app.fresherpools.xpertscan.Utils.Methods;

public class MainActivity extends AppCompatActivity implements Refresh {

    private RecyclerView recyclerView;
    private ArrayList<ListData> listData;
    private ListAdapter listAdapter;

    private FloatingActionButton galary;
    private FloatingActionButton camera;

    private FloatingActionButton menu;

    private FloatingActionButton floatingMenu;
    private LinearLayout animate;

    private Uri uri;
    private ImageView support;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this);

        recyclerView = findViewById(R.id.data_list);
        galary = findViewById(R.id.galary);
        camera = findViewById(R.id.camera);
        menu = findViewById(R.id.main_menu);
        floatingMenu = findViewById(R.id.floating_menu);
        animate = findViewById(R.id.animate);
        support = findViewById(R.id.support);

        File root = new File(getExternalFilesDir("/"),"Xpert Scan");
        if (! root.exists()) {
            root.mkdirs();
        }

        init();

        galary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Methods.collapse(floatingMenu, animate);
                startActivityForResult(new Intent().setAction(Intent.ACTION_GET_CONTENT).setType("image/*"), Code.CODE_IMG_GALLERY);
            }
        });

        floatingMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Code.ISCOLLAPSED) {
                    Methods.expand(floatingMenu, animate);
                } else {
                    Methods.collapse(floatingMenu, animate);
                }
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Methods.collapse(floatingMenu, animate);
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "Temp");
                values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
                uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, Code.CAMERA_REQUEST);
            }
        });

        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Methods.collapse(floatingMenu, animate);
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.menu_dialogu);
                dialog.show();
                dialog.setCancelable(true);

                LinearLayout RateUs = dialog.findViewById(R.id.rate_us);
                LinearLayout ShareApp = dialog.findViewById(R.id.share_app);


                RateUs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("market://details?id=" + getPackageName()));
                            startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
                            startActivity(intent);
                        }
                    }
                });

                ShareApp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + getPackageName());
                        startActivity(intent);
                    }
                });
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PdfListActivity.class);
                startActivity(intent);
            }
        });

    }

    protected void showAppExitDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Please confirm");
        builder.setMessage("Do you want to exit the app?");
        builder.setCancelable(true);

        builder.setPositiveButton("Yes(exit)", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Do something when user want to exit the app
                // Let allow the system to handle the event, such as exit the app
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Do something when want to stay in the app
            }
        });

        // Create the alert dialog using alert dialog builder
        AlertDialog dialog = builder.create();

        // Finally, display the dialog when user press back button
        dialog.show();
    }

    private void init() {
        listData = new ArrayList<ListData>();
        File file = new File(this.getFilesDir().toString());
        String[] dire = file.list();

        for (int i=0 ; i<dire.length; i++) {
            String path = this.getFilesDir().toString() + "/" + dire[i] + "/1.jpeg";
            File file1 = new File(this.getFilesDir().toString() + "/" + dire[i]);
            if (file1.list().length != 0) {
                listData.add(new ListData(dire[i],"Total Pages : " + file1.list().length, path));
            } else {
                file1.delete();
            }
        }

        for (int i=1; i<=listData.size(); i+=3) {
            if (i%4 == 0) {
                listData.add(i, new ListData("", "", ""));
            }
        }

        listAdapter = new ListAdapter(listData, this, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(listAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        init();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Code.CODE_IMG_GALLERY && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                Methods.Corp(imageUri, MainActivity.this);
            }
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            Uri imageUriResultCrop = UCrop.getOutput(data);
            if (imageUriResultCrop != null) {

                String source = imageUriResultCrop.getPath();

                Intent intent = new Intent(MainActivity.this, FilterActivity.class);
                intent.putExtra("source", source);
                intent.putExtra("activity", "main");
                startActivity(intent);

            }

        } else if (requestCode == Code.CAMERA_REQUEST && resultCode == RESULT_OK) {

            if (uri != null) {
                Methods.Corp(uri, MainActivity.this);
            }

        }

    }

    @Override
    public void refresh(Boolean refresh) {
        if (refresh) {
            recyclerView.removeAllViews();
            init();
        }
    }

    @Override
    public void onBackPressed() {
        showAppExitDialog();
    }
}