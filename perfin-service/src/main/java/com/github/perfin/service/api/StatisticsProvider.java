package com.github.perfin.service.api;

import com.github.perfin.service.dto.Statistics;

import java.time.LocalDate;

public interface StatisticsProvider {

    /**
     * Calculates statistics for transactions of current user within the given date range.
     *
     * @param startDate first day of the date range
     * @param endDate   last day of the date range
     * @return statistics for the given date range
     */
    Statistics getStatisticsByDateRange(LocalDate startDate, LocalDate endDate);

}
