package com.github.perfin.service;

import java.util.concurrent.Callable;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RunAs;
import javax.ejb.Stateless;

@Stateless
@RunAs("admin")
@PermitAll
public class AdminCaller {
    public <V> V call(Callable<V> callable) throws Exception {
        return callable.call();
    }
}
