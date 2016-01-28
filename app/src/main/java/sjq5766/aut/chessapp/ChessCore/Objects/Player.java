package sjq5766.aut.chessapp.ChessCore.Objects;

import java.util.ArrayList;

import sjq5766.aut.chessapp.ChessCore.Interface.Cell;
import sjq5766.aut.chessapp.ChessCore.Interface.MoveStatus;
import sjq5766.aut.chessapp.R;

public class Player {
    // Fields
    private ChessColor color;
    private Piece[] pieces;

    // Constructor
    public Player(ChessColor color, Piece[] pieces) {
        this.color = color;
        this.pieces = pieces;
    }

    // Getters
    public ChessColor getColor() {
        return color;
    }

    public Piece[] getPieces() {
        return pieces;
    }

    // Methods
    public boolean move(Piece piece, Cell moveTo) {
        if(piece == null || piece.getColor() != this.color || moveTo == null) {
            return false;
        }

        ArrayList<Cell> availableMoves = piece.getAvailableMoves();
        if(availableMoves.contains(moveTo)) {
            MoveStatus status = piece.tryMove(moveTo);
            if(status != MoveStatus.promote) {
                if(status == MoveStatus.success) {
                    return true;
                } else {
                    return false;
                }
            } else {
                try {
                    PieceType type = PieceType.queen;
                    piece.setType(type);
                    if (piece.getColor() == ChessColor.white)
                        piece.setImage(R.drawable.wq);
                    else
                        piece.setImage(R.drawable.bq);
                    return true;
                } catch (Exception ex) {
                    System.err.println("promotion failed");
                }
            }
        }
        return false;
    }
}
