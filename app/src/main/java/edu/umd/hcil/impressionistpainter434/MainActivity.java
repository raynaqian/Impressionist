package edu.umd.hcil.impressionistpainter434;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements OnMenuItemClickListener {

    private static int RESULT_LOAD_IMAGE = 1;
    private  ImpressionistView _impressionistView;
    // AudioManager
    private AudioManager mAudioManager;
    // SoundPool
    private SoundPool mSoundPool;
    // ID for the meow sound
    private int mSoundID;
    // Audio volume
    private float mStreamVolume;

    // These images are downloaded and added to the Android Gallery when the 'Download Images' button is clicked.
    // This was super useful on the emulator where there are no images by default
    private static String[] IMAGE_URLS ={
            "http://www.cs.umd.edu/class/spring2016/cmsc434/assignments/IA08-AndroidII/Images/BoliviaBird_PhotoByJonFroehlich(Medium).JPG",
            "http://www.cs.umd.edu/class/spring2016/cmsc434/assignments/IA08-AndroidII/Images/BolivianDoor_PhotoByJonFroehlich(Medium).JPG",
            "http://www.cs.umd.edu/class/spring2016/cmsc434/assignments/IA08-AndroidII/Images/MinnesotaFlower_PhotoByJonFroehlich(Medium).JPG",
            "http://www.cs.umd.edu/class/spring2016/cmsc434/assignments/IA08-AndroidII/Images/PeruHike_PhotoByJonFroehlich(Medium).JPG",
            "http://www.cs.umd.edu/class/spring2016/cmsc434/assignments/IA08-AndroidII/Images/ReginaSquirrel_PhotoByJonFroehlich(Medium).JPG",
            "http://www.cs.umd.edu/class/spring2016/cmsc434/assignments/IA08-AndroidII/Images/SucreDog_PhotoByJonFroehlich(Medium).JPG",
            "http://www.cs.umd.edu/class/spring2016/cmsc434/assignments/IA08-AndroidII/Images/SucreStreet_PhotoByJonFroehlich(Medium).JPG",
            "http://www.cs.umd.edu/class/spring2016/cmsc434/assignments/IA08-AndroidII/Images/SucreStreet_PhotoByJonFroehlich2(Medium).JPG",
            "http://www.cs.umd.edu/class/spring2016/cmsc434/assignments/IA08-AndroidII/Images/SucreWine_PhotoByJonFroehlich(Medium).JPG",
            "http://www.cs.umd.edu/class/spring2016/cmsc434/assignments/IA08-AndroidII/Images/WashingtonStateFlower_PhotoByJonFroehlich(Medium).JPG",
            "http://www.cs.umd.edu/class/spring2016/cmsc434/assignments/IA08-AndroidII/Images/JonILikeThisShirt_Medium.JPG",
            "http://www.cs.umd.edu/class/spring2016/cmsc434/assignments/IA08-AndroidII/Images/JonUW_(853x1280).jpg",
            "http://www.cs.umd.edu/class/spring2016/cmsc434/assignments/IA08-AndroidII/Images/MattMThermography_Medium.jpg",
            "http://www.cs.umd.edu/class/spring2016/cmsc434/assignments/IA08-AndroidII/Images/PinkFlower_PhotoByJonFroehlich(Medium).JPG",
            "http://www.cs.umd.edu/class/spring2016/cmsc434/assignments/IA08-AndroidII/Images/PinkFlower2_PhotoByJonFroehlich(Medium).JPG",
            "http://www.cs.umd.edu/class/spring2016/cmsc434/assignments/IA08-AndroidII/Images/PurpleFlowerPlusButterfly_PhotoByJonFroehlich(Medium).JPG",
            "http://www.cs.umd.edu/class/spring2016/cmsc434/assignments/IA08-AndroidII/Images/WhiteFlower_PhotoByJonFroehlich(Medium).JPG",
            "http://www.cs.umd.edu/class/spring2016/cmsc434/assignments/IA08-AndroidII/Images/YellowFlower_PhotoByJonFroehlich(Medium).JPG",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        _impressionistView = (ImpressionistView)findViewById(R.id.viewImpressionist);
        ImageView imageView = (ImageView)findViewById(R.id.viewImage);
        _impressionistView.setImageView(imageView);
    }

    public void onButtonClickClear(View v) {
        new AlertDialog.Builder(this)
                .setTitle("Clear Painting?")
                .setMessage("Do you really want to clear your painting?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Toast.makeText(MainActivity.this, "Painting cleared", Toast.LENGTH_SHORT).show();
                        _impressionistView.clearPainting();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    public void onButtonClickSave(View v) {
        File exSD = Environment.getExternalStorageDirectory();
        // if the gallery has no images, fixes bug in emulators
        if (exSD != null) {
            File gallery = new File(exSD, "DCIM/Camera");
            if (!gallery.exists()) {
                gallery.mkdirs();
            }
        }
        _impressionistView.setDrawingCacheEnabled(true);
        _impressionistView.buildDrawingCache(true); // good
        // THERE'S A BUG HERE AHHHHHHHHH
        ImpressionistView temp = (ImpressionistView)findViewById(R.id.viewImpressionist);
        temp.layout(0, 0, _impressionistView.getHeight(), _impressionistView.getWidth());
        Bitmap b = Bitmap.createBitmap(_impressionistView.getDrawingCache()); // doesn't save colors
        MediaStore.Images.Media.insertImage(getContentResolver(), b, "img", "test");
        _impressionistView.setDrawingCacheEnabled(false);
    }

    public void onButtonClickSetBrush(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }

    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuCircle:
                Toast.makeText(this, "Circle Brush", Toast.LENGTH_SHORT).show();
                _impressionistView.setBrushType(BrushType.Circle);
                return true;
            case R.id.menuSquare:
                Toast.makeText(this, "Square Brush", Toast.LENGTH_SHORT).show();
                _impressionistView.setBrushType(BrushType.Square);
                return true;
            case R.id.menuLine:
                Toast.makeText(this, "Line Brush", Toast.LENGTH_SHORT).show();
                _impressionistView.setBrushType(BrushType.Line);
                return true;
            case R.id.menuWord:
                Toast.makeText(this, "Words Brush", Toast.LENGTH_SHORT).show();
                _impressionistView.setBrushType(BrushType.Words);
                return true;
            case R.id.menuKittens:
                Toast.makeText(this, "Meow", Toast.LENGTH_SHORT).show();
                _impressionistView.setBrushType(BrushType.Kittens);
                mSoundPool.play(mSoundID, mStreamVolume, mStreamVolume, 1, 0, 1.0f);
                return true;
        }
        return false;
    }


    /**
     * Downloads test images to use in the assignment. Feel free to use any images you want. I only made this
     * as an easy way to get images onto the emulator.
     *
     * @param v
     */
    public void onButtonClickDownloadImages(View v){

        // Without this call, the app was crashing in the onActivityResult method when trying to read from file system
        FileUtils.verifyStoragePermissions(this);

        // Amazing Stackoverflow post on downloading images: http://stackoverflow.com/questions/15549421/how-to-download-and-save-an-image-in-android
        final BasicImageDownloader imageDownloader = new BasicImageDownloader(new BasicImageDownloader.OnImageLoaderListener() {

            @Override
            public void onError(String imageUrl, BasicImageDownloader.ImageError error) {
                Log.v("BasicImageDownloader", "onError: " + error);
            }

            @Override
            public void onProgressChange(String imageUrl, int percent) {
                Log.v("BasicImageDownloader", "onProgressChange: " + percent);
            }

            @Override
            public void onComplete(String imageUrl, Bitmap downloadedBitmap) {

            }
        });

        for(String url: IMAGE_URLS){
            imageDownloader.download(url, true);
        }
    }

    /**
     * Loads an image from the Gallery into the ImageView
     *
     * @param v
     */
    public void onButtonClickLoadImage(View v){

        // Without this call, the app was crashing in the onActivityResult method when trying to read from file system
        FileUtils.verifyStoragePermissions(this);

        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    /**
     * Called automatically when an image has been selected in the Gallery
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri imageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                ImageView imageView = (ImageView) findViewById(R.id.viewImage);

                // destroy the drawing cache to ensure that when a new image is loaded, its cached
                imageView.destroyDrawingCache();
                imageView.setImageBitmap(bitmap);
                imageView.setDrawingCacheEnabled(true);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    // This code sample is taken from CMSC436. We learned how to do this in class.
    @Override
    protected void onResume() {
        super.onResume();

        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        mStreamVolume = (float) mAudioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC)
                / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mSoundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        // The .wav is from http://www.wavsource.com/animals/animals.htm
        mSoundID = mSoundPool.load(this, R.raw.cat_meow_x, 1);
    }

    @Override
    protected void onPause() {
        // Release all SoundPool resources
        mSoundPool.unload(mSoundID);
        mSoundPool.release();
        mSoundPool = null;

        super.onPause();
    }
}
