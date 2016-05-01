package ssm.clocktools.clockTool;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import ssm.clocktools.R;

public class AlarmActivity extends AppCompatActivity implements DialogInterface.OnClickListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_view);

        SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        new AlertDialog.Builder(this)
                .setTitle("闹钟")
                .setMessage(sp.format(Calendar.getInstance().getTime()) + " 闹钟响了")
                .setPositiveButton("确定", this)
                .show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

        AlarmMsgSave alarmMsgSave = new AlarmMsgSave(AlarmActivity.this);

        if ("once".equals(alarmMsgSave.getAlarmMsg("cicle"))) {
            alarmMsgSave.setAlarmMsg("alarmContent", "未设定闹钟");
            alarmMsgSave.setAlarmMsg("cicle", "");
        }

        this.finish();
    }
}
