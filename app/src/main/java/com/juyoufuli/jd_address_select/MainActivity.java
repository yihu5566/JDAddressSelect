package com.juyoufuli.jd_address_select;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private TextView tvAddressResult;
    private AddressSelectDialog addressSelectDialog;

    int[] select = {0, 0, 1};
    private List<CityBean> cityBeanList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String jsonData = new String(FileUtil.getAssertsFile(this, "cartdata.json"));
        cityBeanList = new Gson().fromJson(jsonData, new TypeToken<List<CityBean>>() {
        }.getType());
        tvAddressResult = findViewById(R.id.tv_address_result);
        findViewById(R.id.btn_pop_window).setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        addressSelectDialog = new AddressSelectDialog();
        addressSelectDialog.setSelectAddressResultListener(new SelectAddressResultListener() {
            @Override
            public void selectAddressResult(int[] address) {
                LogUtil.i(address.toString());
                select = address;
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("省：" + cityBeanList.get(select[0]).getName() + "\n");
                stringBuffer.append("市：" + cityBeanList.get(select[0]).getCity().get(select[1]).getName() + "\n");
                stringBuffer.append("区：" + cityBeanList.get(select[0]).getCity().get(select[1]).getArea().get(select[2]));

                tvAddressResult.setText(stringBuffer);
            }
        });

        addressSelectDialog.setCityBeanList(cityBeanList);
        addressSelectDialog.show(getSupportFragmentManager(), "bottom");
        //要先初始化，在设置数据
        addressSelectDialog.setSelectAddress(select);
    }
}
