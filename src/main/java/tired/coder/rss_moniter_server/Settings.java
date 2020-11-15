package tired.coder.rss_moniter_server;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Scanner;

@Controller
public class Settings {
    @GetMapping("/settings")
    public String settings(Model model) {
        ScraperConfig config = Common.getConfig();

        if(config ==null)
        {
            model.addAttribute("jobType","");
            model.addAttribute("minimum",0.0f);
            model.addAttribute("maximum",0.0f );
            model.addAttribute("Pro",true);
            model.addAttribute("Standard",true);
            model.addAttribute("Edit",true);
            model.addAttribute("awayMode",false);
            model.addAttribute("hours","");
            model.addAttribute("rssLink","");
            model.addAttribute("cookie","");

        }
        else {
            model.addAttribute("jobType", config.jobType);
            model.addAttribute("awayMode",config.awayMode);
            model.addAttribute("hours",config.hours);
            model.addAttribute("Pro", config.jobType.contains("Pro"));
            model.addAttribute("Standard", config.jobType.contains("Standard"));
            model.addAttribute("Edit", config.jobType.contains("Edit"));
            model.addAttribute("minimum", config.minimum);
            model.addAttribute("maximum", config.maximum);
            model.addAttribute("cookie", config.cookie);
            model.addAttribute("rssLink", config.rssLink);
        }
        if(Common.isRunning())
        model.addAttribute("running","Running");
        else
            model.addAttribute("running","Not Running");

        return "settings";
    }
}
