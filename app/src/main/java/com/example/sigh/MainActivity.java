package com.example.sigh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton add;

    private RecyclerView recyclerView;

    private EditText searchBar;

    ArrayList<Contact> contactList =  new ArrayList<>();

    ContactsReviewAdapter adapter = new ContactsReviewAdapter(this , contactList );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchBar = findViewById(R.id.edtSearchBar);
        add = findViewById(R.id.btnAdd);
        Toolbar toolbar = findViewById(R.id.Toolbar);
        recyclerView = findViewById(R.id.RecyclerView);
        setSupportActionBar(toolbar);


        checkPermission();


        //Recylerview P

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ContactsReviewAdapter adapter = new ContactsReviewAdapter(this , contactList ) ;
        recyclerView.setAdapter(adapter);



        //Search bar for contact list
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        //For adding contacts
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this , Add_Contacts.class);
                startActivity(intent);
                ArrayList<Contact> addContacts = new ArrayList<>();

            }
        });


    }


    //Code for making the search look for contacts in real time
    @SuppressLint("NotifyDataSetChanged")
    private void filter(String text) {
        ArrayList<Contact> filteredcontacts = new ArrayList<>();
        for(Contact contact : contactList){


            if (    contact.getName().toLowerCase().contains(text.toLowerCase())) {

                filteredcontacts.add(contact);
                ContactsReviewAdapter adapter = new ContactsReviewAdapter(this , contactList);
                adapter.setFilterlist(filteredcontacts);

            }

            // Empty Search Result
            if (filteredcontacts.isEmpty()){
                ContactsReviewAdapter adapter = new ContactsReviewAdapter(this , contactList);
                adapter.notifyDataSetChanged();
//            Toast.makeText(this, "Contact doesn't exist", Toast.LENGTH_SHORT).show();


            }
        }

    }



    //Function for checking Permission
    @SuppressLint("NotifyDataSetChanged")
    private void checkPermission() {
        //condition
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]
                    {android.Manifest.permission.READ_CONTACTS}, 100);


            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.WRITE_CONTACTS}, 100);

        }




        else{
            getContactList();
            adapter.notifyDataSetChanged();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getContactList() {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        String sort = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;

        Cursor cursor = getContentResolver().query(
                uri , null , null , null , sort
        );

        if(cursor.getCount() >0){
            while (cursor.moveToNext()) {
                @SuppressLint("Range")
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));

                @SuppressLint("Range")
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                @SuppressLint("Range")
                String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                Contact contacts = new Contact();

                contacts.setName(name);

                contacts.setNumber(number);

                contactList.add(contacts);

                adapter.notifyDataSetChanged();

            }
        }
        else {
            //TODO Add new Contacts
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ){

            getContactList();

            adapter.notifyDataSetChanged();
        }

        else{
            checkPermission();
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();

        }

    }
}