package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public final class User {
    public static final String getFirstName = "firstname";
    public static final String getLastName = "lastname";
    public static final String getPhone = "phone";
    public static final String getAddress = "address";
    public static final String getPassword = "password";
    public static final String getRole = "role";
    private long id;
    private String firstname;
    private String lastname;
    private String phone;
    private String address;
    private byte[] password;
    private String role = "ROLE_USER";

    public User() {
    }

    public User(String firstname, String lastname, String phone, String address, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
        this.address = address;
        this.password = password.getBytes();
    }

    public static User resultSetToUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setFirstname(resultSet.getString(User.getFirstName));
        user.setLastname(resultSet.getString(User.getLastName));
        user.setPhone(resultSet.getString(User.getPhone));
        user.setPassword(resultSet.getBytes(User.getPassword));
        user.setAddress(resultSet.getString(User.getAddress));
        user.setRole(resultSet.getString(User.getRole));
        return user;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public byte[] getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "{\"id\": " + id +
                ", \"firstname\": " + "\"" + firstname + "\"" +
                ", \"lastname\": " + "\"" + lastname + "\"" +
                ", \"phone\": " + "\"" + phone + "\"" +
                ", \"address\": " + "\"" + address + "\"" +
                ", \"role\": " + "\"" + role + "\"" + "}";
    }
}
