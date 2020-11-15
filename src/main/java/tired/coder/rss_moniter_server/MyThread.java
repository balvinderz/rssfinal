package tired.coder.rss_moniter_server;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.host.Element;
import com.gargoylesoftware.htmlunit.javascript.host.event.MouseEvent;
import com.gargoylesoftware.htmlunit.util.Cookie;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        BufferedWriter br = null;
        PrintWriter pr = null;
        FileWriter fr =null ;
        File file = null;
        WebClient client = new WebClient();
        Common.setupWebClient(client);
        try {
        client.getPage("https://gengo.com/auth/form/login/");
        client.getCookieManager().clearCookies();
        client.getCookieManager().addCookie(new Cookie("gengo.com", "myG_myGSession_", "99d7e089b008a8a8281c6c9813ddd8db97f46ff3"));
        client.getPage("https://gengo.com/t/dashboard/");

            file = new File("thread_"+name+".txt");
            fr = null;
            pr = null;
            br = null;


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

                               try {
                                   if (config.awayMode) {
                                       HtmlPage page = (HtmlPage) client.getCurrentWindow().getEnclosedPage();
                                      DomElement element = page.getElementById("time-countdown");
                                      String content = element.getTextContent();
                                       Pattern pattern = Pattern.compile("\\d+");
                                       Matcher matcher = pattern.matcher(content);
                                       ArrayList<Integer>  matches = new ArrayList<Integer>();
                                       while (matcher.find())
                                           matches.add(Integer.valueOf(content.substring(matcher.start(),matcher.end())));
                                       int hoursLeft = 0;
                                       if(matches.size()==3)
                                       {
                                           hoursLeft= matches.get(0) *24+ matches.get(1);

                                       }
                                       else  if(matches.size()==2)
                                           hoursLeft = matches.get(0);


                                    Date date = new Date(config.baseTime);
                                    Date checkTime  = addHoursToJavaUtilDate(date,hoursLeft);
                                    Date maxTime = addHoursToJavaUtilDate(date,config.hours);
                                    if(checkTime.after(maxTime))
                                    {
                                       continue;
                                    }
                                   }
                               }catch (Exception e)
                               {
                                   print(e.toString());
                               }

                                print(name +" got in with link" + item.link);

                               fr = new FileWriter(file, true);
                               br = new BufferedWriter(fr);
                               pr = new PrintWriter(br);

                               pr.println(getTime()+":"+ " Thread "+name+": Link found:"+item.link);
                               client.getPage(item.link);
                               pr.println(getTime()+":"+" Thread "+name+": Gengo page opened");
                               boolean  foundAnchor = false;
                               boolean foundButton = false;

                               for(int i =0;i<301;i++)
                               {
                                   try {
                                       HtmlPage page2 = (HtmlPage) client.getCurrentWindow().getEnclosedPage();
                                       String currentUrl =page2.getUrl().toString();

                                       DomElement element = page2.getElementById("start_job_button");
                                       element.click();
                                       if(!foundAnchor)
                                       {

                                           pr.println(getTime() + " Thread "+ name+" Found Anchor and clicked on it");
                                           Common.lastLink = item.link;
                                           String secondLink = item.link.replace("?referral=rss","");
                                           secondLink = secondLink.replace("jobs/details","workbench");
                                           client.getPage(secondLink);
                                           client.getPage(currentUrl);

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
                                       HtmlPage page = (HtmlPage) client.getCurrentWindow().getEnclosedPage();
                                       String currentUrl = page.getUrl().toString();

                                       List<DomElement> elements = page.getElementsByTagName("button");

                                       for (DomElement element : elements) {

                                           if (element.getTextContent().contains("Start Translating") || element.getTextContent().contains("Start Editing")) {
                                               {
                                                 element.fireEvent("click");
                                                 element.click();

                                                   if (!foundButton) {
                                                       pr.println(getTime() + " Thread "+ name+" Found Button and clicked on it");
                                                       String secondLink = item.link.replace("?referral=rss","");
                                                       secondLink = secondLink.replace("jobs/details","workbench");
                                                       client.getPage(secondLink);
                                                       client.getPage(currentUrl);

                                                       foundButton = true;
                                                       File f= new File("background.html");
                                                       Common.lastLink = item.link;

                                                       FileWriter fr1 = new FileWriter(f,false);
                                                       BufferedWriter br1 = new BufferedWriter(fr1);
                                                       PrintWriter pr1 = new PrintWriter(br1);
                                                       pr1.write(item.link);
                                                       pr1.close();
                                                       br1.close();;
                                                       fr1.close();

                                                   }

                                                   break;

                                               }
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
        int month = c.get(Calendar.MONTH);
        int  dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int seconds = c.get(Calendar.SECOND);
        int millisecond = c.get(Calendar.MILLISECOND);
        String months,days,hours,minutes,secondss,ms;
        if(month<10)
            months = "0"+ month;
        else
            months = String.valueOf(month);
        if(dayOfMonth<10)
            days = "0"+ dayOfMonth;
        else
            days =String.valueOf(dayOfMonth);
        if(hour<10)
            hours = "0"+hour;
        else
            hours = String.valueOf(hour);
        if(minute<10)
            minutes = "0"+ minute;
        else
            minutes = String.valueOf(minute);
        if(seconds<10)
            secondss = "0"+seconds;
        else
            secondss = String.valueOf(seconds);
        if(millisecond<10)
            ms = "00"+millisecond;
        else if(millisecond<100)
            ms="0"+millisecond;
        else
            ms= String.valueOf(millisecond);
        String date = c.get(Calendar.YEAR)+"-"+months+"-"+days;
        String time =hours+":"+minutes+":"+secondss+":"+ms;
        String t = date+" "+time;
        return t;

    }
    public Date addHoursToJavaUtilDate(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }
}