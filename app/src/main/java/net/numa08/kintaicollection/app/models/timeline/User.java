package net.numa08.kintaicollection.app.models.timeline;

public class User {

    public final String name;
    public final String id;
    public final String icon;


    public User(String name, String id, String icon) {
        this.name = name;
        this.id = id;
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (!icon.equals(user.icon)) return false;
        if (!id.equals(user.id)) return false;
        if (!name.equals(user.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + id.hashCode();
        result = 31 * result + icon.hashCode();
        return result;
    }
}
