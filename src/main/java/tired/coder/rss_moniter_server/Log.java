package tired.coder.rss_moniter_server;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RestController
public class Log {
    @GetMapping("/log")
    public String getAllLog()
    {

        Path path1 = Paths.get("thread_"+"One.txt");
        Path path2 = Paths.get("thread_"+"Two.txt");
        Path path3 = Paths.get("thread_"+"Three.txt");
        ArrayList<String> lines = new ArrayList<>();

        try {
            byte[] bytes = Files.readAllBytes(path1);
            List<String> allLines = Files.readAllLines(path1, StandardCharsets.UTF_8);
            lines.addAll(allLines);
             allLines = Files.readAllLines(path2, StandardCharsets.UTF_8);
            lines.addAll(allLines);

          allLines = Files.readAllLines(path3, StandardCharsets.UTF_8);
            lines.addAll(allLines);
            Collections.sort(lines);
            Collections.reverse(lines);
            String fString ="";
            for(String x : lines)
                fString+=x+"<br/>";

            return fString;


        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Exception";

    }
}
