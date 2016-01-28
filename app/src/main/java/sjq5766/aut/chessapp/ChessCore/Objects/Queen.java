package sjq5766.aut.chessapp.ChessCore.Objects;

import java.util.ArrayList;

import sjq5766.aut.chessapp.ChessCore.Interface.AvailableMoves;
import sjq5766.aut.chessapp.ChessCore.Interface.Cell;
import sjq5766.aut.chessapp.ChessCore.Interface.ChessGame;

public class Queen implements AvailableMoves {
    // Fields
    private ChessGame game;
    private Rook horizontalVertical;
    private Bishop diagonal;

    // Constructor
    public Queen(ChessGame game) {
        this.game = game;
    }

    @Override
    public ArrayList<Cell> getAvailableMoves(Piece piece) {
        if (piece.getLocation().getY() == -1)
            return null;
        horizontalVertical = new Rook(game);
        diagonal = new Bishop(game);
        ArrayList<Cell> retList = horizontalVertical
                .getAvailableMoves(piece);
        ArrayList<Cell> moreMoves = diagonal.getAvailableMoves(piece);
        for (int i = 0; i < moreMoves.size(); i++) {
            retList.add(moreMoves.get(i));
        }
        return retList;
    }
}
