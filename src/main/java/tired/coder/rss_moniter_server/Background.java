package tired.coder.rss_moniter_server;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
public class Background {

    @GetMapping("/background")
    public String getBackground(Model model){


            model.addAttribute("link",Common.lastLink);
            return "background";




    }
}
