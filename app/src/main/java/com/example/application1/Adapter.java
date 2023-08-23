package com.example.application1;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import android.content.res.Configuration;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;



import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private ArrayList<String> images;
    private ArrayList<String> text;
    private ArrayList<String> links;
    private Context context;

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
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String imageUrl = images.get(position);
        Picasso.get().load(imageUrl).into(holder.images);
        holder.text.setText(text.get(position));

        holder.readMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = links.get(position);
                openBrowser(link);
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
                text.setTextColor(context.getResources().getColor(R.color.textColorLight));
            } else {
                text.setTextColor(context.getResources().getColor(R.color.textColorDark));
            }
        }
    }

    private void openBrowser(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(intent);
    }
}




//public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
//
//    private ArrayList<String> images;
//    private ArrayList<String> text;
//    private ItemClickListener itemClickListener;
//
//    public Adapter(ArrayList<String> images, ArrayList<String> text) {
//        this.images = images;
//        this.text = text;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        String imageUrl = images.get(position);
//        Picasso.get().load(imageUrl).into(holder.images);
//        holder.text.setText(text.get(position));
//    }
//
//    @Override
//    public int getItemCount() {
//        return text.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        ImageView images;
//        TextView text;
//
//        public ViewHolder(View view) {
//            super(view);
//            images = view.findViewById(R.id.imageView);
//            text = view.findViewById(R.id.textView);
//
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (itemClickListener != null) {
//                        itemClickListener.onItemClick(getAdapterPosition());
//                    }
//                }
//            });
//        }
//    }
//
//    public interface ItemClickListener {
//        void onItemClick(int position);
//    }
//
//    public void setItemClickListener(ItemClickListener itemClickListener) {
//        this.itemClickListener = itemClickListener;
//    }
//}





//public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
//    private ArrayList<String> images;
//    private ArrayList<String> text;
//    private ArrayList<String> links; // Add this member variable
//    private ItemClickListener listener;
//
//    public interface ItemClickListener {
//        void onItemClick(String clickedLink);
//    }
//
//    public Adapter(Context context, ArrayList<String> images, ArrayList<String> text, ArrayList<String> links) {
//        this.images = images;
//        this.text = text;
//        this.links = links; // Initialize links
//    }
//
//    public void setItemClickListener(ItemClickListener listener) {
//        this.listener = listener;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        String imageUrl = images.get(position);
//        Picasso.get().load(imageUrl).into(holder.images);
//        holder.text.setText(text.get(position));
//    }
//
//    @Override
//    public int getItemCount() {
//        return text.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        ImageView images;
//        TextView text;
//
//        public ViewHolder(View view) {
//            super(view);
//            images = view.findViewById(R.id.imageView);
//            text = view.findViewById(R.id.textView);
//
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int position = getAdapterPosition();
//                    if (position != RecyclerView.NO_POSITION && listener != null) {
//                        String clickedLink = links.get(position);
//                        listener.onItemClick(clickedLink);
//                    }
//                }
//            });
//        }
//    }
//}


//public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
//    private ArrayList<String> images;
//    private ArrayList<String> text;
//    private Context context;
//
//    public Adapter(Context context, ArrayList<String> images, ArrayList<String> text) {
//        this.context = context;
//        this.images = images;
//        this.text = text;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        String imageUrl = images.get(position);
//        Picasso.get().load(imageUrl).into(holder.images);
//        holder.text.setText(text.get(position));
//    }
//
//    @Override
//    public int getItemCount() {
//        return text.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        ImageView images;
//        TextView text;
//
//        public ViewHolder(View view) {
//            super(view);
//            images = view.findViewById(R.id.imageView);
//            text = view.findViewById(R.id.textView);
//        }
//    }
//}






//public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
//    ArrayList<String> images;
//    ArrayList<String> text;
//    Context context;
//
//    public interface OnItemClickListener {
//        void onItemClick(int position);
//    }
//
//    private OnItemClickListener listener;
//
//    public Adapter(Context context, ArrayList<String> images, ArrayList<String> text) {
//        this.context = context;
//        this.images = images;
//        this.text = text;
//    }
//
//
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
//        ViewHolder viewHolder = new ViewHolder(view);
//        return viewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        String imageUrl = images.get(position);
//        Picasso.get().load(imageUrl).into(holder.images);
//        holder.text.setText(text.get(position));
//    }
//
//    @Override
//    public int getItemCount() {
//        return text.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        ImageView images;
//        TextView text;
//
//        public ViewHolder(View view) {
//            super(view);
//            images = view.findViewById(R.id.imageView);
//            text = view.findViewById(R.id.textView);
//
//            // Set click listener for the item
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (listener != null) {
//                        int position = getAdapterPosition();
//                        if (position != RecyclerView.NO_POSITION) {
//                            listener.onItemClick(position);
//                        }
//                    }
//                }
//            });
//        }
//    }
//
//    // Set the click listener interface
//    public void setOnItemClickListener(OnItemClickListener listener) {
//        this.listener = listener;
//    }
//}













//public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
//    ArrayList<String> images; // Store image URLs
//
//    ArrayList<String> text;
//    Context context;
//
//    public Adapter(Context context, ArrayList<String> images, ArrayList<String> text) {
//        this.context = context;
//        this.images = images;
//        this.text = text;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
//        ViewHolder viewHolder = new ViewHolder(view);
//        return viewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        String imageUrl = images.get(position);
//        // Load image using Picasso
//        Picasso.get().load(imageUrl).into(holder.images);
//        holder.text.setText(text.get(position));
//    }
//
//
//
//    @Override
//    public int getItemCount() {
//        return text.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        ImageView images;
//        TextView text;
//
//        public ViewHolder(View view) {
//            super(view);
//            images = view.findViewById(R.id.imageView);
//            text = view.findViewById(R.id.textView);
//        }
//    }
//}






//
//public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
//    ArrayList<String> images; // Store image URLs
//    ArrayList<String> text;
//    Context context;
//
//    public Adapter(Context context, ArrayList<String> images, ArrayList<String> text) {
//        this.context = context;
//        this.images = images;
//        this.text = text;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
//        ViewHolder viewHolder = new ViewHolder(view);
//        return viewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        String imageUrl = images.get(position);
//        // Load image using the Uri
//        holder.images.setImageURI(Uri.parse(imageUrl));
//        holder.text.setText(text.get(position));
//    }
//
//    @Override
//    public int getItemCount() {
//        return text.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        ImageView images;
//        TextView text;
//
//        public ViewHolder(View view) {
//            super(view);
//            images = view.findViewById(R.id.imageView);
//            text = view.findViewById(R.id.textView);
//        }
//    }
//}
//












// Extends the Adapter class to RecyclerView.Adapter
// and implement the unimplemented methods

//public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
//    ArrayList images, text;
//    Context context;
//
//    // Constructor for initialization
//    public Adapter(Context context, ArrayList images, ArrayList text) {
//        this.context = context;
//        this.images = images;
//        this.text = text;
//    }
//
//    @NonNull
//    @Override
//    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        // Inflating the Layout(Instantiates list_item.xml layout file into View object)
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
//
//        Adapter.ViewHolder viewHolder = new Adapter.ViewHolder(view);
//        return viewHolder;
//    }
//
//    // Binding data to the into specified position
//    @Override
//    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
//        // TypeCast Object to int type
//        int res = (int) images.get(position);
//        holder.images.setImageResource(res);
//        holder.text.setText((CharSequence) text.get(position));
//    }
//
//    @Override
//    public int getItemCount() {
//        // Returns number of items currently available in Adapter
//        return text.size();
//    }
//
//    // Initializing the Views
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        ImageView images;
//        TextView text;
//
//        public ViewHolder(View view) {
//            super(view);
//            images = (ImageView) view.findViewById(R.id.imageView);
//            text = (TextView) view.findViewById(R.id.textView);
//        }
//    }
//}

