package com.ctb_open_car.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.viewpager.widget.ViewPager;

import com.ctb_open_car.CTBApplication;
import com.ctb_open_car.MainActivity;
import com.ctb_open_car.R;
import com.ctb_open_car.customview.ViewPagerAdapter;
import com.ctb_open_car.utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SplashActivity extends Activity {

    //图片资源数组
    private int[] ImageArray=new int[]{R.drawable.one,R.drawable.two, R.drawable.three};
    //底部小图片数组
    private ImageView[] DotArray;
    //图片列表数据源
    private List<View> ViewList=new ArrayList<View>();
    //小圆点id
    private int[] ids={R.id.dot1,R.id.dot2,R.id.dot3};
    //立即体验按钮
    private Button btn;

    //用来存放用户是否进过的状态标识
    private String isfString;

    private int isPro = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
      //  pro = SplashActivity.getProObject(this);
        isPro = PreferenceUtils.getInt(CTBApplication.getInstance(),"isFlag", 0);

        loading();
        Init();
        InitViewPage();
        InitDot();
    }

    //初始化小圆点的id
    private void InitDot() {
        DotArray = new ImageView[ViewList.size()];
        for (int i=0;i< ViewList.size();i++){
            DotArray[i]=(ImageView) findViewById(ids[i]);
        }
    }

    //初始化viewpager
    private void InitViewPage() {
        ViewPager boot_vp=(ViewPager) findViewById(R.id.boot_vp);
        //获取一个Layout参数，设置为全屏
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        //循环创建View并加入到集合中
        for (int anImageIdArray : ImageArray) {
            //new ImageView并设置全屏和图片资源
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(params);
            imageView.setBackgroundResource(anImageIdArray);

            //将ImageView加入到集合中
            ViewList.add(imageView);
        }
        //设置adapter
        boot_vp.setAdapter(new ViewPagerAdapter(ViewList));
        boot_vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // 设置底部小点选中状态
                for(int i = 0;i<ids.length;i ++){
                    if(position==i){
                        DotArray[i].setImageResource(R.drawable.dot_splas_2);
                    }else {
                        DotArray[i].setImageResource(R.drawable.dot_splas_1);
                    }
                }
                //判断是否是最后一页，若是则显示按钮
                if (position == ImageArray.length - 1){
                    btn.setVisibility(View.VISIBLE);
                }else {
                    btn.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    //是否加载欢迎页
    private void loading() {
        if(isPro ==1){
            //如果是第一次加载，则不走下面的步骤
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    private void Init() {

        btn=(Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPro ==0){
                    //首次加载
                    //存放用户进来状态，初始是0，进入后设为1
                    PreferenceUtils.putInt(CTBApplication.getInstance(),"isFlag", 1);
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            }
        });
    }

}
