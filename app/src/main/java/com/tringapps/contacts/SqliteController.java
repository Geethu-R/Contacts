package com.tringapps.contacts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;



/**
 * Created by geethu on 3/12/16.
 */

public class SqliteController extends SQLiteOpenHelper {

    Bitmap bitmap = null;
    byte[] in = null;




    public SqliteController(Context applicationContext) {

        super(applicationContext, "androidsqlite.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query;

        query = "CREATE TABLE DummyContacts (Phone_Number TEXT, Name TEXT,EMAIL TEXT,Image BLOB)";

        sqLiteDatabase.execSQL(query);
        Log.e("TAG","created..............................................");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        String query;

        query = "DROP TABLE IF EXISTS DummyContacts";

        sqLiteDatabase.execSQL(query);

        Log.e("TAG","dropped..................................");

    }


    public void insert(String num, String name, String email, Bitmap bm)
    {
        SQLiteDatabase database = this.getWritableDatabase();
            in = getBytes(bm);

        ContentValues values = new ContentValues();
        values.put("Phone_Number",num);
        values.put("Name",name);
        values.put("EMAIL",email);
        values.put("Image",in);
        database.insert("DummyContacts",null,values);
        Log.e("TAG","insert.....................");
    }

    public  ArrayList<ContactsItems>  getAll() {

        ArrayList<ContactsItems> arrayListItems = new ArrayList<>();

        String query;
        String[] it = null;
        query = "SELECT *FROM DummyContacts ORDER BY Name ASC";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(query,null);

        if(cursor.moveToFirst())
        {
            do {

                ContactsItems item = new ContactsItems();
                item.number=cursor.getString(0);
                item.name = cursor.getString(1);
                item.email = cursor.getString(2);
                in = cursor.getBlob(3);
                item.image = getBitmap(in);
                arrayListItems.add(item);


            }while(cursor.moveToNext());
        }
        return arrayListItems;
    }

    public String[] getRow(int position) {


        String query;
        String[] result = new String[4];
        query = "SELECT *FROM DummyContacts ORDER BY Name ASC";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(query,null);

        if(cursor.moveToFirst())
        {
            for(int i = 0;i<=position;i++) {


                if (i == position) {

                    result[0] = cursor.getString(0);
                    result[1] = cursor.getString(1);
                    result[2] = cursor.getString(2);
                    in = cursor.getBlob(3);
                    result[3] = toStringOf(in);

                }
                cursor.moveToNext();
            }


        }
        return result;
    }

    public void delete(String phoneNumber) {

        String query;

        query = "DELETE FROM DummyContacts WHERE Phone_Number =" +phoneNumber;
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL(query);


    }

    public void update(String key, String phoneNumber, String name, String email, Bitmap image)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        in = getBytes(image);

        ContentValues cv = new ContentValues();
        cv.put("Phone_Number",phoneNumber);
        cv.put("Name",name);
        cv.put("Email",email);
        cv.put("Image", in);
        database.update("DummyContacts", cv, "Phone_Number="+key, null);
    }

    public static byte[] getBytes(Bitmap bitmap) {

        if(bitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
            return stream.toByteArray();

        }
        else
        {

            return null;
        }
    }

    public static Bitmap getBitmap(byte[] in) {

        if(in != null) {
            return BitmapFactory.decodeByteArray(in, 0, in.length);
        }

        else {

            return null;
        }
    }
    public String toStringOf(byte[] in)
    {
        if(in != null) {
            return Base64.encodeToString(in, Base64.DEFAULT);
        }
        else{

            return null;
        }
    }
    public byte[] toByteOf(String image)
    {
        if(image != null) {
            return Base64.decode(image, Base64.DEFAULT);
        }
        else
        {

            return null;
        }
    }

    public boolean findOnce(String numberInput) {
        String query;

        query = "Select * FROM DummyContacts WHERE Phone_Number ="+numberInput;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(query,null);

        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
}
