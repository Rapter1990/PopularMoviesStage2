package image;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import model.Movie;

public class ImageUtilities {

    // TODO : 199) Creating LOG_TAG
    private static final String LOG_TAG = ImageUtilities.class.getSimpleName();

    // TODO : 198) Saving Image to internal Storage
    public static Target saveImage(Context context,final String imageName) {

        ContextWrapper cw = new ContextWrapper(context);
        // path to /data/data/yourapp/app_data/imageDir
        final File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);


        Target target = new Target() {

            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {

                    @Override
                    public void run() {

                        File file=new File(directory,imageName);
                        FileOutputStream ostream = null;
                        try {
                            file.createNewFile();
                            ostream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                            ostream.flush();
                            ostream.close();
                        }catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                ostream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.i("image", "image saved to >>>" + file.getAbsolutePath());
                    }
                }).start();

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };


        return target;
    }


    // TODO : 199) Deleting Image from internal Storage
    public static void deleteImage(Context context, Movie currentMovie){
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File myImageFile = new File(directory, currentMovie.getId());
        if (myImageFile.delete()) Log.v(LOG_TAG, "Image on the disk deleted successfully!");
    }
}
