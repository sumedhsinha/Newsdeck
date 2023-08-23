package com.example.application1;

import android.os.AsyncTask;
import android.os.Bundle;
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
import java.util.Collections;
import java.util.Random;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class MainActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    ArrayList<String> images = new ArrayList<>();
    ArrayList<String> text = new ArrayList<>();
    ArrayList<String> links = new ArrayList<>(); // Added links ArrayList

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        Adapter adapter = new Adapter(this, images, text, links); // Passing the links ArrayList
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                rearrangeItems();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        // Fetch data when the app is first opened
        fetchAndDisplayData();
    }

    private void fetchAndDisplayData() {
        //noinspection deprecation
        new FetchDataTask(this, "https://timesofindia.indiatimes.com/rssfeedstopstories.cms").execute();
    }

    private void rearrangeItems() {
        long seed = System.nanoTime();
        Collections.shuffle(images, new Random(seed));
        Collections.shuffle(text, new Random(seed));
        Collections.shuffle(links, new Random(seed)); // Shuffling links along with other data
        Adapter adapter = new Adapter(MainActivity.this, images, text, links);
        recyclerView.setAdapter(adapter);
    }

    private static class FetchDataTask extends AsyncTask<Void, Void, Void> {
        private final WeakReference<MainActivity> activityRef;
        private final String urlString;

        public FetchDataTask(MainActivity activity, String urlString) {
            activityRef = new WeakReference<>(activity);
            this.urlString = urlString;
        }

        /** @noinspection deprecation*/
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

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document readFile = builder.parse(new InputSource(new URL(urlString).openStream()));
                readFile.getDocumentElement().normalize();
                NodeList nodeList = readFile.getElementsByTagName("item");

                for (int y = 0; y < nodeList.getLength(); y++) {
                    Node getNode = nodeList.item(y);
                    Element getElement = (Element) getNode;
                    activity.text.add(getElement.getElementsByTagName("title").item(0).getTextContent());
                    activity.links.add(getElement.getElementsByTagName("link").item(0).getTextContent());

                    NodeList enclosures = getElement.getElementsByTagName("enclosure");
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
                Adapter adapter = new Adapter(activity, activity.images, activity.text, activity.links);
                activity.recyclerView.setAdapter(adapter);
            }
        }
    }
}
