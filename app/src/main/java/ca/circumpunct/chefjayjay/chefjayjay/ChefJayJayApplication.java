package ca.circumpunct.chefjayjay.chefjayjay;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class ChefJayJayApplication extends Application {
    @Override
    public void onCreate(){
        super.onCreate();

        Realm.init(this);

        RealmConfiguration config = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(config);

        Realm realm = Realm.getDefaultInstance();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.deleteAll();
            }
        });
    }
}
