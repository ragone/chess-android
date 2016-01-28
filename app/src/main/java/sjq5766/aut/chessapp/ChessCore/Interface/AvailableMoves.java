package sjq5766.aut.chessapp.ChessCore.Interface;

import java.util.ArrayList;

import sjq5766.aut.chessapp.ChessCore.Objects.Piece;

public interface AvailableMoves {
    public ArrayList<Cell> getAvailableMoves(Piece piece);
}
