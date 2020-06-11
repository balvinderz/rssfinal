package tired.coder.rss_moniter_server;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Common {
    private   static boolean isRunning =false;
    public static  ScraperConfig config;
    private static  final ArrayList<MyThread> myThreads = new ArrayList<>();
    private static  final HashMap<String,Integer> linksFound = new HashMap<>();

    public static  boolean isRunning()
    {
        return isRunning;

    }
    public static String stop(){
        if(isRunning) {
            isRunning = false;
            for (MyThread t : myThreads)
                t.stopThread();;

            myThreads.clear();
            return "Stopped Scraper Successfully";

        }
        config = null;

        return "Scraper was not running";

    }
    public static  ScraperConfig getConfig()  {
        if (config == null) {
            try {
                FileInputStream file = new FileInputStream("./serverConfig.txt");
                ObjectInputStream in = new ObjectInputStream(file);

                // Method for deserialization of object
                config = (ScraperConfig) in.readObject();
                in.close();
                file.close();
            }
            catch (Exception e)
            {
                config =null;

            }

    }
       return config;

    }
    public static  void start(){

        isRunning= true;
        myThreads.add(new MyThread("One",config));
        myThreads.add(new MyThread("Two",config));
        myThreads.add(new MyThread("Three",config));

    }

    public static void setConfig(ScraperConfig configuration) {
        FileOutputStream file = null;
        try {
            File f = new File("serverConfig.txt","w");

            file = new FileOutputStream("serverConfig.txt");
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(configuration);
            out.close();
            file.close();
        } catch (Exception e) {
                System.out.println("error aya");

            e.printStackTrace();
        }
        config = configuration;

    }
    public static boolean foundNewLink(String link)
    {

        synchronized (linksFound)
        {
            if(linksFound.containsKey(link))
                return false;

            linksFound.put(link,1);

            return true;

        }
    }

}
