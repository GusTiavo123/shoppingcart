package com.gustavo.shoppingcart.service.category;

import com.gustavo.shoppingcart.exceptions.category.CategoryExistsException;
import com.gustavo.shoppingcart.exceptions.category.CategoryNotFoundException;
import com.gustavo.shoppingcart.model.Category;
import com.gustavo.shoppingcart.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public Category getCategoryById(long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category addCategory(Category category) {
        return Optional.of(category).filter(c -> !categoryRepository.existsByName(c.getName()))
                .map(categoryRepository :: save).orElseThrow(() -> new CategoryExistsException(category.getName()+ " already exits"));
    }

    @Override
    public Category updateCategory(Category category, long id) {
        return Optional.ofNullable(getCategoryById(id))
                .map(oldCategory -> {
                    oldCategory.setName(category.getName());
                    return categoryRepository.save(oldCategory);
                }).orElseThrow(() -> new CategoryNotFoundException("Category not found"));
    }

    @Override
    public void deleteCategoryById(long id) {
        categoryRepository.findById(id).ifPresentOrElse(categoryRepository::delete, () -> {
            throw new CategoryNotFoundException("Category not found");
        });
    }
}
