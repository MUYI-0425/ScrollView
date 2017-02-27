package sxd.scrollview;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.jude.rollviewpager.hintview.ColorPointHintView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private FileOutputStream mOutputStream;
    private FileInputStream mInputStream;
    private SerialPort sp;
    private Thread my_thread;
    private RollPagerView mRollViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setScrollView();
        setJni();
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
        my_thread = new Thread(new MyThread());
        my_thread.start();
    }
    void onDataReceived(final byte[] buffer, final int size) {
        runOnUiThread(new Runnable() {
            public void run() {
                Intent intent = new Intent();
                intent.putExtra("firstValue",new String(buffer, 0, size));
                intent.setClass(MainActivity.this, CallView.class);
                startActivity(intent);
            }
        });
    }

    private class MyThread implements Runnable {
        @Override
        public void run() {
            int size = 0;
            try {
                byte[] buffer = new byte[1024];
                if (mInputStream == null) {
                    return;
                }
                size = mInputStream.read(buffer);
                if (size > 0) {
                    onDataReceived(buffer,size);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void sendJni(String message) {
        try {
            mOutputStream.write(message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void closeJni() {
        sp.close();
    }

    //设置轮播图
    private void setScrollView() {
        mRollViewPager = (RollPagerView) findViewById(R.id.roll_view_pager);
        //设置播放时间间隔
        mRollViewPager.setPlayDelay(3000);
        //设置透明度
        mRollViewPager.setAnimationDurtion(500);
        //设置适配器
        mRollViewPager.setAdapter(new TestNormalAdapter());
        mRollViewPager.setHintView(new ColorPointHintView(this, Color.YELLOW,Color.WHITE));
    }

    private class TestNormalAdapter extends StaticPagerAdapter {
        private int[] imgs = {
                R.drawable.img5,
                R.drawable.img6,
                R.drawable.img7,
        };
        @Override
        public View getView(ViewGroup container, int position) {
            ImageView view = new ImageView(container.getContext());
            view.setImageResource(imgs[position]);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return view;
        }
        public int getCount() {
            return imgs.length;
        }
    }
}
