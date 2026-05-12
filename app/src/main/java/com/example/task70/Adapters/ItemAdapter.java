package com.example.task70.Adapters;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task70.Models.Item;
import com.example.task70.R;

import java.net.URI;
import java.util.ArrayList;



public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder> {
    Context context;
    ArrayList<Item> items;

    public ItemAdapter(Context context, ArrayList<Item> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ItemAdapter.ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recyler_item, parent, false);
        return new ItemAdapter.ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ItemHolder holder, int position) {
        holder.image.setImageURI(Uri.parse(items.get(position).image));
        holder.name.setText(items.get(position).name);
        holder.date.setText(items.get(position).date);
        holder.description.setText(items.get(position).description);
        holder.itemId = items.get(position).id;
        if(items.get(position).catergory == "0") {
            holder.postType.setText("Missing item");
        }
        else {
            holder.postType.setText("Found item");
        }


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ItemHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView postType;
        TextView date;
        TextView description;
        TextView name;
        Button viewButton;
        int itemId;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.item_image);
            postType = itemView.findViewById(R.id.post_type_text);
            date = itemView.findViewById(R.id.date);
            description = itemView.findViewById(R.id.description_text);
            name = itemView.findViewById(R.id.item_name);
            viewButton = itemView.findViewById(R.id.view_button);

            viewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavController navController = Navigation.findNavController(itemView);
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", itemId);
                    navController.navigate(R.id.view_item, bundle);
                }
            });

        }
    }
}
