package com.clubin.neyber;

import android.util.Log;

import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;

/**
 * Created by GAURAV on 12-07-2015.
 */
public class GroupModel implements Cloneable, Serializable{

    @JsonProperty("group_id")
    private long groupId;

    @JsonProperty("name")
    private String groupName;

    @JsonProperty("admin_id")
    private String adminId;

    @JsonProperty("group_info")
    private String groupInfo;

    @JsonProperty("category")
    private String groupCategory;

    @JsonProperty("group_image")
    private String groupImage;

    @JsonProperty("institute")
    private String institute;

    @JsonProperty("latitude")
    private double latitude;

    @JsonProperty("longitude")
    private double longitude;

    @JsonProperty("distance")
    private int distance;

    @JsonProperty("city")
    private String city;



    private int memCount;

    public int getMemCount() {
        return memCount;
    }

    public void setMemCount(int memCount) {
        this.memCount = memCount;
    }

    private static GroupModel activeGroup;

    public static GroupModel getInstance(){
        if(activeGroup == null){
            activeGroup = new GroupModel();
        }
        return activeGroup;
    }

    public static void createInstance(GroupModel model){
        Log.d("TAG", "GroupID should be: "+model.getGroupId());
        try {
            activeGroup = (GroupModel) model.clone();
        } catch (CloneNotSupportedException e) {
            Log.d("TAG", "GroupID should be: "+model.getGroupId());
            e.printStackTrace();
        }
        Log.d("TAG", "GroupID is: "+activeGroup.getGroupId());
    }

    public static void destroyInstance() {
        activeGroup = null;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getInstitute() {
        return institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getGroupInfo() {
        return groupInfo;
    }

    public void setGroupInfo(String groupInfo) {
        this.groupInfo = groupInfo;
    }

    public String getGroupCategory() {
        return groupCategory;
    }

    public void setGroupCategory(String groupCategory) {
        this.groupCategory = groupCategory;
    }

    public String getGroupImage() {
        return groupImage;
    }

    public void setGroupImage(String groupImage) {
        this.groupImage = groupImage;
    }
}
