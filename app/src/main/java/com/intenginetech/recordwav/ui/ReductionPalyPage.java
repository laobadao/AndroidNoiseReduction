package com.intenginetech.recordwav.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.intenginetech.recorder.player.PlayDialog;
import com.intenginetech.recorder.player.PlayUtils;
import com.intenginetech.recordwav.R;
import com.intenginetech.recordwav.WavApp;
import com.intenginetech.recordwav.base.BaseFragment;
import com.intenginetech.recordwav.utils.LoadingDialog;
import com.intenginetech.recordwav.utils.SearchFileUtils;
import com.intenginetech.recordwav.utils.T;
import com.intenginetech.recordwav.view.ItemRemoveRecyclerView;
import com.intenginetech.recordwav.view.MyAdapter;
import com.intenginetech.recordwav.view.OnItemClickListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * wav 降噪后文件播放
 *
 * @time 16/5/20 下午6:40
 */
public class ReductionPalyPage extends BaseFragment {
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
                    tv_des.setText("点击条目，播放降噪后的 wav 文件！");
                } else {
                    tv_des.setText("没有找到降噪后的文件 ！");
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
            List<File> fileArr = SearchFileUtils.search(new File(WavApp.reductionPath), new String[]{".wav"});
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