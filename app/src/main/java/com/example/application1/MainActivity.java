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

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.nio.channels.AsynchronousByteChannel;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

//public class MainActivity extends AppCompatActivity {
//
//    private ListView includeCities;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        includeCities = findViewById(R.id.includeCities);
//
//        ArrayList<String> cities = new ArrayList<>();
//
////        cities.add("test2");
////        cities.add("test2");
//
//                try{
//                    //File inputFile = new File("rssfeed.xml"); //local access to downloaded xml
//                    String inputFile = "https://timesofindia.indiatimes.com/rssfeedstopstories.cms"; //link for rss feed
//
//                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//                    DocumentBuilder builder = factory.newDocumentBuilder();
//                    Document readFile = builder.parse(inputFile);
//                    readFile.getDocumentElement().normalize();
//                    NodeList Nodelist = readFile.getElementsByTagName("item"); //this tag may vary from site to site
//
//                    //need to store the links and tags for image url for sites, perhaps in a HashTable
//
//                    //System.out.println("+++++++++++++++");
//                    for (int y = 0; y < Nodelist.getLength(); y++){
//                        Node getNode = Nodelist.item(y);
//                        Element getElement = (Element) getNode;
//                        cities.add("test2");
//                        //System.out.println(getElement.getElementsByTagName("title").item(0).getTextContent());
//                        //System.out.println(getElement.getElementsByTagName("link").item(0).getTextContent());
//
//                        NodeList enclosures = getElement.getElementsByTagName("enclosure"); //this tag may vary from site to site
//                        if (enclosures.getLength() > 0) {
//
//                            Element enclosure = (Element) enclosures.item(0);
//                            String imageUrl = enclosure.getAttribute("url");
//                            //System.out.println("Image URL: " + imageUrl);
//                        }
//
//                        //System.out.println("----------------");
//                    }
//
//                }
//                catch(Exception e){
//                    e.printStackTrace();
//                }
//
//        ArrayAdapter<String> citiesAdaptor = new ArrayAdapter<String>(
//                this,
//                android.R.layout.simple_list_item_1,
//                cities
//        );
//
//        includeCities.setAdapter(citiesAdaptor);
//
//    }
//}

//public class MainActivity extends AppCompatActivity {
//
//    private ListView includeCities;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        includeCities = findViewById(R.id.includeCities);
//
//        ArrayList<String> contentList = new ArrayList<>();
//
//        // Perform network operations on a background thread
//        AsyncTask<Void, Void, Void> fetch = new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected Void doInBackground(Void... voids) {
//                try {
//                    String urlString = "https://timesofindia.indiatimes.com/rssfeedstopstories.cms";
//
//                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//                    DocumentBuilder builder = factory.newDocumentBuilder();
//                    Document readFile = builder.parse(new InputSource(new URL(urlString).openStream()));
//                    readFile.getDocumentElement().normalize();
//                    NodeList nodeList = readFile.getElementsByTagName("item");
//
//                    for (int y = 0; y < nodeList.getLength(); y++) {
//                        Node getNode = nodeList.item(y);
//                        Element getElement = (Element) getNode;
//                        contentList.add(getElement.getElementsByTagName("title").item(0).getTextContent() + "\n" + getElement.getElementsByTagName("link").item(0).getTextContent());
//
//                        NodeList enclosures = getElement.getElementsByTagName("enclosure");
//                        if (enclosures.getLength() > 0) {
//                            Element enclosure = (Element) enclosures.item(0);
//                            String imageUrl = enclosure.getAttribute("url");
//                            // You can do something with the image URL here
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void aVoid) {
//                ArrayAdapter<String> citiesAdapter = new ArrayAdapter<>(
//                        MainActivity.this,
//                        android.R.layout.simple_list_item_1,
//                        contentList
//                );
//
//                includeCities.setAdapter(citiesAdapter);
//            }
//        };
//
//
//    }
//}

public class MainActivity extends AppCompatActivity {

    private ListView includeCities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        includeCities = findViewById(R.id.includeCities);

        //ArrayList<String> cities = new ArrayList<>();

        new FetchDataTask(this, "https://timesofindia.indiatimes.com/rssfeedstopstories.cms").execute();
        //new FetchDataTask(this,"https://rss.nytimes.com/services/xml/rss/nyt/HomePage.xml").execute();
    }

    private static class FetchDataTask extends AsyncTask<Void, Void, Void> {
        private WeakReference<MainActivity> activityRef;
        private String urlString;
        ArrayList<String> cities = new ArrayList<String>();

        public FetchDataTask(MainActivity activity, String urlString) {
            activityRef = new WeakReference<>(activity);
            this.urlString = urlString;
            //this.cities = cities;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            MainActivity activity = activityRef.get();
            if (activity == null) {
                return null;
            }

            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document readFile = builder.parse(new InputSource(new URL(urlString).openStream()));
                readFile.getDocumentElement().normalize();
                NodeList nodeList = readFile.getElementsByTagName("item");

                for (int y = 0; y < nodeList.getLength(); y++) {
                    Node getNode = nodeList.item(y);
                    Element getElement = (Element) getNode;
                    cities.add(getElement.getElementsByTagName("title").item(0).getTextContent());

                    NodeList enclosures = getElement.getElementsByTagName("enclosure");
                    if (enclosures.getLength() > 0) {
                        Element enclosure = (Element) enclosures.item(0);
                        String imageUrl = enclosure.getAttribute("url");
                        //image URL to be fed into ImageView
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            MainActivity activity = activityRef.get();
            if (activity == null) {
                return;
            }

            ArrayAdapter<String> citiesAdapter = new ArrayAdapter<>(
                    activity,
                    android.R.layout.simple_list_item_1,
                    cities
            );

            activity.includeCities.setAdapter(citiesAdapter);
        }
    }
}
