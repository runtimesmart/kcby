package com.ctb_open_car.constraints;

import java.io.File;

public class AppContraint {
    public static class WeiXin {
        public static final String WXID = "wx3c0ccbcc34600c72";

        public static String sEventUrl = "https://toc.chetuobang.com/opencar-h5/activityDetail.html?id=";
        public static String sFeedUrl = "https://toc.chetuobang.com/opencar-h5/dynamicDetail.html?id=";
        public static String sPersonUrl = "https://toc.chetuobang.com/opencar-h5/personal.html?id=";
    }


    public static class POICollect {
        public static final String HOME_ADDRESS = "address_collect_home";
        public static final String HOME_POI = "poi_collect_home";
        public static final String COMPANY_ADDRESS = "address_collect_company";
        public static final String NORMAL_ADDRESS = "address_collect_normal";
        public static final String COMPANY_POI = "poi_collect_company";
        public static final int LOCATION_COMPANY_RESULT_CODE = 65534;
        public static final int LOCATION_HOME_RESULT_CODE = 65533;
        public static final int LOCATION_NORMAL_RESULT_CODE = 65532;
        public static final int MAP_RESULT_CODE = 0x102;

    }

    public static class SearchHistory {
        public static final String NAVI_HISTORY_KEY = "address_navi_history";
        public static final int MAX_CACHE_SIZE = 10;
        public static final String storage_file = "cache_history_dir";

    }

    public static class WeiBo {
        /**
         * 当前 DEMO 应用的 APP_KEY，第三方应用应该使用自己的 APP_KEY 替换该 APP_KEY
         */
        public static final String APP_KEY = "1631844426";
        public static final String APP_SECRET = "ae6c1e5042c1ca6688a5ac2bd384673d";

        /**
         * 当前 DEMO 应用的回调页，第三方应用可以使用自己的回调页。
         * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
         */
        public static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";

        /**
         * WeiboSDKDemo 应用对应的权限，第三方开发者一般不需要这么多，可直接设置成空即可。
         * 详情请查看 Demo 中对应的注释。
         */
        public static final String SCOPE =
                "email,direct_messages_read,direct_messages_write,"
                        + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                        + "follow_app_official_microblog," + "invitation_write";

    }

    public static class EMCHAT{
        public static final String MESSAGE_ATTR_IS_VIDEO_CALL="em_video_call";
        public static final String MESSAGE_ATTR_IS_VOICE_CALL="em_voice_call";
        public static final String MESSAGE_ATTR_IS_BIG_EXPRESSION="em_big_exp";
    }
}
