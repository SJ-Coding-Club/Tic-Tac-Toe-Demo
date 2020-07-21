package com.e.tictactoe;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button[][] gridOfButtons;
    private boolean[][] isVisible;
    private Board board;

    private Button hardButton;
    private Button easyButton;
    private Button newGameButton;
    private Button mainMenuButton;
    private TextView title;

    private final int HARD = 1;
    private final int EASY = 0;

    private int playerMarker = 1; // add user option to select

    public MainActivity() {
        isVisible = new boolean[3][3];
        gridOfButtons = new Button[3][3];
        board = new Board(playerMarker);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridOfButtons[0][0] = (Button)findViewById(R.id.button00);
        gridOfButtons[0][1] = (Button)findViewById(R.id.button01);
        gridOfButtons[0][2] = (Button)findViewById(R.id.button02);

        gridOfButtons[1][0] = (Button)findViewById(R.id.button10);
        gridOfButtons[1][1] = (Button)findViewById(R.id.button11);
        gridOfButtons[1][2] = (Button)findViewById(R.id.button12);

        gridOfButtons[2][0] = (Button)findViewById(R.id.button20);
        gridOfButtons[2][1] = (Button)findViewById(R.id.button21);
        gridOfButtons[2][2] = (Button)findViewById(R.id.button22);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                gridOfButtons[i][j].setVisibility(View.INVISIBLE);
            }
        }

        newGameButton = (Button)findViewById(R.id.newGameButton);
        newGameButton.setBackgroundColor(Color.TRANSPARENT);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newGame();
            }
        });
        newGameButton.setVisibility(View.INVISIBLE);

        mainMenuButton = (Button)findViewById(R.id.mainMenuButton);
        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToMainMenu();
            }
        });
        mainMenuButton.setVisibility(View.INVISIBLE);

        choiceButtons();



//        gridOfButtons[0][0].setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                swapButtonVisibility(0,0);
//            }
//        });

    }

    public void returnToMainMenu() {
        newGame();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                gridOfButtons[i][j].setVisibility(View.INVISIBLE);
            }
        }
        mainMenuButton.setVisibility(View.INVISIBLE);
        newGameButton.setVisibility(View.INVISIBLE);

        easyButton.setVisibility(View.VISIBLE);
        hardButton.setVisibility(View.VISIBLE);
        title.setVisibility(View.VISIBLE);

    }


    public void choiceButtons() {
        easyButton = (Button)findViewById(R.id.easyButton);
        hardButton = (Button)findViewById(R.id.hardButton);
        title = (TextView) findViewById(R.id.titleText);

        easyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hardButton.setVisibility(View.INVISIBLE);
                easyButton.setVisibility(View.INVISIBLE);
                title.setVisibility(View.INVISIBLE);
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        gridOfButtons[i][j].setVisibility(View.VISIBLE);
                    }
                }


                gameScene(EASY);

            }
        });
        hardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hardButton.setVisibility(View.INVISIBLE);
                easyButton.setVisibility(View.INVISIBLE);
                title.setVisibility(View.INVISIBLE);

                for (Button[] row : gridOfButtons) {
                    for (Button button : row) {
                        button.setVisibility(View.VISIBLE);
                    }
                }

                gameScene(HARD);


            }
        });



    }





    private void newGame() {
        board = new Board(playerMarker);
        processGrid();
    }

    private void gameScene(final int difficulty) {

        //mainMenuButton.setVisibility(View.VISIBLE);
        newGameButton.setVisibility(View.VISIBLE);
        mainMenuButton.setVisibility(View.VISIBLE);

        processGrid();


        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                final int row = r;
                final int column = c;
                gridOfButtons[r][c].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (board.makePlayerMove(row, column)) {
                            int winner = board.getWinner();
                            if (board.hasMovesLeft() && winner == 0) {

                                if (difficulty == HARD) {
                                    board.makeSmartOpponentMove();
                                }
                                else {
                                    board.makeDumbOpponentMove();
                                }

                                winner = board.getWinner();
                            }
                            if (winner == 0 && !board.hasMovesLeft()) {
                                // notify of tie
                                new AlertDialog.Builder(MainActivity.this).setTitle("Tie").setMessage("The game was a tie.").setPositiveButton(android.R.string.yes, null).show();
                                newGame();
                            }
                            else if (winner == 10) {
                                new AlertDialog.Builder(MainActivity.this).setTitle("Loss").setMessage("You lost to the computer.").setPositiveButton(android.R.string.yes, null).show();
                                newGame();
                            }
                            else if (winner == -10){
                                new AlertDialog.Builder(MainActivity.this).setTitle("Win").setMessage("You won!").setPositiveButton(android.R.string.yes, null).show();
                                newGame();
                            }
                        }
                        processGrid();
                    }
                });
                gridOfButtons[r][c].setBackgroundColor(Color.TRANSPARENT);
                gridOfButtons[r][c].setTextSize(32);



            }
        }


    }



    private void processGrid() {
        int[][] grid = board.getBoard();
        for (int r = 0; r < 3; r++)
            for (int c = 0; c < 3; c++) {
                int value = grid[r][c];
                gridOfButtons[r][c].setTextColor(Color.BLACK);
                if (value == 0) {
                    gridOfButtons[r][c].setText("-");
                    gridOfButtons[r][c].setTextColor(Color.GRAY);
                }
                else if (value == 1) {
                    gridOfButtons[r][c].setText("X");

                }
                else {
                    gridOfButtons[r][c].setText("O");
                }
//                gridOfButtons[r][c].setText((grid[r][c] + ""));
            }
    }




}