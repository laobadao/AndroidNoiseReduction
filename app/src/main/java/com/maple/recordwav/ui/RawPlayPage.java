package com.maple.recordwav.ui;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.maple.recorder.player.PlayDialog;
import com.maple.recorder.player.PlayUtils;
import com.maple.recordwav.R;
import com.maple.recordwav.WavApp;
import com.maple.recordwav.base.BaseFragment;
import com.maple.recordwav.utils.LoadingDialog;
import com.maple.recordwav.utils.SearchFileUtils;
import com.maple.recordwav.utils.T;
import com.maple.recordwav.view.ItemRemoveRecyclerView;
import com.maple.recordwav.view.MyAdapter;
import com.maple.recordwav.view.OnItemClickListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * wav 原始文件播放
 *
 * @author maple
 * @time 16/5/20 下午6:40
 */
public class RawPlayPage extends BaseFragment {
    public static final int SEARCH_MESSAGE_CODE = 200;
    @BindView(R.id.tv_des) TextView tv_des;
    @BindView(R.id.lv_wav)
    ItemRemoveRecyclerView rv_wav;

    MyAdapter adapter;
    private ArrayList<String> wavFileList;
    private LoadingDialog loadingDialog;
    PlayUtils playUtils;

    Handler updateProHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            if (msg.what == SEARCH_MESSAGE_CODE) {
                if (loadingDialog != null && loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
                if (wavFileList.size() > 0) {
                    tv_des.setText("点击条目，播放wav文件！");
                } else {
                    tv_des.setText("没有找到文件，请去录制 ！");
                }
                adapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.fragment_play, null);
        ButterKnife.bind(this, view);

        loadingDialog = new LoadingDialog(getActivity());
        return view;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        wavFileList = new ArrayList<>();

        adapter = new MyAdapter(mContext, wavFileList);
        rv_wav.setLayoutManager(new LinearLayoutManager(mContext));
        rv_wav.setAdapter(adapter);

        new Thread(searchSong).start();
        loadingDialog.show("搜索中...");
    }

    @Override
    public void initListener() {

        rv_wav.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                String filePath = wavFileList.get(position);
                File file = new File(filePath);
                if (file.exists()) {
                    dialogPlay(file);
                } else {
                    T.showShort(mContext, "选择的文件不存在!");
                }

            }

            @Override
            public void onDeleteClick(int position) {
                adapter.removeItem(position);
            }
        });
    }


    Runnable searchSong = new Runnable() {
        @Override
        public void run() {
            List<File> fileArr = SearchFileUtils.search(new File(WavApp.rootPath), new String[]{".wav"});
            wavFileList.clear();
            for (int i = 0; i < fileArr.size(); i++) {
                wavFileList.add(fileArr.get(i).getAbsolutePath());
            }
            updateProHandler.sendEmptyMessage(SEARCH_MESSAGE_CODE);
        }
    };


    private void dialogPlay(File file) {
        new PlayDialog(getActivity())
                .addWavFile(file)
                .showDialog();
    }


}