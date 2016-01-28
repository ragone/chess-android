package sjq5766.aut.chessapp.ChessApp.Bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import sjq5766.aut.chessapp.ChessApp.ChessActivity;
import sjq5766.aut.chessapp.ChessCore.Interface.ChessGame;
import sjq5766.aut.chessapp.ChessCore.Objects.Player;

public class BTClient implements BTGame {

    private boolean stopRequested;
    private List<BluetoothDevice> devices;
    private BluetoothSocket socket;
    private List<String> messages;//list of messages still to be mailed
    private ChessActivity chessActivity;
    private ChessGame game;
    private Player player;
    private BroadcastReceiver deviceDiscoveryBroadcastReceiver;
    private static final long serialVersionUID = 1;

    public BTClient()
    {
        devices = new ArrayList<BluetoothDevice>();
        socket = null;
        messages = new ArrayList<String>();
        chessActivity = null;
        deviceDiscoveryBroadcastReceiver = null;
    }

    // implementation of BTGame method
    public void run()
    {
        stopRequested = false;
        devices.clear();
        messages.clear();
        // start device discovery(could instead first try paired devices)
        deviceDiscoveryBroadcastReceiver
                = new DeviceDiscoveryBroadcastReceiver();
        IntentFilter discoveryIntentFilter = new IntentFilter();
        discoveryIntentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        discoveryIntentFilter.addAction
                (BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        discoveryIntentFilter.addAction
                (BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        chessActivity.registerReceiver(deviceDiscoveryBroadcastReceiver,
                discoveryIntentFilter);
        BluetoothAdapter bluetoothAdapter
                = BluetoothAdapter.getDefaultAdapter();
        bluetoothAdapter.startDiscovery();
        // make this thread wait until device discovery has finished
        synchronized (devices)
        {  try
        {  devices.wait();
        }
        catch (InterruptedException e)
        {  // ignore
        }
        }
        if (devices.size() == 0 && !stopRequested)
        {
            Log.w("ChatClient", "No devices discovered");
            stopRequested = true;
            return;
        }
        // now check each device for the Bluetooth application UUID
        // note only newer API support fetchUuidsWithSdp to perform SDP
        socket = null;
        for (BluetoothDevice device : devices)
        {  // try to open a connection to device using UUID
            try
            {  chessActivity.showReceivedMessage
                    ("CLIENT: checking for server on " + device.getName());
                Log.w("ChatClient", "Checking for server on "
                        + device.getName());
                socket = device.createRfcommSocketToServiceRecord
                        (BTGame.SERVICE_UUID);
                // open the connection
                socket.connect();
                bluetoothAdapter.cancelDiscovery();
                break;
            }
            catch (IOException e)
            {  // ignore and try next device
                socket = null;
            }
        }
        if (socket == null)
        {  chessActivity.showReceivedMessage
                ("CLIENT: no server found, restart client");
            Log.e("ChatClient", "No server service found");
            stopRequested = true;
            return;
        }
        chessActivity.showReceivedMessage("CLIENT: chess game server found");
        chessActivity.gameEnabled();
        Log.w("ChatClient", "Chess game server found");
        Mailer mailer = new Mailer();
        Thread mailerThread = new Thread(mailer);
        mailerThread.start();
        // listen for incoming messages
        try
        {  BufferedReader br = new BufferedReader(new InputStreamReader
                (socket.getInputStream()));
            // loop until the connection closes or stop requested
            while (!stopRequested)
            {  String message = br.readLine(); // blocking
                // put message on client display
                if (chessActivity != null) {
                    chessActivity.showReceivedMessage(message);
                }
            }
        }
        catch (IOException e)
        {  chessActivity.showReceivedMessage
                ("CLIENT: Client disconnecting");
            Log.w("ChatClient", "Client Disconnecting");
        }
        finally
        {  try
        {  socket.close();
        }
        catch (IOException e)
        { // ignore
        }
            socket = null;
        }
    }

    // implementation of BTGame method
    public void forward(String message)
    {  synchronized (messages)
    {


        messages.add(message);
        // notify waiting threads that there is a new message to send
        messages.notifyAll();
    }
    }

    // implementation of BTGame method
    public void stop()
    {  stopRequested = true;
        if (deviceDiscoveryBroadcastReceiver != null)
        {  chessActivity.unregisterReceiver
                (deviceDiscoveryBroadcastReceiver);
            deviceDiscoveryBroadcastReceiver = null;
        }
        synchronized (devices)
        {  devices.notifyAll();
        }
        synchronized (messages)
        {  messages.notifyAll();
        }
        if (socket != null)
        {  try
        {  socket.close();
        }
        catch (IOException e)
        { // ignore
        }
        }
    }

    // implementation of BTGame method
    public void registerActivity(ChessActivity chessActivity, ChessGame game)
    {
        this.chessActivity = chessActivity;
        this.game = game;

    }



    // inner class that handles sending messages to host connection
    private class Mailer implements Runnable
    {
        public void run()
        {  PrintWriter pw = null;
            try
            {  pw = new PrintWriter(new BufferedWriter
                    (new OutputStreamWriter(socket.getOutputStream())));
            }
            catch (IOException e)
            {  Log.e("ChatClient", "Mailer IOException: " + e);
                stop();
            }
            while (!stopRequested)
            {  // get a message
                String message;
                synchronized (messages)
                {  while (messages.size() == 0)
                {  try
                {  messages.wait();
                }
                catch (InterruptedException e)
                { // ignore
                }
                    if (stopRequested)
                        return;
                }
                    message = messages.remove(0);
                }
                // forward message to server
                pw.println(message);
                pw.flush();
            }
        }
    }

    // inner class that receives device discovery changes
    public class DeviceDiscoveryBroadcastReceiver
            extends BroadcastReceiver
    {
        public void onReceive(Context context, Intent intent)
        {  String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action))
            {  // a device has been found
                BluetoothDevice device = intent.getParcelableExtra
                        (BluetoothDevice.EXTRA_DEVICE);
                synchronized (devices)
                {
                    devices.add(device);
                }
                // note newer API can use device.fetchUuidsWithSdp for SDP
                chessActivity.showReceivedMessage
                        ("CLIENT: device discovered " + device.getName());
                Log.w("ChatClient", "Device discovered "+device.getName());
            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals
                    (action))
            {  chessActivity.showReceivedMessage
                    ("CLIENT: device discovery started");
                Log.w("ChatClient", "Device discovery started");
            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals
                    (action))
            {  // notify chat client that device discovery has finished
                synchronized (devices)
                {  devices.notifyAll();
                }
                Log.w("ChatClient", "Device discovery finished");
            }
        }
    }

}
