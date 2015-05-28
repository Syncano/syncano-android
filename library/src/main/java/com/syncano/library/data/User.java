package com.syncano.library.data;

/**
 * Default User class.
 * It's profile class contains default DataObject fields.
 * If you want to have custom user, you need to override AbstractUser and provide custom Profile class.
 */
public class User extends AbstractUser<Profile> {

    public User() {
    }

    public User(String userName, String password) {
        super(userName, password);
    }
}
