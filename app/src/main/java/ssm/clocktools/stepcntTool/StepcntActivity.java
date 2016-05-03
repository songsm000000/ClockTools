package ssm.clocktools.stepcntTool;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import ssm.clocktools.R;

public class StepCntActivity extends AppCompatActivity implements View.OnClickListener{

    private Button startStepBtn = null;
    private Button resetStepBtn = null;
    private TextView stepCntInput = null;
    private TextView isStepCnt = null;

    private boolean startStep = false;

    private StepCntSave stepCntSave = null;

    private ShowStepCntHandler showstepCntHandler = null;
    private Timer timer = null;

    private Intent intent = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setpcnt_view);
        init();
    }

    @Override
    protected void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }

    private void init() {

        startStepBtn = (Button) findViewById(R.id.startStepBtn);
        resetStepBtn = (Button) findViewById(R.id.resetStepBtn);
        stepCntInput = (TextView) findViewById(R.id.stepCntInput);
        isStepCnt = (TextView) findViewById(R.id.isStepCnt);

        startStepBtn.setOnClickListener(this);
        resetStepBtn.setOnClickListener(this);

        stepCntSave = new StepCntSave(this);
        stepCntInput.setText(stepCntSave.getStepCnt("stepCnt"));
        startStep = stepCntSave.getStartStep("startStep");
        if (startStep) {
            startStepBtn.setText("停止计步");
            isStepCnt.setText("计步中。。。");
        } else {
            startStepBtn.setText("开始计步");
            isStepCnt.setText("未计步");
        }

        showstepCntHandler = new ShowStepCntHandler();
        timer = new Timer();
        timer.schedule(new ShowStepCntTask(), 0, 200);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.startStepBtn:

                if (!startStep) {

                    intent = new Intent(this, StepCntService.class);
                    startService(intent);
                    startStepBtn.setText("停止计步");
                    isStepCnt.setText("计步中。。。");
                    startStep = true;


                } else {

                    intent = new Intent(this, StepCntService.class);
                    stopService(intent);
                    startStepBtn.setText("开始计步");
                    isStepCnt.setText("未计步");
                    startStep = false;

                }

                stepCntSave.setStartStep("startStep", startStep);

                break;

            case R.id.resetStepBtn:

                if (startStep) {
                    Toast.makeText(this, "请先停止计步", Toast.LENGTH_SHORT).show();
                } else {
                    stepCntSave.setStepCnt("stepCnt", "0");
                }

                break;

        }

    }

    private class ShowStepCntHandler extends Handler {

        public ShowStepCntHandler() {
            super();
        }

        @Override
        public void handleMessage(Message msg) {

            stepCntInput.setText(stepCntSave.getStepCnt("stepCnt"));
            super.handleMessage(msg);
        }
    }

    private class ShowStepCntTask extends TimerTask {

        @Override
        public void run() {
            showstepCntHandler.sendMessage(new Message());
        }
    }
}
