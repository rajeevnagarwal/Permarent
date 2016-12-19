package com.furniture.appliances.rentals.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import com.furniture.appliances.rentals.OtpVerification;

/**
 * Created by Infinia on 04-10-2015.
 */
public class SmsReceiver extends BroadcastReceiver {

    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        final Bundle bundle = intent.getExtras();

        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {
                    SmsMessage currentMessage = SmsMessage
                            .createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage
                            .getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();
                    if(senderNum.contains("PMRENT"))
                    OtpVerification.setSmsReceived(message);

                } // end of for loop
            } // bundle

        } catch (Exception e) {
            // TODO: handle exception
            Log.e("SmsReciver", "Exception smsReciver" + e);
        }
    }
}
