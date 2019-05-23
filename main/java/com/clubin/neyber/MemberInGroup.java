package com.clubin.neyber;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by GAURAV on 13-07-2015.
 */

@JsonAutoDetect(fieldVisibility= JsonAutoDetect.Visibility.ANY)

public class MemberInGroup {
    @JsonProperty("name")
    private String name;
    @JsonProperty("image")
    private String image;
    @JsonProperty("id")
    private String id;
    @JsonProperty("about")
    private String about;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    @Override
    public String toString() {
        return "MemberInGroup{" +
                "name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", id='" + id + '\'' +
                ", about='" + about + '\'' +
                '}';
    }
}
