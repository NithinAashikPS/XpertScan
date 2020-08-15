package app.fresherpools.xpertscan.Adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import app.fresherpools.xpertscan.Data.FilterData;
import app.fresherpools.xpertscan.Override.SetImage;
import app.fresherpools.xpertscan.R;
import app.fresherpools.xpertscan.Utils.ImageFilters;

import java.util.ArrayList;

import static app.fresherpools.xpertscan.Utils.Code.GREEN_FILTER;
import static app.fresherpools.xpertscan.Utils.Code.GREY_DIAGONAL_FILTER;
import static app.fresherpools.xpertscan.Utils.Code.GREY_FILTER;
import static app.fresherpools.xpertscan.Utils.Code.GREY_OUT_BARS_FILTER;
import static app.fresherpools.xpertscan.Utils.Code.NEGATIVE_FILTER;
import static app.fresherpools.xpertscan.Utils.Code.SEPIA_DIAGONAL_FILTER;
import static app.fresherpools.xpertscan.Utils.Code.SEPIA_FILTER;
import static app.fresherpools.xpertscan.Utils.Code.SEPIA_OUT_BARS_FILTER;
import static app.fresherpools.xpertscan.Utils.Code.SKETCH_FILTER;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {

    private ArrayList<FilterData> filterData;
    private Activity activity;
    private SetImage setImage;

    public FilterAdapter(ArrayList<FilterData> filterData, Activity activity, SetImage setImage) {
        this.filterData = filterData;
        this.activity = activity;
        this.setImage = setImage;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_item, parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        String bitmap = filterData.get(position).getImagePath();
        if (position == 0) {
            setImage.setFilter(ImageFilters.setDefault(bitmap, 1));
            holder.filterLoad.setVisibility(View.GONE);
        }
        BitmapFilterTask bitmapFilterTask = new BitmapFilterTask(bitmap,holder,position);
        bitmapFilterTask.execute(position);

    }

    class BitmapFilterTask extends AsyncTask<Integer, Bitmap, Bitmap> {

        String bitmap;
        ViewHolder holder;
        int position;

        public BitmapFilterTask(String bitmap, ViewHolder holder, int position) {
            this.bitmap = bitmap;
            this.holder = holder;
            this.position = position;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Bitmap doInBackground(Integer... integers) {
            switch (position) {
                case GREY_FILTER:
                    return ImageFilters.setGreyFilter(bitmap, 1);
                case NEGATIVE_FILTER:
                    return ImageFilters.setNegativeFilter(bitmap, 1);
                case SEPIA_FILTER:
                    return ImageFilters.setSepiaFilter(bitmap, 1);
                case GREEN_FILTER:
                    return ImageFilters.setGreenFilter(bitmap, 1);
                case GREY_OUT_BARS_FILTER:
                    return ImageFilters.setGreyOutBarsFilter(bitmap, 1);
                case SEPIA_OUT_BARS_FILTER:
                    return ImageFilters.setSepiaOutBarsFilter(bitmap, 1);
                case GREY_DIAGONAL_FILTER:
                    return ImageFilters.setGreyDiagonalFilter(bitmap, 1);
                case SEPIA_DIAGONAL_FILTER:
                    return ImageFilters.setSepiaDiagonalFilter(bitmap, 1);
                case SKETCH_FILTER:
                    return ImageFilters.setSketchFilter(bitmap, 1);
                default:
                    return ImageFilters.setDefault(bitmap, 1);

            }
        }

        @Override
        protected void onPostExecute(final Bitmap bitmap) {
            super.onPostExecute(bitmap);

            holder.filterImage.setImageBitmap(bitmap);
            holder.filterLoad.setVisibility(View.GONE);
            holder.filterName.setText(filterData.get(position).getFilterName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setImage.setFilter(bitmap);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return filterData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView filterImage;
        private TextView filterName;
        private ProgressBar filterLoad;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            filterImage = itemView.findViewById(R.id.filter_image);
            filterName = itemView.findViewById(R.id.filter_name);
            filterLoad = itemView.findViewById(R.id.filter_load);

        }
    }

}
