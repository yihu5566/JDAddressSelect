package com.juyoufuli.jd_address_select;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * @Author : dongfang
 * @Created Time : 2018-12-26  09:57
 * @Description:
 */
public class RecyclerViewCityAdapter extends RecyclerView.Adapter<RecyclerViewCityAdapter.MyHolder> {

    private List<CityBean.CityBeanInner>  addressList;
    private final LayoutInflater layoutInflater;
    private BaseItemClickListener baseItemClickListener;
    private int currentSelect=-1;

    public void setAddressList(List<CityBean.CityBeanInner>  addressList) {
        this.addressList = addressList;
    }

    public List<CityBean.CityBeanInner> getAddressList() {
        return addressList;
    }

    public void setCurrentSelect(int currentSelect) {
        this.currentSelect = currentSelect;
    }



    public RecyclerViewCityAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = layoutInflater.inflate(R.layout.item_address_name, viewGroup, false);
        return new MyHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, final int i) {
        if (currentSelect == i) {
            myHolder.ivCheckTag.setVisibility(View.VISIBLE);
        } else {
            myHolder.ivCheckTag.setVisibility(View.INVISIBLE);
        }
        myHolder.tvCityName.setText(addressList.get(i).getName());
        myHolder.llContentRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (baseItemClickListener != null) {
                    baseItemClickListener.onItemClick(v, i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return addressList != null ? addressList.size() : 0;
    }

    public void setBaseItemClickListener(BaseItemClickListener baseItemClickListener) {
        this.baseItemClickListener = baseItemClickListener;
    }

    class MyHolder extends RecyclerView.ViewHolder {

        private final TextView tvCityName;
        private final ImageView ivCheckTag;
        private final LinearLayout llContentRoot;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tvCityName = itemView.findViewById(R.id.tv_city_name);
            ivCheckTag = itemView.findViewById(R.id.iv_check_tag);
            llContentRoot = itemView.findViewById(R.id.ll_content_root);
        }
    }


}
