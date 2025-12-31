package com.example.timelinelogging.data.entity;

public class Post {
    String time;
    String content;
    public Post(String time, String content){
        this.time=time;
        this.content=content;
    }
    public String getTime(){
        return time;
    }
    public String getContent(){
        return content;
    }
}
