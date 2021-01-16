package com.example.multiappvjava;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.logging.*;

import androidx.constraintlayout.helper.widget.Layer;

import com.example.multiappvjava.entity.Contact;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter implements Filterable {
    Context context;
    ArrayList<Contact> conArray;
    ArrayList<Contact> filteredContacts;
    String filterString;
    ContactFilter cf = new ContactFilter();

    public CustomAdapter(Context context, ArrayList<Contact> conArray) {
        this.context = context;
        this.conArray = conArray;
        this.filteredContacts = conArray;
    }

    @Override
    public int getCount() {
        return filteredContacts.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredContacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return filteredContacts.indexOf(getItem(position));
    }

    public void filter(String newText) {
    }

    private class ViewHolder {
        ImageView avatar;
        TextView contactName;
        TextView contactNr;
        ImageView call;
        Layer edit;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView = mInflater.inflate(R.layout.contact_item, null);
        holder = new ViewHolder();

        holder.avatar = (ImageView) convertView.findViewById(R.id.avatarView);
        holder.contactName = (TextView) convertView.findViewById(R.id.nameView);
        holder.contactNr = (TextView) convertView.findViewById(R.id.numberView);
        holder.call = (ImageView) convertView.findViewById(R.id.call);
        holder.edit = (Layer) convertView.findViewById(R.id.editLayer);

        Contact contact = filteredContacts.get(position); //TODO: change to filtered array
        System.out.println(getCount());

        //if image is null, set depending on gender, else set custom image
        if (contact.getImage() == null) {
            if (contact.isMale()) holder.avatar.setImageResource(R.drawable.ic_man);
            else holder.avatar.setImageResource(R.drawable.ic_woman);
        } else {
            byte[] decodedString = Base64.decode(contact.getImage(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.avatar.setImageBitmap(decodedByte);
        }

        holder.contactName.setText(contact.getName());
        holder.contactNr.setText(contact.getNumber());
        holder.call.setOnClickListener(v -> {
//            Toast.makeText(context, "Making a call", Toast.LENGTH_LONG).show();
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + contact.getNumber()));
            context.startActivity(callIntent);
        });

        holder.edit.setOnClickListener(v -> {
            Intent intent = new Intent(context, CreateContact.class);
            intent.putExtra("ContactId", position);
            context.startActivity(intent);
        });

        return convertView;
    }

    private class ContactFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<Contact> list = conArray;

            int count = list.size();
            final ArrayList<Contact> nList = new ArrayList<>();

            for (int i = 0; i < count; i++) {
                if (conArray.get(i).getName().contains(filterString) || conArray.get(i).getNumber().contains(filterString))
                    nList.add(conArray.get(i));
            }

            results.values = nList;
            results.count = nList.size();
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredContacts = (ArrayList<Contact>) results.values;
            notifyDataSetChanged();
        }
    }

    @Override
    public Filter getFilter() {
        return cf;
    }
}
