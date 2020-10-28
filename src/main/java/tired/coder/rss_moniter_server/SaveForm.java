package tired.coder.rss_moniter_server;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
public class SaveForm {
    @GetMapping("/saveConfig")


    public String sendForm(@RequestParam( value ="minimum") float minimum,@RequestParam( value ="maximum") float maximum,@RequestParam( value ="jobType") String jobType,@RequestParam( value ="cookie") String cookie,@RequestParam(value = "rssLink")String rssLink) {
        System.out.println(rssLink);
        if(!rssLink.contains("https://gengo.com/rss/available_jobs/"))
        return "Invalid rss Link";
        if(cookie.isEmpty())
            return "Cookie is empty";
        if(minimum>maximum)
            return "Minimum cannot be greater than maximum";
        ScraperConfig config = new ScraperConfig(jobType,minimum,maximum,rssLink,cookie);

        Common.setConfig(config);

        return "Successful";
    }

}
