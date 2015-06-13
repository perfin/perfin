package com.github.perfin.service.api;

import com.github.perfin.model.entity.Category;

import java.util.List;

/**
 * Manages all categories. User can only manipulate with his own categories.
 */
public interface CategoryManager {

    /**
     * Creates a new category for the logged user
     *
     * @param name category name
     * @return newly created category
     * @throws IllegalArgumentException if name is empty
     */
    Category createCategory(String name);

    /**
     * Updates the existing category
     *
     * @param id   id of the existing category
     * @param name new category name
     * @return updated category record
     * @throws IllegalArgumentException if id does not exist or it is someone else's category
     */
    Category updateCategory(Long id, String name);

    /**
     * Deletes the existing category
     *
     * @param id id of the existing category
     * @throws IllegalArgumentException if id does not exist, it is someone else's category or there are references
     */
    void deleteCategory(Long id);

    /**
     * Gets all categories of the logged user
     *
     * @return list of all user categories
     */
    List<Category> getUserCategories();

}
