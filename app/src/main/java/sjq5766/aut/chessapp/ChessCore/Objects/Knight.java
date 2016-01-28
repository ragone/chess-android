package sjq5766.aut.chessapp.ChessCore.Objects;

import java.util.ArrayList;

import sjq5766.aut.chessapp.ChessCore.Interface.AvailableMoves;
import sjq5766.aut.chessapp.ChessCore.Interface.Cell;
import sjq5766.aut.chessapp.ChessCore.Interface.ChessGame;

public class Knight implements AvailableMoves {
    // Fields
    private ChessGame game;

    // Constructor
    public Knight(ChessGame game) {
        this.game = game;
    }

    @Override
    public ArrayList<Cell> getAvailableMoves(Piece piece) {
        if (piece.getLocation().getY() == -1)
            return null;
        ArrayList<Cell> retList = new ArrayList<Cell>();
        int currX = piece.getLocation().getX();
        int currY = piece.getLocation().getY();

        if (currX > 1
                && currY > 0
                && (game.getBoard()[currX - 2][currY - 1].getPiece() == null || game.getBoard()[currX - 2][currY - 1].getPiece().getColor() != piece.getColor()))
            retList.add(game.getBoard()[currX - 2][currY - 1]);

        if (currX > 0
                && currY > 1
                && (game.getBoard()[currX - 1][currY - 2].getPiece() == null || game.getBoard()[currX - 1][currY - 2].getPiece().getColor() != piece.getColor()))
            retList.add(game.getBoard()[currX - 1][currY - 2]);

        if (currX < 7
                && currY > 1
                && (game.getBoard()[currX + 1][currY - 2].getPiece() == null || game.getBoard()[currX + 1][currY - 2].getPiece().getColor() != piece.getColor()))
            retList.add(game.getBoard()[currX + 1][currY - 2]);

        if (currX < 6
                && currY > 0
                && (game.getBoard()[currX + 2][currY - 1].getPiece() == null || game.getBoard()[currX + 2][currY - 1].getPiece().getColor() != piece.getColor()))
            retList.add(game.getBoard()[currX + 2][currY - 1]);

        if (currX < 6
                && currY < 7
                && (game.getBoard()[currX + 2][currY + 1].getPiece() == null || game.getBoard()[currX + 2][currY + 1].getPiece().getColor() != piece.getColor()))
            retList.add(game.getBoard()[currX + 2][currY + 1]);

        if (currX < 7
                && currY < 6
                && (game.getBoard()[currX + 1][currY + 2].getPiece() == null || game.getBoard()[currX + 1][currY + 2].getPiece().getColor() != piece.getColor()))
            retList.add(game.getBoard()[currX + 1][currY + 2]);

        if (currX > 0
                && currY < 6
                && (game.getBoard()[currX - 1][currY + 2].getPiece() == null || game.getBoard()[currX - 1][currY + 2].getPiece().getColor() != piece.getColor()))
            retList.add(game.getBoard()[currX - 1][currY + 2]);

        if (currX > 1
                && currY < 7
                && (game.getBoard()[currX - 2][currY + 1].getPiece() == null || game.getBoard()[currX - 2][currY + 1].getPiece().getColor() != piece.getColor()))
            retList.add(game.getBoard()[currX - 2][currY + 1]);

        return retList;
    }
}
