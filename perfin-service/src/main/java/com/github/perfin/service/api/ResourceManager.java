package com.github.perfin.service.api;

import com.github.perfin.model.entity.Currency;
import com.github.perfin.model.entity.Resource;

import java.math.BigDecimal;
import java.util.List;

public interface ResourceManager {

    /**
     * Creates a new resource with currency taken from user defaults.
     *
     * @param name           name of the resource
     * @param initialBalance resource balance before first transaction
     * @return newly created resource
     */
    Resource createResource(String name, BigDecimal initialBalance);

    /**
     * Creates a new resource with the given currency. Only for premium users.
     *
     * @param name           name of the resource
     * @param initialBalance resource balance before first transaction
     * @param currency       resource balance currency
     * @return newly created resource
     */
    Resource createResource(String name, BigDecimal initialBalance, Currency currency);

    /**
     * Updates the existing resource with new name
     *
     * @param id   id of the existing resource
     * @param name new resource name
     * @return updated resource
     * @throws IllegalArgumentException if id does not exist or belong to another user
     */
    Resource updateResource(Long id, String name);

    /**
     * Deletes the existing resource with the given ID
     *
     * @param id id of the existing resource
     * @throws IllegalArgumentException if id does not exist or belong to another user
     */
    void deleteResource(Long id);

    /**
     * Gets all resources of the logged user
     *
     * @return
     */
    List<Resource> getUserResources();

}
