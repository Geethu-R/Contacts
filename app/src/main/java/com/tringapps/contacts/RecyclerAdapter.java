package com.tringapps.contacts;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by geethu on 14/12/16.
 */
public class
RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {


    private ArrayList<ContactsItems> items;
    SqliteController fakeObject;


    public RecyclerAdapter(ArrayList<ContactsItems> arrayListItems) {
        items = arrayListItems;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageView;
        private TextView nameView;
        private TextView phoneNumberView;



        public MyViewHolder(View v) {
            super(v);

            imageView = (ImageView) v.findViewById(R.id.imageSub);
            nameView = (TextView) v.findViewById(R.id.nameSub);
            phoneNumberView = (TextView) v.findViewById(R.id.numberSub);
            v.setOnClickListener(this);
            Log.e("TAG","inside View holder................");
        }


        public void onClick(View v) {

            Context context = itemView.getContext();



            fakeObject = new SqliteController(context);
            int position = getAdapterPosition();
            String[] result = fakeObject.getRow(position);
            Intent intent = new Intent(context,Information.class);
            intent.putExtra("nameView",result[1]);
            intent.putExtra("phoneNumber",result[0]);
            intent.putExtra("emailView",result[2]);
            intent.putExtra("imageView",result[3]);
            intent.putExtra("position",position);
            context.startActivity(intent);


        }


    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.substitute, parent, false);
        return new MyViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.imageView.setImageBitmap(items.get(position).image);
        holder.nameView.setText(items.get(position).name);
        holder.phoneNumberView.setText(items.get(position).number);
        String pos = items.get(position).number;


    }

    @Override
    public int getItemCount() {
        return items.size();
    }


}

