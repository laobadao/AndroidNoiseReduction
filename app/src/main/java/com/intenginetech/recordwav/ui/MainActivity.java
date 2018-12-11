package com.intenginetech.recordwav.ui;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.intenginetech.recordwav.R;
import com.intenginetech.recordwav.utils.T;
import com.intenginetech.recordwav.utils.permission.PermissionFragment;
import com.intenginetech.recordwav.utils.permission.PermissionListener;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author maple
 * @time 2018/4/8.
 */
public class MainActivity extends FragmentActivity {
    @BindView(R.id.tv_title) TextView mTitle;
    @BindView(R.id.tabhost) FragmentTabHost mTabHost;

    // Fragment界面
    private Class[] fragmentArray = {RecordPage.class, RawPlayPage.class, ReductionPalyPage.class};
    // 选项卡图片
    private int[] mImageViewArray = {R.drawable.tab_record_icon, R.drawable.tab_play_icon, R.drawable.tab_parse_icon};
    // 选项卡文字
    @BindArray(R.array.tab_fun_array) String[] mTextViewArray;
    // UI elements.
    private static final int REQUEST_RECORD_AUDIO = 13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();

        requestMicrophonePermission();

        PermissionFragment.getPermissionFragment(this)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {

                    }

                    @Override
                    public void onPermissionDenied(String[] deniedPermissions) {
                        T.showShort(MainActivity.this, "不同意将无法使用");
                    }
                })
                .checkPermissions(new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO
                });
    }

    private void initView() {
        mTabHost.setup(this, getSupportFragmentManager(), R.id.fl_content);
        for (int i = 0; i < fragmentArray.length; i++) {
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(mTextViewArray[i]).setIndicator(getTabItemView(i));
            mTabHost.addTab(tabSpec, fragmentArray[i], null);
            mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.selector_tab_background);
        }

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tag) {
                mTitle.setText(tag);
            }
        });
        mTabHost.setCurrentTab(0);
        mTitle.setText(mTextViewArray[0]);
    }

    private void requestMicrophonePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                    new String[]{android.Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO);
        }
    }
    private View getTabItemView(int index) {
        View view = LayoutInflater.from(this).inflate(R.layout.tab_item_view, null);

        ImageView imageView = view.findViewById(R.id.imageview);
        imageView.setImageResource(mImageViewArray[index]);

        TextView textView = view.findViewById(R.id.textview);
        textView.setText(mTextViewArray[index]);

        return view;
    }
}