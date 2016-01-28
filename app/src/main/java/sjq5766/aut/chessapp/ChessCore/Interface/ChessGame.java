package sjq5766.aut.chessapp.ChessCore.Interface;

import android.util.Log;

import java.util.ArrayList;

import sjq5766.aut.chessapp.ChessCore.Objects.Bishop;
import sjq5766.aut.chessapp.ChessCore.Objects.ChessColor;
import sjq5766.aut.chessapp.ChessCore.Objects.GameState;
import sjq5766.aut.chessapp.ChessCore.Objects.King;
import sjq5766.aut.chessapp.ChessCore.Objects.Knight;
import sjq5766.aut.chessapp.ChessCore.Objects.Pawn;
import sjq5766.aut.chessapp.ChessCore.Objects.Piece;
import sjq5766.aut.chessapp.ChessCore.Objects.PieceType;
import sjq5766.aut.chessapp.ChessCore.Objects.Player;
import sjq5766.aut.chessapp.ChessCore.Objects.Queen;
import sjq5766.aut.chessapp.ChessCore.Objects.Rook;
import sjq5766.aut.chessapp.R;

public class ChessGame {
    // Fields
    private Cell[][] board;
    private Player white, black;
    private ChessColor turn;
    private GameState currentGameState;
    private boolean reversed = false;
    private ArrayList<String> whiteMoves, blackMoves;

    // Constructor
    public ChessGame() {
        board = new Cell[8][8];
        initBoard(board);
        turn = ChessColor.white;
        currentGameState = GameState.normal;
        whiteMoves = new ArrayList<>();
        blackMoves = new ArrayList<>();
    }

    // Getters & Setters

    public boolean isReversed() {return reversed; }

    public void setReversed() {
        this.reversed = !reversed;
    }

    public Cell[][] getBoard() {
        return board;
    }

    public void setBoard(Cell[][] board) {
        this.board = board;
    }

    public ChessColor getTurn() {
        return turn;
    }

    public void setTurn(ChessColor turn) {
        this.turn = turn;
    }

    public GameState getCurrentGameState(){
        return this.currentGameState;
    }

    // Methods
    public GameState checkForCheck(Cell board[][], ChessColor whoseCheck) {
        Piece own[];
        Piece opponent[];
        if (whoseCheck == ChessColor.white) {
            own = white.getPieces();
            opponent = black.getPieces();
        } else {
            own = black.getPieces();
            opponent = white.getPieces();
        }

        for (int i = 0; i < 15; i++) {
            ArrayList<Cell> moves = opponent[i].getAvailableMoves();
            if (moves != null && (!moves.isEmpty() && moves.contains(own[15].getLocation())))
            {
                if (whoseCheck == ChessColor.white)
                    return GameState.whiteCheck;
                else
                    return GameState.blackCheck;
            }
        }
        return GameState.normal;
    }

    public GameState checkForMate() {
        ArrayList<Cell> whiteMoves = new ArrayList<>();
        ArrayList<Cell> blackMoves = new ArrayList<>();

        for (int i = 0; i < 15; i++) {
            ArrayList<Cell> moves = white.getPieces()[i].getAvailableMoves();
            for(int j = 0; j < moves.size(); j++)
                whiteMoves.add(moves.get(j));
            moves = black.getPieces()[i].getAvailableMoves();
            for(int j = 0; j < moves.size(); j++)
                blackMoves.add(moves.get(j));
        }
        if(blackMoves.size() == 0)
        {
            if(checkForCheck(board, ChessColor.black) == GameState.blackCheck)
                return GameState.blackMate;
            else
                return GameState.stalemate;
        }
        if(whiteMoves.size() == 0)
        {
            if(checkForCheck(board, ChessColor.white) == GameState.whiteCheck)
                return GameState.whiteMate;
            else
                return GameState.stalemate;
        }
        if (checkForCheck(board, ChessColor.white) == GameState.whiteCheck)
            return GameState.whiteCheck;
        if (checkForCheck(board, ChessColor.black) == GameState.blackCheck)
            return GameState.blackCheck;
        return GameState.normal;
    }

    public void turnMade(){
        if(currentGameState == GameState.normal)
            currentGameState = GameState.moved;
        else if(currentGameState == GameState.moved)
            currentGameState = GameState.normal;
    }

    public boolean move(int fromX, int fromY, int toX, int toY) {
        Player currentPlayer = turn == ChessColor.white ? white : black;
        Piece piece = board[fromX][fromY].getPiece();
        boolean success = currentPlayer.move(piece, board[toX][toY]);

        if (success) {
            turn = turn == ChessColor.white ? ChessColor.black : ChessColor.white;
        }

        return success;
    }

    //     0   1   2   3   4   5   6   7
    //   ---------------------------------
    // 0 | R | N | B | Q | K | B | N | R |
    //   ---------------------------------
    // 1 | p | p | p | p | p | p | p | p |
    //   ---------------------------------
    // 2 |   |   |   |   |   |   |   |   |
    //   ---------------------------------
    // 3 |   |   |   |   |   |   |   |   |
    //   ---------------------------------
    // 4 |   |   |   |   |   |   |   |   |
    //   ---------------------------------
    // 5 |   |   |   |   |   |   |   |   |
    //   ---------------------------------
    // 6 | p | p | p | p | p | p | p | p |
    //   ---------------------------------
    // 7 | R | N | B | Q | K | B | N | R |
    //   ---------------------------------

    public void initBoard(Cell[][] board) {
        Piece whitePieces[] = new Piece[16];
        Piece blackPieces[] = new Piece[16];

        for(int x = 0; x < 8; x++) {
            for(int y = 0; y < 8; y++) {
                board[x][y] = new Cell(x,y);
            }
        }

        for (int i = 0; i < 8; i++) {
            Piece whitePawn = new Piece(ChessColor.white, PieceType.pawn,
                    board[i][6], new Pawn(this), R.drawable.wp, this);
            whitePieces[i] = whitePawn;
            board[i][6].setPiece(whitePawn);
            Piece blackPawn = new Piece(ChessColor.black, PieceType.pawn,
                    board[i][1], new Pawn(this), R.drawable.bp, this);
            blackPieces[i] = blackPawn;
            board[i][1].setPiece(blackPawn);
        }

        // rooks
        whitePieces[8] = new Piece(ChessColor.white, PieceType.rook,
                board[0][7], new Rook(this), R.drawable.wr, this);
        board[0][7].setPiece(whitePieces[8]);
        whitePieces[9] = new Piece(ChessColor.white, PieceType.rook,
                board[7][7], new Rook(this), R.drawable.wr, this);
        board[7][7].setPiece(whitePieces[9]);
        blackPieces[8] = new Piece(ChessColor.black, PieceType.rook,
                board[0][0], new Rook(this), R.drawable.br, this);
        board[0][0].setPiece(blackPieces[8]);
        blackPieces[9] = new Piece(ChessColor.black, PieceType.rook,
                board[7][0], new Rook(this), R.drawable.br, this);
        board[7][0].setPiece(blackPieces[9]);

        // knights
        whitePieces[10] = new Piece(ChessColor.white, PieceType.knight,
                board[1][7], new Knight(this), R.drawable.wn, this);
        board[1][7].setPiece(whitePieces[10]);
        whitePieces[11] = new Piece(ChessColor.white, PieceType.knight,
                board[6][7], new Knight(this), R.drawable.wn, this);
        board[6][7].setPiece(whitePieces[11]);
        blackPieces[10] = new Piece(ChessColor.black, PieceType.knight,
                board[1][0], new Knight(this), R.drawable.bn, this);
        board[1][0].setPiece(blackPieces[10]);
        blackPieces[11] = new Piece(ChessColor.black, PieceType.knight,
                board[6][0], new Knight(this), R.drawable.bn, this);
        board[6][0].setPiece(blackPieces[11]);

        // bishops
        whitePieces[12] = new Piece(ChessColor.white, PieceType.bishop,
                board[2][7], new Bishop(this), R.drawable.wb, this);
        board[2][7].setPiece(whitePieces[12]);
        whitePieces[13] = new Piece(ChessColor.white, PieceType.bishop,
                board[5][7], new Bishop(this), R.drawable.wb, this);
        board[5][7].setPiece(whitePieces[13]);
        blackPieces[12] = new Piece(ChessColor.black, PieceType.bishop,
                board[2][0], new Bishop(this), R.drawable.bb, this);
        board[2][0].setPiece(blackPieces[12]);
        blackPieces[13] = new Piece(ChessColor.black, PieceType.bishop,
                board[5][0], new Bishop(this), R.drawable.bb, this);
        board[5][0].setPiece(blackPieces[13]);

        // queens
        whitePieces[14] = new Piece(ChessColor.white, PieceType.queen,
                board[3][7], new Queen(this), R.drawable.wq, this);
        board[3][7].setPiece(whitePieces[14]);
        blackPieces[14] = new Piece(ChessColor.black, PieceType.queen,
                board[3][0], new Queen(this), R.drawable.bq, this);
        board[3][0].setPiece(blackPieces[14]);

        // kings
        whitePieces[15] = new Piece(ChessColor.white, PieceType.king,
                board[4][7], new King(this), R.drawable.wk, this);
        board[4][7].setPiece(whitePieces[15]);
        blackPieces[15] = new Piece(ChessColor.black, PieceType.king,
                board[4][0], new King(this), R.drawable.bk, this);
        board[4][0].setPiece(blackPieces[15]);

        white = new Player(ChessColor.white, whitePieces);
        black = new Player(ChessColor.black, blackPieces);
    }

}
