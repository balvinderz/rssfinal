package tired.coder.rss_moniter_server;

public class Greeting
{
    private final long id;
    private final String content;
    public Greeting(long id, String content,String type) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}
