package com.example.sigh;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactsReviewAdapter extends RecyclerView.Adapter<ContactsReviewAdapter.ContactViewHolder>  {

    private Activity activity;
    private Context context;
    private String parentactivity;

    public ContactsReviewAdapter(Context context, ArrayList<Contact> contactList) {
        this.context = context;
        this.contactList = contactList;


        notifyDataSetChanged();
    }
    private static ArrayList<Contact> contactList;
    @NonNull
    @Override
    public ContactsReviewAdapter.ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contacts_view, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsReviewAdapter.ContactViewHolder holder, int position) {
        Contact contact = contactList.get(position);

        holder.contactNameTextView.setText(contact.getName());
        holder.contactNumberTextView.setText(contact.getNumber());



        // Assuming you have a method to get the image resource or URL
//        int imageResource = contact.getImageResource();
//        holder.contactImageView.setImageResource(imageResource);
//
    }

    @Override
    public int getItemCount() {return contactList.size();}

    @SuppressLint("NotifyDataSetChanged")
    public void setFilterlist(ArrayList<Contact> filteredcontacts){

        contactList = filteredcontacts;
        notifyDataSetChanged();


    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        ImageView contactImageView;
        TextView contactNameTextView , detailName , detailNumber ;
        TextView contactNumberTextView;
        RelativeLayout moreDetails;

        ImageView imgContact;
        @SuppressLint("CutPasteId")
        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            contactImageView = itemView.findViewById(R.id.IvContactImage);
            contactNameTextView = itemView.findViewById(R.id.txtName);
            contactNumberTextView = itemView.findViewById(R.id.txtNumber);

            detailName = itemView.findViewById(R.id.txtDetailsNumber);
            detailNumber = itemView.findViewById(R.id.txtDetailsNumber);
            moreDetails = itemView.findViewById(R.id.rlDetails);






            moreDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = contactList.get(getAdapterPosition()).getName();
                    String number = contactList.get(getAdapterPosition()).getNumber();

                    Intent intent = new Intent(context ,ContactDetail.class);

                    Bundle bundle = new Bundle();
                    bundle.putString("Name_Key" , name);
                    bundle.putString("Number_Key" , number);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });


        }
    }
}

