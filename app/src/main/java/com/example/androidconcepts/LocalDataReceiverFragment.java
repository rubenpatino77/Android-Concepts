package com.example.androidconcepts;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Arrays;

public class LocalDataReceiverFragment extends Fragment {

    TextView dataRetrieved;
    public LocalDataReceiverFragment(){
        super(R.layout.local_receiver_fragment);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dataRetrieved = view.findViewById(R.id.dataReceived);
        Bundle bundle = getArguments();
        if(bundle != null && bundle.containsKey("arr")){
            int[] arrayReceived = bundle.getIntArray("arr");
            dataRetrieved.setText(Arrays.toString(arrayReceived));
        }
    }
}
