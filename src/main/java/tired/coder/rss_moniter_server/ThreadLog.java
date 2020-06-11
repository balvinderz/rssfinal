package tired.coder.rss_moniter_server;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RestController
public class ThreadLog {
    @GetMapping("/threadLogging")
    public String sendForm(@RequestParam( value ="threadNumber") int threadNumber) {
        String number = "";

        if(threadNumber ==1 )
            number="One";
        else if(threadNumber==2)
            number ="Two";
        else if(threadNumber ==3 )
            number ="Three";
        else
            return "Invalid thread number";

        Path path = Paths.get("thread_"+number+".txt");
        try {
            byte[] bytes = Files.readAllBytes(path);

                List<String> allLines = Files.readAllLines(path, StandardCharsets.UTF_8);
            String fString ="";
            Collections.reverse(allLines);
            for(String x : allLines)
                fString+=x+"<br/>";

            return fString;


        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Exception";


    }
}
