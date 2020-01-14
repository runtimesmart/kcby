package com.ctb_open_car.bean.community.response.activity;

/**
 * @author L.Y.F
 * @create 2019-09-06 17:42
 */
public class ActivityDetailDto extends ActivityDto {

    public ActivityDetailDto() {}

    public ActivityDetailDto(ActivityDto activity) {
        super.setActivityId(activity.getActivityId());
        super.setActivityTitle(activity.getActivityTitle());
        super.setActivityInviteIcon(activity.getActivityInviteIcon());
        super.setActivityImageList(activity.getActivityImageList());
        super.setActivityDeparturePlace(activity.getActivityDeparturePlace());
        super.setActivityUser(activity.getActivityUser());
        super.setActivityBegintime(activity.getActivityBegintime());
        super.setActivityEnrollEndtime(activity.getActivityEnrollEndtime());
        super.setActivityEndtime(activity.getActivityEndtime());
        super.setActivityEnrollLimit(activity.getActivityEnrollLimit());
        super.setActivityDesc(activity.getActivityDesc());
        super.setPublishTime(activity.getPublishTime());
        super.setActivityStat(activity.getActivityStat());
    }

    /** 是否点赞过 */
    private boolean praised;

    public boolean isPraised() {
        return praised;
    }

    public void setPraised(boolean praised) {
        this.praised = praised;
    }
}
