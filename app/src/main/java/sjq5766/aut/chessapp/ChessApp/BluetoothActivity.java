package sjq5766.aut.chessapp.ChessApp;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import sjq5766.aut.chessapp.ChessApp.Bluetooth.BTClient;
import sjq5766.aut.chessapp.ChessApp.Bluetooth.BTGame;
import sjq5766.aut.chessapp.ChessApp.Bluetooth.BTHost;
import sjq5766.aut.chessapp.R;


public class BluetoothActivity extends Activity implements View.OnClickListener {
    private Button serverStartButton,clientStartButton;
    private TextView statusTextView;
    private BroadcastReceiver bluetoothStatusBroadcastReceiver;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {  super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        serverStartButton=(Button)findViewById(R.id.BTHost);
        serverStartButton.setOnClickListener(this);
        clientStartButton=(Button)findViewById(R.id.BTJoin);
        clientStartButton.setOnClickListener(this);
       // statusTextView = (TextView)findViewById(R.id.status_textview);
    }

    /** Called when the activity is started. */
    @Override
    public void onStart()
    {  super.onStart();
        // check whether device supports Bluetooth
        BluetoothAdapter bluetoothAdapter
                = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null)
            Toast.makeText(getApplicationContext(), "Bluetooth not Supported on this device", Toast.LENGTH_LONG).show();
        else
        {
            if (!bluetoothAdapter.isEnabled())
            {
                // try to enable Bluetooth on device
                Intent enableBluetoothIntent
                        = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivity(enableBluetoothIntent);
            }
        }
    }

    /** Called when the activity is stopped. */
    @Override
    public void onStop()
    {  super.onStop();
        if (bluetoothStatusBroadcastReceiver != null)
            unregisterReceiver(bluetoothStatusBroadcastReceiver);
    }

    // implementation of OnClickListener method
    public void onClick(View view)
    {
    if (view == serverStartButton)
    {  // start as a server

        Toast.makeText(getApplicationContext(), "Hosting a Game Session...", Toast.LENGTH_SHORT).show();
        BTGame btGame = new BTHost();
        Intent intent = new Intent(this, ChessActivity.class);
        intent.putExtra(BTGame.class.getName(), btGame);
        startActivity(intent);
        Intent discoverableIntent
                = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra
                (BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,300); //5 min
        startActivity(discoverableIntent);
    }
    else if (view == clientStartButton)
    {  // start as a client
        Toast.makeText(getApplicationContext(), "Searching for Game Session...", Toast.LENGTH_SHORT).show();
        BTGame btGame = new BTClient();
        Intent intent = new Intent(this, ChessActivity.class);
        intent.putExtra(BTGame.class.getName(), btGame);
        startActivity(intent);
    }
    }



}
