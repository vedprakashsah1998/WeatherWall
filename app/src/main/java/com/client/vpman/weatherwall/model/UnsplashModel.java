package com.client.vpman.weatherwall.model;

public class UnsplashModel {
    private String raw,full,regular,small,thumb,blur_hash,username,name,twitter_username,portfolio_url,bio,location,image_html,sponsorship,unsplash_portfolio;
    private String user_profile_small,user_profile_medium,user_profile_large,instagram_username;

    public UnsplashModel(String raw, String full, String regular, String small, String thumb, String blur_hash, String username, String name, String twitter_username, String portfolio_url, String bio, String location, String image_html, String sponsorship, String unsplash_portfolio, String user_profile_small, String user_profile_medium, String user_profile_large, String instagram_username) {
        this.raw = raw;
        this.full = full;
        this.regular = regular;
        this.small = small;
        this.thumb = thumb;
        this.blur_hash = blur_hash;
        this.username = username;
        this.name = name;
        this.twitter_username = twitter_username;
        this.portfolio_url = portfolio_url;
        this.bio = bio;
        this.location = location;
        this.image_html = image_html;
        this.sponsorship = sponsorship;
        this.unsplash_portfolio = unsplash_portfolio;
        this.user_profile_small = user_profile_small;
        this.user_profile_medium = user_profile_medium;
        this.user_profile_large = user_profile_large;
        this.instagram_username = instagram_username;
    }

    public String getInstagram_username() {
        return instagram_username;
    }

    public void setInstagram_username(String instagram_username) {
        this.instagram_username = instagram_username;
    }

    public String getUser_profile_small() {
        return user_profile_small;
    }

    public void setUser_profile_small(String user_profile_small) {
        this.user_profile_small = user_profile_small;
    }

    public String getUser_profile_medium() {
        return user_profile_medium;
    }

    public void setUser_profile_medium(String user_profile_medium) {
        this.user_profile_medium = user_profile_medium;
    }

    public String getUser_profile_large() {
        return user_profile_large;
    }

    public void setUser_profile_large(String user_profile_large) {
        this.user_profile_large = user_profile_large;
    }

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    public String getFull() {
        return full;
    }

    public void setFull(String full) {
        this.full = full;
    }

    public String getRegular() {
        return regular;
    }

    public void setRegular(String regular) {
        this.regular = regular;
    }

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getBlur_hash() {
        return blur_hash;
    }

    public void setBlur_hash(String blur_hash) {
        this.blur_hash = blur_hash;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTwitter_username() {
        return twitter_username;
    }

    public void setTwitter_username(String twitter_username) {
        this.twitter_username = twitter_username;
    }

    public String getPortfolio_url() {
        return portfolio_url;
    }

    public void setPortfolio_url(String portfolio_url) {
        this.portfolio_url = portfolio_url;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImage_html() {
        return image_html;
    }

    public void setImage_html(String image_html) {
        this.image_html = image_html;
    }

    public String getSponsorship() {
        return sponsorship;
    }

    public void setSponsorship(String sponsorship) {
        this.sponsorship = sponsorship;
    }

    public String getUnsplash_portfolio() {
        return unsplash_portfolio;
    }

    public void setUnsplash_portfolio(String unsplash_portfolio) {
        this.unsplash_portfolio = unsplash_portfolio;
    }
}
