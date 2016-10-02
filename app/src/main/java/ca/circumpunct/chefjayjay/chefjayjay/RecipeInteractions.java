package ca.circumpunct.chefjayjay.chefjayjay;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RecipeInteractions extends RealmObject{
    @PrimaryKey
    private long id;

    private RealmList<ImageLocator> images;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public RealmList<ImageLocator> getImages() {
        return images;
    }

    public void setImages(RealmList<ImageLocator> images) {
        this.images = images;
    }
}
