package com.example.revieit.Model;

public class Book {
    private String Name, Image, Review, Year, MenuId;

    public Book()
    {

    }

    public Book(String name, String image, String review, String year, String menuId)
    {
        Name = name;
        Image = image;
        Review = review;
        Year = year;
        MenuId = menuId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getReview() {
        return Review;
    }

    public void setReview(String review) {
        Review = review;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public String getMenuId() {
        return MenuId;
    }

    public void setMenuId(String menuId) {
        MenuId = menuId;
    }


}
