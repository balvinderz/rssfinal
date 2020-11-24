package tired.coder.rss_moniter_server;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@RestController
public class SaveForm {
    @GetMapping("/saveConfig")


    public String sendForm(@RequestParam( value ="minimum") float minimum,@RequestParam( value ="maximum") float maximum,@RequestParam( value ="jobType") String jobType,@RequestParam( value ="cookie") String cookie,@RequestParam(value = "rssLink")String rssLink,@RequestParam(value = "awayMode") String awayMode,@RequestParam(value ="startTime") String startTime,@RequestParam(value ="endTime") String endTime ) {
        System.out.println(rssLink);
        if(!rssLink.contains("https://gengo.com/rss/available_jobs/"))
        return "Invalid rss Link";
        if(cookie.isEmpty())
            return "Cookie is empty";
        if(minimum>maximum)
            return "Minimum cannot be greater than maximum";
        if(awayMode.equals("true") && (startTime.equals("") || endTime.equals("")))
            return "Invalid time";
        try {

            ScraperConfig config = new ScraperConfig(jobType, minimum, maximum, rssLink, cookie, awayMode.equals("true") ,startTime,endTime);
            Common.setConfig(config);

        }catch (Exception e)
        {
            return e.toString();

        }

        return "Successful";
    }

}
