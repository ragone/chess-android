package sjq5766.aut.chessapp.ChessCore.Objects;

import java.util.ArrayList;

import sjq5766.aut.chessapp.ChessCore.Interface.AvailableMoves;
import sjq5766.aut.chessapp.ChessCore.Interface.Cell;
import sjq5766.aut.chessapp.ChessCore.Interface.ChessGame;

public class Bishop implements AvailableMoves {
    // Fields
    private ChessGame game;

    // Constructor
    public Bishop(ChessGame game) {
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

        // check the diagonal until you see a piece.
        // right, down
        while ((currX + i < 8 && currY + i < 8)
                && (game.getBoard()[currX + i][currY + i].getPiece() == null || game.getBoard()[currX
                + i][currY + i].getPiece().getColor() != piece.getColor())) {
            // if the piece is one of the opponent's, it is a valid move
            if (game.getBoard()[currX + i][currY + i].getPiece() != null
                    && game.getBoard()[currX + i][currY + i].getPiece().getColor() != piece.getColor()) {
                retList.add(game.getBoard()[currX + i][currY + i]);
                break;
            }
            retList.add(game.getBoard()[currX + i][currY + i]);
            i++;
        }

        i = 1;
        // right, up
        while ((currX + i < 8 && currY - i > -1)
                && (game.getBoard()[currX + i][currY - i].getPiece() == null || game.getBoard()[currX
                + i][currY - i].getPiece().getColor() != piece.getColor())) {
            if (game.getBoard()[currX + i][currY - i].getPiece() != null
                    && game.getBoard()[currX + i][currY - i].getPiece().getColor() != piece.getColor()) {
                retList.add(game.getBoard()[currX + i][currY - i]);
                break;
            }
            retList.add(game.getBoard()[currX + i][currY - i]);
            i++;
        }

        i = 1;
        // left, down
        while ((currX - i > -1 && currY + i < 8)
                && (game.getBoard()[currX - i][currY + i].getPiece() == null || game.getBoard()[currX
                - i][currY + i].getPiece().getColor() != piece.getColor())) {
            if (game.getBoard()[currX - i][currY + i].getPiece() != null
                    && game.getBoard()[currX - i][currY + i].getPiece().getColor() != piece.getColor()) {
                retList.add(game.getBoard()[currX - i][currY + i]);
                break;
            }
            retList.add(game.getBoard()[currX - i][currY + i]);
            i++;
        }

        i = 1;
        // left, up
        while ((currX - i > -1 && currY - i > -1)
                && (game.getBoard()[currX - i][currY - i].getPiece() == null || game.getBoard()[currX
                - i][currY - i].getPiece().getColor() != piece.getColor())) {
            if (game.getBoard()[currX - i][currY - i].getPiece() != null
                    && game.getBoard()[currX - i][currY - i].getPiece().getColor() != piece.getColor()) {
                retList.add(game.getBoard()[currX - i][currY - i]);
                break;
            }
            retList.add(game.getBoard()[currX - i][currY - i]);
            i++;
        }

        return retList;
    }
}
