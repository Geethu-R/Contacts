package com.tringapps.contacts;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by geethu on 7/12/16.
 */

public class Information extends AppCompatActivity implements View.OnClickListener {

    public static final int REQUEST_CODE = 200;
    TextView nameView, phoneNumberView, emailView;
    ImageView imageView;
    int position;
    String name, phoneNumber, email,image;
    Button delete, edit;
    Bitmap bm;
    byte[] in;
    SqliteController fakeObject;


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.information);
            fakeObject = new SqliteController(Information.this);

            nameView = (TextView) findViewById(R.id.name);
            phoneNumberView = (TextView) findViewById(R.id.phoneNumber);
            emailView = (TextView) findViewById(R.id.email);
            imageView = (ImageView) findViewById(R.id.image);
            delete = (Button) findViewById(R.id.delete);
            edit = (Button) findViewById(R.id.edit);


            Intent getIntent = getIntent();
            name = getIntent.getStringExtra("nameView");
            phoneNumber = getIntent.getStringExtra("phoneNumber");
            email = getIntent.getStringExtra("emailView");
            image = getIntent.getStringExtra("imageView");
            in = fakeObject.toByteOf(image);
            position = getIntent.getExtras().getInt("position");



            nameView.setText(name);
            phoneNumberView.setText(phoneNumber);
            emailView.setText(email);
            bm = fakeObject.getBitmap(in);
            if(bm!= null) {
                imageView.setImageBitmap(bm);
            }


            delete.setOnClickListener(this);
            edit.setOnClickListener(this);





        }

        public void onClick(View view)
        {
            switch (view.getId())
            {
                case R.id.delete:


                    fakeObject.delete(phoneNumber);
                    Information.this.finish();
                    break;

                case R.id.edit:

                    Intent intent = new Intent (this,CreateContacts.class);
                    intent.putExtra("nameView",name);
                    intent.putExtra("phoneNumber",phoneNumber);
                    intent.putExtra("emailView",email);
                    intent.putExtra("imageView",image);
                    intent.putExtra("position",position);
                    startActivityForResult(intent, REQUEST_CODE);
                    break;


            }

        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if(requestCode == REQUEST_CODE && resultCode == RESULT_OK)
            {
                phoneNumber = data.getStringExtra("phoneNumber");
                name = data.getStringExtra("name");
                email = data.getStringExtra("email");
                image = data.getStringExtra("image");
                in = fakeObject.toByteOf(image);
                bm = fakeObject.getBitmap(in);
                nameView.setText(name);
                phoneNumberView.setText(phoneNumber);
                emailView.setText(email);
                if(bm != null) {
                    imageView.setImageBitmap(bm);
                }

            }
            else
            {
                Log.e("TAG","result cancelled......");
            }
        }

    }