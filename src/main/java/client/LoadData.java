package client;

import java.util.ArrayList;

import com.google.gson.Gson;

public class LoadData {
    private Model model;
    private ArrayList<?> recipes;
    private RecipeList recipeList;
    private RecipeDetails recipeDetails;
    private String username;
    private Gson gson;

    LoadData(String username, RecipeList recipeList) {
        this.model = new Model();
        this.recipeList = recipeList;
        this.recipeDetails = new RecipeDetails(this.recipeList);
        this.gson = new Gson();
        this.username = username;
    }

    public void retrieveRecipes() {
        String response = model.sendRecipeRetrieveRequest(username);
        recipes = (ArrayList<?>) gson.fromJson(response, ArrayList.class);
    }

    public void populateRecipes() {
        for (int i = 0; i < recipes.size(); i++){
            String recipeString = recipes.get(i).toString();
            System.out.println(recipeString);
            String name = recipeString.substring(recipeString.indexOf("recipe_name=") + 12, recipeString.indexOf("recipe_tag") - 2);
            String tag = recipeString.substring(recipeString.indexOf("recipe_tag=") + 11, recipeString.indexOf("recipe_details") - 2);
            String details = recipeString.substring(recipeString.indexOf("recipe_details=") + 15, recipeString.indexOf("creation_time") - 2);
            String creationTime = recipeString.substring(recipeString.indexOf("creation_time") + 13, recipeString.length() - 1);

            // Recipe recipe = new Recipe(name, tag, details);
            // RecipeDisplay recipeDisplay = new RecipeDisplay(recipeDetails);
            // recipeDisplay.setRecipeDisplayName(recipe);
            // recipeList.getChildren().add(recipeDisplay);
            // recipeList.updateRecipeIndices();
        }
    }
}