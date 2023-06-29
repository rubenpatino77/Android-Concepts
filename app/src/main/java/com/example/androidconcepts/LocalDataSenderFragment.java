package com.example.androidconcepts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class LocalDataSenderFragment extends Fragment {
    Button arr1Button;
    Button arr2Button;
    Button arr3Button;

    public LocalDataSenderFragment(){
        super(R.layout.local_sender_fragment);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        arr1Button = view.findViewById(R.id.button_send_arr_1);
        arr2Button = view.findViewById(R.id.button_send_arr_2);
        arr3Button = view.findViewById(R.id.button_send_arr_3);

        arr1Button.setOnClickListener(view1 -> {
            int[] arr1 = {1, 2, 3};
            sendArrayObject(arr1);
        });

        arr2Button.setOnClickListener(view2 -> {
            int[] arr2 = {3, 2, 1};
            sendArrayObject(arr2);
        });

        arr3Button.setOnClickListener(view3 -> {
            int[] arr3 = {0, 0, 0};
            sendArrayObject(arr3);
        });
    }

    public void sendArrayObject(int[] arr){
        LocalDataReceiverFragment receiverFragment = new LocalDataReceiverFragment();
        Bundle bundle = new Bundle();
        bundle.putIntArray("arr", arr);
        receiverFragment.setArguments(bundle);

        requireActivity().getSupportFragmentManager().beginTransaction().replace(getId()+1, receiverFragment).commit();
    }
}
