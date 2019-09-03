package com.example.meivents;

import android.view.View;
import android.widget.ImageView;

public class SharedEventItem {

    private String id, title, date, status;

    ImageView imageView;

    public SharedEventItem(String id, String title, String date, String status){
        this.id = id;
        this.title = title;
        this.date = date;
        this.status = status;
    }


    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setImage(View v, String status){
        imageView = v.findViewById(R.id.status_imageView);
        if(status.equals(Constants.WAITING)) {
            imageView.setImageResource(R.drawable.ic_status_waiting);
        } else if(status.equals(Constants.ACCEPTED)){
            imageView.setImageResource(R.drawable.ic_status_accepted);
        } else if(status.equals(Constants.DECLINED)){
            imageView.setImageResource(R.drawable.ic_status_declined);
        }

    }
}
