package tired.coder.rss_moniter_server;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.io.*;
import java.util.*;

public class MyThread extends Thread {
    static void print(Object toPrint) {
        System.out.println(toPrint.toString());
    }

    String name;
    Thread t;
    ScraperConfig config;

    MyThread(String thread,ScraperConfig config) {
        this.config = config;

        name = thread;
        t = new Thread(this, name);
        System.out.println("New thread: " + t);
        t.start();
    }
    boolean shouldRun = true;
    public void stopThread()
    {
        shouldRun = false;


    }

    public void run() {
        int count = 0;


        WebDriver driver = new HtmlUnitDriver();
        driver.get("https://gengo.com/");

        driver.manage().deleteAllCookies();
        driver.manage().addCookie(new Cookie("myG_myGSession_",config.cookie));
        driver.get("https://gengo.com/t/dashboard/");
        File file = new File("thread_"+name+".txt");
        FileWriter fr = null;
        PrintWriter pr = null;
        BufferedWriter br = null;


        try {
            fr = new FileWriter(file, true);
            br = new BufferedWriter(fr);
            pr = new PrintWriter(br);

            pr.println((getTime()+":"+" Thread "+name+" : Succesffuly logged in to gengo.com"));
            pr.close();
            br.close();
            fr.close();;
        } catch (IOException e) {
            e.printStackTrace();
            print("some error");

        }
        final OkHttpClient httpClient = new OkHttpClient();

        String link = config.rssLink;
        Request request = new Request.Builder()
                .url(link)
                .build();
                while (shouldRun) {

                    try {

                Call call = httpClient.newCall(request);
                Response response = call.execute();

                String res = response.body().string();
                count += 1;

                print("Executed " + name + ":" + count);

                if (res.contains("<item")) {

                    ArrayList<RssItem> items = RssFeedParser.parse(res);
                   items =  filterRssList(config.jobType,config.minimum,config.maximum,items);

                   if(items.size()>0) {
                       for(RssItem item : items)
                       {
                           if(Common.foundNewLink(item.link))
                           {

                                print(name +" got in with link" + item.link);

                               fr = new FileWriter(file, true);
                               br = new BufferedWriter(fr);
                               pr = new PrintWriter(br);

                               pr.println(getTime()+":"+ " Thread "+name+": Link found:"+item.link);
                               driver.get(item.link);
                               pr.println(getTime()+":"+" Thread "+name+": Gengo page opened");
                               boolean  foundAnchor = false;
                               boolean foundButton = false;

                               for(int i =0;i<301;i++)
                               {
                                   try {
                                       WebElement element = driver.findElement(By.id("start_job_button"));
                                       element.click();
                                       if(!foundAnchor)
                                       {

                                           pr.println(getTime() + " Thread "+ name+" Found Anchor and clicked on it");
                                           foundAnchor = true;
                                           File f= new File("background.html");
                                           FileWriter fr1 = new FileWriter(f,false);
                                           BufferedWriter br1 = new BufferedWriter(fr1);
                                           PrintWriter pr1 = new PrintWriter(br1);
                                           pr1.write(item.link);
                                           pr1.close();
                                           br1.close();;
                                           fr1.close();
                                       }
                                       Thread.sleep(25);
                                   }catch (Exception e)
                                   {
                                       List<WebElement> elements = driver.findElements(By.tagName("button"));

                                       for (WebElement element : elements) {

                                           if (element.getText().contains("Start Translating") || element.getText().contains("Start Editing")) {
                                               {

                                                   if (!foundButton) {
                                                       pr.println(getTime() + " Thread "+ name+" Found Button and clicked on it");

                                                       foundButton = true;
                                                       File f= new File("background.html");
                                                       FileWriter fr1 = new FileWriter(f,false);
                                                       BufferedWriter br1 = new BufferedWriter(fr1);
                                                       PrintWriter pr1 = new PrintWriter(br1);
                                                       pr1.write(item.link);
                                                       pr1.close();
                                                       br1.close();;
                                                       fr1.close();

                                                   }

                                                   element.click();
                                               }
                                               break;
                                           }
                                       }
                                      Thread.sleep(25);
                                   }
                               }
                               if(!foundAnchor && !foundButton)
                                   pr.println(getTime()+"Not found button or anchor");
                               pr.close();
                               fr.close();
                               br.close();



                           }

                       }
                       call =null;
                       response = null;

                   }
                }
                Thread.sleep(100);
            } catch (Exception e) {

                System.out.println(name + "Interrupted");

            }
        }
                try{

                    if (pr != null) {
                        pr.close();
                    }
                    if (br != null) {
                        br.close();
                    }
                    if (fr != null) {
                        fr.close();
                    }
                    ;


                }catch (Exception e)
                {
                    e.printStackTrace();
                }
        System.out.println("I Stopped : "+name);

    }

    private static ArrayList<RssItem> filterRssList(ArrayList<String> type, float min, float max, ArrayList<RssItem> rssItems) {

        ArrayList<RssItem> filteredList = new ArrayList<>();
        for(RssItem item : rssItems)
        {
            if(type.contains(item.type) && item.amount>=min && item.amount<=max)
                filteredList.add(item);
        }



        filteredList.sort(new SortByPrice());


        return filteredList;

    }
    String getTime(){
        Calendar c = Calendar.getInstance();

        c.setTimeInMillis(System.currentTimeMillis());

        String date = c.get(Calendar.YEAR)+"-"+c.get(Calendar.MONTH)+"-"+c.get(Calendar.DAY_OF_MONTH);
        String time = c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND)+":"+c.get(Calendar.MILLISECOND);
        String t = date+" "+time;
        return t;

    }

}