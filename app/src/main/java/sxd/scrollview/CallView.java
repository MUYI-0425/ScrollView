package sxd.scrollview;

import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.os.Handler;


public class CallView extends AppCompatActivity {
    private FileOutputStream mOutputStream;
    private FileInputStream mInputStream;
    private SerialPort sp;
    //按钮
    EditText ed ;
    ImageButton button1 ;
    ImageButton button2 ;
    ImageButton button3 ;
    ImageButton button4 ;
    ImageButton button5 ;
    ImageButton button6 ;
    ImageButton button7 ;
    ImageButton button8 ;
    ImageButton button9 ;
    ImageButton button0 ;
    ImageButton button0l ;
    ImageButton button0r ;
    List<ImageButton> list = new ArrayList<ImageButton>();
    private int TIME = 200;
    private SoundPool soundPool;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callview);
        uiInit();
        setJni();
        loadSound();
    }
    //初始化界面
    private void uiInit() {
        ed = (EditText) findViewById(R.id.et);
        Intent intent = getIntent();
        String text  = intent.getStringExtra("firstValue");
        ed.append(text);
        button1 = (ImageButton) findViewById(R.id.bt1);
        button2 = (ImageButton) findViewById(R.id.bt2);
        button3 = (ImageButton) findViewById(R.id.bt3);
        button4 = (ImageButton) findViewById(R.id.bt4);
        button5 = (ImageButton) findViewById(R.id.bt5);
        button6 = (ImageButton) findViewById(R.id.bt6);
        button7 = (ImageButton) findViewById(R.id.bt7);
        button8 = (ImageButton) findViewById(R.id.bt8);
        button9 = (ImageButton) findViewById(R.id.bt9);
        button0 = (ImageButton) findViewById(R.id.bt0);
        button0l = (ImageButton) findViewById(R.id.bt0l);
        button0r = (ImageButton) findViewById(R.id.bt0r);
        list.add(button0);
        list.add(button1);
        list.add(button2);
        list.add(button3);
        list.add(button4);
        list.add(button5);
        list.add(button6);
        list.add(button7);
        list.add(button8);
        list.add(button9);
        list.add(button0l);
        list.add(button0r);
    }
    public void loadSound(){
        soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM,5);
        soundPool.load(this,R.raw.sound,1);
    }
    //设置串口监听
    public void setJni() {
        try {
            sp=new SerialPort(new File("/dev/ttyS0"),9600);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mOutputStream=(FileOutputStream) sp.getOutputStream();
        mInputStream=(FileInputStream) sp.getInputStream();
        new Thread(new MyThread()).start();
    }
    void onDataReceived(final byte[] buffer, final int size) {
        runOnUiThread(new Runnable() {
            public void run() {
                soundPool.play(1,1, 1, 0, 0, 1);
                handler.postDelayed(runnable, TIME); //每隔1s执行
                String receiveS = new String(buffer,0,size);
                if (!receiveS.equals("*") && !receiveS.equals("#")) {
                    ed.append(receiveS);
                }else if ("*".equals(receiveS)) {
                    int index=ed.getSelectionStart();
                    String str=ed.getText().toString();
                    if (!str.equals("")) {//判断输入框不为空，执行删除
                        ed.getText().delete(index-1,index);
                    }
                }else if ("#".equals(receiveS)) {
                    //确定

                }
                switch (receiveS) {
                    case ("0"): {
                        button0.setEnabled(false);
                        return;
                    }
                    case ("1"): {
                        button1.setEnabled(false);
                        return;
                    }
                    case ("2"): {
                         button2.setEnabled(false);
                        return;
                    }
                    case ("3"): {
                        button3.setEnabled(false);
                        return;
                    }
                    case ("4"): {
                        button4.setEnabled(false);
                        return;
                    }
                    case ("5"): {
                        button5.setEnabled(false);
                        return;
                    }
                    case ("6"): {
                        button6.setEnabled(false);
                        return;
                    }
                    case ("7"): {
                        button7.setEnabled(false);
                        return;
                    }
                    case ("8"): {
                        button8.setEnabled(false);
                        return;
                    }
                    case ("9"): {
                        button9.setEnabled(false);
                        return;
                    }
                    case ("*"): {
                        button0l.setEnabled(false);
                        return;
                    }
                    case ("#"): {
                        button0r.setEnabled(false);
                        return;
                    }
                    default:{
                        break;
                    }
                }
            }
        });
    }
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // handler自带方法实现定时器
            for (ImageButton button:list) {
                if (!button.isEnabled()) {
                    button.setEnabled(true);
                }
            }
            try {
                button1.setEnabled(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    private class MyThread implements Runnable {
        @Override
        public void run() {
            int size = 0;
            try {
                while (true) {
                    byte[] buffer = new byte[1024];
                    if (mInputStream == null) {
                        return;
                    }
                    size = mInputStream.read(buffer);
                    if (size > 0) {
                        onDataReceived(buffer,size);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
