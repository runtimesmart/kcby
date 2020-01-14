package com.ctb_open_car.engine.net;


import com.ctb_open_car.bean.areacode.AreaCodeBean;
import com.ctb_open_car.bean.community.ActivityDetailData;
import com.ctb_open_car.bean.community.BannerData;
import com.ctb_open_car.bean.community.FeedDetailData;
import com.ctb_open_car.bean.community.request.TopicListReq;
import com.ctb_open_car.bean.community.response.CommentData;
import com.ctb_open_car.bean.community.response.EventData;
import com.ctb_open_car.bean.community.response.FanData;
import com.ctb_open_car.bean.community.response.FocusData;
import com.ctb_open_car.bean.community.response.FocusFeedData;
import com.ctb_open_car.bean.community.response.GroupData;
import com.ctb_open_car.bean.community.response.GroupRecommendData;
import com.ctb_open_car.bean.community.response.HotFeedData;
import com.ctb_open_car.bean.community.response.NearbyData;
import com.ctb_open_car.bean.community.response.RecommendData;
import com.ctb_open_car.bean.community.response.RoadData;
import com.ctb_open_car.bean.community.response.SNSMapData;
import com.ctb_open_car.bean.community.response.SearchUserData;
import com.ctb_open_car.bean.community.response.UserData;
import com.ctb_open_car.bean.im.CarLibaryBean;
import com.ctb_open_car.bean.im.GroupDetailsBean;
import com.ctb_open_car.bean.im.GroupMemberListBean;
import com.ctb_open_car.bean.im.SearchGroupBean;
import com.ctb_open_car.bean.im.TagDtoBean;
import com.ctb_open_car.bean.im.TagListBean;
import com.ctb_open_car.bean.login.UserBean;
import com.ctb_open_car.bean.newsbean.ColumnDataBean;
import com.ctb_open_car.bean.newsbean.NewsInfoBean;
import com.ctb_open_car.bean.userInfo.CarListBean;
import com.ctb_open_car.bean.userInfo.UserInfoBean;
import com.ctb_open_car.bean.vehicletools.VehicleToolsBean;
import com.google.gson.JsonObject;
import com.rxretrofitlibrary.Api.BaseResultEntity;

import java.util.HashMap;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * 网络接口定义到此类里面
 *
 * @Query、@QueryMap：用于Http Get请求传递参数
 * @Field：用于Post方式传递参数,需要在请求接口方法上添加@FormUrlEncoded,即以表单的方式传递参数
 * @Body：用于Post,根据转换方式将实例对象转化为对应字符串传递参数.比如Retrofit添加GsonConverterFactory 则是将body转化为gson字符串进行传递
 * @Path：用于URL上占位符
 * @Part：配合@Multipart使用,一般用于文件上传
 * @Header：添加http header
 * @Headers：跟@Header作用一样,只是使用方式不一样,@Header是作为请求方法的参数传入,@Headers是以固定方式直接添加到请求方法上
 */

public interface HttpRequestInterface {

    //    最新活动列表
    @POST("/a/l/activity/hot/list")
    Observable<BaseResultEntity<EventData>> getHotEventsList(@QueryMap HashMap<String, String> queryMap);

    //    本地活动列表
    @POST("/a/l/activity/local/list")
    Observable<BaseResultEntity<EventData>> getLocalEventsList(@QueryMap HashMap<String, String> queryMap);

    @POST("/a/l/feed/home/topic/list")
    Observable<BaseResultEntity<TopicListReq>> getTopitList(@Body HashMap<String, String> queryMap);

    //    关注动态
    @POST("/a/l/feed/home/attention/list")
    Observable<BaseResultEntity<FocusFeedData>> getFocusFeedList(@QueryMap HashMap<String, String> queryMap);

    //    热门动态
    @POST("/a/l/feed/home/hot/list")
    Observable<BaseResultEntity<HotFeedData>> getHotFeedList(@QueryMap HashMap<String, String> queryMap);

    //    个人主页动态
    @POST("/a/l/user/feed/list")
    Observable<BaseResultEntity<HotFeedData>> getPersonFeedList(@QueryMap HashMap<String, String> queryMap);

    //    个人主页活动
    @POST("/a/l/user/activity/list")
    Observable<BaseResultEntity<EventData>> getPersonEventList(@QueryMap HashMap<String, String> queryMap);

    //    附近动态
    @POST("/a/l/feed/home/near/list")
    Observable<BaseResultEntity<NearbyData>> getNearbyFeedList(@QueryMap HashMap<String, String> queryMap);

    //    用户关注
    @POST("/a/l/connection/attention")
    Observable<BaseResultEntity<Object>> userFollow(@QueryMap HashMap<String, String> queryMap);

    //    用户取消关注
    @POST("/a/l/connection/attention/cancel")
    Observable<BaseResultEntity<Object>> userCancelFollow(@QueryMap HashMap<String, String> queryMap);

    //    关注列表
    @POST("/a/l/connection/attention/list")
    Observable<BaseResultEntity<FocusData>> getFocusList(@QueryMap HashMap<String, String> queryMap);
    //    达人榜
    @POST("/a/l/user/recommend/list")
    Observable<BaseResultEntity<RecommendData>> getRecommendList(@QueryMap HashMap<String, String> queryMap);

    //    粉丝列表
    @POST("/a/l/connection/fans/list")
    Observable<BaseResultEntity<FanData>> getFanList(@QueryMap HashMap<String, String> queryMap);

    //    轮播
    @POST("/a/l/sys/ad/list")
    Observable<BaseResultEntity<BannerData>> getBannerList();

    //    动态详情
    @POST("/a/l/feed/detail")
    Observable<BaseResultEntity<FeedDetailData>> getFeedsDetail(@QueryMap HashMap<String, String> queryMap);

    //   活动详情
    @POST("/a/l/activity/detail")
    Observable<BaseResultEntity<ActivityDetailData>> getActivityDetail(@QueryMap HashMap<String, String> queryMap);

    //   热门群列表
    @POST("/a/l/car/group/recommend/hot")
    Observable<BaseResultEntity<GroupData>> getHotGroupList(@QueryMap HashMap<String, String> queryMap);

    //   推荐群列表
    @POST("/a/l/car/group/recommend/tag")
    Observable<BaseResultEntity<GroupRecommendData>> getOtherGroupList(@QueryMap HashMap<String, String> queryMap);

    //   我的群列表
    @POST("/a/l/car/group/self")
    Observable<BaseResultEntity<GroupData>> getMyGroupList(@QueryMap HashMap<String, String> queryMap);

    //   解散群
    @POST("/a/l/im/group/remove")
    Observable<BaseResultEntity<Object>> removeMyGroup(@QueryMap HashMap<String, String> queryMap);
    //   转让群
    @POST("/a/l/im/group/transfer")
    Observable<BaseResultEntity<Object>> changeMyGroupOwner(@QueryMap HashMap<String, String> queryMap);


    //   转让群
    @POST("/a/l/car/group/search")
    Observable<BaseResultEntity<SearchGroupBean>> searchGroup(@QueryMap HashMap<String, String> queryMap);


    //   首页社交地图
    @POST("/a/l/feed/home/map/near/list")
    Observable<BaseResultEntity<SNSMapData>> getMapSNS(@QueryMap HashMap<String, String> queryMap);

    //   首页社交地图
    @POST("/a/l/road/condition/list/range")
    Observable<BaseResultEntity<RoadData>> queryNearTraffic(@QueryMap HashMap<String, String> queryMap);

    //   首页社交地图
    @POST("/a/l/road/condition/check")
    Observable<BaseResultEntity<Object>> trafficEventCheck(@QueryMap HashMap<String, String> queryMap);

    /******  获取 阿里云 服务鉴权*****/
    @POST("/a/l/sys/aliyun/get-ststoken")
    Observable<JsonObject> getAliStsToken();


    //   用户搜索
    @POST("/a/l/user/search")
    Observable<BaseResultEntity<SearchUserData>> searchUser(@QueryMap HashMap<String, String> queryMap);

    //   动态评论列表
    @POST("/a/l/feed/comment/list")
    Observable<BaseResultEntity<CommentData>> getFeedComments(@QueryMap HashMap<String, String> queryMap);

    //   动态删除
    @POST("/a/l/feed/remove")
    Observable<BaseResultEntity<Object>> deleteFeed(@QueryMap HashMap<String, String> queryMap);

    //   活动删除
    @POST("/a/l/activity/remove")
    Observable<BaseResultEntity<Object>> deleteActivity(@QueryMap HashMap<String, String> queryMap);

    //   动态评论列表
    @POST("/a/l/user/base")
    Observable<BaseResultEntity<UserData>> getUserBaseInfo(@QueryMap HashMap<String, String> queryMap);

    //   活动评论列表
    @POST("/a/l/activity/comment/list")
    Observable<BaseResultEntity<CommentData>> getActivityComments(@QueryMap HashMap<String, String> queryMap);

    //   发布动态评论
    @POST("/a/l/feed/comment/publish")
    Observable<BaseResultEntity<Object>> publishFeedComments(@QueryMap HashMap<String, String> queryMap);

    //   发布活动评论
    @POST("/a/l/activity/comment/publish")
    Observable<BaseResultEntity<Object>> publishActivigtyComments(@QueryMap HashMap<String, String> queryMap);

    //   动态转发接口
    @POST("/a/l/feed/forward")
    Observable<BaseResultEntity<CommentData>> transmitFeed(@QueryMap HashMap<String, String> queryMap);

    //   动态点赞
    @POST("/a/l/feed/praise")
    Observable<BaseResultEntity<CommentData>> feedLiked(@QueryMap HashMap<String, String> queryMap);

    //   取消动态点赞
    @POST("/a/l/feed/praise/cancel")
    Observable<BaseResultEntity<CommentData>> feedCancelLiked(@QueryMap HashMap<String, String> queryMap);

    //   动态评论点赞
    @POST("/a/l/feed/comment/praise")
    Observable<BaseResultEntity<CommentData>> feedCommentLiked(@QueryMap HashMap<String, String> queryMap);

    //   取消动态评论点赞
    @POST("/a/l/feed/comment/praise/cancel")
    Observable<BaseResultEntity<CommentData>> feedCancelCommentLiked(@QueryMap HashMap<String, String> queryMap);

    //   活动点赞
    @POST("/a/l/activity/praise")
    Observable<BaseResultEntity<CommentData>> activitytLiked(@QueryMap HashMap<String, String> queryMap);

    //   取消活动点赞
    @POST("/a/l/activity/praise/cancel")
    Observable<BaseResultEntity<CommentData>> activitytCancelLiked(@QueryMap HashMap<String, String> queryMap);

    //   活动评论点赞
    @POST("/a/l/activity/comment/praise")
    Observable<BaseResultEntity<CommentData>> activitytCommentLiked(@QueryMap HashMap<String, String> queryMap);

    //   取消活动评论点赞
    @POST("/a/l/activity/comment/praise/cancel")
    Observable<BaseResultEntity<CommentData>> activitytCancelCommentLiked(@QueryMap HashMap<String, String> queryMap);


    /******  发布动态接口 *****/
    @POST("/a/l/feed/publish")
    Observable<JsonObject> publishDetail(@QueryMap HashMap<String, String> queryMap);

    /******  发布活动接口 *****/
    @POST("/a/l/activity/publish")
    Observable<JsonObject> publishactivity(@QueryMap HashMap<String, Object> queryMap);

    /****** 资讯首页 *****/
    @POST("/a/l/information/index")
    Observable<BaseResultEntity<NewsInfoBean>> getNewsHomeTabList(@QueryMap HashMap<String, String> queryMap);

    /****** 订阅资讯栏目*****/
    @POST("/a/l/information/theme/follow")
    Observable<JsonObject> getNewsColumnFollow(@QueryMap HashMap<String, String> queryMap);

    /****** 资讯栏目详情页 包括（动态&热门*）****/
    @POST("/a/l/information/list/detail/theme/id")
    Observable<BaseResultEntity<ColumnDataBean>> getNewsColumnInfoDynamic(@QueryMap HashMap<String, String> queryMap);

    /****** 资讯内容详情页****/
    @POST("/a/l/information/detail/id")
    Observable<JsonObject> getNewsBloggerInfo(@QueryMap HashMap<String, String> queryMap);

    /****** 资讯点赞****/
    @POST("/a/l/information/like")
    Observable<JsonObject> newsLikesPost(@QueryMap HashMap<String, String> queryMap);

    /****** 资讯评论****/
    @POST("/a/l/information/comment")
    Observable<JsonObject> newsCommentPost(@QueryMap HashMap<String, String> queryMap);

    /****** 资讯评论点赞****/
    @POST("/a/l/information/comment/like")
    Observable<JsonObject> newsCommentLinktPost(@QueryMap HashMap<String, String> queryMap);

    /****** 资讯评论点赞****/
    @POST("/a/l/road/condition/publish")
    Observable<JsonObject> publishRoadConditionPost(@QueryMap HashMap<String, Object> queryMap);

    /****** 平台下发验证码接口 ****/
    @POST("/a/n/send-validate-code.json")
    Observable<JsonObject> sendValidateCode(@QueryMap HashMap<String, Object> queryMap);

    /****** 登录注册接口 ****/
    @POST("/a/n/reg-login.json")
    Observable<BaseResultEntity<UserBean>> loginPost(@QueryMap HashMap<String, Object> queryMap);

    /****** 登录注册接口 ****/
    @POST("/a/n/login-wx")
    Observable<JsonObject> loginWeiXinPost(@QueryMap HashMap<String, Object> queryMap);


    /****** 三方登录绑定手机号 ****/
    @POST("/a/n/mobile/bind")
    Observable<JsonObject> mobileBindPost(@QueryMap HashMap<String, Object> queryMap);

    /****** 获取个人信息 ****/
    @POST("/a/l/my/info/basic")
    Observable<BaseResultEntity<UserInfoBean>> getMyInifo(@QueryMap HashMap<String, Object> queryMap);

    /****** 退出登录****/
    @POST("/a/l/user/cancel")
    Observable<JsonObject> signOut();

    /****** 添加车辆信息 ***/
    @POST("/a/l/vehicle/tools/car/add")
    Observable<BaseResultEntity> addCarInfo(@QueryMap HashMap<String, Object> queryMap);

    /****** 车主工具-主页面***/
    @POST("/a/l/vehicle/tools/index")
    Observable<BaseResultEntity<VehicleToolsBean>> getVehicleTools(@QueryMap HashMap<String, Object> queryMap);

    /****** 车辆管理-车辆列表 ***/
    @POST("/a/l/vehicle/tools/list/car")
    Observable<BaseResultEntity<CarListBean>> getCarList();

    /****** 车辆管理-车辆列表 ***/
    @POST("/a/l/vehicle/tools/car/delete")
    Observable<BaseResultEntity> deleteCarInfo(@QueryMap HashMap<String, Object> queryMap);

    /****** 车辆管理-车辆列表 ***/
    @POST("/a/l/user/modify")
    Observable<BaseResultEntity> userInfoModify(@QueryMap HashMap<String, Object> queryMap);

    /****** 获取车友群  ***/
    @POST("/a/l/sys/tag/list")
    Observable<BaseResultEntity<TagListBean>> getImGroupLable(@QueryMap HashMap<String, Object> queryMap);

    /****** 创建车友群  ***/
    @POST("/a/l/im/group/create")
    Observable<BaseResultEntity> createImGroup(@QueryMap HashMap<String, Object> queryMap);

    /****** 获取车型库 ***/
    @POST("/a/l/car/brand/list")
    Observable<BaseResultEntity<CarLibaryBean>> getCarLibary();

    /****** 获取地区编码列表 ***/
    @POST("/a/l/sys/area-code/list")
    Observable<BaseResultEntity<AreaCodeBean>> getAreaCodeList(@QueryMap HashMap<String, Object> queryMap);

    /****** 车友群-获取群详情接口***/
    @POST("/a/l/im/group/detail")
    Observable<BaseResultEntity<GroupDetailsBean>> getGroupDetails(@QueryMap HashMap<String, Object> queryMap);

    /****** 车友群-获取群成员列表***/
    @POST("/a/l/im/group/members")
    Observable<BaseResultEntity<GroupMemberListBean>> getGroupUserList(@QueryMap HashMap<String, Object> queryMap);

    /****** 修改群资料  ***/
    @POST("/a/l/im/group/modify")
    Observable<BaseResultEntity> modifyGroupInfo(@QueryMap HashMap<String, Object> queryMap);


}

