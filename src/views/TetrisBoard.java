package views;

import controllers.TetrisController;
import controllers.tetrisGameLogic;
import models.Tetronimo;
import wheelsunh.users.*;
import wheelsunh.users.Frame;
import wheelsunh.users.Rectangle;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * TetrisBoard.java:
 * Class to model the tetris board
 *
 * @author Tyler Sherry
 * @version 1.0 December 8, 2021
 *
 * @see java.awt.Color
 * @see java.awt.event.KeyListener
 * @see java.awt.event.KeyEvent
 */
public class TetrisBoard implements KeyListener
{
    /**
     * Constant to represent the width of the board
     */
    public static final int WIDTH = 10;

    /**
     * Constant to represent the height of the board
     */
    public static final int HEIGHT = 24;

    private final TetrisController CONTROLLER;
    private Tetronimo tetronimo;
    private Rectangle[][] playingField;

    tetrisGameLogic boardGameLogic;

    private TextBox nextTetronimoTextBox;
    private TextBox gameScoreTextBox;

    /**
     * Constructor to initialize the board
     *
     * @param frame The wheelsunh frame (so we can add this class as a key listener for the frame)
     */
    public TetrisBoard( Frame frame )
    {
        frame.addKeyListener( this );
        this.CONTROLLER = new TetrisController( this );
        this.boardGameLogic = new tetrisGameLogic();
        this.buildBoard();

        this.nextTetronimoTextBox = new TextBox("Next Tetronimo");
        this.nextTetronimoTextBox.setLocation(400, 100);

        this.gameScoreTextBox = new TextBox("Game Score");
        this.gameScoreTextBox.setLocation(400, 20);


        this.run();
    }

    /**
     * Builds the playing field for tetris
     */
    private void buildBoard()
    {
        this.playingField = new Rectangle[ WIDTH ][ HEIGHT ];

        for ( int i = 0; i < TetrisBoard.WIDTH; i++ )
        {
            for ( int j = 0; j < TetrisBoard.HEIGHT; j++ )
            {
                this.playingField[ i ][ j ] = new Rectangle();
                this.playingField[ i ][ j ].setLocation( i * 20 + 40, j * 20 );
                this.playingField[ i ][ j ].setSize( Tetronimo.SIZE, Tetronimo.SIZE );
                this.playingField[ i ][ j ].setColor( Color.WHITE );
                this.playingField[ i ][ j ].setFrameColor( Color.BLACK );
            }
        }
    }

    /**
     * Starts gameplay and is responsible for keeping the game going (INCOMPLETE)
     */
    public void run()
    {
        //Get level of game and load tetronimosForLevel appropriately
        CONTROLLER.loadTetronimosArrayList(CONTROLLER.tetronimosForTheLevel);

        while(!CONTROLLER.tetronimosForTheLevel.isEmpty())
        {
            Utilities.sleep( 1000 );

            //Create the tetronimo that was displayed and now add to grid
            this.tetronimo = this.CONTROLLER.getNextTetrominoFromArrayList(CONTROLLER.tetronimosForTheLevel);
            CONTROLLER.addTetronimoRectanglesMovingToGrid(tetronimo);

            while( !this.CONTROLLER.hasTetronimoLanded(this.tetronimo) && this.tetronimo != null)
            {
                if(boardGameLogic.pauseGame != true)
                {
                    this.tetronimo.setLocation( this.tetronimo.getXLocation(), this.tetronimo.getYLocation() + Tetronimo.SIZE );
                    Utilities.sleep( 5 );

                }
                Utilities.sleep( 1000 );
            }
            //At this point the tetronimo has landed and need to check if the row is full

            Utilities.sleep( 500 );

            //***TESTING AREA***
            System.out.println("The tetronimo landed - need to spawn a new one");
            CONTROLLER.removeTetronimosFromArrayList(CONTROLLER.tetronimosForTheLevel);
            CONTROLLER.test_DisplayTetronimosIntheArrayList(CONTROLLER.tetronimosForTheLevel);
            boardGameLogic.gameLevel++;
            System.out.println(boardGameLogic.gameScore);


            /*
             * This next line is a placeholder for now, you need to change this code so when a piece lands
             * the right squares on the board are painted the color of the tetronimo and the teetronimo itself gets hidden
             */
            this.tetronimo = null;
        }
        System.out.println("Congratulations!  The level has been completed!");

    }

    /**
     * Getter method for the array representing the playing field, not used yet but will be needed by the controller later
     *
     * @return The playing field
     */
    public Rectangle[][] getPlayingField()
    {
        return playingField;
    }

    /**
     * This method is not used in this program
     *
     * @param e The key event
     */
    @Override
    public void keyTyped( KeyEvent e )
    {
        //not in use
    }

    /**
     * Handles the key events by the user (INCOMPLETE)
     *
     * @param e The key event
     */
    @Override
    public void keyPressed( KeyEvent e )
    {
        int key = e.getKeyCode();

        if( this.tetronimo == null )
        {
            return;
        }

        switch( key )
        {
            case 38:
                if(this.tetronimo.collisions_getLeftEdgeOfTetronimo() > 40 && this.tetronimo.collision_getRightEdgeOfTetronimo() < 220)
                {
                    this.tetronimo.rotate();
                }
                break;
            case 37:
                if( this.tetronimo.collisions_getLeftEdgeOfTetronimo() > 40 && !(CONTROLLER.isTetronimoRectangleToTheLeft(this.tetronimo)))
                {
                    this.tetronimo.shiftLeft();
                }
                break;
            case 39:
                if( this.tetronimo.collision_getRightEdgeOfTetronimo() < 220 && !(CONTROLLER.isTetronimoRectangleToTheRight(this.tetronimo)))
                {
                    this.tetronimo.shiftRight();
                }
                break;
            case 80:
                boardGameLogic.pauseGame();
                System.out.println("The game is paused.  Press 'u' to unpause the game.");
                break;
            case 85:
                boardGameLogic.unPauseGame();
                System.out.println("The game has been unpaused.");
                break;
        }

    }

    /**
     * This method is not used in this program
     *
     * @param e The key event
     */
    @Override
    public void keyReleased( KeyEvent e )
    {
        //not in use
    }
}