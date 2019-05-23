package com.clubin.neyber;

/**
 * Created by GAURAV on 13-07-2015.
 */
public class QuesModel implements Cloneable{
    private String quesId;
    private String groupId ;
    private String profileId;
    private String ques;
    private String views;
    private String date;
    private String profileName;
    private String profilePic;



    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public AnswerModel topAns;

    private static QuesModel activeQues;

    boolean isAnswered;

    public boolean isAnswered() {
        return isAnswered;
    }

    public void setIsAnswered(boolean isAnswered) {
        this.isAnswered = isAnswered;
    }


   public QuesModel()
   {
       super();
       this.topAns = new AnswerModel();
   }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public static QuesModel getInstance(){
        if(activeQues == null){
            activeQues = new QuesModel();
        }
        return activeQues;
    }

    public static void createInstance(QuesModel model){
        try {
            activeQues = (QuesModel) model.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    public static void destroyInstance() {
        activeQues = null;
    }

    public String getQuesId() {
        return quesId;
    }

    public void setQuesId(String quesId) {
        this.quesId = quesId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getQues() {
        return ques;
    }

    public void setQues(String ques) {
        this.ques = ques;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        if(date==null)
            this.date="";
        else
        this.date =date;
    }
}
