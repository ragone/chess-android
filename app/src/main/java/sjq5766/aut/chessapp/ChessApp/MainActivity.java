package sjq5766.aut.chessapp.ChessApp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import sjq5766.aut.chessapp.ChessApp.ServerSide.ServletPostAsyncTask;
import sjq5766.aut.chessapp.ChessCore.Interface.BoardView;
import sjq5766.aut.chessapp.ChessCore.Interface.ChessGame;
import sjq5766.aut.chessapp.ChessCore.Misc.FEN;
import sjq5766.aut.chessapp.ChessCore.Objects.ChessColor;
import sjq5766.aut.chessapp.R;


public class MainActivity extends ActionBarActivity {

    private ChessGame game;
    private BoardView boardUI;
    private boolean isTraining;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isTraining = true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boardUI = new BoardView(this);
        game = new ChessGame();
        boardUI.setGame(game);
        new ServletPostAsyncTask().execute(new Pair<Context, String>(this, "Manfred"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            Intent intent = new Intent(MainActivity.this, BluetoothActivity.class);
            startActivity(intent);
            // Handle bluetooth
        }else if(id == R.id.list_item2) {
            Intent intent = new Intent(MainActivity.this, SMSActivity.class);
            startActivity(intent);

        } else if(id == R.id.list_item3) {
            Intent intent = new Intent(MainActivity.this, TutorialActivity.class);
            startActivity(intent);
        }

        else if(id == R.id.list_item4) {
           if(isTraining) {
               setContentView(boardUI);
               isTraining = false;
           }
           else{
               setContentView(R.layout.activity_main);
               isTraining = true;
           }
        }



    return super.onOptionsItemSelected(item);
}
}
