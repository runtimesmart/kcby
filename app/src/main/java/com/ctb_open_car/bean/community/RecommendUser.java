package com.ctb_open_car.bean.community;

public class RecommendUser {
    private long userId;        //		用户ID
    private String nickName;    //用户昵称
    private String userIcon;    //用户头像
    private int userSex;   //用户性别 -1：未知、1：男、2：女
    private int userAuthStatus;   //用户认证状态 0：未认证、1：认证大V
    private String userMoodIcon;  //用户心情图标地址
    private int relationStatus;   //当前用户对此用户的关注状态  0：未关注、1：关注
    private long attentionCnt; //此用户的关注数量
    private long fansCnt;    //此用户的粉丝数量
}
