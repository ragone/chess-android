package sjq5766.aut.chessapp.ChessApp;

        import java.util.ArrayList;

        import android.app.Activity;
        import android.app.AlertDialog;
        import android.app.PendingIntent;
        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.IntentFilter;
        import android.os.Bundle;
        import android.telephony.SmsManager;
        import android.telephony.SmsMessage;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.CheckBox;
        import android.widget.TextView;
        import android.widget.Toast;

        import sjq5766.aut.chessapp.R;


public class SMSActivity extends Activity {
    private static final int MAX_SMS_MESSAGE_LENGTH = 160;
    private static final int SMS_PORT = 8091;
    private static final String SMS_DELIVERED = "SMS_DELIVERED";
    private static final String SMS_SENT = "SMS_SENT";
    private TextView message;
    private String senderPhoneNo;
    private static SMSActivity insta;

    private boolean success = false;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        ((Button)findViewById(R.id.btnStartGame)).setOnClickListener(onButtonClick);

        registerReceiver(sendreceiver, new IntentFilter(SMS_SENT));
        registerReceiver(deliveredreceiver, new IntentFilter(SMS_DELIVERED));

//        registerReceiver(smsreceiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));

        message = (TextView)findViewById(R.id.textView2);
        insta = this;
    }

    public static SMSActivity instance() {
        return insta;
    }

    public boolean makeStartDialog(final String player1Phone) {
        new AlertDialog.Builder(this)
                .setTitle("Start Game?")
                .setMessage("Would you like to play some chess?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(SMSActivity.this, SMSChessActivity.class);
                        intent.putExtra("OppPhone", player1Phone);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        success = false;
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
        return success;
    }

    @Override
    protected void onDestroy()
    {
        unregisterReceiver(sendreceiver);
        unregisterReceiver(deliveredreceiver);
//        unregisterReceiver(smsreceiver);

        super.onDestroy();
    }

    private void sendSms(String phonenumber,String message, boolean isBinary)
    {
        SmsManager manager = SmsManager.getDefault();

        PendingIntent piSend = PendingIntent.getBroadcast(this, 0, new Intent(SMS_SENT), 0);
        PendingIntent piDelivered = PendingIntent.getBroadcast(this, 0, new Intent(SMS_DELIVERED), 0);

        if(isBinary)
        {
            byte[] data = new byte[message.length()];

            for(int index=0; index<message.length() && index < MAX_SMS_MESSAGE_LENGTH; ++index)
            {
                data[index] = (byte)message.charAt(index);
            }

            manager.sendDataMessage(phonenumber, null, (short) SMS_PORT, data,piSend, piDelivered);
        }
        else
        {
            int length = message.length();

            if(length > MAX_SMS_MESSAGE_LENGTH)
            {
                ArrayList<String> messagelist = manager.divideMessage(message);

                manager.sendMultipartTextMessage(phonenumber, null, messagelist, null, null);
            }
            else
            {
                manager.sendTextMessage(phonenumber, null, message, piSend, piDelivered);
            }
        }
    }

    private View.OnClickListener onButtonClick = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            switch(v.getId())
            {
                case R.id.btnStartGame:
                {
                    String phonenumber = ((TextView)findViewById(R.id.phoneText)).getText().toString();
                    String message = "startgame";

                    sendSms(phonenumber, message, true);

                    Intent intent = new Intent(SMSActivity.this, SMSChessActivity.class);
                    intent.putExtra("OppPhone", phonenumber);
                    startActivity(intent);

                    break;
                }
            }
        }
    };

    private BroadcastReceiver sendreceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String info = "Send information: ";

            switch(getResultCode())
            {
                case Activity.RESULT_OK: info += "send successful"; break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE: info += "send failed, generic failure"; break;
                case SmsManager.RESULT_ERROR_NO_SERVICE: info += "send failed, no service"; break;
                case SmsManager.RESULT_ERROR_NULL_PDU: info += "send failed, null pdu"; break;
                case SmsManager.RESULT_ERROR_RADIO_OFF: info += "send failed, radio is off"; break;
            }

            Toast.makeText(getBaseContext(), info, Toast.LENGTH_SHORT).show();

        }
    };

    private BroadcastReceiver deliveredreceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String info = "Delivery information: ";

            switch(getResultCode())
            {
                case Activity.RESULT_OK: info += "delivered"; break;
                case Activity.RESULT_CANCELED: info += "not delivered"; break;
            }

            Toast.makeText(getBaseContext(), info, Toast.LENGTH_SHORT).show();
        }
    };
}