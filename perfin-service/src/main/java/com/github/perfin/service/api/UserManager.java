package com.github.perfin.service.api;

import com.github.perfin.model.entity.Currency;
import com.github.perfin.model.entity.User;

/**
 * Manages internal user records. Fields like password or user role are stored in LDAP and cannot be changed.
 */
public interface UserManager {

    /**
     * Changes the default currency of the current user.
     *
     * @param currency new currency
     */
    void changeDefaultCurrency(Currency currency);

    /**
     * Gets current logged user.
     *
     * @return logged user
     */
    User getCurrentUser();

}
