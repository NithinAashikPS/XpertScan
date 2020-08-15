package app.fresherpools.xpertscan.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import app.fresherpools.xpertscan.Adapter.FilterAdapter;
import app.fresherpools.xpertscan.Data.FilterData;
import app.fresherpools.xpertscan.Override.SetImage;
import app.fresherpools.xpertscan.R;
import com.google.android.gms.ads.MobileAds;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class FilterActivity extends AppCompatActivity implements SetImage {

    private ImageView filterImage;
    private ImageView filter_close;
    private ImageView filter_check;
    private String[] filterName = {"Original Image", "Sketch Filter", "Sepia Filter","Grey Filter", "Green Filter",
            "Negative Filter", "Grey Filter Outside Bars", "Sepia Filter Outside Bars", "Grey Diagonal Filter"
            , "Sepia Diagonal Filter"};
    private ArrayList<FilterData> filterData;
    private FilterAdapter filterAdapter;
    private RecyclerView recyclerView;

    private String source;
    private String uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        MobileAds.initialize(this);

        filterImage = findViewById(R.id.filter_image);
        filter_close = findViewById(R.id.filter_close);
        filter_check = findViewById(R.id.filter_check);
        recyclerView = findViewById(R.id.fliter_recyclerview);
        source = getIntent().getStringExtra("source");

        File file = new File(source);

        filter_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterActivity.super.onBackPressed();
            }
        });
        filter_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (getIntent().getStringExtra("activity")) {
                    case "main":
                        main();
                        break;

                    case "image200":
                        image200();
                        break;

                    case "image100":
                        image100();
                        break;
                }
            }
        });

        init();

    }

    private void image100() {
        File file = new File(FilterActivity.this.getFilesDir().toString() + "/" + getIntent().getStringExtra("Title"));
        int fileName = file.list().length;
        String path = FilterActivity.this.getFilesDir().toString();
        String destination = path + "/" + getIntent().getStringExtra("Title");

        File file1 = new File(uri);
        File file2 = new File(getIntent().getStringExtra("source"));

        file1.renameTo(file2);

        Intent intent = new Intent(FilterActivity.this, ImageActivity.class);
        intent.putExtra("Title", getIntent().getStringExtra("Title"));
        startActivity(intent);
    }

    private void image200() {
        File file = new File(FilterActivity.this.getFilesDir().toString() + "/" + getIntent().getStringExtra("Title"));
        int fileName = file.list().length;
        String path = FilterActivity.this.getFilesDir().toString();
        String destination = path + "/" + getIntent().getStringExtra("Title");

        File file1 = new File(uri);
        File file2 = new File(destination, String.valueOf(++fileName) + ".jpeg");

        file1.renameTo(file2);

        Log.i("ASDFG",uri);

        Intent intent = new Intent(FilterActivity.this, ImageActivity.class);
        intent.putExtra("Title", getIntent().getStringExtra("Title"));
        startActivity(intent);
    }

    private void main() {
        SimpleDateFormat formatter = new SimpleDateFormat("MM_dd_yyyy_HH_mm_ss", Locale.US);
        Date now = new Date();
        String ts = formatter.format(now);

        String path = FilterActivity.this.getFilesDir().toString();
        String destination = path + "/" + ts;
        File file = new File(destination);
        file.mkdirs();

        File file1 = new File(uri);
        File file2 = new File(destination, "/1.jpeg");

        file1.renameTo(file2);


        Intent intent = new Intent(FilterActivity.this, ImageActivity.class);
        intent.putExtra("Title",ts);
        startActivity(intent);
    }

    private void init() {
        filterData = new ArrayList<>();

        for (int i=0; i<4; i++) {
            filterData.add(new FilterData(source,filterName[i]));
        }

        filterAdapter = new FilterAdapter( filterData,this, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(filterAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
    public void setFilter(Bitmap bitmap) {
        filterImage.setImageBitmap(bitmap);
        uri = getUri(bitmap);
    }

    private String getUri(Bitmap bitmap) {
        File file = new File(FilterActivity.this.getCacheDir(), "image.jpeg");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }
}