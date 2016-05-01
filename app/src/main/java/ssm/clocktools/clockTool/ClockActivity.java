package ssm.clocktools.clockTool;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import ssm.clocktools.R;

public class ClockActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener{

    private FrameLayout fragClock = null;

    private RadioGroup barGroup = null;
    private RadioButton bar1 = null;
    private RadioButton bar2 = null;
    private RadioButton bar3 = null;

    private FragmentTransaction ftrans = null;
    private Clock1 clock1 = null;
    private Clock2 clock2 = null;
    private Clock3 clock3 = null;

    private SimpleDateFormat sd1 = null;

    private Timer startTime = null;
    private Timer countTime = null;
    private Handler showTime = null;
    private Message msg = null;

    private long duration = 0;
    private boolean countStart = false;

    private String showCountTime = "";
    private String timeList ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clocktool_view);
        init();
    }

    @Override
    protected void onDestroy() {

        if (startTime != null) {
            startTime.cancel();
        }

        if (countTime != null) {
            countTime.cancel();
        }
        super.onDestroy();
    }

    private void init() {

        fragClock = (FrameLayout) findViewById(R.id.fragClock);

        barGroup = (RadioGroup) findViewById(R.id.barGroup);
        bar1 = (RadioButton) findViewById(R.id.bar1);
        bar2 = (RadioButton) findViewById(R.id.bar2);
        bar3 = (RadioButton) findViewById(R.id.bar3);

        barGroup.setOnCheckedChangeListener(this);

        ftrans = getFragmentManager().beginTransaction();

        if (clock1 == null) {
            clock1= new Clock1();
            ftrans.add(R.id.fragClock, clock1);
        }

        if (clock2 == null) {
            clock2= new Clock2();
            ftrans.add(R.id.fragClock, clock2);
        }

        if (clock3 == null) {
            clock3= new Clock3();
            ftrans.add(R.id.fragClock, clock3);
        }

        ftrans.commit();

        sd1 = new SimpleDateFormat("HH时 mm分 ss秒");

        showTime = new ShowTime();

        bar1.performClick();

        startTime = new Timer();
        startTime.schedule(new OneSecondTask(), 500, 1000);

    }

    private void hideFragment() {

        ftrans.hide(clock1);
        ftrans.hide(clock2);
        ftrans.hide(clock3);

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        ftrans = getFragmentManager().beginTransaction();
        hideFragment();

        switch (checkedId) {

            case R.id.bar1:
                ftrans.show(clock1);
                break;
            case R.id.bar2:
                ftrans.show(clock2);
                break;
            case R.id.bar3:
                ftrans.show(clock3);
                break;
        }

        ftrans.commit();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.startCountBtn:

                if (!countStart) {

                    countTime = new Timer();
                    countTime.schedule(new CountTimeTask(), 0, 100);
                    countStart = true;
                    clock2.setStartCountBtnName("  计时停止  ");

                } else {

                    if (countTime != null) {
                        countTime.cancel();
                    }
                    countStart = false;
                    clock2.setStartCountBtnName("  计时开始  ");
                }
                break;

            case R.id.resetCountBtn:

                if (countStart) {
                    Toast.makeText(this, "请先停止计时", Toast.LENGTH_SHORT).show();
                } else {
                    duration = 0;
                    clock2.setCountTimeInput("00:00:00 0");
                    clock2.setTimeList("");
                    timeList = "";
                }

                break;

            case R.id.getCountBtn:

                if (countStart) {
                    timeList = timeList + showCountTime + "\n";
                    clock2.setTimeList(timeList);
                } else {
                    Toast.makeText(this, "请先开始计时", Toast.LENGTH_SHORT).show();
                }

                break;
        }

    }

    private class ShowTime extends Handler{

        public ShowTime(){
            super();
        }

        @Override
        public void handleMessage(Message msg) {

            long hour = 0;
            long minute = 0;
            long second = 0;
            long ms = 0;

            DecimalFormat df = new DecimalFormat("00");

            if (msg.what == 1) {
                clock1.setTimeInput(sd1.format(Calendar.getInstance().getTime()));
            }

            if (msg.what == 2) {
                duration = duration + 100;
                ms = (duration%1000)/100;
                second = (duration/1000)%60;
                minute = (duration/1000/60)%60;
                hour = duration/1000/60/60;
                showCountTime = df.format(hour) + ":" + df.format(minute) +":" + df.format(second) + " " + ms;
                clock2.setCountTimeInput(showCountTime);
            }

            super.handleMessage(msg);

        }
    }

    private class OneSecondTask extends TimerTask {

        @Override
        public void run() {
            msg = new Message();
            msg.what = 1;
            showTime.sendMessage(msg);
        }
    }

    private class CountTimeTask extends TimerTask{

        @Override
        public void run() {
            msg = new Message();
            msg.what = 2;
            showTime.sendMessage(msg);
        }
    }

}
