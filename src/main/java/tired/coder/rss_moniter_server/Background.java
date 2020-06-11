package tired.coder.rss_moniter_server;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
public class Background {

    @GetMapping("/background")
    public String getBackground(){

        try
        {        Path path = Paths.get("background.html");

            List<String> allLines = Files.readAllLines(path, StandardCharsets.UTF_8);
            String fString ="";
            for(String x : allLines)
                fString+=x+"<br/>";

            return fString;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Exception";

    }
}
