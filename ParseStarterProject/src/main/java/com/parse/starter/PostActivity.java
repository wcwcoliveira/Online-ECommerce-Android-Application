package com.parse.starter;


import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;

/**
 * Created by shibajyotidebbarma on 06/03/16.
 */
public class PostActivity extends ActionBarActivity {


    private String selectedImagePath = "";
    ImageView postImage;
    Spinner regionSpinner, categorySpinner;
    ParseQueryAdapter<ParseObject> regionAdapter, categoryAdapter;
    ParseObject regionObject, categoryObject;
    ScrollView scrollView;
    EditText editText_title, editText_desc, editText_price, editText_password, editText_phone;
    Button submit;
    Bitmap bitmapPost = null;
    ProgressDialog dialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scrollView = (ScrollView) findViewById(R.id.scroll_view_post);
        regionSpinner = (Spinner) findViewById(R.id.spinnerRegion);
        categorySpinner = (Spinner) findViewById(R.id.spinnerCategory);
        editText_desc = (EditText) findViewById(R.id.et_desc);
        editText_title = (EditText) findViewById(R.id.et_title);
        editText_price = (EditText) findViewById(R.id.et_price);
        editText_password = (EditText) findViewById(R.id.et_password);
        editText_phone = (EditText) findViewById(R.id.et_phone);
        submit = (Button) findViewById(R.id.btn_post);

        setActionBar();
        scrollView.setVerticalScrollBarEnabled(false);
        regionSpinnerSetup();
        categorySpinnerSetup();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText_price.getText().toString().isEmpty()
                        || editText_title.getText().toString().isEmpty()
                        || editText_password.getText().toString().isEmpty()
                        || editText_desc.getText().toString().isEmpty()) {


                    Toast.makeText(PostActivity.this, "Please fill in all the fields", Toast.LENGTH_LONG).show();




                }

                else{
                    dialog = new ProgressDialog(PostActivity.this);
                    dialog.setTitle("Posting");
                    dialog.setMessage("Please Wait");
                    dialog.show();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmapPost.compress(Bitmap.CompressFormat.JPEG, 75, stream);
                    final ParseFile parseFile = new ParseFile(stream.toByteArray());


                    parseFile.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (dialog.isShowing())
                                dialog.dismiss();


                            if(e==null){
                                final ParseObject object = new ParseObject("Produce");
                                object.put("title", editText_title.getText().toString());
                                object.put("price", Double.valueOf(editText_price.getText().toString()));
                                object.put("desc", editText_desc.getText().toString());
                                object.put("password", editText_password.getText().toString());

                                object.put("cat_object", categoryObject);
                                object.put("reg_object", regionObject);
                                object.put("phone", Double.valueOf(editText_phone.getText().toString()));
                                object.put("image", parseFile);

                                object.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {

                                        if (dialog.isShowing())
                                            dialog.dismiss();




                                        if(e == null){


                                            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Product");
                                            query.orderByDescending("updatedAt");
                                            query.getFirstInBackground(new GetCallback<ParseObject>() {
                                                @Override
                                                public void done(ParseObject parseObject, ParseException e) {
                                                    if(object !=null){

                                                        Log.d("Product", "the get first request failed");


                                                    }

                                                    else{

                                                        Intent i =new Intent(PostActivity.this, ActivityItemDetails.class);
                                                        i.putExtra("price", object.getNumber("price").toString());
                                                        i.putExtra("phone", object.getNumber("phone").toString());
                                                        i.putExtra("title", object.getString("title"));
                                                        i.putExtra("date", object.getCreatedAt().toString());
                                                        i.putExtra("objectId", object.getObjectId());
                                                        i.putExtra("desc", object.getString("desc"));
                                                        ParseFile file = object.getParseFile("image");
                                                        i.putExtra("url", file.getUrl());
                                                        startActivity(i);
                                                        finish();




                                                    }
                                                }
                                            });

                                        }

                                    }
                                });


                            }


                        }
                    });

                }


                }
        });


        postImage =(ImageView) findViewById(R.id.imageViewDetail);
        Uri extras = getIntent().getParcelableExtra("extras");
        int image_from = getIntent().getIntExtra("image_from", 0);
        extractImage(extras, image_from);
    }

    public String getAbsolutePath(Uri uri) {


        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();

            return cursor.getString(column_index);


        } else

            return null;

    }


    public void regionSpinnerSetup() {

        ParseQueryAdapter.QueryFactory<ParseObject> factory = new ParseQueryAdapter.QueryFactory<ParseObject>() {
            @Override
            public ParseQuery create() {
                ParseQuery query = new ParseQuery("Region");
                return query;

            }
        };


        regionAdapter = new ParseQueryAdapter<ParseObject>(this, factory);
        regionAdapter.setTextKey("name");
        regionSpinner.setAdapter(regionAdapter);
        regionSpinner.setOnItemSelectedListener(new RegionSpinnerListener());
        regionSpinner.setSelection(1);


    }


    class RegionSpinnerListener implements Spinner.OnItemSelectedListener {


        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            ParseObject theSelectedObject = regionAdapter.getItem(i);
            Log.e("ABC", "Name is :" + theSelectedObject.getString("name") + "objectId is " + theSelectedObject.getObjectId());
            regionObject = theSelectedObject;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {


        }
    }

    public void categorySpinnerSetup() {

        ParseQueryAdapter.QueryFactory<ParseObject> factory = new ParseQueryAdapter.QueryFactory<ParseObject>() {
            @Override
            public ParseQuery create() {

                ParseQuery query = new ParseQuery("Category");
                return query;

            }
        };


        categoryAdapter = new ParseQueryAdapter<ParseObject>(this, factory);
        categoryAdapter.setTextKey("name");
        categorySpinner.setAdapter(categoryAdapter);
        categorySpinner.setSelection(1);
        categorySpinner.setOnItemSelectedListener(new CategorySpinnerListener());


    }


    class CategorySpinnerListener implements Spinner.OnItemSelectedListener {


        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            ParseObject theSelectedObject = categoryAdapter.getItem(i);
            Log.e("ABC", "Name is :" + theSelectedObject.getString("name") +

                    "objectId is " + theSelectedObject.getObjectId());
            categoryObject = theSelectedObject;
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }


    }

    public void extractImage(Uri data, int image_from) {


        if (image_from == 50)

            selectedImagePath = getAbsolutePath(data);

        else if (image_from == 51)

            selectedImagePath = data.getPath();

        Bitmap bitmap = decoderFile(selectedImagePath);
        postImage.setImageBitmap(bitmap);
        bitmapPost = bitmap;


    }


    private void setActionBar() {

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.blue)));


    }


    public Bitmap decoderFile(String path) {
        try {

            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;

            BitmapFactory.decodeFile(path, o);

            final int REQUIRED_SIZE = 160;

            int scale = 1;

            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;


            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;

            return BitmapFactory.decodeFile(path, o2);

        } catch (Throwable e) {
            e.printStackTrace();

        }

        return null;


    }





}







