package com.github.perfin.service.api;

import com.github.perfin.model.entity.Category;
import com.github.perfin.service.dto.PaginatedListWrapper;

/**
 * Manages all categories. User can only manipulate with his own categories.
 */
public interface CategoryManager {

    /**
     * Creates a new category for the logged user
     * 
     * If is category already saved, only its name can be changed
     *
     * @param category category to be saved
     * @return saved category
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
    
    PaginatedListWrapper<Category> getCategories (Integer page, String sortFields, String sortDirections);
    
    Category getCategory(Long id);
}
