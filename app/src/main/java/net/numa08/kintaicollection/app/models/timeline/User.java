package net.numa08.kintaicollection.app.models.timeline;

public class User {

    public final String name;
    public final String id;
    public final String icon;
    public final String token;


    public User(String name, String id, String icon, String token) {
        this.name = name;
        this.id = id;
        this.icon = icon;
        this.token = token;
    }
}
