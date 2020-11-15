package tired.coder.rss_moniter_server;

import java.io.Serializable;
import java.util.ArrayList;

public class ScraperConfig  implements Serializable {
    ArrayList<String> jobType;
    float minimum;
    float maximum;
    String rssLink;
    String cookie;
    int hours;
    long baseTime;

    boolean awayMode;
    public ScraperConfig(String jobs, float minimum, float maximum, String rssLink, String cookie,boolean awayMode,int hours,long baseTime) {
        ArrayList<String> list = new ArrayList<>();
        for(String x : jobs.split(","))
            list.add(x);
        this.jobType = list;

        this.minimum = minimum;
        this.maximum = maximum;
        this.baseTime = baseTime;

        this.rssLink = rssLink;
        this.cookie = cookie;
        this.awayMode = awayMode;
        this.hours  =hours;

    }
}
