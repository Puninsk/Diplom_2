package project;

import java.util.List;

public class OrdersData {

    private List<String> ingredients;
    public OrdersData() {
    }
    public OrdersData(List<String> ingredients) {
        this.ingredients = ingredients;
    }
    public List<String> getIngredients() {
        return ingredients;
    }
    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public static OrdersData getIngredientsList() {
        List<String> list = List.of("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa72", "61c0c5a71d1f82001bdaaa73", "61c0c5a71d1f82001bdaaa70", "61c0c5a71d1f82001bdaaa79");
        return new OrdersData(list);
    }

    public static OrdersData getInvalidIngredientsList() {
        List<String> list = List.of("invalidIngredientHash", "233253532nkjo34434o");
        return new OrdersData(list);
    }
}
