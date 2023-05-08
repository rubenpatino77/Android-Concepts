package com.example.androidconcepts;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentContainerView;
import androidx.recyclerview.widget.RecyclerView;

public class cardViewHolder extends RecyclerView.ViewHolder {
    FragmentContainerView container;

    public cardViewHolder(@NonNull View itemView) {
        super(itemView);
        container = itemView.findViewById(R.id.cardFragmentContainer);
    }
}
