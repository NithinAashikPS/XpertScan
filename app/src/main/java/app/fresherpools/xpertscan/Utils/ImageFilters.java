package app.fresherpools.xpertscan.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

import static app.fresherpools.xpertscan.Utils.Methods.rotate;

public class ImageFilters {



    public static Bitmap setDefault(String string, int num) {

        Bitmap oldBitmap;
        if (num == 1) {
            oldBitmap = rotate(string);
        } else {
            oldBitmap = BitmapFactory.decodeFile(string);
        }

        return oldBitmap;
    }


    public static Bitmap setGreyFilter(String string, int num) {

        Bitmap oldBitmap;
        if (num == 1) {
            oldBitmap = rotate(string);
        } else {
            oldBitmap = BitmapFactory.decodeFile(string);
        }

        Bitmap newBitmap = oldBitmap.copy(Bitmap.Config.ARGB_8888, true);

        int imageHeight = newBitmap.getHeight();
        int imageWidth = newBitmap.getWidth();

        Log.e("Image Size", "Height=" + imageHeight + " Width=" + imageWidth);


        for (int i = 0; i < imageWidth; i++) {

            for (int j = 0; j < imageHeight; j++) {

                int oldPixel = oldBitmap.getPixel(i, j);

                int oldRed = Color.red(oldPixel);
                int oldBlue = Color.blue(oldPixel);
                int oldGreen = Color.green(oldPixel);
                int oldAlpha = Color.alpha(oldPixel);


                int intensity = (oldRed + oldBlue + oldGreen) / 3;
                int newRed = intensity;
                int newBlue = intensity;
                int newGreen = intensity;


                int newPixel = Color.argb(oldAlpha, newRed, newGreen, newBlue);
                newBitmap.setPixel(i, j, newPixel);

            }
        }

        return newBitmap;
    }
    

    public static Bitmap setNegativeFilter(String string, int num) {

        Bitmap oldBitmap;
        if (num == 1) {
            oldBitmap = rotate(string);
        } else {
            oldBitmap = BitmapFactory.decodeFile(string);
        }
        Bitmap newBitmap = oldBitmap.copy(Bitmap.Config.ARGB_8888, true);

        int imageHeight = newBitmap.getHeight();
        int imageWidth = newBitmap.getWidth();

        Log.e("Image Size", "Height=" + imageHeight + " Width=" + imageWidth);

        for (int i = 0; i < imageWidth; i++) {

            for (int j = 0; j < imageHeight; j++) {

                int oldPixel = oldBitmap.getPixel(i, j);

                int oldRed = Color.red(oldPixel);
                int oldBlue = Color.blue(oldPixel);
                int oldGreen = Color.green(oldPixel);


                int newRed = Code.HIGHEST_COLOR_VALUE - oldRed;
                int newBlue = Code.HIGHEST_COLOR_VALUE - oldBlue;
                int newGreen = Code.HIGHEST_COLOR_VALUE - oldGreen;


                int newPixel = Color.rgb(newRed, newGreen, newBlue);
                newBitmap.setPixel(i, j, newPixel);
            }
        }

        return newBitmap;
    }


    public static Bitmap setSepiaFilter(String string, int num) {

        Bitmap oldBitmap;
        if (num == 1) {
            oldBitmap = rotate(string);
        } else {
            oldBitmap = BitmapFactory.decodeFile(string);
        }
        Bitmap newBitmap = oldBitmap.copy(Bitmap.Config.ARGB_8888, true);

        int imageHeight = newBitmap.getHeight();
        int imageWidth = newBitmap.getWidth();

        Log.e("Image Size", "Height=" + imageHeight + " Width=" + imageWidth);

        for (int i = 0; i < imageWidth; i++) {
            for (int j = 0; j < imageHeight; j++) {

                int oldPixel = oldBitmap.getPixel(i, j);

                int oldRed = Color.red(oldPixel);
                int oldBlue = Color.blue(oldPixel);
                int oldGreen = Color.green(oldPixel);
                int oldAlpha = Color.alpha(oldPixel);

                int newRed = (int) (0.393 * oldRed + 0.769 * oldGreen + 0.189 * oldBlue);
                int newGreen = (int) (0.349 * oldRed + 0.686 * oldGreen + 0.168 * oldBlue);
                int newBlue = (int) (0.272 * oldRed + 0.534 * oldGreen + 0.131 * oldBlue);

                newRed = newRed > Code.HIGHEST_COLOR_VALUE ? Code.HIGHEST_COLOR_VALUE : newRed;
                newGreen = newGreen > Code.HIGHEST_COLOR_VALUE ? Code.HIGHEST_COLOR_VALUE : newGreen;
                newBlue = newBlue > Code.HIGHEST_COLOR_VALUE ? Code.HIGHEST_COLOR_VALUE : newBlue;

                int newPixel = Color.argb(oldAlpha, newRed, newGreen, newBlue);
                newBitmap.setPixel(i, j, newPixel);
            }
        }

        return newBitmap;
    }


    public static Bitmap setGreenFilter(String string, int num) {

        Bitmap oldBitmap;
        if (num == 1) {
            oldBitmap = rotate(string);
        } else {
            oldBitmap = BitmapFactory.decodeFile(string);
        }
        Bitmap newBitmap = oldBitmap.copy(Bitmap.Config.ARGB_8888, true);

        int imageHeight = newBitmap.getHeight();
        int imageWidth = newBitmap.getWidth();

        Log.e("Image Size", "Height=" + imageHeight + " Width=" + imageWidth);

        for (int i = 0; i < imageWidth; i++) {
            for (int j = 0; j < imageHeight; j++) {

                int oldPixel = oldBitmap.getPixel(i, j);

                int oldGreen = Color.green(oldPixel);
                int oldAlpha = Color.alpha(oldPixel);

                int newRed = 0;
                int newGreen = oldGreen;
                int newBlue = 0;

                int newPixel = Color.argb(oldAlpha, newRed, newGreen, newBlue);
                newBitmap.setPixel(i, j, newPixel);
            }
        }

        return newBitmap;
    }


    public static Bitmap setGreyOutBarsFilter(String string, int num) {

        Bitmap oldBitmap;
        if (num == 1) {
            oldBitmap = rotate(string);
        } else {
            oldBitmap = BitmapFactory.decodeFile(string);
        }
        Bitmap newBitmap = oldBitmap.copy(Bitmap.Config.ARGB_8888, true);

        int imageHeight = newBitmap.getHeight();
        int imageWidth = newBitmap.getWidth();

        Log.e("Image Size", "Height=" + imageHeight + " Width=" + imageWidth);

        for (int i = 0; i < imageWidth; i++) {
            for (int j = 0; j < imageHeight; j++) {

                int oldPixel = oldBitmap.getPixel(i, j);

                int oldRed = Color.red(oldPixel);
                int oldBlue = Color.blue(oldPixel);
                int oldGreen = Color.green(oldPixel);
                int oldAlpha = Color.alpha(oldPixel);


                int intensity = (oldRed + oldBlue + oldGreen) / 3;
                int newRed = intensity;
                int newBlue = intensity;
                int newGreen = intensity;
                int newPixel = 0;

                if (i <= imageWidth / 3 || i >= (imageWidth - imageWidth / 3)) {
                    newPixel = Color.argb(oldAlpha, newRed, newGreen, newBlue);

                } else {
                    newPixel = oldPixel;
                }

                newBitmap.setPixel(i, j, newPixel);
            }
        }

        return newBitmap;
    }


    public static Bitmap setSepiaOutBarsFilter(String string, int num) {

        Bitmap oldBitmap;
        if (num == 1) {
            oldBitmap = rotate(string);
        } else {
            oldBitmap = BitmapFactory.decodeFile(string);
        }
        Bitmap newBitmap = oldBitmap.copy(Bitmap.Config.ARGB_8888, true);

        int imageHeight = newBitmap.getHeight();
        int imageWidth = newBitmap.getWidth();

        Log.e("Image Size", "Height=" + imageHeight + " Width=" + imageWidth);

        for (int i = 0; i < imageWidth; i++) {
            for (int j = 0; j < imageHeight; j++) {

                int oldPixel = oldBitmap.getPixel(i, j);

                int oldRed = Color.red(oldPixel);
                int oldBlue = Color.blue(oldPixel);
                int oldGreen = Color.green(oldPixel);
                int oldAlpha = Color.alpha(oldPixel);

                int newRed = (int) (0.393 * oldRed + 0.769 * oldGreen + 0.189 * oldBlue);
                int newGreen = (int) (0.349 * oldRed + 0.686 * oldGreen + 0.168 * oldBlue);
                int newBlue = (int) (0.272 * oldRed + 0.534 * oldGreen + 0.131 * oldBlue);

                newRed = newRed > 255 ? 255 : newRed;
                newGreen = newGreen > 255 ? 255 : newGreen;
                newBlue = newBlue > 255 ? 255 : newBlue;


                int newPixel = 0;
                if (i <= imageWidth / 3 || i >= (imageWidth - imageWidth / 3)) {
                    newPixel = Color.argb(oldAlpha, newRed, newGreen, newBlue);

                } else {
                    newPixel = oldPixel;
                }

                newBitmap.setPixel(i, j, newPixel);
            }
        }

        return newBitmap;
    }


    public static Bitmap setGreyDiagonalFilter(String string, int num) {

        Bitmap oldBitmap;
        if (num == 1) {
            oldBitmap = rotate(string);
        } else {
            oldBitmap = BitmapFactory.decodeFile(string);
        }
        Bitmap newBitmap = oldBitmap.copy(Bitmap.Config.ARGB_8888, true);

        int imageHeight = newBitmap.getHeight();
        int imageWidth = newBitmap.getWidth();

        Log.e("Image Size", "Height=" + imageHeight + " Width=" + imageWidth);


        for (int i = 0; i < imageWidth; i++) {
            for (int j = 0; j < imageHeight; j++) {

                int oldPixel = oldBitmap.getPixel(i, j);

                int oldRed = Color.red(oldPixel);
                int oldBlue = Color.blue(oldPixel);
                int oldGreen = Color.green(oldPixel);
                int oldAlpha = Color.alpha(oldPixel);

                int intensity = (oldRed + oldBlue + oldGreen) / 3;
                int newRed = intensity;
                int newBlue = intensity;
                int newGreen = intensity;

                int newPixel = 0;
                if (i < j - imageHeight / 4) {
                    newPixel = Color.argb(oldAlpha, newRed, newGreen, newBlue);

                } else if ((i - (imageHeight / 4)) > j) {
                    newPixel = Color.argb(oldAlpha, newRed, newGreen, newBlue);

                } else {
                    newPixel = oldPixel;
                }

                newBitmap.setPixel(i, j, newPixel);
            }
        }

        return newBitmap;
    }


    public static Bitmap setSepiaDiagonalFilter(String string, int num) {

        Bitmap oldBitmap;
        if (num == 1) {
            oldBitmap = rotate(string);
        } else {
            oldBitmap = BitmapFactory.decodeFile(string);
        }
        Bitmap newBitmap = oldBitmap.copy(Bitmap.Config.ARGB_8888, true);

        int imageHeight = newBitmap.getHeight();
        int imageWidth = newBitmap.getWidth();

        Log.e("Image Size", "Height=" + imageHeight + " Width=" + imageWidth);

        for (int i = 0; i < imageWidth; i++) {
            for (int j = 0; j < imageHeight; j++) {

                int oldPixel = oldBitmap.getPixel(i, j);

                int oldRed = Color.red(oldPixel);
                int oldBlue = Color.blue(oldPixel);
                int oldGreen = Color.green(oldPixel);
                int oldAlpha = Color.alpha(oldPixel);


                int newRed = (int) (0.393 * oldRed + 0.769 * oldGreen + 0.189 * oldBlue);
                int newGreen = (int) (0.349 * oldRed + 0.686 * oldGreen + 0.168 * oldBlue);
                int newBlue = (int) (0.272 * oldRed + 0.534 * oldGreen + 0.131 * oldBlue);

                newRed = newRed > 255 ? 255 : newRed;
                newGreen = newGreen > 255 ? 255 : newGreen;
                newBlue = newBlue > 255 ? 255 : newBlue;


                int newPixel = 0;
                if (i < j - imageHeight / 2) {
                    newPixel = Color.argb(oldAlpha, newRed, newGreen, newBlue);

                } else if ((i - (imageHeight / 2)) > j) {
                    newPixel = Color.argb(oldAlpha, newRed, newGreen, newBlue);

                } else {
                    newPixel = oldPixel;
                }

                newBitmap.setPixel(i, j, newPixel);
            }
        }

        return newBitmap;
    }
    

    public static Bitmap setSketchFilter(String string, int num) {

        Bitmap oldBitmap;
        if (num == 1) {
            oldBitmap = rotate(string);
        } else {
            oldBitmap = BitmapFactory.decodeFile(string);
        }
        Bitmap newBitmap = oldBitmap.copy(Bitmap.Config.ARGB_8888, true);

        int imageHeight = newBitmap.getHeight();
        int imageWidth = newBitmap.getWidth();

        Log.e("Image Size", "Height=" + imageHeight + " Width=" + imageWidth);

        for (int i = 0; i < imageWidth; i++) {
            for (int j = 0; j < imageHeight; j++) {

                int oldPixel = oldBitmap.getPixel(i, j);

                int oldRed = Color.red(oldPixel);
                int oldBlue = Color.blue(oldPixel);
                int oldGreen = Color.green(oldPixel);
                int oldAlpha = Color.alpha(oldPixel);

                int intensity = (oldRed + oldBlue + oldGreen) / 3;

                int newPixel = 0;
                int INTENSITY_FACTOR = 120;

                if (intensity > INTENSITY_FACTOR) {
                    newPixel = Color.argb(oldAlpha, Code.HIGHEST_COLOR_VALUE, Code.HIGHEST_COLOR_VALUE, Code.HIGHEST_COLOR_VALUE);

                } else if (intensity > 100) {
                    newPixel = Color.argb(oldAlpha, 150, 150, 150);
                } else {
                    newPixel = Color.argb(oldAlpha, Code.LOWEST_COLOR_VALUE, Code.LOWEST_COLOR_VALUE, Code.LOWEST_COLOR_VALUE);
                }


                newBitmap.setPixel(i, j, newPixel);
            }

        }

        return newBitmap;
    }


}
