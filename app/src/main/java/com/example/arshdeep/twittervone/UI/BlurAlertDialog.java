package com.example.arshdeep.twittervone.UI;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import com.example.arshdeep.twittervone.R;
import com.ms_square.etsyblur.BlurConfig;
import com.ms_square.etsyblur.BlurDialogFragment;
import com.ms_square.etsyblur.SmartAsyncPolicy;

/**
 * Created by Arshdeep on 7/22/2017.
 */

public class BlurAlertDialog extends BlurDialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog alert_dialog = new AlertDialog.Builder(getContext(), R.style.EtsyBlurAlertDialogTheme)
                .setTitle("Confirm to follow ?")
                .setPositiveButton("Ok" , null)
                .setNegativeButton("Cancel" , null)
                .create();
        return alert_dialog;
    }

    @NonNull
    @Override
    protected BlurConfig blurConfig() {
        return new BlurConfig.Builder()
                .overlayColor(Color.argb(136, 255, 255, 255))
                .asyncPolicy(SmartAsyncPolicyHolder.INSTANCE.smartAsyncPolicy())
                .debug(true)
                .build();
    }
}
