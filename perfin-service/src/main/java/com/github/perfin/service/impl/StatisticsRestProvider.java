package com.github.perfin.service.impl;

import com.github.perfin.service.api.StatisticsProvider;
import com.github.perfin.service.dto.Statistics;

import javax.inject.Inject;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.time.LocalDate;

/**
 * Simple wrapper, which acts as a REST provider for statistics.
 */
@ApplicationPath("/service")
@Path("statistics")
@Produces(MediaType.APPLICATION_JSON)
public class StatisticsRestProvider {

    @Inject
    private StatisticsProvider statisticsProvider;

    public Statistics getStatisticsByDateRange(LocalDate startDate, LocalDate endDate) {
        Statistics statistics = null;
        return statistics;
    }

}
