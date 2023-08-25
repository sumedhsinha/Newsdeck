package com.example.application1;

public class RssFeed {
    private String url;
    private String titleTag;
    private String item;
    private String linkTag;
    private String imageTag;

    public RssFeed(String url, String item, String titleTag, String linkTag, String imageTag) {
        this.url = url;
        this.item = item;
        this.titleTag = titleTag;
        this.linkTag = linkTag;
        this.imageTag = imageTag;
    }

    public String getUrl() {
        return url;
    }

    public String getItem(){
        return item;
    }
    public String getTitleTag() {
        return titleTag;
    }

    public String getLinkTag() {
        return linkTag;
    }

    public String getImageTag() {
        return imageTag;
    }
}

