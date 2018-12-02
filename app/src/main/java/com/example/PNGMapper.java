package com.example;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.example.fred.uscfit.R;

import java.util.HashMap;

public class PNGMapper {
    private static PNGMapper instance;

    private HashMap<String, Drawable> pngResourceMapper;

    private PNGMapper(){
        pngResourceMapper = new HashMap<>();
    }

    public static PNGMapper getInstance(){
        if (instance == null) {
            instance = new PNGMapper();
        }
        return instance;
    }

    public void initPNGIcons(Context context){
        pngResourceMapper.put("running", context.getResources().getDrawable(R.drawable.activity_running));
        pngResourceMapper.put("baseball", context.getResources().getDrawable(R.drawable.activity_baseball));
        pngResourceMapper.put("basketball", context.getResources().getDrawable(R.drawable.activity_basketball));
        pngResourceMapper.put("bowling", context.getResources().getDrawable(R.drawable.activity_bowling));
        pngResourceMapper.put("boxing", context.getResources().getDrawable(R.drawable.activity_boxing));
        pngResourceMapper.put("cycling", context.getResources().getDrawable(R.drawable.activity_cycling));
        pngResourceMapper.put("soccer", context.getResources().getDrawable(R.drawable.activity_soccer));
        pngResourceMapper.put("climbing", context.getResources().getDrawable(R.drawable.activity_climbing));
        pngResourceMapper.put("default", context.getResources().getDrawable(R.drawable.activity_default));
    }

    public Drawable getPNGIcons(String sportType){
        return pngResourceMapper.getOrDefault(sportType, null);
    }
}
