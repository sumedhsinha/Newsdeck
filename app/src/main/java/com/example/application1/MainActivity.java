package com.example.application1;

//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//
//public class MainActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//    }
//}

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity {

    private ListView includeCities;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        includeCities = findViewById(R.id.includeCities);

        ArrayList<String> cities = new ArrayList<>();

        try{
            //File inputFile = new File("rssfeed.xml"); //local access to downloaded xml
            String inputFile = "https://timesofindia.indiatimes.com/rssfeedstopstories.cms"; //link for rss feed

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document readFile = builder.parse(inputFile);
            readFile.getDocumentElement().normalize();
            NodeList Nodelist = readFile.getElementsByTagName("item"); //this tag may vary from site to site

            //need to store the links and tags for image url for sites, perhaps in a HashTable

            System.out.println("+++++++++++++++");
            for (int y = 0; y < Nodelist.getLength(); y++){
                Node getNode = Nodelist.item(y);
                Element getElement = (Element) getNode;
                cities.add("test2");
                //System.out.println(getElement.getElementsByTagName("title").item(0).getTextContent());
                //System.out.println(getElement.getElementsByTagName("link").item(0).getTextContent());

                NodeList enclosures = getElement.getElementsByTagName("enclosure"); //this tag may vary from site to site
                if (enclosures.getLength() > 0) {

                    Element enclosure = (Element) enclosures.item(0);
                    String imageUrl = enclosure.getAttribute("url");
                    //System.out.println("Image URL: " + imageUrl);
                }

                //System.out.println("----------------");
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }

        ArrayAdapter<String> citiesAdaptor = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                cities
        );

        includeCities.setAdapter(citiesAdaptor);

    }
}