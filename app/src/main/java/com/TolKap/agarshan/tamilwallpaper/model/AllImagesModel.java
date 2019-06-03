package com.TolKap.agarshan.tamilwallpaper.model;

public class AllImagesModel {

    private String image1;
    private String imageHigh;
    private Long count;
    private Long imageNumber;



    public String getImageHigh() {

        return imageHigh;
    }

    public void setImageHigh(String imageHigh) {
        this.imageHigh = imageHigh;
    }



    public Long getImageNumber() {
        return imageNumber;
    }

    public void setImageNumber(Long imageNumber) {
        this.imageNumber = imageNumber;
    }

    public AllImagesModel(String image1, Long count, Long imageNumber,String imageHigh) {
        this.image1 = image1;
        this.count = count;
        this.imageNumber = imageNumber;
        this.imageHigh =imageHigh;
    }



    public AllImagesModel() {

    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }




}
