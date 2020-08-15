package app.fresherpools.xpertscan.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import app.fresherpools.xpertscan.Adapter.PdfAdapter;
import app.fresherpools.xpertscan.Data.PdfData;
import app.fresherpools.xpertscan.R;
import app.fresherpools.xpertscan.Override.Refresh;
import com.google.android.gms.ads.MobileAds;

import java.io.File;
import java.util.ArrayList;

public class PdfListActivity extends AppCompatActivity implements Refresh {

    private RecyclerView recyclerView;
    private ArrayList<PdfData> pdfData;
    private PdfAdapter pdfAdapter;

    private ImageView back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_list);

        MobileAds.initialize(this);

        back_btn = findViewById(R.id.back_btn);
        recyclerView = findViewById(R.id.pdf_recyclerview);

        init();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PdfListActivity.super.onBackPressed();
            }
        });

    }

    private void init() {
        pdfData = new ArrayList<PdfData>();
        File root = new File(getExternalFilesDir("/"),"Xpert Scan");
        String[] dire = root.list();

        for (int i=0 ; i<dire.length; i++) {
            pdfData.add(new PdfData(dire[i], root));
        }

        for (int i=1; i<=pdfData.size(); i+=3) {
            if (i%4 == 0) {
                pdfData.add(i, new PdfData( "", root));
            }
        }

        pdfAdapter = new PdfAdapter( this, pdfData, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(pdfAdapter);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void refresh(Boolean refresh) {
        if (refresh) {
            recyclerView.removeAllViews();
            init();
        }
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

}