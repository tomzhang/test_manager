package com.xn.performance.api;

import java.util.List;
import java.util.Map;

/**
 * Created by xn056839 on 2017/5/17.
 */
public interface PerformanceReportService {
    Map<String, Object> generateReport(List<String> dbnames);

    Map<String, Object> chartDataAll(Integer id);
    public  Map<String,Object> chartInfluxdbData_start_end(String jmeterPrefix,long starttime,long endtime);

}
