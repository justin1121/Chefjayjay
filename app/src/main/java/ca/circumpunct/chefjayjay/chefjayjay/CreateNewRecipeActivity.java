package ca.circumpunct.chefjayjay.chefjayjay;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import io.realm.Realm;

public class CreateNewRecipeActivity extends AppCompatActivity {
    private Recipe recipe;
    private Realm realm;
    private int cookingsteps_count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_recipe);

        realm = Realm.getDefaultInstance();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        realm.close();
    }

    public void addRecipeName(View view){
        ViewGroup layout = (ViewGroup) findViewById(R.id.layout_add_recipe_name);

        EditText editText = (EditText) findViewById(R.id.recipe_name);
        final String recipe_name = editText.getText().toString();

        layout.removeView(findViewById(R.id.add_name));
        layout.removeView(editText);

        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText(recipe_name);

        layout.addView(textView);

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                int next_id;

                try {
                    next_id = realm.where(Recipe.class).max("id").intValue() + 1;
                } catch(NullPointerException ex) {
                    next_id = 0;
                }

                recipe = realm.createObject(Recipe.class, next_id);
                recipe.setRecipe_name(recipe_name);
            }
        });

        showAddIngreditentsList();
    }

    private void showAddIngreditentsList(){
        ViewGroup layout = (ViewGroup) findViewById(R.id.scroll_recipe_create);

        layout.setVisibility(ScrollView.VISIBLE);
    }

    public void addIngredient(View view){
        ViewGroup layout = (ViewGroup) findViewById(R.id.layout_add_ingredients);

        EditText editText = (EditText) findViewById(R.id.ingredient_name);
        final String ingredient_name = editText.getText().toString();
        editText.setText("");

        Realm realm = Realm.getDefaultInstance();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Ingredient ingredient = realm.createObject(Ingredient.class);

                ingredient.ingredient_name = ingredient_name;

                recipe.getIngredients().add(ingredient);
            }
        });

        TextView textView = new TextView(this);
        textView.setTextSize(20);
        textView.setText("- " + ingredient_name);

        layout.addView(textView);
    }

    public void addCookingStep(View view){
        ViewGroup layout = (ViewGroup) findViewById(R.id.layout_add_cooking_steps);

        EditText editText = (EditText) findViewById(R.id.cooking_step);
        final String desc = editText.getText().toString();
        editText.setText("");

        Realm realm = Realm.getDefaultInstance();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                CookingStep cooking_step = realm.createObject(CookingStep.class);

                cooking_step.desc = desc;

                recipe.getCooking_steps().add(cooking_step);
            }
        });

        TextView textView = new TextView(this);
        textView.setTextSize(20);
        textView.setText(cookingsteps_count + ". " + desc);

        layout.addView(textView);
        cookingsteps_count++;
    }
}
