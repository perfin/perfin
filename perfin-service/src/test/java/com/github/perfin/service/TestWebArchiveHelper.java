package com.github.perfin.service;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

public class TestWebArchiveHelper {
    
    public static Archive<?> getDeployment() {
        WebArchive war = ShrinkWrap
                .create(WebArchive.class)
                .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
                .addPackages(true, "com.github.perfin.service.api",
                        "com.github.perfin.service.impl",
                        "com.github.perfin.model.entity",
                        "com.github.perfin.service.rest",
                        "com.github.perfin.service.dto",
                        "com.github.perfin.model.util",
                        "org.assertj.core");

        war.addAsLibraries(Maven.resolver().loadPomFromFile("pom.xml")
                .resolve("org.mockito:mockito-all").withTransitivity().asFile());

        war.addAsLibraries(Maven.resolver().loadPomFromFile("pom.xml")
                .resolve("org.apache.commons:commons-lang3").withTransitivity().asFile());

        return war;
    }
}
