package tired.coder.rss_moniter_server;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
public class SaveForm {
    @GetMapping("/saveConfig")


    public String sendForm(@RequestParam( value ="minimum") float minimum,@RequestParam( value ="maximum") float maximum,@RequestParam( value ="jobType") String jobType,@RequestParam( value ="cookie") String cookie,@RequestParam(value = "rssLink")String rssLink,@RequestParam(value = "awayMode") String awayMode,@RequestParam(value ="hours") String hours) {
        System.out.println(rssLink);
        if(!rssLink.contains("https://gengo.com/rss/available_jobs/"))
        return "Invalid rss Link";
        if(cookie.isEmpty())
            return "Cookie is empty";
        if(minimum>maximum)
            return "Minimum cannot be greater than maximum";
        if(awayMode.equals("true") && hours.equals(""))
            return "Invalid time";
        try {
            Date date = new Date();
            long baseTime = date.getTime();

            ScraperConfig config = new ScraperConfig(jobType, minimum, maximum, rssLink, cookie, awayMode.equals("true"), awayMode.equals("true") ? Integer.parseInt(hours) : 0,baseTime);
            Common.setConfig(config);

        }catch (Exception e)
        {
            return e.toString();

        }

        return "Successful";
    }

}
