package app.fresherpools.xpertscan.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import app.fresherpools.xpertscan.Activity.ImageActivity;
import app.fresherpools.xpertscan.Data.ListData;
import app.fresherpools.xpertscan.R;
import app.fresherpools.xpertscan.Override.Refresh;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private ArrayList<ListData> listData;
    private Activity activity;
    private Refresh refresh;

    public ListAdapter(ArrayList<ListData> listData, Activity activity, Refresh refresh) {
        this.listData = listData;
        this.activity = activity;
        this.refresh = refresh;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item, parent, false);
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
            holder.main.setVisibility(View.VISIBLE);
            holder.ad.setVisibility(View.GONE);
            holder.thumbnail.setImageURI(Uri.fromFile(new File(listData.get(position).getThumbnail())));
            holder.fileName.setText(listData.get(position).getFileName());
            holder.date.setText(listData.get(position).getDate());
            holder.shareImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final File file = new File(createPdf(position).getPath());
                    final Dialog dialog = new Dialog(activity);
                    dialog.setContentView(R.layout.export_dialogu);
                    dialog.setCancelable(false);
                    final AdView frameLayout = dialog.findViewById(R.id.dialog_ad);
                    frameLayout.loadAd(new AdRequest.Builder().build());
                    dialog.show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_SEND);
                            intent.setType("application/pdf");
                            intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(activity,activity.getPackageName(),file));
                            activity.startActivity(intent);
                            dialog.cancel();
                        }
                    },20*1000);
                }
            });
            holder.listMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, ImageActivity.class);
                    intent.putExtra("Title",listData.get(position).getFileName());
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
                                String path = activity.getFilesDir().toString() + "/" + listData.get(position).getFileName();
                                File file = new File(path);
                                String[] files = file.list();
                                String dest = activity.getFilesDir().toString() + "/" + editText.getText().toString();
                                File temp = new File(dest);
                                temp.mkdirs();
                                for (int i=0; i<files.length; i++) {
                                    File file1 = new File(path, files[i]);
                                    File file2 = new File(dest, files[i]);
                                    file1.renameTo(file2);
                                }
                                delete(position);
                                refresh.refresh(true);
                                dialog.cancel();
                            }
                        }
                    });
                }
            });
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
        }
    }

    private Uri createPdf(int position) {
        File file = getOutputFile(position);
        if (file != null) {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                PdfDocument pdfDocument = new PdfDocument();

                String path = activity.getFilesDir().toString() + "/" + listData.get(position).getFileName();
                File file1 = new File(path);
                String[] dire = file1.list();
                Arrays.sort(dire);
                for (int i=0 ; i<dire.length; i++) {
                    Bitmap bitmap = BitmapFactory.decodeFile(path + "/" + dire[i]);
                    PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), (i + 1)).create();
                    PdfDocument.Page page = pdfDocument.startPage(pageInfo);
                    Canvas canvas = page.getCanvas();
                    Paint paint = new Paint();
                    paint.setColor(Color.BLUE);
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

    private File getOutputFile(int position) {
        File root = new File(activity.getExternalFilesDir("/"),"Xpert Scan");
        boolean isFolderCreated = true;
        if (! root.exists()) {
            isFolderCreated = root.mkdir();
        }
        if (isFolderCreated) {
            return new File(root, listData.get(position).getFileName() + ".pdf");
        } else {
            return null;
        }
    }

    private void delete(int position) {
        String path = activity.getFilesDir().toString() + "/" + listData.get(position).getFileName();
        File file = new File(path);
        String[] files = file.list();
        for (int i=0; i<files.length; i++) {
            File del = new File(path, files[i]);
            del.delete();
        }
        file.delete();
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView thumbnail;
        private TextView fileName;
        private TextView date;
        private ImageView delete;
        private ImageView rename;
        private CardView listMain;
        private AdView ad;
        private ConstraintLayout main;
        private ImageView shareImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            thumbnail = itemView.findViewById(R.id.thumbnail);
            fileName = itemView.findViewById(R.id.filename);
            date = itemView.findViewById(R.id.date);
            delete = itemView.findViewById(R.id.delete);
            rename = itemView.findViewById(R.id.rename);
            listMain = itemView.findViewById(R.id.list_main);
            ad = itemView.findViewById(R.id.ad);
            main = itemView.findViewById(R.id.main);
            shareImage = itemView.findViewById(R.id.share_image);

        }
    }
}
