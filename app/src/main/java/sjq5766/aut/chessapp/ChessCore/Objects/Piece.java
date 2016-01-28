package sjq5766.aut.chessapp.ChessCore.Objects;

import java.util.ArrayList;

import sjq5766.aut.chessapp.ChessCore.Interface.AvailableMoves;
import sjq5766.aut.chessapp.ChessCore.Interface.Cell;
import sjq5766.aut.chessapp.ChessCore.Interface.ChessGame;
import sjq5766.aut.chessapp.ChessCore.Interface.MoveStatus;

public class Piece {
    // Fields
    private Cell location;
    private PieceState state;
    private int image;
    private ChessColor color;
    private PieceType type;
    private AvailableMoves availableMoves;
    private ChessGame game;

    // Constructor
    public Piece(ChessColor color, PieceType type, Cell location, AvailableMoves availableMoves, int image, ChessGame game) {
        this.location = location;
        this.image = image;
        this.type = type;
        this.color = color;
        this.availableMoves = availableMoves;
        this.game = game;
    }

    // Getters & Setters

    public Cell getLocation() {
        return location;
    }

    public void setLocation(Cell location) {
        this.location = location;
    }

    public PieceState getState() {
        return state;
    }

    public void setState(PieceState state) {
        this.state = state;
        if(state == PieceState.dead) {
            location.setPiece(null);
            location.setX(-1);
            location.setY(-1);
        }
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public ChessColor getColor() {
        return color;
    }

    public void setColor(ChessColor chesscolor) {
        this.color = chesscolor;
    }

    public PieceType getType() {
        return type;
    }

    public void setType(PieceType type) {
        switch (type) {
            case bishop:
                availableMoves = new Bishop(game);
                break;
            case knight:
                availableMoves = new Knight(game);
                break;
            case rook:
                availableMoves = new Rook(game);
                break;
            case queen:
                availableMoves = new Queen(game);
                break;
            case pawn:
            case king:
        }
    }

    // Methods
    public boolean isValidMove(Cell moveTo) {
        if(moveTo.getPiece() != null)
            if(moveTo.getPiece().getColor() == this.color)
                return false;

        // try move
        Cell targetCell = game.getBoard()[moveTo.getX()][moveTo.getY()];
        Piece oldPiece = targetCell.getPiece();
        Cell sourceCell = this.getLocation();
        targetCell.setPiece(this);
        sourceCell.setPiece(null);
        this.location = targetCell;
        this.setState(PieceState.alive);

        GameState tryState = game.checkForCheck(game.getBoard(), this.color);

        //revert
        sourceCell.setPiece(this);
        targetCell.setPiece(oldPiece);
        this.location = sourceCell;
        this.setState(PieceState.alive);
        if(oldPiece!= null)
        {
            oldPiece.location = targetCell;
            oldPiece.setState(PieceState.alive);
        }

        // see if move is valid
        // validity of the move is determined by whether the move puts
        // the player into check. We know that the pieces cannot move
        // in invalid patterns because this has already been checked in
        // the only caller, which should be player.move()
        if ((color == ChessColor.white && tryState == GameState.whiteCheck)
                || (color == ChessColor.black && tryState == GameState.blackCheck))
            return false;
        return true;
    }

    public MoveStatus tryMove(Cell moveTo) {
        if (!isValidMove(moveTo))
            return MoveStatus.fail;
        // move is valid; apply move and check for promotion if pawn
        // empty old location
        this.location.setPiece(null);
        // update new location
        moveTo.setPiece(this);
        // update self location
        this.location = moveTo;

        if (type == PieceType.pawn) {
            return (location.getY() == 0 || location.getY() == 7) ? MoveStatus.promote : MoveStatus.success;
        }

        return MoveStatus.success;
    }

    public ArrayList<Cell> getAvailableMoves() {
        return availableMoves.getAvailableMoves(this);
    }
}
