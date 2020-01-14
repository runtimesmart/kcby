package com.ctb_open_car.view.activity.im;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ctb_open_car.R;
import com.ctb_open_car.base.BaseActivity;
import com.ctb_open_car.bean.community.response.group.GroupDto;
import com.ctb_open_car.ui.GridViewDivider;
import com.ctb_open_car.ui.RecycleViewDivider;
import com.ctb_open_car.view.adapter.im.HotGoupListAdapter;
import com.hyphenate.easeui.widget.EaseTitleBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TagGroupMoreActivity extends BaseActivity {
    @BindView(R.id.tag_more_group)
    RecyclerView mTagMoreListView;
    @BindView(R.id.tag_title)
    EaseTitleBar mTagTitle;

    private GridViewDivider mMoreDivider;
    private HotGoupListAdapter mTagGoupListAdapter;
    private List<GroupDto> mTagMoreGroupList;
    private String mTagName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.im_tag_group);
        mTagMoreGroupList = (List<GroupDto>) getIntent().getSerializableExtra("tag_group_list");
        mTagName = getIntent().getStringExtra("tag_type");
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mTagTitle.setTitle(mTagName);
        GridLayoutManager hotLayoutManager = new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false);
        mTagMoreListView.setLayoutManager(hotLayoutManager);
        /**防止fragment缓存item 导致每次加载divider导致 间隔不断变大*/
        if (null == mMoreDivider) {
            mMoreDivider = new GridViewDivider(2,15);
            mTagMoreListView.addItemDecoration(mMoreDivider);
        }

        mTagMoreListView.setLayoutManager(hotLayoutManager);
        mTagGoupListAdapter = new HotGoupListAdapter(this);
        mTagMoreListView.setAdapter(mTagGoupListAdapter);
        mTagGoupListAdapter.setData(mTagMoreGroupList);
        mTagTitle.setLeftLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public Object getTag() {
        return null;
    }
}
