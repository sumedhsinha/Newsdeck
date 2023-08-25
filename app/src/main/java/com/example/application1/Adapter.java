package com.example.application1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import android.content.res.Configuration;
import android.content.Intent;
import android.net.Uri;
import android.widget.Button;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    protected ArrayList<String> images;
    protected ArrayList<String> text;
    protected ArrayList<String> links;

    //

    //
    protected Context context;


    public Adapter(Context context, ArrayList<String> images, ArrayList<String> text, ArrayList<String> links) {
        this.context = context;
        this.images = images;
        this.text = text;
        this.links = links;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        int nightModeFlags = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        boolean isDarkMode = nightModeFlags == Configuration.UI_MODE_NIGHT_YES;

        if (isDarkMode) {
            //view.setBackground(context.getDrawable(R.drawable.border));
            view.setBackground(AppCompatResources.getDrawable(context, R.drawable.border));
        } else {
            //view.setBackground(context.getDrawable(R.drawable.borderdark));
            view.setBackground(AppCompatResources.getDrawable(context, R.drawable.borderdark));
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String imageUrl = images.get(position);
        Picasso.get().load(imageUrl).into(holder.images);
        holder.text.setText(text.get(position));

        //

        //

//        holder.readMoreButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String link = links.get(position);
//                openBrowser(link);
//            }
//        });
        holder.readMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String articleTitle = text.get(position);
                String articleImage = images.get(position);
//                String articleSummary = text.get(position);
                String articleUrl = links.get(position);

                Intent intent = new Intent(context, ShowSummaryActivity.class);
                intent.putExtra(ShowSummaryActivity.EXTRA_ARTICLE_TITLE, articleTitle);
                intent.putExtra(ShowSummaryActivity.EXTRA_ARTICLE_IMAGE, articleImage);
//                intent.putExtra(ShowSummaryActivity.EXTRA_ARTICLE_SUMMARY, articleSummary);
                intent.putExtra(ShowSummaryActivity.EXTRA_ARTICLE_URL, articleUrl);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return text.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView images;
        TextView text;
        Button readMoreButton;

        public ViewHolder(View view) {
            super(view);
            images = view.findViewById(R.id.imageView);
            text = view.findViewById(R.id.textView);
            readMoreButton = view.findViewById(R.id.readMoreButton);

            int nightModeFlags = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
            boolean isDarkMode = nightModeFlags == Configuration.UI_MODE_NIGHT_YES;

            if (!isDarkMode) {
                //noinspection deprecation
                text.setTextColor(context.getResources().getColor(R.color.textColorLight));
            } else {
                //noinspection deprecation
                text.setTextColor(context.getResources().getColor(R.color.textColorDark));
            }
        }
    }

    private void openBrowser(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(intent);
    }
}
