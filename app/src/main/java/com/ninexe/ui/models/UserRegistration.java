/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.models;

import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;

/**
 * Created by nagesh on 28/10/15.
 */
public class UserRegistration {
    String name;
    String email;
    String password;
    String phone;
    TypedFile profilePic;

    public TypedString getName() {
        return new TypedString(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public TypedString getEmail() {
        return new TypedString(email);
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public TypedString getPassword() {
        return new TypedString(password);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public TypedString getPhone() {
        return new TypedString(phone);
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public TypedFile getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(TypedFile profilePic) {
        this.profilePic = profilePic;
    }


}
