package ssm.clocktools.clockTool;


import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.Calendar;

import ssm.clocktools.R;

public class Clock3 extends Fragment implements View.OnClickListener, TimePickerDialog.OnTimeSetListener,
        RadioGroup.OnCheckedChangeListener{

    private View view = null;
    private TextView alarmTime = null;
    private Button setAlarmBtn = null;
    private Button cancelAlarmBtn = null;
    private RadioGroup cicleGroup = null;
    private TextView alarmContent = null;

    private int alarmHour = -1;
    private int alarmMinute = -1;
    private String cicle = "";

    private AlarmManager alarmManager = null;
    private Calendar calendar = null;
    private Intent intent = null;
    private PendingIntent pendingIntent = null;

    AlarmMsgSave alarmMsgSave = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.clock3_view, container, false);

            alarmTime = (TextView) view.findViewById(R.id.alarmTime);
            setAlarmBtn = (Button)  view.findViewById(R.id.setAlarmBtn);
            cancelAlarmBtn = (Button)  view.findViewById(R.id.cancelAlarmBtn);
            cicleGroup = (RadioGroup) view.findViewById(R.id.cicleGroup);
            alarmContent = (TextView) view.findViewById(R.id.alarmContent);

            alarmTime.setOnClickListener(this);
            setAlarmBtn.setOnClickListener(this);
            cancelAlarmBtn.setOnClickListener(this);
            cicleGroup.setOnCheckedChangeListener(this);
            cicleGroup.check(R.id.once);
        }

        return view;

    }

    @Override
    public void onResume() {

        alarmMsgSave = new AlarmMsgSave(getActivity());
        alarmContent.setText(alarmMsgSave.getAlarmMsg("alarmContent"));
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.alarmTime:

                alarmHour = (alarmHour == -1)?Calendar.getInstance().get(Calendar.HOUR_OF_DAY):alarmHour;
                alarmMinute = (alarmMinute == -1)?Calendar.getInstance().get(Calendar.MINUTE):alarmMinute;

                new TimePickerDialog(getActivity(), this, alarmHour, alarmMinute, true).show();
                break;

            case R.id.setAlarmBtn:

                if (alarmHour == -1) {
                    Toast.makeText(getActivity(), "请设置时间。", Toast.LENGTH_SHORT).show();
                    break;
                }
                if ("".equals(cicle)) {
                    Toast.makeText(getActivity(), "请设置频率。", Toast.LENGTH_SHORT).show();
                    break;
                }

                calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR_OF_DAY, alarmHour);
                calendar.set(Calendar.MINUTE, alarmMinute);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
                    calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1);
                }

                intent = new Intent(getActivity(), AlarmActivity.class);
                pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, 0);

                alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

                if ("once".equals(cicle)) {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                    alarmContent.setText("闹钟：一次，" + alarmTime.getText().toString());
                } else if("everyday".equals(cicle)) {
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pendingIntent);
                    alarmContent.setText("闹钟：每天，" + alarmTime.getText().toString());
                }

                alarmMsgSave = new AlarmMsgSave(getActivity());
                alarmMsgSave.setAlarmMsg("alarmContent", alarmContent.getText().toString());
                alarmMsgSave.setAlarmMsg("cicle", cicle);
                Toast.makeText(getActivity(), "闹钟设置完毕", Toast.LENGTH_SHORT).show();

                break;

            case R.id.cancelAlarmBtn:

                intent = new Intent(getActivity(), AlarmActivity.class);
                pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, 0);
                alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                alarmManager.cancel(pendingIntent);

                alarmMsgSave = new AlarmMsgSave(getActivity());
                alarmMsgSave.setAlarmMsg("alarmContent", "未设定闹钟");
                alarmMsgSave.setAlarmMsg("cicle", "");
                Toast.makeText(getActivity(), "闹钟解除完毕", Toast.LENGTH_SHORT).show();

                alarmContent.setText("未设定闹钟");

                break;

        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId) {

            case R.id.once:
                cicle = "once";
                break;
            case R.id.everyday:
                cicle = "everyday";
                break;
        }

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        alarmHour = hourOfDay;
        alarmMinute = minute;
        alarmTime.setText(new DecimalFormat("00").format(hourOfDay) + ":" + new DecimalFormat("00").format(minute));
    }
}
