package tired.coder.rss_moniter_server;

class RssItem {
    String link;
    float amount;
    String type;

    public RssItem(String link, float amount, String type) {
        this.link = link;
        this.amount = amount;
        this.type = type;
    }

}