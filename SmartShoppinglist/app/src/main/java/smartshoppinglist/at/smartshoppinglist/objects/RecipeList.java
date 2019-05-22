package smartshoppinglist.at.smartshoppinglist.objects;

import java.util.ArrayList;
import java.util.List;

import smartshoppinglist.at.smartshoppinglist.localsave.Save;

public class RecipeList {
    private List<Recipe> recipes;

    public RecipeList() {
        recipes = new ArrayList<>();
    }

    public void addRecipe(Recipe recipe){
        if(findRecipeByName(recipe.getName()) == null) {
            recipes.add(recipe);
            setChanges();
        }
    }
    public Recipe findRecipeByName(String name){
        for (Recipe recipe:recipes) {
            if(recipe.getName().equals(name)) return recipe;
        }
        return null;
    }
    public void removeRecipe(Recipe recipe){
        recipes.remove(recipe);
        setChanges();
    }
    public Recipe[] getRecipes(){
        return recipes.toArray(new Recipe[0]);
    }
    public String[] getNames(){
        List<String> result = new ArrayList<>();
        for (Recipe recipe:recipes) {
            result.add(recipe.getName());
        }
        return result.toArray(new String[0]);
    }
    protected void setChanges(){
        Save.save(this);
    }
}
