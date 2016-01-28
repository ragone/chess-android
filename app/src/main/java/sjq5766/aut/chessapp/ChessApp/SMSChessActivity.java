package sjq5766.aut.chessapp.ChessApp;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import sjq5766.aut.chessapp.ChessCore.Interface.BoardView;
import sjq5766.aut.chessapp.ChessCore.Interface.ChessGame;
import sjq5766.aut.chessapp.ChessCore.Misc.FEN;
import sjq5766.aut.chessapp.R;


public class SMSChessActivity extends ActionBarActivity {

    private static final int MAX_SMS_MESSAGE_LENGTH = 160;
    private static final int SMS_PORT = 8091;
    private static final String SMS_DELIVERED = "SMS_DELIVERED";
    private static final String SMS_SENT = "SMS_SENT";

    private ChessGame game;
    private BoardView boardUI;

    private String phone;
    private static SMSChessActivity insta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smschess);

        boardUI = new BoardView(this);
        game = new ChessGame();
        boardUI.setGame(game);
        boardUI.setSMSChessActivity(this);
        setContentView(boardUI);

        phone = getIntent().getExtras().get("OppPhone").toString();
        insta = this;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_smschess, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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

    public static SMSChessActivity instance() {
        return insta;
    }


    public void updateGame(String fen) {
        game.setBoard(FEN.decode(game, fen));
    }

    public void sendGame() {
        sendSms(phone, FEN.encode(game, game.getBoard()) ,true);
    }
}
