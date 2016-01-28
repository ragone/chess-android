package sjq5766.aut.chessapp.ChessApp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sjq5766.aut.chessapp.ChessApp.Bluetooth.BTGame;
import sjq5766.aut.chessapp.ChessCore.Interface.BoardView;
import sjq5766.aut.chessapp.ChessCore.Interface.ChessGame;
import sjq5766.aut.chessapp.ChessCore.Misc.FEN;
import sjq5766.aut.chessapp.ChessCore.Objects.GameState;
import sjq5766.aut.chessapp.R;


public class ChessActivity extends ActionBarActivity
{
    private BTGame btGame;
    private Boolean gameIsEnabled;
    private View view;
    private ChessGame game;
    private BoardView boardUI;
    private TextView receivedTextView;
    private List<String> receivedMessages;
    private static int MAX_RECEIVED_DISPLAY = 1;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {  super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chess);


        // obtain the chatNode passed in with the intent
        Intent intent = getIntent();
        gameIsEnabled = false;
        btGame = (BTGame)intent.getExtras().get
                (BTGame.class.getName());
        boardUI = new BoardView(this);
        game = new ChessGame();
        game.checkForMate();
        boardUI.setGame(game);
        boardUI.setChessActivity(this);
        receivedTextView =(TextView)findViewById(R.id.received_textview);
        receivedMessages = new ArrayList<String>();

        if(receivedMessages.size() != 0){
            for(int i = 0; i < receivedMessages.size(); i++){
                showReceivedMessage(receivedMessages.get(i));
            }
        }

    }

    public void onStart()
    {  super.onStart();

        btGame.registerActivity(this, game);
        Thread thread = new Thread(btGame);
        thread.start();
    }

    public void onStop()
    {  super.onStop();
        btGame.stop();
        Toast.makeText(getApplicationContext(),"Ending Session", Toast.LENGTH_LONG).show();
        btGame.registerActivity(null, null);
    }

    public synchronized void showReceivedMessage(String message)
    {
        receivedMessages.add(0, message);
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i=0; i<receivedMessages.size()&&i<MAX_RECEIVED_DISPLAY;
             i++) {
            stringBuilder.append(receivedMessages.get(i));
            stringBuilder.append("\n");

            if (gameIsEnabled) {
                if (game.getCurrentGameState() == GameState.normal) {
                    String fen = stringBuilder.toString();
                    game.setBoard(FEN.decode(game, fen));
                }


            }
        }


        // update the received TextView in the UI thread
        receivedTextView.post(new Runnable()
        {
            public void run()
            {
                receivedTextView.setText(stringBuilder.toString());
            }
        });
    }

    public void updateChess(){
        if(game.getCurrentGameState() == GameState.moved) {
            String message = FEN.encode(game, game.getBoard());
            btGame.forward(message);
            game.turnMade();
        }
    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chess, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.list_item1) {

            if(gameIsEnabled) {
                setContentView(boardUI);
            }

            else
              Toast.makeText(getApplicationContext(), "Connection not complete. You will be prompted when available.", Toast.LENGTH_LONG).show();
        } else if(id == R.id.list_item2) {

            setContentView(R.layout.activity_chess);
            receivedTextView =(TextView)findViewById(R.id.received_textview);

            }


        return super.onOptionsItemSelected(item);
    }

    public void gameEnabled(){
        this.gameIsEnabled = true;
    }

}
