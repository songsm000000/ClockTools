package ssm.clocktools;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ssm.clocktools.clockTool.ClockActivity;
import ssm.clocktools.compassTool.CompassActivity;
import ssm.clocktools.stepcntTool.StepCntActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button clockToolBtn = null;
    private Button compassToolBtn = null;
    private Button stepCntBtn = null;

    private Intent intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {

        clockToolBtn = (Button) findViewById(R.id.clockToolBtn);
        compassToolBtn = (Button) findViewById(R.id.compassToolBtn);
        stepCntBtn = (Button) findViewById(R.id.stepCntBtn);

        clockToolBtn.setOnClickListener(this);
        compassToolBtn.setOnClickListener(this);
        stepCntBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.clockToolBtn:

                intent = new Intent(this, ClockActivity.class);
                startActivity(intent);
                break;

            case R.id.compassToolBtn:
                intent = new Intent(this, CompassActivity.class);
                startActivity(intent);
                break;

            case R.id.stepCntBtn:
                intent = new Intent(this, StepCntActivity.class);
                startActivity(intent);
                break;

        }

    }
}
