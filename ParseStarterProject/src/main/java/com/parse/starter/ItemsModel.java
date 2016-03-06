package com.parse.starter;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shibajyotidebbarma on 06/03/16.
 */
public class ItemsModel implements Parcelable {


    private String imageFile, title, date, price, phone, objectId, desc;

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


    public ItemsModel(){



    }


    protected ItemsModel(Parcel in) {
        String[] array= new String[7];

        in.readStringArray(array);


        title = array[0];
        price = array[1];
        date = array[2];
        imageFile = array[3];
        objectId = array[4];
        phone = array[5];
        desc = array[6];

    }

    public static final Creator<ItemsModel> CREATOR = new Creator<ItemsModel>() {
        @Override
        public ItemsModel createFromParcel(Parcel in) {
            return new ItemsModel(in);
        }

        @Override
        public ItemsModel[] newArray(int size) {
            return new ItemsModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeStringArray(new String[]{ this.title, this.price,
                this.date, this.imageFile, this.objectId, this.phone, this.desc});


    }
}
