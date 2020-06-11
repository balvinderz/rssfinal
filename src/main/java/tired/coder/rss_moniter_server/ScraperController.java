package tired.coder.rss_moniter_server;


import org.openqa.selenium.Cookie;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

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


        HtmlUnitDriver driver = new HtmlUnitDriver();
        driver.get("https://gengo.com/");
        driver.manage().deleteAllCookies();
        driver.manage().addCookie(new Cookie("myG_myGSession_",config.cookie));
        driver.get("https://gengo.com/t/dashboard/");
        print(driver.getCurrentUrl());
        if(driver.getCurrentUrl().contains("login"))
            return "Invalid Cookie";
        Common.start();

           return "Successful";






    }

}
