package com.github.perfin.service.batch;

import java.util.Properties;

import javax.batch.runtime.BatchRuntime;
import javax.ejb.Schedule;
import javax.inject.Singleton;

@Singleton
public class RatesDownloaderSchedule {

    @Schedule(hour = "0", minute = "1", second = "0")
    public void myJob() {
        BatchRuntime.getJobOperator().start("RatesDownloader", new Properties());
    }

}
