package sjq5766.aut.chessapp.ChessApp.SMS;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import sjq5766.aut.chessapp.ChessApp.SMSActivity;
import sjq5766.aut.chessapp.ChessApp.SMSChessActivity;

public class SMSReceiver extends BroadcastReceiver {
    public static final String SMS_EXTRA_NAME = "pdus";
    private String player1Phone;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        SmsMessage[] msgs = null;

        if(bundle != null)
        {
            String info = "";
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];
            byte[] data = null;

            for (int i=0; i<pdus.length; i++){
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                player1Phone = msgs[i].getOriginatingAddress();

                data = msgs[i].getUserData();

                for(int index=0; index<data.length; ++index)
                {
                    info += Character.toString((char)data[index]);
                }
            }

            if(info.equals("startgame")) {
                SMSActivity insta = SMSActivity.instance();
                Log.i("startgame", player1Phone);
                insta.makeStartDialog(player1Phone);
            } else {
                SMSChessActivity insta = SMSChessActivity.instance();
                insta.updateGame(info);
            }
        }
    }
}
