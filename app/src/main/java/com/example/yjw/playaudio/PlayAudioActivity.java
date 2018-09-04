package com.example.yjw.playaudio;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.yjw.common.ActivityCollecter;
import com.example.yjw.common.BaseActivity;
import com.example.yjw.myviewpager.R;


public class PlayAudioActivity extends BaseActivity implements View.OnClickListener{
    private MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_audio);

        Button paly =findViewById(R.id.play);
        Button pause =findViewById(R.id.pause);
        Button stop =findViewById(R.id.stop);
        paly.setOnClickListener(this);
        pause.setOnClickListener(this);
        stop.setOnClickListener(this);

        if (ContextCompat.checkSelfPermission(PlayAudioActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(PlayAudioActivity.this,new String[]
                    {Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }else{
            initMediaPlayer();//初始化MediaPlayer
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length >0 && grantResults[0] ==PackageManager.PERMISSION_GRANTED){
                    initMediaPlayer();
                }else{
                    Toast.makeText(this,"You denied the permission",Toast.LENGTH_SHORT).show();
                    ActivityCollecter.finishAll();
                }
                break;
            default:
        }
    }

    private void initMediaPlayer(){
        /*File file =new File(Environment.getExternalStorageDirectory(),"music.mp3");
        try {
                        mediaPlayer.setDataSource(file.getPath());//指定音频文件的路径
                        mediaPlayer.prepare();//让MediaPlayer进入到准备状态
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/

        new Thread(new Runnable() {
            @Override
            public void run() {
                mediaPlayer=MediaPlayer.create(getApplication(),R.raw.daogu);//
            }
        }).start();

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.play:
                if (!mediaPlayer.isPlaying()){
                    mediaPlayer.start();//开始播放
                }
                break;
            case R.id.pause:
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.pause();//暂停播放
                }
                break;
            case R.id.stop:
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.reset();//停止播放
                    initMediaPlayer();
                }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer !=null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}
