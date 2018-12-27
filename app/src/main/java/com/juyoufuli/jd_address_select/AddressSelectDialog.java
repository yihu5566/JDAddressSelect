package com.juyoufuli.jd_address_select;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Author : dongfang
 * @Created Time : 2018-12-27  09:00
 * @Description:
 */
public class AddressSelectDialog extends DialogFragment implements BaseItemClickListener, View.OnClickListener {
    private RecyclerViewTabAdapter recyclerViewTabAdapter;
    private LayoutInflater inflater;
    private TabLayout mTabLayout;
    /**
     * 当前的选中的tab
     */
    private int tabCurrentPosition;
    private int oldTabCurrentPosition;
    /**
     * 当前选中
     */
    private HashMap<Integer, Integer> currentSelectMap = new HashMap<>();
    private List<CityBean> cityBeanList;
    private RecyclerViewCityAdapter recyclerViewCityAdapter;
    private RecyclerViewAreaAdapter recyclerViewAreaAdapter;
    private int initPosition = 0;
    private int cityIndex;
    private RecyclerView rvTabExample;
    private ImageView ivCloseDialog;
    private SelectAddressResultListener selectAddressResultListener;


    public AddressSelectDialog() {

    }

    public void setCityBeanList(List<CityBean> cityBeanList) {
        this.cityBeanList = cityBeanList;
    }

    public void setSelectAddress(final int[] selectAddress) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setSelectAddress(selectAddress, true);
            }
        }, 60);


    }

    private void setSelectAddress(int[] selectAddress, boolean isUpdata) {
        if (selectAddress != null && selectAddress.length != 0 && cityBeanList != null) {
            for (int i = 0; i < 3; i++) {
                //保存选择
                currentSelectMap.put(i, selectAddress[i]);
            }

            CityBean cityBean = cityBeanList.get(currentSelectMap.get(0));
            CityBean.CityBeanInner cityBeanInner = cityBean.getCity().get(currentSelectMap.get(1));
            String name = cityBeanInner.getArea().get(currentSelectMap.get(2));

            mTabLayout.removeAllTabs();
            //初始化tab
            mTabLayout.addTab(mTabLayout.newTab().setText(cityBean.getName()), false);
            mTabLayout.addTab(mTabLayout.newTab().setText(cityBeanInner.getName()), false);
            mTabLayout.addTab(mTabLayout.newTab().setText(name), true);

            tabCurrentPosition = currentSelectMap.size() - 1;
            //选择最后
            mTabLayout.setScrollPosition(tabCurrentPosition, 0f, true);
            //更新集合
            upDataArray();
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        String jsonData = new String(FileUtil.getAssertsFile(getContext(), "cartdata.json"));
//        cityBeanList = new Gson().fromJson(jsonData, new TypeToken<List<CityBean>>() {
//        }.getType());
        inflater = LayoutInflater.from(getContext());
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.BottomDialog);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflateView = inflater.inflate(R.layout.dialog_address_select, container, false);
        mTabLayout = inflateView.findViewById(R.id.tb_head_indicator);
        rvTabExample = inflateView.findViewById(R.id.rv_tab_example);
        ivCloseDialog = inflateView.findViewById(R.id.iv_close_dialog);
        ivCloseDialog.setOnClickListener(this);
        initTabLayout();
        initRecyclerView();
        return inflateView;
    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window dialogWindow = dialog.getWindow();
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            if (dialogWindow != null) {
                dialogWindow.setLayout(displayMetrics.widthPixels, displayMetrics.widthPixels);
                dialogWindow.setGravity(Gravity.BOTTOM);
                dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
                dialogWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            }

        }
    }


    private void initRecyclerView() {
        rvTabExample.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTabExample.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        //省地址
        recyclerViewTabAdapter = new RecyclerViewTabAdapter(getContext());
        //市地址
        recyclerViewCityAdapter = new RecyclerViewCityAdapter(getContext());
        //区地址
        recyclerViewAreaAdapter = new RecyclerViewAreaAdapter(getContext());
        recyclerViewTabAdapter.setAddressList(cityBeanList);

        recyclerViewTabAdapter.setBaseItemClickListener(this);
        recyclerViewCityAdapter.setBaseItemClickListener(this);
        recyclerViewAreaAdapter.setBaseItemClickListener(this);

        rvTabExample.setAdapter(recyclerViewTabAdapter);
    }


    private void initTabLayout() {
        currentSelectMap.put(0, -1);
        mTabLayout.addTab(mTabLayout.newTab().setText("请选择"), true);
        mTabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabCurrentPosition = mTabLayout.getSelectedTabPosition();
                LogUtil.i("onTabSelected--" + tabCurrentPosition);
                upDataArray();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                LogUtil.i("onTabUnselected--" + mTabLayout.getSelectedTabPosition());
                tabCurrentPosition = mTabLayout.getSelectedTabPosition();
                upDataArray();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                tabCurrentPosition = mTabLayout.getSelectedTabPosition();
                LogUtil.i("onTabReselected--" + mTabLayout.getSelectedTabPosition());
                upDataArray();
            }
        });
    }


    private void upDataArray() {
        if (tabCurrentPosition == oldTabCurrentPosition) {
            return;
        }
        //设置颜色
        LogUtil.i("upDataArray--更新列表啊:::" + tabCurrentPosition);
        switch (tabCurrentPosition) {
            case 0:
                recyclerViewTabAdapter.setCurrentSelect(currentSelectMap.get(tabCurrentPosition));
                recyclerViewTabAdapter.setAddressList(cityBeanList);
                rvTabExample.setAdapter(recyclerViewTabAdapter);
                break;
            case 1:
                cityIndex = currentSelectMap.get(0) == -1 ? initPosition : currentSelectMap.get(0);
                recyclerViewCityAdapter.setAddressList(cityBeanList.get(cityIndex).getCity());
                recyclerViewCityAdapter.setCurrentSelect(currentSelectMap.get(tabCurrentPosition));
                rvTabExample.setAdapter(recyclerViewCityAdapter);
                break;
            case 2:
                cityIndex = currentSelectMap.get(0) == -1 ? initPosition : currentSelectMap.get(0);
                int cityInnerIndex = currentSelectMap.get(1) == -1 ? initPosition : currentSelectMap.get(1);
                recyclerViewAreaAdapter.setAddressList(cityBeanList.get(cityIndex).getCity().get(cityInnerIndex).getArea());
                recyclerViewAreaAdapter.setCurrentSelect(currentSelectMap.get(tabCurrentPosition));
                rvTabExample.setAdapter(recyclerViewAreaAdapter);
                break;
            default:
                break;
        }
        oldTabCurrentPosition = tabCurrentPosition;
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (tabCurrentPosition) {
            case 0:
                mTabLayout.getTabAt(tabCurrentPosition).setText(recyclerViewTabAdapter.getAddressList().get(position).getName());
                break;
            case 1:
                mTabLayout.getTabAt(tabCurrentPosition).setText(recyclerViewCityAdapter.getAddressList().get(position).getName());
                break;
            case 2:
                mTabLayout.getTabAt(tabCurrentPosition).setText(recyclerViewAreaAdapter.getAddressList().get(position));
                break;
            default:
                break;
        }
        //相同tab下进行的position变化处理
        if (currentSelectMap.get(tabCurrentPosition) != position) {
            int removeTab = mTabLayout.getTabCount() - 1;
            while (removeTab > tabCurrentPosition) {
                if (mTabLayout.getTabAt(removeTab) != null) {
                    mTabLayout.removeTabAt(removeTab);
                    currentSelectMap.put(removeTab, -1);
                }
                removeTab--;
            }
            if (tabCurrentPosition >= 2) {
                tabCurrentPosition = 2;
            } else {
                mTabLayout.addTab(mTabLayout.newTab().setText("请选择"), false);
                currentSelectMap.put(tabCurrentPosition + 1, -1);
            }
        }
        //设置当前tab下的选中状态
        currentSelectMap.put(tabCurrentPosition, position);
        //滚动到下一个tab
        tabCurrentPosition++;
        //越界的话处理为最后一个tab
        if (tabCurrentPosition >= mTabLayout.getTabCount()) {
            tabCurrentPosition = mTabLayout.getTabCount() - 1;

            if (selectAddressResultListener != null) {
                int[] result = {currentSelectMap.get(0), currentSelectMap.get(1), currentSelectMap.get(2)};
                selectAddressResultListener.selectAddressResult(result);
                dismiss();

            }
        } else {
            mTabLayout.setScrollPosition(tabCurrentPosition, 0f, true);
        }
        LogUtil.i("tabCurrentPosition--" + tabCurrentPosition + "tabCount" + mTabLayout.getTabCount());
        if (tabCurrentPosition == oldTabCurrentPosition) {
            switch (tabCurrentPosition) {
                case 0:
                    recyclerViewTabAdapter.setCurrentSelect(currentSelectMap.get(tabCurrentPosition));
                    recyclerViewTabAdapter.notifyDataSetChanged();
                    break;
                case 1:
                    recyclerViewCityAdapter.setCurrentSelect(currentSelectMap.get(tabCurrentPosition));
                    recyclerViewCityAdapter.notifyDataSetChanged();
                    break;
                case 2:
                    recyclerViewAreaAdapter.setCurrentSelect(currentSelectMap.get(tabCurrentPosition));
                    recyclerViewAreaAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        } else {
            upDataArray();
        }
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dismiss();
    }

    public void setSelectAddressResultListener(SelectAddressResultListener selectAddressResultListener) {
        this.selectAddressResultListener = selectAddressResultListener;
    }


}
