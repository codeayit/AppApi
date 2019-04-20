package com.robot.baseapi.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lny on 2017/12/12.
 */

public abstract class BaseFragment extends Fragment {
    private static final Map<String, Boolean> fragmentSate = new HashMap<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(setLayoutResId(), container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentSate.put(getClass().getSimpleName() + "_exist", true);
        initData();
    }

    public abstract int setLayoutResId();

    public abstract void initView();

    public  void initData(){};

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }


    public void t(String msg){
        Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
    }


    public static boolean isExist(Class<? extends BaseFragment> clazz) {

        String key = clazz.getSimpleName() + "_exist";
        if (fragmentSate.containsKey(key)) {
            return fragmentSate.get(key);
        }
        return false;
    }

    public static boolean isActive(Class<? extends BaseFragment> clazz) {
        String key = clazz.getSimpleName() + "_active";
        if (fragmentSate.containsKey(key)) {
            return fragmentSate.get(key);
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentSate.put(getClass().getSimpleName() + "_active", true);
    }

    @Override
    public void onPause() {
        super.onPause();
        fragmentSate.remove(getClass().getSimpleName() + "_active");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        fragmentSate.remove(getClass().getSimpleName() + "_exist");
    }


}
