package app.fresherpools.xpertscan.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import app.fresherpools.xpertscan.Data.PdfData;
import app.fresherpools.xpertscan.R;
import app.fresherpools.xpertscan.Override.Refresh;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PdfAdapter extends RecyclerView.Adapter<PdfAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<PdfData> pdfData;
    private Refresh refresh;

    public PdfAdapter(Activity activity, ArrayList<PdfData> pdfData, Refresh refresh) {
        this.activity = activity;
        this.pdfData = pdfData;
        this.refresh = refresh;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_pdf, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        if (position%4 == 0 && position != 0) {
            holder.ad.setVisibility(View.VISIBLE);
            holder.main.setVisibility(View.GONE);
            holder.ad.loadAd(new AdRequest.Builder().build());
        } else {
            holder.ad.setVisibility(View.GONE);
            holder.main.setVisibility(View.VISIBLE);
            holder.filename.setText(pdfData.get(position).getFileName());
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(activity);
                    dialog.setContentView(R.layout.delete_dialogu);
                    dialog.setCancelable(false);
                    dialog.show();
                    dialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });
                    dialog.findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            delete(position);
                            refresh.refresh(true);
                            dialog.cancel();
                        }
                    });
                }
            });
            holder.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    File file = new File(pdfData.get(position).getFilePath().toString(),pdfData.get(position).getFileName());
                    Log.i("File Path : ", String.valueOf(file));
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setType("application/pdf");
                    intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(activity,activity.getPackageName(),file));
                    activity.startActivity(intent);
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    File file = new File(pdfData.get(position).getFilePath().toString(),pdfData.get(position).getFileName());
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(FileProvider.getUriForFile(activity,activity.getPackageName(),file),"application/pdf");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    activity.startActivity(intent);
                }
            });
            holder.rename.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(activity);
                    dialog.setContentView(R.layout.rename_dialogu);
                    dialog.setCancelable(false);
                    dialog.show();
                    dialog.findViewById(R.id.cancel1).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });
                    dialog.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EditText editText = dialog.findViewById(R.id.rename_edit_text);
                            if (editText.getText().toString().isEmpty()) {
                                editText.setError("Rename field should not be empty.");
                            } else {
                                File file = new File(pdfData.get(position).getFilePath().toString(),pdfData.get(position).getFileName());
                                File file1 = new File(pdfData.get(position).getFilePath().toString(), editText.getText().toString()+".pdf");
                                try {
                                    file1.createNewFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                file.renameTo(file1);
                                delete(position);
                                refresh.refresh(true);
                                dialog.cancel();
                            }
                        }
                    });
                }
            });
        }

    }

    private void delete(int position) {
        File file = new File(pdfData.get(position).getFilePath().toString(),pdfData.get(position).getFileName());
        file.delete();
    }

    @Override
    public int getItemCount() {
        return pdfData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView filename;
        private ImageView rename;
        private ImageView share;
        private ImageView delete;

        private AdView ad;
        private ConstraintLayout main;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            filename = itemView.findViewById(R.id.pdf_filename);
            share = itemView.findViewById(R.id.share_pdf);
            rename = itemView.findViewById(R.id.rename_pdf);
            delete = itemView.findViewById(R.id.delete_pdf);
            ad = itemView.findViewById(R.id.ad_pdf);
            main = itemView.findViewById(R.id.main_pdf);

        }
    }
}
