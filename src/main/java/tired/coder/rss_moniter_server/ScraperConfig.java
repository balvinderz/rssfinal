package tired.coder.rss_moniter_server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ScraperConfig  implements Serializable {
        ArrayList<String> jobType;
    float minimum;
    float maximum;
    String rssLink;
    String cookie;

    boolean awayMode;
    Date startTime;

    public Date getStartTime() {
        Date date = new Date();
        date.setMinutes(Integer.parseInt(start.split(":")[1]));
        date.setHours(Integer.parseInt(start.split(":")[0]));
        return date;

    }

    public Date getEndTime() {
        Date date = new Date();
        date.setMinutes(Integer.parseInt(end.split(":")[1]));
        date.setHours(Integer.parseInt(end.split(":")[0]));
        Date startTime = getStartTime();
        if(startTime.getTime()>date.getTime()) {
            date= addHoursToJavaUtilDate(date,24);
        }

        return date;
    }



    Date endTime;
    String start;
    String end;
    private Date addHoursToJavaUtilDate(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);

        return calendar.getTime();
    }
    public ScraperConfig(String jobs, float minimum, float maximum, String rssLink, String cookie, boolean awayMode,String start,String end) {
        ArrayList<String> list = new ArrayList<>();
        for(String x : jobs.split(","))
            list.add(x);
        this.jobType = list;

        this.minimum = minimum;
        this.maximum = maximum;
        this.start  = start;
        this.end= end;

        this.rssLink = rssLink;
        this.cookie = cookie;
        this.awayMode = awayMode;

    }
}
