package com.scorg.dms.bottom_menus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.scorg.dms.R;
import java.util.ArrayList;



@SuppressWarnings("unused")
@SuppressLint("Registered")
public class BottomMenuActivity extends AppCompatActivity implements BottomMenuAdapter.OnBottomMenuClickListener {

    private static final long ANIMATION_DUR = 300;
    RecyclerView recyclerView;
    private BottomMenuAdapter bottomMenuAdapter;
    private int mPosition;
    private ColorGenerator mColorGenerator;
    private Context mContext;
    private ArrayList<BottomMenu> mBottomMenuList;
    private String[] mMenuNames = {"Home", "Profile", "AppLogo", "Settings", "Support"};
    private FrameLayout mFrame;
    private String activityName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.bottom_menu_activity);
        init();
    }

    public void init() {
        mFrame = (FrameLayout) findViewById(R.id.activityView);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView) ;


    }

    public void setCurrentActivityTab(String nameOfActivity){
        mBottomMenuList = new ArrayList<>();
      if(nameOfActivity.equals(getString(R.string.home))){
          for (String mMenuName : mMenuNames) {
              if(mMenuName.equalsIgnoreCase(getString(R.string.home))) {
                  BottomMenu bottomMenu = new BottomMenu();
                  bottomMenu.setMenuName(mMenuName);
                  bottomMenu.setAppIcon(mMenuName.equals(getString(R.string.app_logo)));
                  bottomMenu.setSelected(true);
                  mBottomMenuList.add(bottomMenu);
              }else{
                  BottomMenu bottomMenu = new BottomMenu();
                  bottomMenu.setMenuName(mMenuName);
                  bottomMenu.setAppIcon(mMenuName.equals(getString(R.string.app_logo)));
                  bottomMenu.setSelected(false);
                  mBottomMenuList.add(bottomMenu);
              }
          }
      }else  if(nameOfActivity.equals(getString(R.string.profile))){
          for (String mMenuName : mMenuNames) {
              if(mMenuName.equalsIgnoreCase(getString(R.string.profile))) {
                  BottomMenu bottomMenu = new BottomMenu();
                  bottomMenu.setMenuName(mMenuName);
                  bottomMenu.setAppIcon(mMenuName.equals(getString(R.string.app_logo)));
                  bottomMenu.setSelected(true);
                  mBottomMenuList.add(bottomMenu);
              }else{
                  BottomMenu bottomMenu = new BottomMenu();
                  bottomMenu.setMenuName(mMenuName);
                  bottomMenu.setAppIcon(mMenuName.equals(getString(R.string.app_logo)));
                  bottomMenu.setSelected(false);
                  mBottomMenuList.add(bottomMenu);
              }
          }
      }else  if(nameOfActivity.equals(getString(R.string.settings))){
          for (String mMenuName : mMenuNames) {
              if(mMenuName.equalsIgnoreCase(getString(R.string.settings))) {
                  BottomMenu bottomMenu = new BottomMenu();
                  bottomMenu.setMenuName(mMenuName);
                  bottomMenu.setAppIcon(mMenuName.equals(getString(R.string.app_logo)));
                  bottomMenu.setSelected(true);
                  mBottomMenuList.add(bottomMenu);
              }else{
                  BottomMenu bottomMenu = new BottomMenu();
                  bottomMenu.setMenuName(mMenuName);
                  bottomMenu.setAppIcon(mMenuName.equals(getString(R.string.app_logo)));
                  bottomMenu.setSelected(false);
                  mBottomMenuList.add(bottomMenu);
              }
          }
      }else  if(nameOfActivity.equals(getString(R.string.support))){
          for (String mMenuName : mMenuNames) {
              if(mMenuName.equalsIgnoreCase(getString(R.string.support))) {
                  BottomMenu bottomMenu = new BottomMenu();
                  bottomMenu.setMenuName(mMenuName);
                  bottomMenu.setAppIcon(mMenuName.equals(getString(R.string.app_logo)));
                  bottomMenu.setSelected(true);
                  mBottomMenuList.add(bottomMenu);
              }else{
                  BottomMenu bottomMenu = new BottomMenu();
                  bottomMenu.setMenuName(mMenuName);
                  bottomMenu.setAppIcon(mMenuName.equals(getString(R.string.app_logo)));
                  bottomMenu.setSelected(false);
                  mBottomMenuList.add(bottomMenu);
              }
          }
      }
        mContext = BottomMenuActivity.this;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        recyclerView.setLayoutParams(params);
        recyclerView.setClipToPadding(false);
        recyclerView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.black));
        int widthPixels = Resources.getSystem().getDisplayMetrics().widthPixels;
        mColorGenerator = ColorGenerator.MATERIAL;
        createBottomMenu();
    }

    public void createBottomMenu() {
        bottomMenuAdapter = new BottomMenuAdapter(this, mBottomMenuList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 5));
        recyclerView.setAdapter(bottomMenuAdapter);


    }

    /*public void addBottomMenu(BottomMenu bottomMenu) {
        bottomMenus.add(bottomMenu);
        bottomMenuAdapter.notifyItemInserted(bottomMenus.size() - 1);
    }*/

    /* public void addBottomSheetMenu(BottomSheetMenu bottomSheetMenu) {
         bottomSheetMenus.add(bottomSheetMenu);
     }
 */
    @Override
    public void onBackPressed() {

       /* if (isOpen)
            closeSheet();
        else*/
        super.onBackPressed();
    }

  /*  @Override
    public void onBottomSheetMenuClick(BottomSheetMenu bottomMenu) {
        if (isOpen)
            closeSheet();
    }*/

    @Override
    public void onBottomMenuClick(BottomMenu bottomMenu) {
      /*  if (bottomMenu.getMenuName().equalsIgnoreCase(getString(R.string.app_logo))) {
            if (isOpen)
                closeSheet();
            else
                openSheet();
        } else if (isOpen) {
            closeSheet();
        }*/
    }

    @Override
    public void onProfileImageClick() {

    }

    public void doNotifyDataSetChanged() {
        bottomMenuAdapter.notifyDataSetChanged();
    }

    @Override
    public void setContentView(int layoutResID) {
        mFrame.removeAllViews();
        View.inflate(this, layoutResID, mFrame);
    }

    @Override
    public void setContentView(View view) {
        mFrame.removeAllViews();
        mFrame.addView(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        mFrame.removeAllViews();
        mFrame.addView(view, params);
    }

    @Override
    public void addContentView(View view, ViewGroup.LayoutParams params) {
        mFrame.addView(view, params);
    }
}