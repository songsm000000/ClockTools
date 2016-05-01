package ssm.clocktools.clockTool;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ssm.clocktools.R;

public class Clock2 extends Fragment {

    private Button startCountBtn = null;
    private Button resetCountBtn = null;
    private Button getCountBtn = null;

    private TextView countTimeInput =  null;
    private TextView timeList = null;

    private View view = null;

    public void setCountTimeInput(String strTime) {

        if (this.countTimeInput != null) {
            this.countTimeInput.setText(strTime);
        }
    }

    public void setTimeList(String strTime) {

        if (this.timeList != null) {
            this.timeList.setText(strTime);
            if (strTime.split("\n").length > 10) {
                this.timeList.scrollTo(0, (strTime.split("\n").length - 10) * 47);
            }

        }
    }

    public void setStartCountBtnName(String btnName) {
        this.startCountBtn.setText(btnName);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.clock2_view, container, false);

            startCountBtn = (Button) view.findViewById(R.id.startCountBtn);
            resetCountBtn = (Button) view.findViewById(R.id.resetCountBtn);
            getCountBtn = (Button) view.findViewById(R.id.getCountBtn);

            countTimeInput = (TextView) view.findViewById(R.id.countTimeInput);
            timeList = (TextView) view.findViewById(R.id.timeList);
            timeList.setMovementMethod(new ScrollingMovementMethod());
        }

        return view;
    }
}
