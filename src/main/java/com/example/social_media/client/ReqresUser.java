package com.example.social_media.client;

import lombok.Data;

@Data
public class ReqresUser {
    private int id;
    private String email;
    private String first_name;
    private String last_name;
    private String avatar;
}
