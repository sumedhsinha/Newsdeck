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

    private ArrayList<String> images = new ArrayList<>();
    private ArrayList<String> text = new ArrayList<>();
    private ArrayList<String> links = new ArrayList<>(); // Added links ArrayList

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









//
//import android.content.Intent;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import com.squareup.picasso.Picasso;
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.NodeList;
//import org.xml.sax.InputSource;
//import java.net.URL;
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import java.util.ArrayList;
//
//public class MainActivity extends AppCompatActivity {
//
//    private ArrayList<String> images = new ArrayList<>();
//    private ArrayList<String> text = new ArrayList<>();
//    private ArrayList<String> links = new ArrayList<>();
//
//    private RecyclerView recyclerView;
//    private Adapter adapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        recyclerView = findViewById(R.id.recyclerView);
//        adapter = new Adapter(images, text);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        adapter.setItemClickListener(new Adapter.ItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//                String clickedLink = links.get(position);
//                openWebPage(clickedLink);
//            }
//        });
//
//        // Fetch and populate data
//        new FetchDataTask().execute();
//    }
//
//    private class FetchDataTask extends AsyncTask<Void, Void, Void> {
//        @Override
//        protected Void doInBackground(Void... voids) {
//            try {
//                // Replace with your RSS feed URL
//                String rssUrl = "https://timesofindia.indiatimes.com/rssfeedstopstories.cms";
//
//                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//                DocumentBuilder builder = factory.newDocumentBuilder();
//                Document doc = builder.parse(new InputSource(new URL(rssUrl).openStream()));
//                doc.getDocumentElement().normalize();
//
//                NodeList nodeList = doc.getElementsByTagName("item");
//
//                for (int i = 0; i < nodeList.getLength(); i++) {
//                    Element item = (Element) nodeList.item(i);
//                    String title = item.getElementsByTagName("title").item(0).getTextContent();
//                    String link = item.getElementsByTagName("link").item(0).getTextContent();
//
//                    text.add(title);
//                    links.add(link);
//
//                    // Extract and add image URL if available
//                    NodeList enclosures = item.getElementsByTagName("enclosure");
//                    if (enclosures.getLength() > 0) {
//                        Element enclosure = (Element) enclosures.item(0);
//                        String imageUrl = enclosure.getAttribute("url");
//                        images.add(imageUrl);
//                    } else {
//                        images.add(""); // Add an empty URL if image is not available
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            adapter.notifyDataSetChanged(); // Notify the adapter of data change
//        }
//    }
//
//    private void openWebPage(String url) {
//        Uri webpageUri = Uri.parse(url);
//        Intent intent = new Intent(Intent.ACTION_VIEW, webpageUri);
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivity(intent);
//        }
//    }
//}







//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.StrictMode;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
//import com.squareup.picasso.Picasso;
//import org.w3c.dom.Element;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//import org.xml.sax.InputSource;
//import java.io.IOException;
//import java.lang.ref.WeakReference;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Random;
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.ParserConfigurationException;
//import org.w3c.dom.Document;
//import org.xml.sax.SAXException;
//
//public class MainActivity extends AppCompatActivity {
//
//    private SwipeRefreshLayout swipeRefreshLayout;
//    private RecyclerView recyclerView;
//
//    private ArrayList<String> images = new ArrayList<>();
//    private ArrayList<String> text = new ArrayList<>();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
//        recyclerView = findViewById(R.id.recyclerView);
//
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(linearLayoutManager);
//
//        Adapter adapter = new Adapter(this, images, text);
//        recyclerView.setAdapter(adapter);
//
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                rearrangeItems();
//                swipeRefreshLayout.setRefreshing(false);
//            }
//        });
//
//        new FetchDataTask(this, "https://timesofindia.indiatimes.com/rssfeedstopstories.cms").execute();
//    }
//
//    private void rearrangeItems() {
//        long seed = System.nanoTime();
//        Collections.shuffle(images, new Random(seed));
//        Collections.shuffle(text, new Random(seed));
//        Adapter adapter = new Adapter(MainActivity.this, images, text);
//        recyclerView.setAdapter(adapter);
//    }
//
//    private static class FetchDataTask extends AsyncTask<Void, Void, Void> {
//        private final WeakReference<MainActivity> activityRef;
//        private final String urlString;
//
//        public FetchDataTask(MainActivity activity, String urlString) {
//            activityRef = new WeakReference<>(activity);
//            this.urlString = urlString;
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            MainActivity activity = activityRef.get();
//            if (activity == null) {
//                return null;
//            }
//            try {
//                // Clear existing data before fetching new data
//                activity.text.clear();
//                activity.images.clear();
//
//                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//                DocumentBuilder builder = factory.newDocumentBuilder();
//                Document readFile = builder.parse(new InputSource(new URL(urlString).openStream()));
//                readFile.getDocumentElement().normalize();
//                NodeList nodeList = readFile.getElementsByTagName("item");
//
//                for (int y = 0; y < nodeList.getLength(); y++) {
//                    Node getNode = nodeList.item(y);
//                    Element getElement = (Element) getNode;
//                    activity.text.add(getElement.getElementsByTagName("title").item(0).getTextContent());
//
//                    NodeList enclosures = getElement.getElementsByTagName("enclosure");
//                    if (enclosures.getLength() > 0) {
//                        Element enclosure = (Element) enclosures.item(0);
//                        String imageUrl = enclosure.getAttribute("url");
//                        activity.images.add(imageUrl);
//                    }
//                }
//            } catch (IOException | SAXException | ParserConfigurationException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//    }
//}








//public class MainActivity extends AppCompatActivity {
//    SwipeRefreshLayout swipeRefreshLayout;
//    RecyclerView recyclerView;
//    WebView webView;
//
//    ArrayList<String> images = new ArrayList<>();
//    ArrayList<String> text = new ArrayList<>();
//    ArrayList<String> links = new ArrayList<>();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        // Initialize WebView settings
//        WebView.setWebContentsDebuggingEnabled(false);
//
//        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
//        recyclerView = findViewById(R.id.recyclerView);
//        webView = findViewById(R.id.webView);
//        webView.setVisibility(View.GONE);
//
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
//        recyclerView.setLayoutManager(linearLayoutManager);
//
//        Adapter adapter = new Adapter(MainActivity.this, images, text);
//        recyclerView.setAdapter(adapter);
//
//        adapter.setOnItemClickListener(position -> {
//            String clickedLink = links.get(position);
//            Log.d("url check", clickedLink);
//            openWebsiteInWebView(clickedLink);
//        });
//
//        swipeRefreshLayout.setOnRefreshListener(() -> {
//            swipeRefreshLayout.setRefreshing(false);
//            rearrangeItems();
//        });
//
//        new FetchDataTask(this, "https://timesofindia.indiatimes.com/rssfeedstopstories.cms").execute();
//    }
//
//    private void rearrangeItems() {
//        long seed = System.nanoTime();
//        Collections.shuffle(images, new Random(seed));
//        Collections.shuffle(text, new Random(seed));
//        Adapter adapter = new Adapter(MainActivity.this, images, text);
//        recyclerView.setAdapter(adapter);
//    }
//
//    private void openWebsiteInWebView(String url) {
//        webView.setVisibility(View.VISIBLE);
//        recyclerView.setVisibility(View.GONE);
//
//        // Set WebViewClient before loading the URL
//        webView.setWebViewClient(new WebViewClient() {
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                // This callback is called when the page finishes loading
//                // You can use it to handle UI updates or other actions
//                // For example, you can hide the WebView and show the RecyclerView again
//                webView.setVisibility(View.GONE);
//                recyclerView.setVisibility(View.VISIBLE);
//            }
//        });
//
//        // Load the URL
//        webView.loadUrl(url);
//    }
//
//    private static class FetchDataTask extends AsyncTask<Void, Void, Void> {
//        private final WeakReference<MainActivity> activityRef;
//        private final String urlString;
//
//        public FetchDataTask(MainActivity activity, String urlString) {
//            activityRef = new WeakReference<>(activity);
//            this.urlString = urlString;
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            MainActivity activity = activityRef.get();
//            if (activity == null) {
//                return null;
//            }
//            try {
//                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//                DocumentBuilder builder = factory.newDocumentBuilder();
//                Document readFile = builder.parse(new InputSource(new URL(urlString).openStream()));
//                readFile.getDocumentElement().normalize();
//                NodeList nodeList = readFile.getElementsByTagName("item");
//
//                for (int y = 0; y < nodeList.getLength(); y++) {
//                    Node getNode = nodeList.item(y);
//                    Element getElement = (Element) getNode;
//                    activity.text.add(getElement.getElementsByTagName("title").item(0).getTextContent());
//                    activity.links.add(getElement.getElementsByTagName("link").item(0).getTextContent());
//
//
//                    NodeList enclosures = getElement.getElementsByTagName("enclosure");
//                    if (enclosures.getLength() > 0) {
//                        Element enclosure = (Element) enclosures.item(0);
//                        String imageUrl = enclosure.getAttribute("url");
//                        activity.images.add(imageUrl);
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            MainActivity activity = activityRef.get();
//            if (activity == null) {
//                return;
//            }
//        }
//    }
//}






//public class MainActivity extends AppCompatActivity {
//    SwipeRefreshLayout swipeRefreshLayout;
//    RecyclerView recyclerView;
//    WebView webView;
//
//    ArrayList<String> images = new ArrayList<>();
//    ArrayList<String> text = new ArrayList<>();
//    ArrayList<String> links = new ArrayList<>();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
//        recyclerView = findViewById(R.id.recyclerView);
//        webView = findViewById(R.id.webView);
//        webView.setVisibility(View.GONE); // Initially hide WebView
//
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
//        recyclerView.setLayoutManager(linearLayoutManager);
//
//        Adapter adapter = new Adapter(MainActivity.this, images, text);
//        recyclerView.setAdapter(adapter);
//
//        adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//                String clickedLink = links.get(position); // Get the corresponding link
//                openWebsiteInWebView(clickedLink); // Open in WebView
//            }
//        });
//
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                swipeRefreshLayout.setRefreshing(false);
//                rearrangeItems();
//            }
//        });
//
//        new FetchDataTask(this, "https://timesofindia.indiatimes.com/rssfeedstopstories.cms").execute();
//    }
//
//    private class FetchDataTask extends AsyncTask<Void, Void, Void> {
//        private WeakReference<MainActivity> activityRef;
//        private String urlString;
//
//        public FetchDataTask(MainActivity activity, String urlString) {
//            activityRef = new WeakReference<>(activity);
//            this.urlString = urlString;
//
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            MainActivity activity = activityRef.get();
//            if (activity == null) {
//                return null;
//            }
//            try {
//                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//                DocumentBuilder builder = factory.newDocumentBuilder();
//                Document readFile = builder.parse(new InputSource(new URL(urlString).openStream()));
//                readFile.getDocumentElement().normalize();
//                NodeList nodeList = readFile.getElementsByTagName("item");
//
//                for (int y = 0; y < nodeList.getLength(); y++) {
//                    Node getNode = nodeList.item(y);
//                    Element getElement = (Element) getNode;
//                    text.add(getElement.getElementsByTagName("title").item(0).getTextContent());
//                    links.add(getElement.getElementsByTagName("link").item(0).getTextContent());
//
//                    Log.d("", links.get(links.size() - 1));
//
//                    NodeList enclosures = getElement.getElementsByTagName("enclosure");
//                    if (enclosures.getLength() > 0) {
//                        Element enclosure = (Element) enclosures.item(0);
//                        String imageUrl = enclosure.getAttribute("url");
//                        images.add(imageUrl);
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            MainActivity activity = activityRef.get();
//            if (activity == null) {
//                return;
//            }
//        }
//    }
//
//    public void rearrangeItems() {
//        long seed = System.nanoTime();
//        Collections.shuffle(images, new Random(seed));
//        Collections.shuffle(text, new Random(seed));
//        Adapter adapter = new Adapter(MainActivity.this, images, text);
//        recyclerView.setAdapter(adapter);
//    }
//
//    private void openWebsiteInWebView(String url) {
//
//        Log.d("WebView", "Opening URL: " + url);
//
//        webView.setVisibility(View.VISIBLE); // Show WebView
//        recyclerView.setVisibility(View.GONE);
//
//        WebSettings webSettings = webView.getSettings();
//        webSettings.setJavaScriptEnabled(true); // Enable JavaScript if needed
//
//        // Load the URL in WebView
//        webView.loadUrl(url);
//
//        // Hide WebView after loading (if needed)
//        webView.setWebViewClient(new WebViewClient() {
//            @Override
//            public void onPageFinished(WebView view, String url) {
//
//
//                Log.d("WebView", "Page finished loading: " + url);
//
//
//                webView.setVisibility(View.GONE);
//                recyclerView.setVisibility(View.VISIBLE);
//            }
//        });
//    }
//}















//public class MainActivity extends AppCompatActivity {
//    SwipeRefreshLayout swipeRefreshLayout;
//    RecyclerView recyclerView;
//
//    WebView webView;
//
//    ArrayList<String> images = new ArrayList<>(Arrays.asList());
//
//    ArrayList<String> text = new ArrayList<>(Arrays.asList());
//
//    ArrayList<String> links = new ArrayList<String>(Arrays.asList());
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//
//        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
//        recyclerView = findViewById(R.id.recyclerView);
//
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
//        recyclerView.setLayoutManager(linearLayoutManager);
//
//        Adapter adapter = new Adapter(MainActivity.this, images, text);
//        recyclerView.setAdapter(adapter);
//
//        webView = findViewById(R.id.webView);
//        webView.setVisibility(View.GONE); // Initially hide WebView
//
//
//        adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//                String clickedLink = links.get(position); // Get the corresponding link
//                openWebsiteInWebView(clickedLink); // Open in WebView
//            }
//        });
//
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                swipeRefreshLayout.setRefreshing(false);
//                rearrangeItems();
//            }
//        });
//
//        new FetchDataTask(this, "https://timesofindia.indiatimes.com/rssfeedstopstories.cms").execute();
//    }
//
//    private class FetchDataTask extends AsyncTask<Void, Void, Void> {
//        private WeakReference<MainActivity> activityRef;
//        private String urlString;
//
//        public FetchDataTask(MainActivity activity, String urlString) {
//            activityRef = new WeakReference<>(activity);
//            this.urlString = urlString;
//            //this.cities = cities;
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//
//            MainActivity activity = activityRef.get();
//            if (activity == null) {
//                return null;
//            }
//            try {
//                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//                DocumentBuilder builder = factory.newDocumentBuilder();
//                Document readFile = builder.parse(new InputSource(new URL(urlString).openStream()));
//                readFile.getDocumentElement().normalize();
//                NodeList nodeList = readFile.getElementsByTagName("item");
//
//                for (int y = 0; y < nodeList.getLength(); y++) {
//                    Node getNode = nodeList.item(y);
//                    Element getElement = (Element) getNode;
//                    text.add(getElement.getElementsByTagName("title").item(0).getTextContent());
//                    links.add(getElement.getElementsByTagName("link").item(0).getTextContent());
//
//
//                    NodeList enclosures = getElement.getElementsByTagName("enclosure");
//                    if (enclosures.getLength() > 0) {
//                        Element enclosure = (Element) enclosures.item(0);
//                        String imageUrl = enclosure.getAttribute("url");
//                        //image URL to be fed into ImageView
//                        images.add(imageUrl);
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            MainActivity activity = activityRef.get();
//            if (activity == null) {
//                return;
//            }
//        }
//    }
//
//    public void rearrangeItems() {
//        long seed = System.nanoTime();
//        Collections.shuffle(images, new Random(seed));
//        Collections.shuffle(text, new Random(seed));
//        Adapter adapter = new Adapter(MainActivity.this, images, text);
//        recyclerView.setAdapter(adapter);
//    }
//
//    private void openWebsiteInWebView(String url) {
//
//        webView.setVisibility(View.VISIBLE); // Show WebView
//        recyclerView.setVisibility(View.GONE);
//
//        WebSettings webSettings = webView.getSettings();
//        webSettings.setJavaScriptEnabled(true); // Enable JavaScript if needed
//
//        webView.loadUrl(url); // Load the URL in WebView
//    }
//
//}












//public class MainActivity extends AppCompatActivity {
//    SwipeRefreshLayout swipeRefreshLayout;
//    RecyclerView recyclerView;
//
//    // Using ArrayList to store images and text data
//    ArrayList images = new ArrayList<>(Arrays.asList(R.drawable.ic_launcher_background, R.drawable.ic_launcher_foreground,
//            R.drawable.ic_launcher_background, R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_background, R.drawable.ic_launcher_foreground));
//    ArrayList text = new ArrayList<>(Arrays.asList("Facebook", "Twitter", "Instagram", "LinkedIn", "Youtube", "Whatsapp"));
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        // Getting reference of swipeRefreshLayout and recyclerView
//        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
//        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//
//        // Setting the layout as Linear for vertical orientation to have swipe behavior
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
//        recyclerView.setLayoutManager(linearLayoutManager);
//
//        // Sending reference and data to Adapter
//        Adapter adapter = new Adapter(MainActivity.this, images, text);
//
//        // Setting Adapter to RecyclerView
//        recyclerView.setAdapter(adapter);
//
//        // SetOnRefreshListener on SwipeRefreshLayout
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                swipeRefreshLayout.setRefreshing(false);
//                RearrangeItems();
//            }
//        });
//    }
//
//    public void RearrangeItems() {
//        // Shuffling the data of ArrayList using system time
//        Collections.shuffle(images, new Random(System.currentTimeMillis()));
//        Collections.shuffle(text, new Random(System.currentTimeMillis()));
//        Adapter adapter = new Adapter(MainActivity.this, images, text);
//        recyclerView.setAdapter(adapter);
//    }
//}





//end test code 2
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
//
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//import org.xml.sax.InputSource;
//
//import java.lang.ref.WeakReference;
//import java.net.URL;
//import java.nio.channels.AsynchronousByteChannel;
//import java.util.ArrayList;
//
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;


//working code ---
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
//        //ArrayList<String> cities = new ArrayList<>();
//
//        new FetchDataTask(this, "https://timesofindia.indiatimes.com/rssfeedstopstories.cms").execute();
//        //new FetchDataTask(this,"https://rss.nytimes.com/services/xml/rss/nyt/HomePage.xml").execute();
//    }
//
//    private static class FetchDataTask extends AsyncTask<Void, Void, Void> {
//        private WeakReference<MainActivity> activityRef;
//        private String urlString;
//        ArrayList<String> cities = new ArrayList<String>();
//
//        public FetchDataTask(MainActivity activity, String urlString) {
//            activityRef = new WeakReference<>(activity);
//            this.urlString = urlString;
//            //this.cities = cities;
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            MainActivity activity = activityRef.get();
//            if (activity == null) {
//                return null;
//            }
//            try {
//                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//                DocumentBuilder builder = factory.newDocumentBuilder();
//                Document readFile = builder.parse(new InputSource(new URL(urlString).openStream()));
//                readFile.getDocumentElement().normalize();
//                NodeList nodeList = readFile.getElementsByTagName("item");
//
//                for (int y = 0; y < nodeList.getLength(); y++) {
//                    Node getNode = nodeList.item(y);
//                    Element getElement = (Element) getNode;
//                    cities.add(getElement.getElementsByTagName("title").item(0).getTextContent());
//
//                    NodeList enclosures = getElement.getElementsByTagName("enclosure");
//                    if (enclosures.getLength() > 0) {
//                        Element enclosure = (Element) enclosures.item(0);
//                        String imageUrl = enclosure.getAttribute("url");
//                        //image URL to be fed into ImageView
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            MainActivity activity = activityRef.get();
//            if (activity == null) {
//                return;
//            }
//
//            ArrayAdapter<String> citiesAdapter = new ArrayAdapter<>(
//                    activity,
//                    android.R.layout.simple_list_item_1,
//                    cities
//            );
//
//            activity.includeCities.setAdapter(citiesAdapter);
//        }
//    }
//}
