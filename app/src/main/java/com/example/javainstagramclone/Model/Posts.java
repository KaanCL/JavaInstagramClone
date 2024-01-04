package com.example.javainstagramclone.Model;

import java.util.Date;

public class Posts {


   public String email ;
   public String comment ;
    public String dowloadUrl;

    public Posts( String email, String comment,  String dowloadUrl) {
        this.email = email;
        this.comment = comment;
        this.dowloadUrl = dowloadUrl;
    }
}
