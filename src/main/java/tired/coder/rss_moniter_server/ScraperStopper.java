package tired.coder.rss_moniter_server;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScraperStopper {
    @GetMapping("/stopScraper")
    public String scraperStopper()
    {
        return Common.stop();

    }
}
