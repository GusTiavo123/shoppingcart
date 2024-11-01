package com.gustavo.shoppingcart.service.category;

import com.gustavo.shoppingcart.model.Category;

import java.util.List;

public interface ICategoryService {
    Category getCategoryById(long id);
    Category getCategoryByName(String name);
    List<Category> getAllCategories();

    Category addCategory(Category category);
    Category updateCategory(Category category, long id);
    void deleteCategoryById(long id);
}
