package sjq5766.aut.chessapp.ChessApp.Bluetooth;

import java.io.Serializable;
import java.util.UUID;

import sjq5766.aut.chessapp.ChessApp.ChessActivity;
import sjq5766.aut.chessapp.ChessCore.Interface.ChessGame;
import sjq5766.aut.chessapp.ChessCore.Objects.Player;

public interface BTGame extends Runnable, Serializable{

    // uuid for the Bluetooth application
    public static final UUID SERVICE_UUID
            = UUID.fromString("aa7e561f-591f-4767-bf26-e4bff3f0895f");
    // name for the Bluetooth application
    public static final String SERVICE_NAME = "ChessApp";
    // forward a message to all chat nodes in the Bluetooth network
    public void forward(String message);
    // stop this chat node and clean up
    public void stop();
    // registers or unregisters (if null) a ChatActivity for display
    public void registerActivity(ChessActivity chessActivity, ChessGame game);
}
