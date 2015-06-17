package com.github.perfin.service.api;

import com.github.perfin.model.entity.Category;
import com.github.perfin.service.dto.PaginatedListWrapper;

import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

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
    Category saveCategory(Category category);

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
    List<Category> getUserCategories(Integer userId);

    
    PaginatedListWrapper<Category> getCategories (Integer userId, Integer page, String sortFields, String sortDirections);
}
