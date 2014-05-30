package net.numa08.kintaicollection.app.models.timeline;

public class User {
    final String userId;
    final String userName;

    public User(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
