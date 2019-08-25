package com.example.meivents;

import android.view.View;
import android.widget.ImageView;

public class SharedEventItem {

    private String title;
    private String date;
    private int status;

    ImageView imageView;

    public SharedEventItem(String title, String date, int status){
        this.title = title;
        this.date = date;
        this.status = status;
    }


    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setImage(View v, int status){
        imageView = v.findViewById(R.id.statusImageView);
        if(status == Constants.WAITING) {
            imageView.setImageResource(R.drawable.ic_status_waiting);
        } else if(status == Constants.ACCEPTED){
            imageView.setImageResource(R.drawable.ic_status_accepted);
        } else if(status == Constants.DECLINED){
            imageView.setImageResource(R.drawable.ic_status_declined);
        }

    }
}
