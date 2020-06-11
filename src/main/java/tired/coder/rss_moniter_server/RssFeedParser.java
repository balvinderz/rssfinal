package tired.coder.rss_moniter_server;//import com.gargoylesoftware.htmlunit.javascript.NamedNodeMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RssFeedParser {
    static void print(Object  toPrint)
    {
        System.out.println(toPrint.toString());
    }
    static ArrayList<RssItem> parse(String xml)
    {            ArrayList<RssItem> filteredList;

        try {

            String str =xml;

            //print(str);

            DocumentBuilderFactory factory =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            ByteArrayInputStream input = new ByteArrayInputStream(
                    str.toString().getBytes("UTF-8"));
            Document doc = builder.parse(input);
            NodeList x = doc.getElementsByTagName("item");
            ArrayList<RssItem> rssItems = new ArrayList<>();

            for(int i =0;i<x.getLength(); i++)
            {
                Node nNode = x.item(i);
                if(nNode.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element eElement = (Element) nNode;
                    String link =  eElement.getElementsByTagName("link").item(0).getTextContent();
                    String title = eElement.getElementsByTagName("title").item(0).getTextContent();
                    Pattern typePattern = Pattern.compile("\\(\\w*\\)");
                    Matcher m = typePattern.matcher(title);
                    m.find();
                    Pattern pricePattern  = Pattern.compile("\\$\\d*\\.\\d*");
                    Matcher p = pricePattern.matcher(title);
                    p.find();
                    float price = Float.parseFloat(title.substring(p.start()+1,p.end()));
                    String type = title.substring(m.start()+1,m.end()-1);
                    rssItems.add(new RssItem(link,price,type));
                }

            }

            String type = "Edit";
            float min = 0;
            float max =4.5f;

            //filteredList = filterRssList(type,min,max,rssItems);
            filteredList = new ArrayList<>();
            filteredList.addAll(rssItems);

        } catch (Exception e) {
            filteredList = new ArrayList<>();

            e.printStackTrace();
        }
        return filteredList;

    }
    public  static  void main(String[] args)
    {
        //File f = new File("E:\\rssbot\\src\\main\\java\\feed_example.txt");



    }


}
