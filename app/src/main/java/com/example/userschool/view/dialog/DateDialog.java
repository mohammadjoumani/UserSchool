package com.example.userschool.view.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.userschool.R;

import org.greenrobot.eventbus.EventBus;

public class DateDialog extends DialogFragment {
    private DatePicker dpDateDialogDate;
    private Button btnDateDialogOk;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setStyle( DialogFragment.STYLE_NORMAL, R.style.datepicker );
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = layoutInflater.inflate( R.layout.dialog_date, null );

        dpDateDialogDate = view.findViewById( R.id.dpDateDialogDate );
        btnDateDialogOk = view.findViewById( R.id.btnDateDialogOk );
        btnDateDialogOk.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = dpDateDialogDate.getYear() + "-" +
                        (dpDateDialogDate.getMonth() + 1) + "-" +
                        dpDateDialogDate.getDayOfMonth();

                EventBus.getDefault().post( date );
                dismiss();
            }
        } );

        return view;
    }


}
