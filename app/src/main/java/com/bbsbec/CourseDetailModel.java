package com.bbsbec;

import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import java.io.Serializable;

public class CourseDetailModel implements Serializable {
    private String course_description;
    private String course_actual_price;
    private String course_discounted_price;
    private String course_source;
    private String url_id;

    public String getUrl_id() {
        return url_id;
    }

    public void setUrl_id(String url_id) {
        this.url_id = url_id;
    }

    private String course_image;
    private boolean is_course_certificate;
    private String course_type;
    private String course_rating;
    private String heading;
    private String is_youtube_link;
    private String course_url;

    public String getCourse_type() {
        return course_type;
    }

    public void setCourse_type(String course_type) {
        this.course_type = course_type;
    }

    public String getCourse_description() {
        return course_description;
    }

    public void setCourse_description(String course_description) {
        this.course_description = course_description;
    }

    public String getCourse_actual_price() {
        return course_actual_price;
    }

    public void setCourse_actual_price(String course_actual_price) {
        this.course_actual_price = course_actual_price;
    }

    public String getCourse_discounted_price() {
        return course_discounted_price;
    }

    public void setCourse_discounted_price(String course_discounted_price) {
        this.course_discounted_price = course_discounted_price;
    }

    public String getCourse_source() {
        return course_source;
    }

    public void setCourse_source(String course_source) {
        this.course_source = course_source;
    }

    public String getCourse_image() {
        return course_image;
    }

    public void setCourse_image(String course_image) {
        this.course_image = course_image;
    }

    public boolean isIs_course_certificate() {
        return is_course_certificate;
    }

    public void setIs_course_certificate(boolean is_course_certificate) {
        this.is_course_certificate = is_course_certificate;
    }

    public String getCourse_rating() {
        return course_rating;
    }

    public void setCourse_rating(String course_rating) {
        this.course_rating = course_rating;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getIs_youtube_link() {
        return is_youtube_link;
    }

    public void setIs_youtube_link(String is_youtube_link) {
        this.is_youtube_link = is_youtube_link;
    }

    public String getCourse_url() {
        return course_url;
    }

    public void setCourse_url(String course_url) {
        this.course_url = course_url;
    }

    public CourseDetailModel(String course_description, String course_actual_price, String course_discounted_price, String course_source, String course_image, boolean is_course_certificate, String course_rating, String course_type, String heading, String is_youtube_link, String course_url, String url_id) {
        this.course_description = course_description;
        this.course_actual_price = course_actual_price;
        this.course_discounted_price = course_discounted_price;
        this.course_source = course_source;
        this.course_image = course_image;
        this.is_course_certificate = is_course_certificate;
        this.course_type = course_type;
        this.course_rating = course_rating;
        this.heading = heading;
        this.is_youtube_link = is_youtube_link;
        this.course_url = course_url;
        this.url_id = url_id;
    }


}
