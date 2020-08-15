package app.fresherpools.xpertscan.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import app.fresherpools.xpertscan.Adapter.ImageAdapter;
import app.fresherpools.xpertscan.Data.ImageData;
import app.fresherpools.xpertscan.R;
import app.fresherpools.xpertscan.Override.Refresh;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import app.fresherpools.xpertscan.Utils.Code;
import app.fresherpools.xpertscan.Utils.Methods;

public class ImageActivity extends AppCompatActivity implements Refresh {

    private ImageView back;
    private TextView title;

    private RecyclerView recyclerView;
    private ArrayList<ImageData> imageData;
    private ImageAdapter imageAdapter;

    private LinearLayout floatingIcon;
    private FloatingActionButton galary;
    private FloatingActionButton camera;

    private ImageView exportPdf;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        MobileAds.initialize(this);

        back = findViewById(R.id.back);
        title = findViewById(R.id.title);
        recyclerView = findViewById(R.id.main_recyclerview);
        floatingIcon = findViewById(R.id.floating_icons1);
        galary = findViewById(R.id.galary1);
        camera = findViewById(R.id.camera1);
        exportPdf = findViewById(R.id.export_pdf);

        init();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ImageActivity.this, MainActivity.class));
            }
        });
        title.setText(getIntent().getStringExtra("Title"));

        galary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent().setAction(Intent.ACTION_GET_CONTENT).setType("image/*"), Code.CODE_IMG_GALLERY);
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "Temp");
                values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
                uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, Code.CAMERA_REQUEST);
            }
        });

        exportPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(ImageActivity.this);
                dialog.setContentView(R.layout.export_dialogu);
                dialog.setCancelable(false);
                final AdView frameLayout = dialog.findViewById(R.id.dialog_ad);
                frameLayout.loadAd(new AdRequest.Builder().build());
                dialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        File file = new File(createPdf().getPath());
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setDataAndType(FileProvider.getUriForFile(ImageActivity.this,getPackageName(),file),"application/pdf");
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(intent);
                        dialog.cancel();
                    }
                },20*1000);
            }
        });

    }

    private Uri createPdf() {
        File file = getOutputFile();
        if (file != null) {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                PdfDocument pdfDocument = new PdfDocument();

                String path = ImageActivity.this.getFilesDir().toString() + "/" + getIntent().getStringExtra("Title");
                File file1 = new File(path);
                String[] dire = file1.list();
                Arrays.sort(dire);
                for (int i=0 ; i<dire.length; i++) {
                    Bitmap bitmap = BitmapFactory.decodeFile(path + "/" + dire[i]);
                    PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), (i + 1)).create();
                    PdfDocument.Page page = pdfDocument.startPage(pageInfo);
                    Canvas canvas = page.getCanvas();
                    Paint paint = new Paint();
                    paint.setColor(Color.BLACK);
                    canvas.drawPaint(paint);
                    canvas.drawBitmap(bitmap, 0f, 0f, null);
                    pdfDocument.finishPage(page);
                    bitmap.recycle();
                }
                pdfDocument.writeTo(fileOutputStream);
                pdfDocument.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Uri.fromFile(file);
    }

    private File getOutputFile() {
        File root = new File(getExternalFilesDir("/"),"Xpert Scan");
        boolean isFolderCreated = true;
        if (! root.exists()) {
            isFolderCreated = root.mkdir();
        }
        if (isFolderCreated) {
            return new File(root, getIntent().getStringExtra("Title") + ".pdf");
        } else {
            return null;
        }
    }

    private void init() {
        imageData = new ArrayList<ImageData>();
        String path = this.getFilesDir().toString() + "/" + getIntent().getStringExtra("Title");
        File file = new File(path);
        String[] dire = file.list();
        Arrays.sort(dire);
        if (dire.length == 0) {
            startActivity(new Intent(ImageActivity.this, MainActivity.class));
            finish();
        }
        for (int i=0 ; i<dire.length; i++) {
            imageData.add(new ImageData(path + "/" + dire[i]));
        }

        imageAdapter = new ImageAdapter(imageData, this, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setAdapter(imageAdapter);

        recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (oldScrollY > 0) {
                    floatingIcon.setVisibility(View.VISIBLE);
                } else if (oldScrollY < 0) {
                    floatingIcon.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ImageActivity.this, MainActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Code.CODE_IMG_GALLERY && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                Methods.Corp(imageUri, ImageActivity.this);
                Code.CROP_IMAGE_CODE = 200;
            }
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK && Code.CROP_IMAGE_CODE == 200) {
            Uri imageUriResultCrop = UCrop.getOutput(data);
            if (imageUriResultCrop != null) {

                String source = imageUriResultCrop.getPath();

                Intent intent = new Intent(ImageActivity.this, FilterActivity.class);
                intent.putExtra("source", source);
                intent.putExtra("activity", "image200");
                intent.putExtra("Title", getIntent().getStringExtra("Title"));
                startActivity(intent);

            }
            Code.CROP_IMAGE_CODE = 0;
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK && Code.CROP_IMAGE_CODE == 100) {
            Uri imageUriResultCrop = UCrop.getOutput(data);
            if (imageUriResultCrop != null) {

                String source = imageUriResultCrop.getPath();

                Intent intent = new Intent(ImageActivity.this, FilterActivity.class);
                intent.putExtra("source", source);
                intent.putExtra("activity", "image100");
                intent.putExtra("Title", getIntent().getStringExtra("Title"));
                startActivity(intent);

            }
            Code.CROP_IMAGE_CODE = 0;
        } else if (requestCode == Code.CAMERA_REQUEST && resultCode == RESULT_OK) {
            if (uri != null) {
                Methods.Corp(uri, ImageActivity.this);
                Code.CROP_IMAGE_CODE = 200;
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

}