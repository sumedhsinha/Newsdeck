package com.example.application1;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.core.view.GravityCompat;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    ArrayList<String> images = new ArrayList<>();
    ArrayList<String> text = new ArrayList<>();
    ArrayList<String> links = new ArrayList<>();
    ArrayList<String> authors = new ArrayList<>();
    ArrayList<String> pubDate = new ArrayList<>();
    ArrayList<String> summaries = new ArrayList<>();
    ArrayList<RssFeed> rssFeeds = new ArrayList<>();

    //
    /*
    Tasks:

        to do:-
            Update adapter and layout to incorporate the PubDate and authors
            Update RssFeed.java to obtain tags for the newly added portions
            Fix multi source menus
            Add custom theme
            Add app icon

        possibilities:-
            Possibly combine ArrayLists and have one object
            Change bkg colors depending upon dark mode (already included for top bar, but background does not change)

        future improvements:-
            Remove buttons and incorporate onclick for recyclerview
            Better text formatting
            Side navigation drawer
            Replace deprecated components like Background/Async tasks with Lambda/newer versions - suppressed warnings

        Nice to have later:-
            User Accounts
            Notifications
            customisable home screen

    */
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        Adapter adapter = new Adapter(this, images, text, links, authors, pubDate, summaries); // Passing the links ArrayList
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                rearrangeItems();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        //rssFeeds.add(new RssFeed("https://www.hindustantimes.com/feeds/rss/trending/rssfeed.xml", "item","title", "link", "media:content"));
        rssFeeds.add(new RssFeed("https://rss.nytimes.com/services/xml/rss/nyt/HomePage.xml", "item", "title", "link", "media:content"));
        // Fetch data when the app is first opened
        fetchAndDisplayData();
    }

    private void fetchAndDisplayData() {
        //noinspection deprecation

        //new FetchDataTask(this, "https://www.hindustantimes.com/feeds/rss/trending/rssfeed.xml").execute();
        for (RssFeed rssFeed : rssFeeds) {
            new FetchDataTask(this, rssFeed).execute();
        }
    }

    private void rearrangeItems() {
        long seed = System.nanoTime();
        Collections.shuffle(images, new Random(seed));
        Collections.shuffle(text, new Random(seed));
        Collections.shuffle(links, new Random(seed)); // Shuffling links along with other data
        //
        Collections.shuffle(authors, new Random(seed));
        Collections.shuffle(pubDate, new Random(seed));
        Collections.shuffle(summaries, new Random(seed));
        //
        Adapter adapter = new Adapter(MainActivity.this, images, text, links, authors, pubDate, summaries);
        recyclerView.setAdapter(adapter);
    }

    private static class FetchDataTask extends AsyncTask<Void, Void, Void> {
        private final WeakReference<MainActivity> activityRef;
        private final RssFeed rssFeed;

        public FetchDataTask(MainActivity activity, RssFeed rssFeed) {
            activityRef = new WeakReference<>(activity);
            this.rssFeed = rssFeed;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            MainActivity activity = activityRef.get();
            if (activity == null) {
                return null;
            }
            try {
                // Clear existing data before fetching new data
                activity.text.clear();
                activity.links.clear();
                activity.images.clear();
                //
                activity.authors.clear();
                activity.pubDate.clear();
                activity.summaries.clear();
                //

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document readFile = builder.parse(new InputSource(new URL(rssFeed.getUrl()).openStream()));
                readFile.getDocumentElement().normalize();
                NodeList nodeList = readFile.getElementsByTagName(rssFeed.getItem());
                for (int y = 0; y < nodeList.getLength(); y++) {
                    Node getNode = nodeList.item(y);
                    Element getElement = (Element) getNode;
                    activity.text.add(getElement.getElementsByTagName(rssFeed.getTitleTag()).item(0).getTextContent());
                    activity.links.add(getElement.getElementsByTagName(rssFeed.getLinkTag()).item(0).getTextContent());

                    //work in progress - to change adapter
                    activity.pubDate.add(getElement.getElementsByTagName("pubDate").item(0).getTextContent());
                    activity.authors.add(getElement.getElementsByTagName("dc:creator").item(0).getTextContent());
                    // to reconfigure adapter to accept authors and pubDates
                    Log.d("authors", getElement.getElementsByTagName("dc:creator").item(0).getTextContent());
                    Log.d("pubDate", getElement.getElementsByTagName("pubDate").item(0).getTextContent());

                    //summary works as intended
                    activity.summaries.add(getElement.getElementsByTagName("description").item(0).getTextContent());
                    //Log.d("pubDateCheck", getElement.getElementsByTagName("pubDate").item(0).getTextContent());
                    //

                    NodeList enclosures = getElement.getElementsByTagName(rssFeed.getImageTag());
                    if (enclosures.getLength() > 0) {
                        Element enclosure = (Element) enclosures.item(0);
                        String imageUrl = enclosure.getAttribute("url");
                        activity.images.add(imageUrl);
                    }
                }
            } catch (IOException | SAXException | ParserConfigurationException e) {
                e.printStackTrace();
            }
            return null;
        }

        /** @noinspection deprecation*/
        @Override
        protected void onPostExecute(Void aVoid) {
            MainActivity activity = activityRef.get();
            if (activity != null) {
                Adapter adapter = new Adapter(activity, activity.images, activity.text, activity.links, activity.authors, activity.pubDate, activity.summaries);
                activity.recyclerView.setAdapter(adapter);
            }
        }
    }
}
