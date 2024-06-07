package com.example.passwords.database;

import android.os.Parcel;
import android.os.Parcelable;

public class Password implements Parcelable {
    private int id;
    private String name;
    private String username;
    private String password;
    private String url;

    public Password(int id, String name, String username, String password, String url) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.url = url;
    }

    // Parcelable 构造函数
    protected Password(Parcel in) {
        id = in.readInt();
        name = in.readString();
        username = in.readString();
        password = in.readString();
        url = in.readString();
    }

    public static final Creator<Password> CREATOR = new Creator<Password>() {
        @Override
        public Password createFromParcel(Parcel in) {
            return new Password(in);
        }

        @Override
        public Password[] newArray(int size) {
            return new Password[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(url);
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
}
