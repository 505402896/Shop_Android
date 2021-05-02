package com.example.a50540.lastorder.entity;

public class Good {
  private String picUrl;
  private String title;
  private String price;

  public Good(String picUrl, String title, String price) {
    this.picUrl = picUrl;
    this.title = title;
    this.price = price;
  }

  public String getPicUrl() {
    return picUrl;
  }

  public void setPicUrl(String picUrl) {
    this.picUrl = picUrl;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getPrice() {
    return price;
  }

  public void setPrice(String price) {
    this.price = price;
  }

  @Override
  public String toString() {
    return "Good{" +
            "picUrl='" + picUrl + '\'' +
            ", title='" + title + '\'' +
            ", price='" + price + '\'' +
            '}';
  }
}
