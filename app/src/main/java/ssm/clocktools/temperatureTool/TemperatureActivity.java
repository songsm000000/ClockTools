package ssm.clocktools.temperatureTool;


import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import ssm.clocktools.R;

public class TemperatureActivity extends AppCompatActivity implements SensorEventListener{

    private TextView temperatureInput = null;

    private float nowTemp = 0;

    private ShowTemperatureHandler showTemperatureHandler = null;
    private Timer timer = null;

    private SensorManager sensorManager = null;
    private Sensor sensor = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temperature_view);
        init();
    }

    @Override
    protected void onDestroy() {
        timer.cancel();
        sensorManager.unregisterListener(this);
        super.onDestroy();
    }

    private void init() {

        temperatureInput = (TextView) findViewById(R.id.temperatureInput);

        sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);

        showTemperatureHandler = new ShowTemperatureHandler();
        timer = new Timer();
        timer.schedule(new ShowTemperatureTask(), 0, 2000);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        nowTemp = event.values[0];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private class ShowTemperatureHandler extends Handler {

        public ShowTemperatureHandler() {
            super();
        }

        @Override
        public void handleMessage(Message msg) {

            temperatureInput.setText(nowTemp + " ℃");
            super.handleMessage(msg);
        }
    }

    private class ShowTemperatureTask extends TimerTask {

        @Override
        public void run() {
            showTemperatureHandler.sendMessage(new Message());
        }
    }
}
