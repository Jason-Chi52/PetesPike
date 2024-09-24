package petespike.view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import petespike.model.Direction;
import petespike.model.GameState;
import petespike.model.Move;
import petespike.model.PetesPike;
import petespike.model.PetesPikeException;
import petespike.model.PetesPikeObserver;
import petespike.model.PetesPikeSolver;
import petespike.model.Position;
import java.io.IOException;
import java.util.*;

import backtracker.Backtracker;


public class PetesPikeGUI extends Application implements PetesPikeObserver {
    // characters
    private static final String PATH = "file:";
    private static final Image BLUE_GOAT = new Image(PATH + "data/images/blueGoat.png");
    private static final Image GREEN_GOAT = new Image(PATH + "data/images/greenGoat.png");
    private static final Image MOUTAIN_TOP = new Image(PATH + "data/images/mountainTop.png");
    private static final Image ORANGE_GOAT = new Image(PATH + "data/images/orangeGoat.png");
    private static final Image YELLO_GOAT = new Image(PATH + "data/images/yellowGoat.png");
    private static final Image GOAT4 = new Image(PATH + "data/images/goat4.jpg");
    private static final Image GOAT5 = new Image(PATH + "data/images/goat5.jpg");
    private static final Image GOAT6 = new Image(PATH + "data/images/goat6.jpg");
    private static final Image GOAT7 = new Image(PATH + "data/images/goat7.jpg");
    private static final Image GOAT8 = new Image(PATH + "data/images/goat8.jpg");
    private static final Image PETE = new Image(PATH + "data/images/pete.png");
    public static final Image BLANK = new Image(PATH +"data/images/blank.png");
    public static final Image SQUARE = new Image(PATH +"data/images/square.png");


    // arrows
    private static final Image LEFT_ARROW = new Image(PATH + "data/images/left.png");
    private static final Image RIGHT_ARROW = new Image(PATH + "data/images/right.png");
    private static final Image UP_ARROW = new Image(PATH + "data/images/up.png");
    private static final Image DOWN_ARROW = new Image(PATH + "data/images/down.png");
    private static final Image BACKGROUND = new Image( "file:data/images/background.jpg");


    
    // Initiaize varibles 
    private Position tempP;
    private PetesPike petesPike;
    // private Button[][] buttons;
    // private Label statusLabel;
    private Label hint;
    // private TextField text;
    private GridPane board;
    private int rows;
    private int cols;
    private Label move;
    private Label statusMessage;
    private Button right;
    private Button left;
    private Button up;
    private Button down;
    private Button hintButton;
    private Button solveButton;



    /**
     * Called when there is a change in the board. It processes the move made by the player or solver,
     * updates the game status, and refreshes the board view.
     *
     * @param from The starting position of the move.
     * @param to The direction of the move to be made.
     */

    
    @Override
    public void boardChanged(Position from , Direction to) {
        try {
            petesPike.makeMove(new Move(from , to));
            
            statusMessage.setText("Great Move!");

            statusMessage.setStyle("-fx-text-fill: rgb(50, 255, 0);");
        } catch (PetesPikeException e) {
            
            statusMessage.setFont(new Font ("Courier New", 15));

            statusMessage.setText(e.getMessage());

            statusMessage.setStyle("-fx-text-fill: rgb(250, 100, 0);");
        }
        //System.out.println(petesPike.getMoveCount());
        move.setText("Move: " + petesPike.getMoveCount());
        if(this.petesPike.getGameState().equals(GameState.WON)){
            Font scalFont = new Font ("Courier New", 10);
            statusMessage.setFont(scalFont);
            statusMessage.setText("Congratulations, you have scal the mountain!");
            board.setDisable(true);
            left.setDisable(true);
            right.setDisable(true);
            up.setDisable(true);
            down.setDisable(true);
            hintButton.setDisable(true);
            solveButton.setDisable(true);

            
        }
        boardUpdate();
    }

    /**
     * Attempts to solve the game using a backtracking algorithm.
     * This method starts a new thread to animate each move of the solution.
     */
    private void solve() {
        PetesPikeSolver pps = new PetesPikeSolver(petesPike);
        Backtracker<PetesPikeSolver> backtracker = new Backtracker<>(false);
        PetesPikeSolver solution = backtracker.solve(pps);
        //statusMessage.setText("Solved!");
        if(solution != null) {
            List<Move> ppSolved = solution.getMoves();



            new Thread(() -> {
                // for each move
                for(Move move: ppSolved){
                     Platform.runLater(() -> {
                         // make move
                         boardChanged(move.getPosition(), move.getDirection());
                         boardUpdate();
                     });
                     try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    }
                        
                       // sleep small amount time (~250ms)
       
            }).start();
            
            statusMessage.setText("Solved!");
        } else {
            statusMessage.setText("No solution...");
        }
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.board = new GridPane();

        // File setOnAction when entered a filename
        TextField file = new TextField("data/petes_pike_5_5_4_0.txt");

        // Turn back on if you want to use enter to go through a new file instead of new puzzle

        file.setOnAction(e -> {
            // Update PetesPike instance or create a new one based on the new file
            try {

                this.petesPike = new PetesPike(file.getText());
            } catch (IOException ioe) {
                System.out.println("The file is not available " + ioe.getMessage());
      
            }
        
            // Retrieve new rows and cols
            this.cols = this.petesPike.getCol();
            this.rows = this.petesPike.getRows();

            board.getChildren().clear(); 
            
            boardUpdate();
            
        });

        this.petesPike = new PetesPike(file.getText());
        this.cols = this.petesPike.getCol();
        this.rows = this.petesPike.getRows();
        // System.out.println(rows);

        GridPane grid = new GridPane();
        
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                board.add(makeButton(row, col), col, row);
            }
        }
        
        BorderPane.setAlignment(board, Pos.CENTER);
        // Set the GridPane as the center node of the BorderPane
        VBox vbox =new VBox();
        HBox.setHgrow(grid, Priority.ALWAYS);
        

        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
        // Create a BackgroundImage
        BackgroundImage backgroundImage = new BackgroundImage(BACKGROUND, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        // Create a Background and set it on a Pane
        Background background = new Background(backgroundImage);

        BorderPane root = new BorderPane();
        root.setBackground(background);

        // Arrow buttons  and reset button and hint 
        
        this.left = new Button(null, createImageView(LEFT_ARROW, 30, 30)); 
        left.setPrefSize(40, 40);  // Set the preferred size slightly larger than the image
        left.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
        left.setMaxSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
        
        
        left.setOnAction( e->{
            if(this.tempP!=null){
                boardChanged(this.tempP, Direction.LEFT);
                

            } else{
                statusMessage.setText("No Position choose");
            }
        });
        this.right = new Button(null, createImageView(RIGHT_ARROW, 30, 30));
        right.setPrefSize(40, 40);  // Set the preferred size slightly larger than the image
        right.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
        right.setMaxSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
        right.setOnAction( e->{
            if(this.tempP!=null){
                boardChanged(this.tempP, Direction.RIGHT);
            } else{
                statusMessage.setText("No Position choose");
            }
        });
        this.up = new Button(null, createImageView(UP_ARROW, 30, 30));
        up.setPrefSize(40, 40);  // Set the preferred size slightly larger than the image
        up.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
        up.setMaxSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
        up.setOnAction( e->{
            if(this.tempP!=null){
                boardChanged(this.tempP, Direction.UP);
            } else{
                statusMessage.setText("No Position choose");
            }
        });
        this.down = new Button(null, createImageView(DOWN_ARROW, 30, 30));
        down.setPrefSize(40, 40);  // Set the preferred size slightly larger than the image
        down.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
        down.setMaxSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
        down.setOnAction( e->{
            if(this.tempP!=null){
                boardChanged(this.tempP, Direction.DOWN);
            } else{
                statusMessage.setText("No Position choose");
            }
        });
        VBox hint = makeHintButton("Hint");





        // Commented out for border around arrows 
        // grid.setBorder(new Border(
        //     new BorderStroke(
        //         Color.BROWN, 
        //         BorderStrokeStyle.SOLID, 
        //         CornerRadii.EMPTY, 
        //         BorderStroke.THICK)));
        Button reset = makeResetButton("Reset");


        // Top HBox 
        Font fileFont = new Font ("Courier New", 20);

        file.setFont(fileFont);
        Button newPuzzle = makeNewPuzzleButton("New Puzzle");
        newPuzzle.setOnAction(e -> {
            // Update PetesPike instance or create a new one based on the new file
            try {
                Font resetMessageFont = new Font ("Courier New", 24);
                statusMessage.setFont(resetMessageFont);
                this.petesPike.reset(petesPike);
                statusMessage.setText("New Game");
                this.hint.setText("");
                statusMessage.setStyle("-fx-text-fill: rgb(50, 255, 0);");
                board.setDisable(false);
                left.setDisable(false);
                right.setDisable(false);
                up.setDisable(false);
                down.setDisable(false);
                hintButton.setDisable(false);
                solveButton.setDisable(false);
                this.petesPike = new PetesPike(file.getText());
                boardUpdate();

            } catch (IOException ioe) {
                System.out.println("The file is not available " + ioe.getMessage());
      
            }
        
            // Retrieve new rows and cols
            this.cols = this.petesPike.getCol();
            this.rows = this.petesPike.getRows();

            board.getChildren().clear(); 
            boardUpdate();
        });
        
        HBox hbox = new HBox();
        HBox.setHgrow(reset, Priority.ALWAYS);
        HBox.setHgrow(file, Priority.ALWAYS);
        HBox.setHgrow(newPuzzle, Priority.ALWAYS);
        hbox.getChildren().addAll(reset,file,newPuzzle);       

        grid.add(left, 0, 1);
        grid.add(right, 2, 1);
        grid.add(up, 1, 0);
        grid.add(down, 1, 2);
 

        // bottom status 
        move = makeLabel("Move: 0");
        //move.setText("Move: " + petesPike.getMoveCount());
        statusMessage = makeLabel("New game");


        //statusMessage.setText(petesPike.);
        Font bottonFont = new Font ("Courier New", 24);
        statusMessage.setFont(bottonFont);
        move.setFont(bottonFont);
        HBox bottonHbox = new HBox();
        HBox.setHgrow(statusMessage, Priority.ALWAYS);
        //HBox.setHgrow(move, Priority.ALWAYS);
      
        bottonHbox.getChildren().addAll(statusMessage,move);

        root.setBottom(bottonHbox);
        vbox.getChildren().addAll(grid,hint);
        root.setRight(vbox);
        root.setCenter(board);
        root.setTop(hbox);
        
        Scene scene = new Scene(root, 700, 500);
        // Scene scene2 = new Scene(grid);
        primaryStage.setScene(scene);

        primaryStage.setTitle("PetesPike");
        primaryStage.show();
    }


    
// makebutton functions 


    /**
     * Creates and configures a button for each cell on the game board.
     * The button's appearance and behavior are set based on the game state.
     *
     * @param row The row index of the button in the grid.
     * @param col The column index of the button in the grid.
     * @return A configured Button for the specified grid position.
     */
    private Button makeButton(int row, int col) {
        ImageView squareView = new ImageView(BLANK);
        squareView.setFitHeight(60);
        squareView.setFitWidth(60);
        Button button = new Button("",squareView);
        button.setPrefSize(60, 60);
        char c = petesPike.getBoard(row, col);
        if(c== 'T'){
            ImageView image = new ImageView(MOUTAIN_TOP);
            // squareView.setBackground(image);
            image.setFitWidth(60); 
            image.setFitHeight(60);
            button.setGraphic(image);
            
        }    
        else if(petesPike.getElement().containsKey(c)){
            
            ImageView image;
            if(c== 'P'){
                image = new ImageView(PETE);
            } else if(c== '0'){
                image = new ImageView(BLUE_GOAT);
            } else if(c== '1'){
                image = new ImageView(ORANGE_GOAT);
            } else if(c== '2'){
                image = new ImageView(YELLO_GOAT);
            } else if(c== '3'){
                image = new ImageView(GREEN_GOAT);
            } else if(c== '4'){
                image = new ImageView(GOAT4);
            } else if(c== '5'){
                image = new ImageView(GOAT5);
            } else if(c== '6'){
                image = new ImageView(GOAT6);
            } else if(c== '7'){
                image = new ImageView(GOAT7);
            }else{
                image = new ImageView(GOAT8);
            }
            image.setFitWidth(60); 
            image.setFitHeight(60);
            button.setGraphic(image);
        }
        button.setPadding(new Insets(0));
        button.setOnAction(e->{
            // button.setBackground(new BackgroundImage(BACKGROUND, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, null));
            this.tempP = new Position(row,col);

        //     BackgroundImage backgroundImage = new BackgroundImage(BLANK, 
        //     BackgroundRepeat.NO_REPEAT, 
        //     BackgroundRepeat.NO_REPEAT, 
        //     BackgroundPosition.DEFAULT, 
        //     BackgroundSize.DEFAULT);
        
        // Background background = new Background(backgroundImage);
        // button.setBackground(background);


        });
        return button;
    }

    /**
     * Creates a button that resets the game to the initial state.
     *
     * @param text The text to display on the button.
     * @return A configured Button to reset the game.
     */

    private Button makeResetButton(String text) {
        Button button = new Button(text);
        
        button.setPadding(new Insets(10));
        button.setAlignment(Pos.CENTER);
        button.setMaxSize(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);

        button.setOnAction(e -> {
            Font resetMessageFont = new Font ("Courier New", 24);
            statusMessage.setFont(resetMessageFont);
            this.petesPike.reset(petesPike);
            statusMessage.setText("New Game");
            this.hint.setText("");
            statusMessage.setStyle("-fx-text-fill: rgb(50, 255, 0);");
            board.setDisable(false);
            left.setDisable(false);
            right.setDisable(false);
            up.setDisable(false);
            down.setDisable(false);
            hintButton.setDisable(false);
            solveButton.setDisable(false);
            boardUpdate();
            
        });
        return button;
    }


    /**
     * Creates a button to load a new puzzle.
     *
     * @param text The text to display on the button.
     * @return A configured Button to load a new puzzle.
     */
    
    private Button makeNewPuzzleButton(String text) {
        Button button = new Button(text);
        button.setPadding(new Insets(10));
        button.setAlignment(Pos.CENTER);
        button.setMaxSize(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        return button;
    }
    

     /**
     * Updates the board view to reflect the current state of the game.
     * This method clears the existing board view and repopulates it based on the current game state.
     */
    public void boardUpdate(){
        
        board.getChildren().clear(); 
        // Repopulate the board
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                board.add(makeButton(row, col), col, row);
            }
        }
        
        move.setText("Move: " + petesPike.getMoveCount());
    }



    /**
     * Creates an ImageView for use within a button, setting the image and size.
     *
     * @param image The image to display in the ImageView.
     * @param width The desired width of the ImageView.
     * @param height The desired height of the ImageView.
     * @return An ImageView configured with the specified image and size.
     */
    private ImageView createImageView(Image image, double width, double height) {
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setPreserveRatio(true);
        return imageView;
    }


    /**
     * Creates a layout containing a hint button, label for hints, and a solve button.
     * This method sets up the hint mechanism and the button to initiate solving the puzzle.
     *
     * @param text The text to display on the hint button.
     * @return A VBox containing the hint button and label, and solve button.
     */
    
    private VBox makeHintButton(String text) {

        this.hintButton = new Button(text);
        hintButton.setPadding(new Insets(10));
        hintButton.setAlignment(Pos.CENTER);
        hintButton.setMaxSize(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    

        this.hint = new Label();
        hint.setAlignment(Pos.CENTER);
        hint.setMaxWidth(Double.POSITIVE_INFINITY);


        

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        this.solveButton = new Button("Solve");
        solveButton.setPadding(new Insets(10));
        solveButton.setMaxSize(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        solveButton.setOnAction(e-> {
            solve();
        });

        if(this.petesPike.getGameState().equals(GameState.WON)){
            solveButton.setDisable(true);
            hintButton.setDisable(true);
     
        }


        layout.getChildren().addAll(hintButton, hint, solveButton);

        hintButton.setOnAction(e -> {

 
            List<Move> hintList = petesPike.getPossibleMoves();

            // Set the text of the hint label to display the hints
            hint.setText(hintList.toString());
            
            
        });
    
        return layout; 
    }

    private Label makeLabel(String msg){
        Label label = new Label(msg);
        label.setMaxWidth(Integer.MAX_VALUE);
        
        
        return label;
    }


    public static void main(String[] args) {
        launch(args);
    }

}