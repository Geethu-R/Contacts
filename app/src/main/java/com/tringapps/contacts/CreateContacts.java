package com.tringapps.contacts;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.IOException;

/**
 * Created by geethu on 7/12/16.
 */

public class CreateContacts extends AppCompatActivity{

    public static final int REQUEST_CODE_FOR_CAMERA = 100;
    public static final int REQUEST_CODE_FOR_GALLERY = 101;
    String nameInput, numberInput, emailInput,imageStr;
    Bitmap imageInput;
    byte[] in;
    EditText nameView;
    EditText phoneNumberView;
    EditText emailView;
    ImageView imageView;
    Bitmap bm=null;
    Button save;
    int position = -1;
    String name,phoneNumber,email,image;
    SqliteController fakeObject;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save);

        fakeObject = new SqliteController(this);
        final Intent getintent = getIntent();



        nameView = (EditText) findViewById(R.id.name);
        phoneNumberView = (EditText) findViewById(R.id.phoneNumber);
        emailView = (EditText) findViewById(R.id.email);
        imageView = (ImageView) findViewById(R.id.image);

        nameView.setText("");
        phoneNumberView.setText("");
        emailView.setText("");
        imageView.setImageBitmap(null);
        save = (Button) findViewById(R.id.save);



        if( getintent.getExtras() != null ) {

            name = getintent.getStringExtra("nameView");
            phoneNumber = getintent.getStringExtra("phoneNumber");
            email = getintent.getStringExtra("emailView");
            image = getintent.getStringExtra("imageView");
            byte[] in = fakeObject.toByteOf(image);
            position = getintent.getExtras().getInt("position");
            nameView.setText(name);
            phoneNumberView.setText(phoneNumber);
            emailView.setText(email);
            Bitmap bm = fakeObject.getBitmap(in);
            imageView.setImageBitmap(bm);
        }



        View.OnClickListener listenerForButton = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(position>=0)
                {

                    nameInput = nameView.getText().toString();
                    numberInput = phoneNumberView.getText().toString();
                    emailInput = emailView.getText().toString();
                    imageInput = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                    in = fakeObject.getBytes(imageInput);
                    imageStr = fakeObject.toStringOf(in);



                    fakeObject.update(phoneNumber,numberInput,nameInput,emailInput,imageInput);
                    Toast.makeText(CreateContacts.this,"Edited...",Toast.LENGTH_SHORT).show();
                    getintent.putExtra("phoneNumber",numberInput);
                    getintent.putExtra("name",nameInput);
                    getintent.putExtra("email",emailInput);
                    getintent.putExtra("image",imageStr);
                    setResult(RESULT_OK,getintent);
                    finish();

                }
                else {

                    nameInput = nameView.getText().toString();
                    numberInput = phoneNumberView.getText().toString();
                    emailInput = emailView.getText().toString();


                    if(numberInput.matches("")) {
                        Toast.makeText(CreateContacts.this, "please enter the Phone Number", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        if(fakeObject.findOnce(numberInput))
                        {
                            popUpDialog("This number already exist","Replace It","Ignore");
                        }
                        else {
                            fakeObject.insert(numberInput, nameInput, emailInput, bm);
                        }
                        nameView.setText("");
                        phoneNumberView.setText("");
                        emailView.setText("");
                        imageView.setImageBitmap(null);
                        Toast.makeText(CreateContacts.this, "Saved...", Toast.LENGTH_SHORT).show();

                    }
                }
            }

        };
        save.setOnClickListener(listenerForButton);


        View.OnClickListener listenerForImageView = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                popUpDialog("Please Select one","Camera","Gallery");
            }

        };
        imageView.setOnClickListener(listenerForImageView);

    }

    private void popUpDialog(String title, String option1, String option2) {

        final String[] items = {option1,option2};
        final AlertDialog.Builder builder = new AlertDialog.Builder(CreateContacts.this);
        builder.setTitle(title);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (items[i].matches("Camera")) {

                    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(camera, REQUEST_CODE_FOR_CAMERA);

                }

                else if(items[i].matches("Gallery"))
                {

                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select File"), REQUEST_CODE_FOR_GALLERY);

                }
                else if(items[i].matches("Replace It"))
                {
                    fakeObject.update(numberInput,numberInput,nameInput,emailInput,imageInput);
                }
                else if(items[i].matches("Ignore"))
                {
                    nameView.setText("");
                    phoneNumberView.setText("");
                    emailView.setText("");
                    imageView.setImageBitmap(null);
                }
                else
                {
                    dialogInterface.dismiss();
                }
            }
        });

        builder.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQUEST_CODE_FOR_GALLERY && resultCode == RESULT_OK) {

            if (data != null) {
                try {
                    bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            imageView.setImageBitmap(bm);

        }
        else if (requestCode ==  REQUEST_CODE_FOR_CAMERA && resultCode == RESULT_OK)
        {
            bm = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bm);


        }

        else
        {
            Toast.makeText(CreateContacts.this,"No Image",Toast.LENGTH_LONG).show();
        }
    }


}