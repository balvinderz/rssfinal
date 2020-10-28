package tired.coder.rss_moniter_server;


import com.gargoylesoftware.css.parser.CSSErrorHandler;
import com.gargoylesoftware.css.parser.CSSException;
import com.gargoylesoftware.css.parser.CSSParseException;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindowEvent;
import com.gargoylesoftware.htmlunit.WebWindowListener;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptErrorListener;
import com.gargoylesoftware.htmlunit.util.Cookie;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;

@RestController

public class ScraperController {
    static void print(Object toPrint) {
        System.out.println(toPrint.toString());
    }
    private final AtomicLong counter = new AtomicLong();
    private static final ThreadLocal<ArrayList<Object>> result = new ThreadLocal<ArrayList<Object>>();

    @GetMapping("/startScraper")
    public Object scraperController()
    {
        ScraperConfig config = Common.getConfig();

        if(config==null)
            return "Invalid configuration";

        if(Common.isRunning())
            return "Scraper already running . Please stop it to run again";

        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);

        final WebClient client = new WebClient();
        Common.setupWebClient(client);
        try {
            client.getPage("https://gengo.com/auth/form/login/");
            print(client.getCookieManager().getCookie("myG_myGSession_"));
            client.getCookieManager().clearCookies();
            client.getCookieManager().addCookie(new Cookie("gengo.com", "myG_myGSession_", "99d7e089b008a8a8281c6c9813ddd8db97f46ff3"));
            client.getPage("https://gengo.com/t/dashboard/");

            String url = client.getCurrentWindow().getEnclosedPage().getUrl().toString();
            if (url
            .contains("login"))
            return "Invalid Cookie";

            Common.start();

            return "Successful";


        }catch (Exception e)
        {
            return "Error occured";

        }



    }

}
