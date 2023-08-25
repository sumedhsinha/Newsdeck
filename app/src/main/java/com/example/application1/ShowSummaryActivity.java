package com.example.application1;

//import android.os.Bundle;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//public class ShowSummaryActivity extends AppCompatActivity {
//
//    public static final String EXTRA_ARTICLE_SUMMARY = "article_summary";
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.show_summary);
//
//        // Get the summary from the intent's extra
//
//        String articleSummary = getIntent().getStringExtra(EXTRA_ARTICLE_SUMMARY);
//
//        // You can customize how you want to display the summary
//        // For example, you could update a TextView in the layout with the summary text
//    }
//}

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class ShowSummaryActivity extends AppCompatActivity {

    public static final String EXTRA_ARTICLE_TITLE = "article_title";
    public static final String EXTRA_ARTICLE_IMAGE = "article_image";
    public static final String EXTRA_ARTICLE_SUMMARY = "article_summary";
    public static final String EXTRA_ARTICLE_URL = "article_url";
    public static final String EXTRA_ARTICLE_AUTHORS = "authors";
    public static final String EXTRA_ARTICLE_PUBDATE = "pubdate";

    private Button readMoreButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_summary);

        TextView summaryTextView = findViewById(R.id.scrollTextView);
        TextView titleTextView = findViewById(R.id.topTextView);
        ImageView articleImageView = findViewById(R.id.imageView);
        TextView authView = findViewById(R.id.author);
        TextView date = findViewById(R.id.dated);
        readMoreButton = findViewById(R.id.readMoreButton);

        // Get the title, image, summary, and URL from the intent's extras
        String articleTitle = getIntent().getStringExtra(EXTRA_ARTICLE_TITLE);
        String articleImage = getIntent().getStringExtra(EXTRA_ARTICLE_IMAGE);
        String articleSummary = getIntent().getStringExtra(EXTRA_ARTICLE_SUMMARY);
        //
        String authors = getIntent().getStringExtra(EXTRA_ARTICLE_AUTHORS);
        String pubdate = getIntent().getStringExtra(EXTRA_ARTICLE_PUBDATE);
        //
        final String articleUrl = getIntent().getStringExtra(EXTRA_ARTICLE_URL);

        // Set the title, image, and summary in the appropriate views
        titleTextView.setText(articleTitle);
        Picasso.get().load(articleImage).into(articleImageView);
        summaryTextView.setText(articleSummary);
        authView.setText(authors);
        date.setText(pubdate);

        readMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebView(articleUrl);
            }
        });
    }

    private void openWebView(String url) {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra(WebViewActivity.EXTRA_ARTICLE_URL, url);
        startActivity(intent);
    }
}




