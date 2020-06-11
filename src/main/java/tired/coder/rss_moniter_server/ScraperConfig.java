package tired.coder.rss_moniter_server;

import java.io.Serializable;
import java.util.ArrayList;

public class ScraperConfig  implements Serializable {
    ArrayList<String> jobType;
    float minimum;
    float maximum;
    String rssLink;
    String cookie;

    public ScraperConfig(String jobs, float minimum, float maximum, String rssLink, String cookie) {
        ArrayList<String> list = new ArrayList<>();
        for(String x : jobs.split(","))
            list.add(x);
        this.jobType = list;

        this.minimum = minimum;
        this.maximum = maximum;
        this.rssLink = rssLink;
        this.cookie = cookie;
    }
}
