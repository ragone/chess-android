package sjq5766.aut.chessapp.ChessCore.Misc;


import sjq5766.aut.chessapp.ChessCore.Interface.Cell;
import sjq5766.aut.chessapp.ChessCore.Interface.ChessGame;
import sjq5766.aut.chessapp.ChessCore.Objects.Bishop;
import sjq5766.aut.chessapp.ChessCore.Objects.ChessColor;
import sjq5766.aut.chessapp.ChessCore.Objects.King;
import sjq5766.aut.chessapp.ChessCore.Objects.Knight;
import sjq5766.aut.chessapp.ChessCore.Objects.Pawn;
import sjq5766.aut.chessapp.ChessCore.Objects.Piece;
import sjq5766.aut.chessapp.ChessCore.Objects.PieceType;
import sjq5766.aut.chessapp.ChessCore.Objects.Queen;
import sjq5766.aut.chessapp.ChessCore.Objects.Rook;
import sjq5766.aut.chessapp.R;

/**
 * http://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation
 */
public abstract class FEN {

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

    public static String getMoveNotation(ChessGame game, int fromX, int fromY, int toX, int toY) {
        String result = "";
        Piece piece = game.getBoard()[fromX][fromY].getPiece();

        switch (piece.getType()) {
            case pawn:
                break;
            case knight:
                result += "N";
                break;
            case bishop:
                result += "B";
                break;
            case rook:
                result += "R";
                break;
            case queen:
                result += "Q";
                break;
            case king:
                result += "K";
                break;
        }


        if (game.getBoard()[toX][toY].getPiece() != null) {
            result += "x";
        }

        if (!game.isReversed()) {
            switch (toX) {
                case 0:
                    result += "a";
                    break;
                case 1:
                    result += "b";
                    break;
                case 2:
                    result += "c";
                    break;
                case 3:
                    result += "d";
                    break;
                case 4:
                    result += "e";
                    break;
                case 5:
                    result += "f";
                    break;
                case 6:
                    result += "g";
                    break;
                case 7:
                    result += "h";
                    break;
            }
            result += (8-toY);
        } else {
            switch (toX) {
                case 0:
                    result += "h";
                    break;
                case 1:
                    result += "g";
                    break;
                case 2:
                    result += "f";
                    break;
                case 3:
                    result += "e";
                    break;
                case 4:
                    result += "d";
                    break;
                case 5:
                    result += "c";
                    break;
                case 6:
                    result += "b";
                    break;
                case 7:
                    result += "a";
                    break;
            }
            result += (toY+1);
        }


        return result;
    }

    public static String reverse(ChessGame game, String fen) {
        String result= "";

        char[] fenChars = fen.toCharArray();
        for(int i = fenChars.length - 3; i >= 0; i--) {
            result += fenChars[i];
        }
        result += fenChars[fenChars.length - 2] + fenChars[fenChars.length - 1];

        return result;
    }

    public static String encode(ChessGame game, Cell[][] board) {
        String fen = "";

        for(int y = 0; y < 8; y++) {
            if(y > 0) {
                fen += "/";
            }
            int consecutiveSpaces = 0;
            for(int x = 0; x < 8; x++) {
                Piece piece = board[x][y].getPiece();
                if(piece == null ) {
                    consecutiveSpaces++;
                    if(x == 7 || board[x+1][y].getPiece() != null) {
                        fen += consecutiveSpaces;
                        consecutiveSpaces = 0;
                    }
                } else {
                    switch (piece.getType()) {
                        case rook:
                            if(piece.getColor() == ChessColor.white) {
                                fen += "R";
                            } else {
                                fen += "r";
                            }
                            break;
                        case pawn:
                            if(piece.getColor() == ChessColor.white) {
                                fen += "P";
                            } else {
                                fen += "p";
                            }
                            break;
                        case bishop:
                            if(piece.getColor() == ChessColor.white) {
                                fen += "B";
                            } else {
                                fen += "b";
                            }
                            break;
                        case knight:
                            if(piece.getColor() == ChessColor.white) {
                                fen += "N";
                            } else {
                                fen += "n";
                            }
                            break;
                        case queen:
                            if(piece.getColor() == ChessColor.white) {
                                fen += "Q";
                            } else {
                                fen += "q";
                            }
                            break;
                        case king:
                            if(piece.getColor() == ChessColor.white) {
                                fen += "K";
                            } else {
                                fen += "k";
                            }
                            break;
                    }
                }
            }
        }

        fen += " ";
        if(game.getTurn() == ChessColor.white) {
            fen += "T";
        } else {
            fen += "t";
        }
        return fen;
    }

    public static Cell[][] decode(ChessGame game, String fen) {
        Cell[][] board = new Cell[8][8];
        int x = 0;
        int y = 0;

        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                board[i][j] = new Cell(i,j);
            }
        }

        for(char ch : fen.toCharArray()) {
            switch (ch) {
                case 'r':
                    board[x][y].setPiece(new Piece(ChessColor.black, PieceType.rook, board[x][y], new Rook(game), R.drawable.br, game));
                    x++;
                    break;
                case 'n':
                    board[x][y].setPiece(new Piece(ChessColor.black, PieceType.knight, board[x][y], new Knight(game), R.drawable.bn, game));
                    x++;
                    break;
                case 'b':
                    board[x][y].setPiece(new Piece(ChessColor.black, PieceType.bishop, board[x][y], new Bishop(game), R.drawable.bb, game));
                    x++;
                    break;
                case 'q':
                    board[x][y].setPiece(new Piece(ChessColor.black, PieceType.queen, board[x][y], new Queen(game), R.drawable.bq, game));
                    x++;
                    break;
                case 'k':
                    board[x][y].setPiece(new Piece(ChessColor.black, PieceType.king, board[x][y], new King(game), R.drawable.bk, game));
                    x++;
                    break;
                case 'p':
                    board[x][y].setPiece(new Piece(ChessColor.black, PieceType.pawn, board[x][y], new Pawn(game), R.drawable.bp, game));
                    x++;
                    break;
                case 'R':
                    board[x][y].setPiece(new Piece(ChessColor.white, PieceType.rook, board[x][y], new Rook(game), R.drawable.wr, game));
                    x++;
                    break;
                case 'N':
                    board[x][y].setPiece(new Piece(ChessColor.white, PieceType.knight, board[x][y], new Knight(game), R.drawable.wn, game));
                    x++;
                    break;
                case 'B':
                    board[x][y].setPiece(new Piece(ChessColor.white, PieceType.bishop, board[x][y], new Bishop(game), R.drawable.wb, game));
                    x++;
                    break;
                case 'Q':
                    board[x][y].setPiece(new Piece(ChessColor.white, PieceType.queen, board[x][y], new Queen(game), R.drawable.wq, game));
                    x++;
                    break;
                case 'K':
                    board[x][y].setPiece(new Piece(ChessColor.white, PieceType.king, board[x][y], new King(game), R.drawable.wk, game));
                    x++;
                    break;
                case 'P':
                    board[x][y].setPiece(new Piece(ChessColor.white, PieceType.pawn, board[x][y], new Pawn(game), R.drawable.wp, game));
                    x++;
                    break;
                case '/':
                    y++;
                    x=0;
                    break;
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                    int increase = Character.getNumericValue(ch);
                    x += increase;
                    break;
                case ' ':
                    break;
                case 't':
                    game.setTurn(ChessColor.black);
                    break;
                case 'T':
                    game.setTurn(ChessColor.white);
                    break;
            }
        }
        return board;
    }
}
