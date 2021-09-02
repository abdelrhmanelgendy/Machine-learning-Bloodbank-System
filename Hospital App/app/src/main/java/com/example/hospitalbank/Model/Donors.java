package com.example.hospitalbank.Model;

public class Donors {
    private String UserName;
    private String email;
    private String address;
    private String age;
    private String bloodType;
    private String lastDonationDay;
    private String lastDonationMonth;
    private String lastDonationYear;
    private String phoneNumber;
    private String profilePicture;
    private String userID;
    private String isDonor;
    private String prediction_msg;

    public String getPrediction_msg() {
        return prediction_msg;
    }

    public void setPrediction_msg(String prediction_msg) {
        this.prediction_msg = prediction_msg;
    }


    public Donors(String userName, String email, String address, String age, String bloodType, String lastDonationDay, String lastDonationMonth, String lastDonationYear, String phoneNumber, String profilePicture, String userID, String isDonor,String msg) {
        this.prediction_msg=msg;
        UserName = userName;
        this.email = email;
        this.address = address;
        this.age = age;
        this.bloodType = bloodType;
        this.lastDonationDay = lastDonationDay;
        this.lastDonationMonth = lastDonationMonth;
        this.lastDonationYear = lastDonationYear;
        this.phoneNumber = phoneNumber;
        this.profilePicture = profilePicture;
        this.userID = userID;
        this.isDonor = isDonor;
    }

    public Donors() {
    }

    public String getIsDonor() {
        return isDonor;
    }

    public void setIsDonor(String isDonor) {
        this.isDonor = isDonor;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getLastDonationDay() {
        return lastDonationDay;
    }

    public void setLastDonationDay(String lastDonationDay) {
        this.lastDonationDay = lastDonationDay;
    }

    public String getLastDonationMonth() {
        return lastDonationMonth;
    }

    public void setLastDonationMonth(String lastDonationMonth) {
        this.lastDonationMonth = lastDonationMonth;
    }

    public String getLastDonationYear() {
        return lastDonationYear;
    }

    public void setLastDonationYear(String lastDonationYear) {
        this.lastDonationYear = lastDonationYear;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    @Override
    public String toString() {
        return "Donors{" +
                "UserName='" + UserName + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", age='" + age + '\'' +
                ", bloodType='" + bloodType + '\'' +
                ", lastDonationDay='" + lastDonationDay + '\'' +
                ", lastDonationMonth='" + lastDonationMonth + '\'' +
                ", lastDonationYear='" + lastDonationYear + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", profilePicture='" + profilePicture + '\'' +
                ", userID='" + userID + '\'' +
                ", isDonor='" + isDonor + '\'' +
                ", prediction_msg='" + prediction_msg + '\'' +
                '}';
    }
}
