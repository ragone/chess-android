package sjq5766.aut.chessapp.ChessCore.Objects;

import java.util.ArrayList;

import sjq5766.aut.chessapp.ChessCore.Interface.AvailableMoves;
import sjq5766.aut.chessapp.ChessCore.Interface.Cell;
import sjq5766.aut.chessapp.ChessCore.Interface.ChessGame;

public class King implements AvailableMoves {
    // Fields
    private ChessGame game;

    // Constructor
    public King(ChessGame game) {
        this.game = game;
    }

    @Override
    public ArrayList<Cell> getAvailableMoves(Piece piece) {
        if (piece.getLocation().getY() == -1)
            return null;
        ArrayList<Cell> retList = new ArrayList<Cell>();
        int currX = piece.getLocation().getX();
        int currY = piece.getLocation().getY();

        if (currX > 0 && currY > 0
                && (piece.isValidMove(game.getBoard()[currX - 1][currY - 1])))
            retList.add(game.getBoard()[currX - 1][currY - 1]);

        if (currY > 0 && (piece.isValidMove(game.getBoard()[currX][currY - 1])))
            retList.add(game.getBoard()[currX][currY - 1]);

        if (currX < 7 && currY > 0
                && (piece.isValidMove(game.getBoard()[currX + 1][currY - 1])))
            retList.add(game.getBoard()[currX + 1][currY - 1]);

        if (currX < 7 && (piece.isValidMove(game.getBoard()[currX + 1][currY])))
            retList.add(game.getBoard()[currX + 1][currY]);

        if (currX < 7 && currY < 7
                && (piece.isValidMove(game.getBoard()[currX + 1][currY + 1])))
            retList.add(game.getBoard()[currX + 1][currY + 1]);

        if (currY < 7 && (piece.isValidMove(game.getBoard()[currX][currY + 1])))
            retList.add(game.getBoard()[currX][currY + 1]);

        if (currX > 0 && currY < 7
                && (piece.isValidMove(game.getBoard()[currX - 1][currY + 1])))
            retList.add(game.getBoard()[currX - 1][currY + 1]);

        if (currX > 0 && piece.isValidMove(game.getBoard()[currX - 1][currY]))
            retList.add(game.getBoard()[currX - 1][currY]);

        return retList;
    }
}
