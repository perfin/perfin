package com.github.perfin.service.api;

import com.github.perfin.model.entity.Resource;
import com.github.perfin.service.dto.PaginatedListWrapper;

public interface ResourceManager {

    /**
     * Creates a new resource with the given currency. Only for premium users.
     *
     * @param name           name of the resource
     * @param initialBalance resource balance before first transaction
     * @param currency       resource balance currency
     * @return newly created resource
     */
    Resource saveResource(Resource resource);

    /**
     * Deletes the existing resource with the given ID
     *
     * @param id id of the existing resource
     * @throws IllegalArgumentException if id does not exist or belong to another user
     */
    void deleteResource(Long id);
    
    PaginatedListWrapper<Resource> getUserResources(Integer page, String sortFields, String sortDirections);

}
