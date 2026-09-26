package io.github.pedrohribeiross.lancecerto.support.fixtures;

import io.github.pedrohribeiross.lancecerto.category.Category;

public final class Categories {

    private Categories() {
    }

    public static Category with(String name) {
        return new Category(null, name);
    }

    public static Category any() {
        return with("Categoria");
    }

}
