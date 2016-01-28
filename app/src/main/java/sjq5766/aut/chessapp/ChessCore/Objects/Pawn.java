package sjq5766.aut.chessapp.ChessCore.Objects;

import java.util.ArrayList;

import sjq5766.aut.chessapp.ChessCore.Interface.AvailableMoves;
import sjq5766.aut.chessapp.ChessCore.Interface.Cell;
import sjq5766.aut.chessapp.ChessCore.Interface.ChessGame;

public class Pawn implements AvailableMoves {
    // Fields
    private ChessGame game;
    private int modifier1;
    private int modifier2;
    private int modifier3;
    private int modifier4;

    private int modifier5;
    private int modifier6;

    // Constructor
    public Pawn(ChessGame game) {
        this.game = game;
        if(!game.isReversed()) {
            modifier1 = 1;
            modifier2 = 2;

            modifier3 = 1;
            modifier4 = 6;

            modifier5 = 0;
            modifier6 = 7;
        } else {
            modifier1 = -1;
            modifier2 = -2;

            modifier3 = 6;
            modifier4 = 1;

            modifier5 = 7;
            modifier6 = 0;
        }
    }

    @Override
    public ArrayList<Cell> getAvailableMoves(Piece piece) {
        if (piece.getLocation().getY() == -1)
            return null;
        ArrayList<Cell> retList = new ArrayList<Cell>();
        int currX = piece.getLocation().getX();
        int currY = piece.getLocation().getY();

        if (piece.getColor() == ChessColor.white) {
            // move forward one cell
            if (game.getBoard()[currX][currY - modifier1].getPiece() == null)
                retList.add(game.getBoard()[currX][currY - modifier1]);

            // check moving left
            if (currX > modifier5
                    && game.getBoard()[currX - modifier1][currY - modifier1].getPiece() != null
                    && game.getBoard()[currX - modifier1][currY - modifier1].getPiece()
                    .getColor() == ChessColor.black)
                retList.add(game.getBoard()[currX - modifier1][currY - modifier1]);
            if (currX < modifier5
                    && game.getBoard()[currX - modifier1][currY - modifier1].getPiece() != null
                    && game.getBoard()[currX - modifier1][currY - modifier1].getPiece()
                    .getColor() == ChessColor.black
                    && game.isReversed())
                retList.add(game.getBoard()[currX - modifier1][currY - modifier1]);

            // check moving right
            if (currX < modifier6
                    && game.getBoard()[currX + modifier1][currY - modifier1].getPiece() != null
                    && game.getBoard()[currX + modifier1][currY - modifier1].getPiece()
                    .getColor() == ChessColor.black)
                retList.add(game.getBoard()[currX + modifier1][currY - modifier1]);
            if (currX > modifier6
                    && game.getBoard()[currX + modifier1][currY - modifier1].getPiece() != null
                    && game.getBoard()[currX + modifier1][currY - modifier1].getPiece()
                    .getColor() == ChessColor.black
                    && game.isReversed())
                retList.add(game.getBoard()[currX + modifier1][currY - modifier1]);

            // default location, making 4 available moves, rather than 3
            if (currY == modifier4 && game.getBoard()[currX][currY - modifier2].getPiece() == null
                    && game.getBoard()[currX][currY - modifier2].getPiece() == null)
                retList.add(game.getBoard()[currX][currY - modifier2]);
        }
        if (piece.getColor() == ChessColor.black) {
            // move forward one cell
            if (game.getBoard()[currX][currY + modifier1].getPiece() == null)
                retList.add(game.getBoard()[currX][currY + modifier1]);

            // check moving left
            if (currX > modifier5
                    && game.getBoard()[currX - modifier1][currY + modifier1].getPiece() != null
                    && game.getBoard()[currX - modifier1][currY + modifier1].getPiece().getColor() == ChessColor.white)
                retList.add(game.getBoard()[currX - modifier1][currY + modifier1]);
            else if(currX < modifier5
                    && game.getBoard()[currX - modifier1][currY + modifier1].getPiece() != null
                    && game.getBoard()[currX - modifier1][currY + modifier1].getPiece().getColor() == ChessColor.white
                    && game.isReversed()) {
                retList.add(game.getBoard()[currX - modifier1][currY + modifier1]);
            }

            // check moving right
            if (currX < modifier6
                    && game.getBoard()[currX + modifier1][currY + modifier1].getPiece() != null
                    && game.getBoard()[currX + modifier1][currY + modifier1].getPiece().getColor() == ChessColor.white)
                retList.add(game.getBoard()[currX + modifier1][currY + modifier1]);
            else if(currX > modifier6
                    && game.getBoard()[currX + modifier1][currY + modifier1].getPiece() != null
                    && game.getBoard()[currX + modifier1][currY + modifier1].getPiece().getColor() == ChessColor.white
                    && game.isReversed()) {
                retList.add(game.getBoard()[currX + modifier1][currY + modifier1]);
            }

            // default location, making 4 available moves, rather than 3
            if (currY == modifier3 && game.getBoard()[currX][currY + modifier2].getPiece() == null
                    && game.getBoard()[currX][currY + modifier2].getPiece() == null)
                retList.add(game.getBoard()[currX][currY + modifier2]);
        }
        return retList;
    }
}
