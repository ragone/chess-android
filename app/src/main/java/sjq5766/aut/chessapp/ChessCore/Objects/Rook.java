package sjq5766.aut.chessapp.ChessCore.Objects;

import java.util.ArrayList;

import sjq5766.aut.chessapp.ChessCore.Interface.AvailableMoves;
import sjq5766.aut.chessapp.ChessCore.Interface.Cell;
import sjq5766.aut.chessapp.ChessCore.Interface.ChessGame;

public class Rook implements AvailableMoves {
    // Fields
    private ChessGame game;

    // Constructor
    public Rook(ChessGame game) {
        this.game = game;
    }

    @Override
    public ArrayList<Cell> getAvailableMoves(Piece piece) {
        if (piece.getLocation().getY() == -1)
            return null;
        ArrayList<Cell> retList = new ArrayList<Cell>();
        int currX = piece.getLocation().getX();
        int currY = piece.getLocation().getY();
        int i = 1;

        // right
        while (currX + i < 8
                && (game.getBoard()[currX + i][currY].getPiece() == null || game.getBoard()[currX
                + i][currY].getPiece().getColor() != piece.getColor())) {
            if (game.getBoard()[currX + i][currY].getPiece() != null
                    && game.getBoard()[currX + i][currY].getPiece().getColor() != piece.getColor()) {
                retList.add(game.getBoard()[currX + i][currY]);
                break;
            }
            retList.add(game.getBoard()[currX + i][currY]);
            i++;
        }

        i = 1;
        // left
        while (currX - i > -1
                && (game.getBoard()[currX - i][currY].getPiece() == null || game.getBoard()[currX
                - i][currY].getPiece().getColor() != piece.getColor())) {
            if (game.getBoard()[currX - i][currY].getPiece() != null
                    && game.getBoard()[currX - i][currY].getPiece().getColor() != piece.getColor()) {
                retList.add(game.getBoard()[currX - i][currY]);
                break;
            }
            retList.add(game.getBoard()[currX - i][currY]);
            i++;
        }

        i = 1;
        // down
        while (currY + i < 8
                && (game.getBoard()[currX][currY + i].getPiece() == null || game.getBoard()[currX][currY
                + i].getPiece().getColor() != piece.getColor())) {
            if (game.getBoard()[currX][currY + i].getPiece() != null
                    && game.getBoard()[currX][currY + i].getPiece().getColor() != piece.getColor()) {
                retList.add(game.getBoard()[currX][currY + i]);
                break;
            }
            retList.add(game.getBoard()[currX][currY + i]);
            i++;
        }

        i = 1;
        // up
        while (currY - i > -1
                && (game.getBoard()[currX][currY - i].getPiece() == null || game.getBoard()[currX][currY
                - i].getPiece().getColor() != piece.getColor())) {
            if (game.getBoard()[currX][currY - i].getPiece() != null
                    && game.getBoard()[currX][currY - i].getPiece().getColor() != piece.getColor()) {
                retList.add(game.getBoard()[currX][currY - i]);
                break;
            }
            retList.add(game.getBoard()[currX][currY - i]);
            i++;
        }
        return retList;
    }
}
