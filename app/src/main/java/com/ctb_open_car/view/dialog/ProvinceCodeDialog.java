package com.ctb_open_car.view.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ctb_open_car.R;
import com.ctb_open_car.view.adapter.vehicletoolsadpter.ProvinceCodeAdapter;

import es.dmoral.toasty.Toasty;

public class ProvinceCodeDialog extends DialogFragment {

    //点击发表，内容不为空时的回调
    public SelectListener mSelectListener;
    public interface  SelectListener{
        void selectListener(String inputText);
    }

    private Dialog dialog;
    private RecyclerView mRecyclerView;

    public ProvinceCodeDialog(SelectListener sendBackListener){//提示文字
        this.mSelectListener = sendBackListener;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 使用不带Theme的构造器, 获得的dialog边框距离屏幕仍有几毫米的缝隙。
        dialog = new Dialog(getActivity(), R.style.Comment_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        View contentview = View.inflate(getActivity(), R.layout.province_code_dialog, null);
        dialog.setContentView(contentview);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消
        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.alpha = 1;
        lp.dimAmount = 0.5f;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        window.setAttributes(lp);
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mRecyclerView = (RecyclerView) contentview.findViewById(R.id.recyclerview);
        GridLayoutManager  mLayoutManager = new GridLayoutManager(getActivity(),7);
        mRecyclerView.setLayoutManager(mLayoutManager);
        ProvinceCodeAdapter adapter = new ProvinceCodeAdapter(getActivity(), new ProvinceCodeAdapter.SelectAdpateOnListener() {
            @Override
            public void selectListener(String select) {
                mSelectListener.selectListener(select);
                dismiss();
            }
        });
        mRecyclerView.setAdapter(adapter);
        return dialog;
    }

}
