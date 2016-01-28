package sjq5766.aut.chessapp.ChessCore.Interface;


import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import sjq5766.aut.chessapp.ChessApp.ChessActivity;
import sjq5766.aut.chessapp.ChessApp.SMSChessActivity;
import sjq5766.aut.chessapp.ChessCore.Misc.FEN;
import sjq5766.aut.chessapp.ChessCore.Objects.Piece;
import sjq5766.aut.chessapp.R;

public class BoardView extends View
{
    // Fields
    boolean pieceSelected;
    boolean reset;
    private int fromX, fromY, toX, toY, Yzero, width;
    private ChessGame game;
    private ChessActivity chessActivity;
    private SMSChessActivity SMSChessActivity;

    private Resources res;
    private Canvas canvas;

    // Constructor
    public BoardView(Context context)
    {
        super(context);

        pieceSelected = false;
        reset = true;
        fromX =-1;
        fromY = -1;
        toX = -1;
        toY = -1;
        res = getResources();

        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    // Getters & Setters
    public void setSMSChessActivity(SMSChessActivity game) {
        if (SMSChessActivity == null)
            this.SMSChessActivity = game;
    }

    public void setChessActivity(ChessActivity chessActivity){
        if(this.chessActivity == null)
            this.chessActivity = chessActivity;
    }

    public void setGame(ChessGame game)
    {
        if(game != null)
            this.game = game;
    }


    // Methods
    private int getBoardX(int x)
    {
        return x * width / 8;
    }

    private int getBoardY(int y)
    {
        return y * width / 8 + Yzero;
    }

    private void drawBoard(Cell[][] board)
    {
        super.invalidate();
        Drawable boardImage = res.getDrawable(R.drawable.board);
        width =  canvas.getWidth();
        Yzero =  (canvas.getHeight() - width) / 2;
        boardImage.setBounds(0, Yzero, width, width + Yzero);
        boardImage.draw(canvas);
        for(int x = 0; x < 8; x++)
        {
            for(int y = 0; y < 8; y++)
            {
                Piece piece = board[x][y].getPiece();
                if(piece != null)
                {
                    Drawable figure = res.getDrawable(piece.getImage());
                    figure.setBounds(getBoardX(x), getBoardY(y), getBoardX(x) + width/8, getBoardY(y) + width /8);
                    figure.draw(canvas);
                }
            }
        }
    }

    private void drawAvailableMoves(Cell[][] board, int x, int y)
    {
        Piece piece = board[x][y].getPiece();
        if(piece != null && piece.getColor() == game.getTurn())
        {
            Drawable selection = res.getDrawable(R.drawable.selected);
            selection.setBounds(getBoardX(x), getBoardY(y), getBoardX(x) + width/8, getBoardY(y) + width /8);
            selection.draw(canvas);

            ArrayList<Cell> availMoves = piece.getAvailableMoves();
            for(int i = 0; i < availMoves.size(); i ++)
            {
                if(piece.isValidMove(availMoves.get(i)))
                {
                    Cell availMove = availMoves.get(i);
                    Drawable circle = res.getDrawable(R.drawable.selected);
                    circle.setBounds(getBoardX(availMove.getX()), getBoardY(availMove.getY()), getBoardX(availMove.getX()) + width/8, getBoardY(availMove.getY()) + width /8);
                    circle.draw(canvas);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (event.getAction() != MotionEvent.ACTION_DOWN)
            return super.onTouchEvent(event);

        if(!pieceSelected)
        {
            fromX = (int)(event.getX() / (width / 8.0));
            fromY = (int)((event.getY() - Yzero) / (width / 8.0));
            if( fromX < 0 || fromX > 7 || fromY < 0 || fromY > 7)
                return true;
        }
        else
        {
//            MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.click);
//            mp.start();
            toX = (int)(event.getX() / (width / 8.0));
            toY = (int)((event.getY() - Yzero) / (width / 8.0));

            if(toX == fromX && toY == fromY);
            else if(game.getBoard()[fromX][fromY].getPiece().getAvailableMoves().contains(game.getBoard()[toX][toY]))
                Log.i("Move", FEN.getMoveNotation(game, fromX, fromY, toX, toY));

            // TODO: write move to database

            if( toX < 0 || toX > 7 || toY < 0 || toY > 7 || (toX == fromX && toY == fromY));
            else
            {
                try
                {
                    game.move(fromX, fromY, toX, toY);

                    if(chessActivity != null) {
                        game.turnMade();
                        chessActivity.updateChess();

                    }
                    if (SMSChessActivity != null)
                        SMSChessActivity.sendGame();

                    pieceSelected = false;
                    return true;
                }catch( Exception ex)
                {
                    pieceSelected = false;
                    return true;
                }
            }
        }
        if(game.getBoard()[fromX][fromY].getPiece() != null && game.getBoard()[fromX][fromY].getPiece().getColor() == game.getTurn())
            pieceSelected = !pieceSelected;
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        this.canvas = canvas;
        this.drawBoard(game.getBoard());
        if(pieceSelected)
            drawAvailableMoves(game.getBoard(), fromX, fromY);
    }
}
