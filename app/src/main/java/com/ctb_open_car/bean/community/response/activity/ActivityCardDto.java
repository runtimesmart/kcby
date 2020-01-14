package com.ctb_open_car.bean.community.response.activity;


import com.ctb_open_car.bean.community.ResourceFileDto;

/**
 * @author L.Y.F
 * @create 2019-09-10 18:48
 */
public class ActivityCardDto extends ActivityDto {

    public ActivityCardDto() {}

    public ActivityCardDto(ActivityDto activity) {
        super.setActivityId(activity.getActivityId());
        super.setActivityTitle(activity.getActivityTitle());
        super.setActivityDeparturePlace(activity.getActivityDeparturePlace());
        super.setActivityUser(activity.getActivityUser());
        super.setActivityBegintime(activity.getActivityBegintime());
        super.setActivityEnrollEndtime(activity.getActivityEnrollEndtime());
        super.setActivityEndtime(activity.getActivityEndtime());
        super.setActivityEnrollLimit(activity.getActivityEnrollLimit());
        super.setActivityDesc(activity.getActivityDesc());
        super.setPublishTime(activity.getPublishTime());
        super.setActivityStat(activity.getActivityStat());
        this.activityCoverImage = activity.getActivityImageList().get(0);
    }

    /** 活动封面图 */
    private ResourceFileDto activityCoverImage;
    /** 是否点赞过 */
    private boolean praised;

    public ResourceFileDto getActivityCoverImage() {
        return activityCoverImage;
    }

    public void setActivityCoverImage(ResourceFileDto activityCoverImage) {
        this.activityCoverImage = activityCoverImage;
    }

    public boolean isPraised() {
        return praised;
    }

    public void setPraised(boolean praised) {
        this.praised = praised;
    }

    public void setActivity(ActivityDto activity) {
        super.setActivityId(activity.getActivityId());
        super.setActivityTitle(activity.getActivityTitle());
        super.setActivityDeparturePlace(activity.getActivityDeparturePlace());
        super.setActivityInviteIcon(activity.getActivityInviteIcon());
        super.setActivityImageList(activity.getActivityImageList());
        super.setActivityUser(activity.getActivityUser());
        super.setActivityBegintime(activity.getActivityBegintime());
        super.setActivityEnrollEndtime(activity.getActivityEnrollEndtime());
        super.setActivityEndtime(activity.getActivityEndtime());
        super.setActivityEnrollLimit(activity.getActivityEnrollLimit());
        super.setActivityDesc(activity.getActivityDesc());
        super.setPublishTime(activity.getPublishTime());
        super.setActivityStat(activity.getActivityStat());
    }
}
