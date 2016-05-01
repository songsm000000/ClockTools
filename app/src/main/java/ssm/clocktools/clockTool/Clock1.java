package ssm.clocktools.clockTool;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ssm.clocktools.R;

public class Clock1 extends Fragment{

    private View view = null;
    private TextView timeInput = null;

    public void setTimeInput(String timeInput) {

        if (this.timeInput != null) {
            this.timeInput.setText(timeInput);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.clock1_view, container, false);
            timeInput = (TextView) view.findViewById(R.id.timeInput);
        }

        return view;
    }

}
