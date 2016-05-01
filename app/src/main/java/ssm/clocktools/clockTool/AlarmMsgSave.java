package ssm.clocktools.clockTool;

import android.content.Context;
import android.content.SharedPreferences;

public class AlarmMsgSave {

    private SharedPreferences sp = null;
    SharedPreferences.Editor editor = null;
    private Context context = null;

    public AlarmMsgSave(Context context) {
        this.context = context;

    }

    public void setAlarmMsg(String key, String value) {
        sp = context.getSharedPreferences("alarmclock", Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getAlarmMsg(String key) {
        sp = context.getSharedPreferences("alarmclock", Context.MODE_PRIVATE);
        return sp.getString(key, "");
    }

}
