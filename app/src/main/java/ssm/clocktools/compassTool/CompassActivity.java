package ssm.clocktools.compassTool;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import ssm.clocktools.R;

public class CompassActivity extends AppCompatActivity implements SensorEventListener{

    private TextView directionTxt = null;
    private ImageView compassImg = null;

    private SensorManager sensorManager = null;
    private float lastDegree = 0;
    private String direction = "";

    private Bitmap bitmap = null;

    private ShowCompass showCompass = null;
    private Timer runTimer = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compass_view);
        init();

    }

    @Override
    protected void onDestroy() {
        runTimer.cancel();
        sensorManager.unregisterListener(this);
        super.onDestroy();
    }

    private void init() {

        directionTxt = (TextView) findViewById(R.id.directionTxt);
        compassImg = (ImageView) findViewById(R.id.compassImg);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_UI);

        showCompass = new ShowCompass();
        runTimer = new Timer();
        runTimer.schedule(new drawTask(), 0, 200);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (Math.abs(lastDegree - event.values[0]) < 1) {
            return;
        }

        lastDegree = event.values[0];

        if (event.values[0] <= 5 || event.values[0] > 355) {
            direction = "正北";
        }
        if (event.values[0] > 85 && event.values[0] <= 95) {
            direction = "正东";
        }
        if (event.values[0] > 175 && event.values[0] <= 185) {
            direction = "正南";
        }
        if (event.values[0] > 265 && event.values[0] <= 275) {
            direction = "正西";
        }
        if (event.values[0] > 5 && event.values[0] <= 45) {
            direction = "东北偏北";
        }
        if (event.values[0] > 45 && event.values[0] <= 85) {
            direction = "东北偏东";
        }
        if (event.values[0] > 95 && event.values[0] <= 135) {
            direction = "东南偏东";
        }
        if (event.values[0] > 135 && event.values[0] <= 175) {
            direction = "东南偏南";
        }
        if (event.values[0] > 185 && event.values[0] <= 225) {
            direction = "西南偏南";
        }
        if (event.values[0] > 225 && event.values[0] <= 265) {
            direction = "西南偏西";
        }
        if (event.values[0] > 275 && event.values[0] <= 315) {
            direction = "西北偏西";
        }
        if (event.values[0] > 315 && event.values[0] < 355) {
            direction = "西北偏北";
        }

        directionTxt.setText("方向：" + direction);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private class ShowCompass extends Handler{

        public ShowCompass() {
            super();
        }

        @Override
        public void handleMessage(Message msg) {

            compassImg.setImageBitmap(bitmap);
            super.handleMessage(msg);
        }
    }

    private class drawTask extends TimerTask {

        @Override
        public void run() {

            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.compass);
            Matrix matrix = new Matrix();
            matrix.postRotate(Math.round(lastDegree)*(-1));
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

            showCompass.sendMessage(new Message());

        }
    }
}
