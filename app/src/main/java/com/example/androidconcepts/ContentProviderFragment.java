package com.example.androidconcepts;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ContentProviderFragment extends Fragment {

    EditText inputContact;
    EditText inputPhoneNumber;
    Button contentResolverButton;
    TextView contactReceived;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_content_provider, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputContact = view.findViewById(R.id.inputContactName);
        inputPhoneNumber = view.findViewById(R.id.inputPhoneNumber);
        contentResolverButton = view.findViewById(R.id.contentResolverButton);
        contactReceived = view.findViewById(R.id.contactReceived);

        inputPhoneNumber.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (inputPhoneNumber.getText().toString().trim().isEmpty() || inputContact.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getContext(), "Missing name or number", Toast.LENGTH_LONG).show();
                    return true; // Event handled
                } else {
                    if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_CONTACTS) !=
                            PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(getContext(), "access denied", Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(requireActivity(), new String[] {Manifest.permission.WRITE_CONTACTS}, 0);
                    } else {
                        createContact(inputContact.getText().toString(), inputPhoneNumber.getText().toString());
                    }
                }
            }
            return false; // Let the system handle the event
        });

        contentResolverButton.setOnClickListener(resolverButton -> {
            if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS) !=
                    PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(requireActivity(), new String[] {Manifest.permission.READ_CONTACTS}, 0);
            } else {
                contactReceived.setText(getFirstContact());
            }

        });

    }


    public void createContact(String name, String phoneNumber) {
        // Step 1: Create a new ContentValues object to hold the contact data
        ContentValues values = new ContentValues();
        values.put(ContactsContract.RawContacts.ACCOUNT_TYPE, (String) null);
        values.put(ContactsContract.RawContacts.ACCOUNT_NAME, (String) null);

        // Step 2: Insert the new raw contact
        Uri rawContactUri = requireContext().getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, values);
        long rawContactId = ContentUris.parseId(rawContactUri);

        // Step 3: Add the contact name
        values.clear();
        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name);
        Uri nameUri = requireContext().getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);

        // Step 4: Add the phone number
        values.clear();
        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber);
        Uri phoneUri = requireContext().getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);

        // Check if contact creation was successful
        if (nameUri != null && phoneUri != null) {
            Toast.makeText(getContext(), "Contact created successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Failed to create contact", Toast.LENGTH_SHORT).show();
        }

        // The ContactsProvider will automatically handle the aggregation of the raw contacts based on the data provided
    }

    @SuppressLint("Range")
    public String getFirstContact() {
        String firstContactDisplay = "No Contacts.";
        ContentResolver contentResolver = requireContext().getContentResolver();
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = {
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY,
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };
        String selection = ContactsContract.Contacts.HAS_PHONE_NUMBER + " = 1";
        String[] selectionArgs = null;
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " COLLATE LOCALIZED ASC LIMIT 1";
        Cursor cursor = contentResolver.query(uri, projection, selection, selectionArgs, sortOrder);
        if (cursor != null && cursor.moveToFirst()) {
            String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String fullName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY));
            String[] nameComponents = fullName.split(" ");
            String firstName = nameComponents[0];
            firstContactDisplay = String.format("Name: %s\nTel: %s", firstName, phoneNumber);
            cursor.close();
        } else {
            Toast.makeText(getContext(), "No contacts", Toast.LENGTH_LONG).show();
        }
        return firstContactDisplay;
    }
}