package tired.coder.rss_moniter_server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class ScraperConfig  implements Serializable {
        ArrayList<String> jobType;
    float minimum;
    float maximum;
    String rssLink;
    String cookie;

    boolean awayMode;
    Date startTime;
    Date endTime;
    String start;
    String end;

    public ScraperConfig(String jobs, float minimum, float maximum, String rssLink, String cookie, boolean awayMode, Date startTime, Date endTime,String start,String end) {
        ArrayList<String> list = new ArrayList<>();
        for(String x : jobs.split(","))
            list.add(x);
        this.jobType = list;
        this.startTime = startTime;
        this.endTime = endTime;

        this.minimum = minimum;
        this.maximum = maximum;
        this.start  = start;
        this.end= end;

        this.rssLink = rssLink;
        this.cookie = cookie;
        this.awayMode = awayMode;

    }
}
