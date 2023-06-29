package com.example.androidconcepts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView myRecyclerView;
    ArrayList<Fragment> fragmentsList;
    RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentsList = new ArrayList<>();
        getFragments(fragmentsList);
        displayRecyclerViews(fragmentsList);

    }

    public void getFragments(ArrayList<Fragment> fragmentsList){
        fragmentsList.add(new LocalDataSenderFragment());
        fragmentsList.add(new LocalDataReceiverFragment());
        fragmentsList.add(new ApiExampleFragment());
        fragmentsList.add(new ContentProviderFragment());
    }

    public void displayRecyclerViews(ArrayList<Fragment> fragmentsList){
        myRecyclerView = findViewById(R.id.myRecyclerView);
        myRecyclerView.setHasFixedSize(true);
        myRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new RecyclerViewAdapter(this, fragmentsList, getSupportFragmentManager());
        myRecyclerView.setAdapter(adapter);
    }
}