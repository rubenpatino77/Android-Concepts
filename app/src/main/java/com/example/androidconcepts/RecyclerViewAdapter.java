package com.example.androidconcepts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<cardViewHolder> {

    ArrayList<Fragment> allFragments;
    FragmentManager fragmentManager;
    Context context;

    public RecyclerViewAdapter(Context context, ArrayList<Fragment> allFragments, FragmentManager fragmentManager){
        this.allFragments = allFragments;
        this.fragmentManager = fragmentManager;
        this.context = context;
    }

    @NonNull
    @Override
    public cardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new cardViewHolder(LayoutInflater.from(context).inflate(R.layout.card_view, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull cardViewHolder holder, int position) {
        holder.container.setId(position + 1);
        fragmentManager.beginTransaction().add(holder.container.getId(), allFragments.get(position)).commit();
    }

    @Override
    public int getItemCount() {
        return allFragments.size();
    }
}
