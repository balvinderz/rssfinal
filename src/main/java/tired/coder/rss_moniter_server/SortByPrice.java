package tired.coder.rss_moniter_server;

import java.util.Comparator;

public class SortByPrice implements Comparator<RssItem>
{
    public int compare(RssItem a,RssItem b)
    {
        return String.valueOf(b.amount).compareTo(String.valueOf(a.amount));



    }
}