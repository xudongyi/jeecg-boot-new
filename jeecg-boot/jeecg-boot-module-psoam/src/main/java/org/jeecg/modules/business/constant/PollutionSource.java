package org.jeecg.modules.business.constant;

public class PollutionSource {

    public static class DataType{

        public final static String REALTIME = "realTime";
        public final static String MINUTE =  "minute";
        public final static String HOUR = "hour";
        public final static String DAY = "day";


    }
    //报表中的dateType
    public static class ReportDateType{

        public final static String YEAR = "year";
        public final static String QUARTERLY =  "quarterly";
        public final static String MONTH = "month";
        public final static String DATE = "date";





    }
    public static class DataFormat{

        public final static String REALTIME = "yyyy-MM-dd HH:mm:ss";
        public final static String MINUTE =  "yyyy-MM-dd HH:mm";
        public final static String HOUR = "yyyy-MM-dd HH";
        public final static String DAY = "yyyy-MM-dd";

    }

}
