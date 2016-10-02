package ca.circumpunct.chefjayjay.chefjayjay;

import android.net.Uri;

import io.realm.RealmObject;

public class ImageLocator extends RealmObject {
    private String image_uri;

    public String getImage_uri() {
        return image_uri;
    }

    public void setImage_uri(String image_uri) {
        this.image_uri = image_uri;
    }
}
