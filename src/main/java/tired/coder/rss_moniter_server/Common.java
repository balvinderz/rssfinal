package tired.coder.rss_moniter_server;

import com.gargoylesoftware.css.parser.CSSErrorHandler;
import com.gargoylesoftware.css.parser.CSSException;
import com.gargoylesoftware.css.parser.CSSParseException;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindowEvent;
import com.gargoylesoftware.htmlunit.WebWindowListener;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptErrorListener;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Common {
    private   static boolean isRunning =false;
    public static  ScraperConfig config;
    private static  final ArrayList<MyThread> myThreads = new ArrayList<>();
    private static  final LinkedHashMap<String,Integer> linksFound = new LinkedHashMap<>();
    public  static String lastLink = "";

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
    public static  void removeNewLink(String link)
    {
        synchronized (linksFound)
        {
            linksFound.remove(link);
            link = null;
        }
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
    public static  void setupWebClient(WebClient client)
    {

        client.getOptions().setCssEnabled(false);
        client.getOptions().setCssEnabled(false);
        client.getOptions().setThrowExceptionOnScriptError(false);
        client.setJavaScriptErrorListener(new JavaScriptErrorListener() {

            @Override
            public void timeoutError(HtmlPage arg0, long arg1, long arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void scriptException(HtmlPage arg0, ScriptException arg1) {
                // TODO Auto-generated method stub

            }

            @Override
            public void malformedScriptURL(HtmlPage arg0, String arg1, MalformedURLException arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void loadScriptError(HtmlPage arg0, URL arg1, Exception arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void warn(String message, String sourceName, int line, String lineSource, int lineOffset) {

            }
        });
        client.setCssErrorHandler(new CSSErrorHandler() {
            @Override
            public void warning(CSSParseException exception) throws CSSException {

            }

            @Override
            public void error(CSSParseException exception) throws CSSException {

            }

            @Override
            public void fatalError(CSSParseException exception) throws CSSException {

            }
        });
        client.getOptions().setJavaScriptEnabled(true);
        client.addWebWindowListener(new WebWindowListener() {
            @Override
            public void webWindowOpened(WebWindowEvent event) {

            }

            @Override
            public void webWindowContentChanged(WebWindowEvent event) {

            }

            @Override
            public void webWindowClosed(WebWindowEvent event) {

            }
        });

    }

}
