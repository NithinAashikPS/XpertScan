package app.fresherpools.xpertscan.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import app.fresherpools.xpertscan.Data.ImageData;
import app.fresherpools.xpertscan.R;
import app.fresherpools.xpertscan.Override.Refresh;

import java.io.File;
import java.util.ArrayList;

import app.fresherpools.xpertscan.Utils.Code;

import static app.fresherpools.xpertscan.Utils.Methods.startCorp;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private ArrayList<ImageData> imageData;
    private Activity activity;
    private Refresh refresh;

    public ImageAdapter(ArrayList<ImageData> imageData, Activity activity, Refresh refresh) {
        this.imageData = imageData;
        this.activity = activity;
        this.refresh = refresh;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == R.layout.list_image) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_image, parent, false);
        } else if (viewType == R.layout.add_new_pages) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_new_pages, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return (position == imageData.size()) ? R.layout.add_new_pages : R.layout.list_image;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if (position == imageData.size()) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.startActivityForResult(new Intent().setAction(Intent.ACTION_GET_CONTENT).setType("image/*"), Code.CODE_IMG_GALLERY);
                }
            });
        } else {
            holder.imageView.setImageURI(Uri.fromFile(new File(imageData.get(position).getImage())));
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    File file = new File(imageData.get(position).getImage());
                    Code.CROP_IMAGE_CODE = 100;
                    startCorp(Uri.fromFile(file), activity);
                }
            });
            holder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
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
                    return true;
                }
            });
        }
    }

    private void delete(int position) {
        File file = new File(imageData.get(position).getImage());
        file.delete();
    }

    @Override
    public int getItemCount() {
        return imageData.size()+1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.main_image);

        }
    }

}
