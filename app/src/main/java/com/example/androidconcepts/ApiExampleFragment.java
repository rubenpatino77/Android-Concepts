package com.example.androidconcepts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.androidconcepts.services.ApiFetchService;

import java.util.Objects;

public class ApiExampleFragment extends Fragment{
    public ApiExampleFragment(){
        super(R.layout.api_example_fragment);
    }

    TextView tempDisplay;
    EditText cityEditText;

    private final BroadcastReceiver serviceDoneReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Service is done. Extract the string data from the intent
            String data = intent.getStringExtra("data");

            if(Objects.equals(data, "Location not found.")){
                tempDisplay.setText(data);
            } else {
                String temperatureString = getString(R.string.fahrenheit_format, data);
                tempDisplay.setText(temperatureString);
            }

        }
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tempDisplay = view.findViewById(R.id.tempDisplay);
        cityEditText = view.findViewById(R.id.fetchApiEditText);

        cityEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        cityEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                String city = cityEditText.getText().toString().trim();

                if (!city.isEmpty()) {
                    ApiFetchService service = new ApiFetchService();
                    Intent intent = new Intent(getContext(), service.getClass());
                    intent.putExtra("location", city);
                    requireContext().startService(intent);
                    return true; // Consume the event
                } else {
                    Toast.makeText(getContext(), "Please enter a city", Toast.LENGTH_SHORT).show();
                }
            }
            return false; // Let the system handle the event
        });





        IntentFilter filter = new IntentFilter("com.example.myapp.SERVICE_DONE");
        requireActivity().registerReceiver(serviceDoneReceiver, filter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requireActivity().unregisterReceiver(serviceDoneReceiver);
    }
}

