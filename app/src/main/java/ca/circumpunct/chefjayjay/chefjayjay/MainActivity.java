package ca.circumpunct.chefjayjay.chefjayjay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    private Realm realm;
    private ArrayList<Recipe> recipesAdded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RealmResults<Recipe> recipeResults;

        recipesAdded = new ArrayList<>();

        realm = Realm.getDefaultInstance();

        recipeResults = realm.where(Recipe.class).findAll();

        fill_recipes_list(recipeResults);

        recipeResults.addChangeListener(new RealmChangeListener<RealmResults<Recipe>>() {
            @Override
            public void onChange(RealmResults<Recipe> element) {
                fill_recipes_list(element);
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();

        RealmResults<Recipe> recipeResults = realm.where(Recipe.class).findAll();

        fill_recipes_list(recipeResults);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        realm.close();
    }

    public void createNewRecipe(View view){
        Intent intent = new Intent(this, CreateNewRecipeActivity.class);
        startActivity(intent);
    }

    private void fill_recipes_list(RealmResults<Recipe> recipes){
        Log.e("Main", "WTF");
        for(final Recipe recipe : recipes){
            if(!recipesAdded.contains(recipe)) {
                Button button = new Button(this);
                button.setText(recipe.getRecipe_name());
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, RecipeViewActivity.class);
                        intent.putExtra("recipe_id", recipe.getId());
                        startActivity(intent);
                    }
                });

                recipesAdded.add(recipe);
                ViewGroup layout = (ViewGroup) findViewById(R.id.layout_recipes_list);
                layout.addView(button);
            }
        }

    }
}
