package ssm.clocktools.stepcntTool;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class StepCntService extends Service implements SensorEventListener{

    private Sensor sensor = null;
    private SensorManager sensorManager = null;

    private StepCntSave stepCntSave = null;
    private int stepCnt = 0;

    private float lastAcc = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        stepCntSave = new StepCntSave(this);
        stepCnt = Integer.parseInt(stepCntSave.getStepCnt("stepCnt"));

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
        super.onDestroy();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (Math.abs(lastAcc - event.values[1]) > 8) {

            lastAcc = event.values[1];
            stepCnt = stepCnt + 1;
            stepCntSave.setStepCnt("stepCnt", stepCnt+"");

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
