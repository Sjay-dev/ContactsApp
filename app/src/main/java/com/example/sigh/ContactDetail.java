package com.example.sigh;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

public class ContactDetail extends AppCompatActivity {

    TextView detailsName, detailsNumber;
    FloatingActionButton Edit, Delete;
    ImageView img;




    ActivityResultLauncher<Intent> resultLauncher ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);
        detailsName = findViewById(R.id.txtDetailsName);
        detailsNumber = findViewById(R.id.txtDetailsNumber);
        Edit = findViewById(R.id.btnEdit);
        Delete = findViewById(R.id.btnDelete);
        img = findViewById(R.id.ivDetailsImage);
        registerResult();

        Bundle bundle = getIntent().getExtras();
        String detailName = bundle.getString("Name_Key");
        detailsName.setText(detailName);

        String detailNumber = bundle.getString("Number_Key");
        detailsNumber.setText(detailNumber);

        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactDetail.this, EditContacts.class);

                Bundle bundle = new Bundle();
                bundle.putString("NAME_KEY", detailName);
                bundle.putString("NUMBER_KEY", detailNumber);

                intent.putExtras(bundle);

                startActivity(intent);


            }
        });

        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ContactDetail.this);

                builder.setMessage("Delete this contact?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        deleteContact(ContactDetail.this, detailName);
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();
            }
        });

        //TODO : To view instead
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("MYPREFS", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
                resultLauncher.launch(intent);






            }
        });

    }

   

    private void registerResult() {
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {
                        try{    Uri imageUri = o.getData().getData();
                            img.setImageURI(imageUri);}
                        catch (Exception e){
                            Toast.makeText(ContactDetail.this, "No image selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    public static void deleteContact(Context context, String contactName) {

        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = null;
        try {
            cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                    null,
                    ContactsContract.Contacts.DISPLAY_NAME + " = ?",
                    new String[]{contactName},
                    null);

            if (cursor != null && cursor.moveToFirst()) {
                @SuppressLint("Range")
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                Uri contactUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, id);
                int deleted = contentResolver.delete(contactUri, null, null);
                if (deleted > 0) {
                    Toast.makeText(context, "Contact deleted successfully", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            Toast.makeText(context, "Contact not found", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }






    }
}