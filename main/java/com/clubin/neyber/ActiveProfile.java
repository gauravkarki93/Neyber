package com.clubin.neyber;

import com.facebook.Profile;

/**
 * Created by Piyush on 7/29/2015.
 */
public class ActiveProfile {
    String id;
    String name;
    String facebook_link;
    String profilePic;
    String about;
    private static ActiveProfile activeProfile;

    private ActiveProfile(Profile p){
        this.id=p.getId();
        this.name=p.getName();
        this.facebook_link=p.getLinkUri().toString();
        this.profilePic=p.getProfilePictureUri(128,128).toString();
    }

    public static ActiveProfile getInstance(){
        if(activeProfile==null)
            return null;
        else
            return activeProfile;
    }

    public static ActiveProfile createNewInstance(Profile p){
        activeProfile = new ActiveProfile(p);
        return activeProfile;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFacebook_link() {
        return facebook_link;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public String getAbout() {
        return about;
    }
}
