package ca.circumpunct.chefjayjay.chefjayjay;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;

import io.realm.Realm;
import io.realm.RealmList;

public class RecipeInteractActivity extends AppCompatActivity {
    private static final int SELECT_PICTURE = 1;
    private long interaction_id;

    private Recipe recipe;
    private RecipeInteractions interaction;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_interact);

        long recipe_id =  getIntent().getLongExtra("recipe_id", 1);

        realm = Realm.getDefaultInstance();

        interaction_id = getIntent().getLongExtra("interaction_id", -1);
        recipe = realm.where(Recipe.class).equalTo("id", recipe_id).findFirst();

        if(interaction_id == -1){
            save_new_interaction();
        }
        else{
            RecipeInteractions interaction = recipe.getInteractions().where()
                                                   .equalTo("id", interaction_id).findFirst();
            RealmList<ImageLocator> images = interaction.getImages();

            for(final ImageLocator image : images){
                display_image(image.getImage_uri());
            }
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        realm.close();
    }

    public void add_picture(View view){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selected_image_uri = data.getData();

                Log.i("RecipeInteractActivity", "" + selected_image_uri.toString());

                display_image(selected_image_uri.toString());
                store_image_url_data(selected_image_uri);
            }
        }
    }

    private void display_image(String image_uri){
        ViewGroup layout = (ViewGroup) findViewById(R.id.layout_images);
        FileDescriptor file_desc = get_file_descriptor(image_uri);

        ImageView image = new ImageView(getApplicationContext());
        Bitmap bitmap = decodeSampledBitmapFromFileDesc(file_desc, 1000, 1000);

        image.setImageBitmap(bitmap);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(1000, 1000);

        image.setLayoutParams(params);

        layout.addView(image);
    }

    private FileDescriptor get_file_descriptor(String text_uri){
        ParcelFileDescriptor parcelFileDescriptor;
        FileDescriptor fileDescriptor;
        Uri uri = Uri.parse(text_uri);

        try{
            parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
        }
        catch(FileNotFoundException e){
            Log.i("RecipeInteractActivity", "File not found");
            return null;
        }

        if(parcelFileDescriptor != null){
            fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        }
        else{
            Log.i("RecipeInteractActivity", "File not found");
            return null;
        }

        return fileDescriptor;
    }

    private void store_image_url_data(final Uri image_uri){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ImageLocator image_location = realm.createObject(ImageLocator.class);
                image_location.setImage_uri(image_uri.toString());

                interaction.getImages().add(image_location);
            }
        });
    }

    private void save_new_interaction(){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                int next_id;

                try {
                    next_id = realm.where(RecipeInteractions.class).max("id").intValue() + 1;
                } catch(NullPointerException ex) {
                    next_id = 0;
                }
                interaction_id = next_id;

                interaction = realm.createObject(RecipeInteractions.class, next_id);
                recipe.getInteractions().add(interaction);
            }
        });
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromFileDesc(FileDescriptor fd, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fd, null, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFileDescriptor(fd, null, options);
    }
}
