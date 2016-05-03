package ssm.clocktools.stepcntTool;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;

public class StepCntService extends Service implements SensorEventListener{

    private Sensor sensor = null;
    private SensorManager sensorManager = null;

    private StepCntSave stepCntSave = null;

    private int stepCnt = 0;
    private double curValue = 0;
    private double maxValue = 0;
    private long lstMillion = 0;
    private double range = 12;
    private long duration = 200;

    private PowerManager.WakeLock wakeLock = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        PowerManager pm = (PowerManager) getSystemService(this.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, this.getClass().getName());
        wakeLock.acquire();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        stepCntSave = new StepCntSave(this);
        stepCnt = Integer.parseInt(stepCntSave.getStepCnt("stepCnt"));
        lstMillion = System.currentTimeMillis();

        new Thread() {

            @Override
            public void run() {

                sensorManager = (SensorManager) StepCntService.this.getSystemService(SENSOR_SERVICE);
                sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                sensorManager.registerListener(StepCntService.this, sensor, SensorManager.SENSOR_DELAY_UI);

                super.run();
            }
        }.start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        sensorManager.unregisterListener(this);
        wakeLock.release();
        wakeLock = null;
        super.onDestroy();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        curValue = magnitude(event.values[0], event.values[1], event.values[2]);

        if (System.currentTimeMillis() - lstMillion > duration) {

            if (maxValue > range) {
                stepCnt = stepCnt + 1;
                stepCntSave.setStepCnt("stepCnt", stepCnt + "");
            }

            maxValue = 0;
            lstMillion = System.currentTimeMillis();

        } else {

            if (curValue > maxValue) {
                maxValue = curValue;
            }

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private double magnitude(float x, float y, float z) {
        double magnitude = 0;
        magnitude = Math.sqrt(x * x + y * y + z * z);
        return magnitude;
    }
}
