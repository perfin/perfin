package com.github.perfin.service.impl;

import com.github.perfin.service.api.StatisticsProvider;
import com.github.perfin.service.dto.Statistics;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Simple wrapper, which acts as a REST provider for statistics.
 */
@ApplicationPath("/service")
@Path("statistics")
@Produces(MediaType.APPLICATION_JSON)
public class StatisticsRestProvider {

    @Inject
    private StatisticsProvider statisticsProvider;

    @GET
    public Statistics getStatisticsByDateRange(@QueryParam("startDate") String startDate, @QueryParam("endDate") String endDate) {
        LocalDate start;
        LocalDate end;
        try {
            start = LocalDate.parse(startDate, DateTimeFormatter.BASIC_ISO_DATE);
            end = LocalDate.parse(endDate, DateTimeFormatter.BASIC_ISO_DATE);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Date format is wrong!", ex);
        }

        return statisticsProvider.getStatisticsByDateRange(start, end);
    }

}
