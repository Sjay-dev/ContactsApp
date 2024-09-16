package com.example.sigh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentProviderOperation;
import android.content.OperationApplicationException;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class EditContacts extends AppCompatActivity {
    EditText Name , Number;

    Button Save;
    ImageView EditContactProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contacts);

        Name = findViewById(R.id.edtUpdateName);
        Number = findViewById(R.id.edtUpdateNumber);
        Save = findViewById(R.id.btnUpdate);
        EditContactProfileImage = findViewById(R.id.ivEditContactImage);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        String name = bundle.getString("NAME_KEY");
        String number = bundle.getString("NUMBER_KEY");

        Name.setText(name);
        Number.setText(number);

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<ContentProviderOperation> contentProviderOperation = new ArrayList<>();

                if (!Name.getText().toString().isEmpty()
                        && !Number.getText().toString().isEmpty()) {
                    contentProviderOperation.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                            .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                            .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                            .build());

                    //Adding name
                    contentProviderOperation.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                            .withValue(ContactsContract.Data.MIMETYPE,
                                    ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                            .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, Name.getText().toString()).build());

                    //Adding number
                    contentProviderOperation.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                            .withValue(ContactsContract.Data.MIMETYPE,
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                            .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, Number.getText().toString())
                            .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                                    ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build());

                    try {
                        getContentResolver().applyBatch(ContactsContract.AUTHORITY, contentProviderOperation);
                    } catch (OperationApplicationException | RemoteException e) {
                        throw new RuntimeException(e);
                    }

                    Toast.makeText(EditContacts.this, "Contact saved", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(EditContacts.this, "Fill in your name and number",
                            Toast.LENGTH_SHORT).show();}




            }
        });

        //TODO : EditContactProfileImage



    }
}