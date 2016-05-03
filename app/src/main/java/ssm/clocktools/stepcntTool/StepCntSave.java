package ssm.clocktools.stepcntTool;


import android.content.Context;
import android.content.SharedPreferences;

public class StepCntSave {

    private SharedPreferences sp = null;
    SharedPreferences.Editor editor = null;
    private Context context = null;

    public StepCntSave(Context context) {
        this.context = context;

    }

    public void setStepCnt(String key, String value) {
        sp = context.getSharedPreferences("stepcnt", Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void setStartStep(String key, Boolean value) {
        sp = context.getSharedPreferences("stepcnt", Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public String getStepCnt(String key) {
        sp = context.getSharedPreferences("stepcnt", Context.MODE_PRIVATE);
        return sp.getString(key, "0");
    }

    public Boolean getStartStep(String key) {
        sp = context.getSharedPreferences("stepcnt", Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }
}
