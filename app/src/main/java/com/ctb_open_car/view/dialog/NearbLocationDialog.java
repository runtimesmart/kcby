package com.ctb_open_car.view.dialog;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ctb_open_car.R;
import com.ctb_open_car.view.adapter.dynamic.LocationAdapter;
import com.ctb_open_car.view.adapter.newsadapter.ParentRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*******   发布动态，显示附近地理位置的列表  ******/
public class NearbLocationDialog extends DialogFragment implements LocationAdapter.AdapterClickListener{

    @BindView(R.id.recyuclerview)
    RecyclerView mRecyclerView;

    private LocationAdapter mLocationAdapter;
    private List<String> mLocationList = new ArrayList<>();
    private DialogClickListener mDialogClickListener;

    public static NearbLocationDialog newInstance(String title, ArrayList<String> locationList) {
        NearbLocationDialog frag = new NearbLocationDialog();
        Bundle args = new Bundle();
        args.putStringArrayList("locationList", locationList);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.dialogTheme);
        mLocationList = getArguments().getStringArrayList("locationList");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.nearb_location_dialog, container, false);
        ButterKnife.bind(this, view);

        mLocationAdapter = new LocationAdapter(getContext(), mLocationList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mLocationAdapter);
        mLocationAdapter.setOnClickListener(this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @OnClick(R.id.back_return)
    public void onViewClicked() {
        dismiss();
    }

    public void setDialogClickListener(DialogClickListener dialogClickListener) {
        mDialogClickListener = dialogClickListener;
    }
    @Override
    public void onClickListenerSelected(String address) {
         mDialogClickListener.onClickListenerSelected(address);
          dismiss();
    }

    public interface DialogClickListener{
        void onClickListenerSelected(String address);
    }
}
