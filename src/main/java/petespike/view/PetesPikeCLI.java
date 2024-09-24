package petespike.view;
import java.io.IOException;
import java.util.Scanner;

import petespike.model.Move;
import petespike.model.PetesPike;
import petespike.model.PetesPikeException;

import petespike.model.Position;

public class PetesPikeCLI {
    /**
     * Command to display help information.
     */
    public static final String HELP = "help";

    /**
     * Command to quit the game.
     */
    public static final String QUIT = "quit";

    /**
     * Command to make a move in the game.
     */
    public static final String MOVE = "move";

    /**
     * Command to reset the game to its initial state.
     */
    public static final String RESET = "reset";

   /**
     * Command to display the game board.
     */
    public static final String BOARD = "board";

        /**
     * Command to start a new game.
     */
    public static final String NEW = "new";

        /**
     * Command to receive a hint for the next move.
     */
    public static final String HINT = "hint";

    /**
     * Command to automatically solve the game.
     */
    public static final String SOLVE ="solve";

    /**
     * Instance of {@link PetesPike} that this CLI interacts with, representing the game state.
     */
    private PetesPike petesPike;

     /**
     * Scanner used for reading user input from the command line.
     */
    private Scanner scanner;

    /**
     * Flag indicating whether the game has been won.
     */
    private boolean gameWon;

    /**
     * Constructs a new command-line interface for managing and interacting with the Pete's Pike game.
     * Initializes a scanner to read input from the command line.
     */
    public PetesPikeCLI() {
        scanner = new Scanner(System.in);
    }

    /**
     * Starts the game by prompting the user to enter a puzzle filename, then initializes the game with that file.
     * Displays a list of available commands to the user and handles the initialization of the game environment.
     * After successful initialization, it enters a loop to play the game.
     * If the puzzle file cannot be loaded, it will display an error message and exit the game setup.
     * 
     * @throws PetesPikeException if there are issues with game logic initialization.
     */
    public void startGame() throws PetesPikeException {
        System.out.print("Puzzle filename: ");
        String filename = scanner.nextLine();

        System.out.println("Commands: ");
        System.out.println("   help - this help menu");
        System.out.println("   board - display current board");
        System.out.println("   reset - resets the current puzzle");
        System.out.println("   new <puzzle_filename> - start a new puzzle");
        System.out.println("   move <row> <col> <direction> - moves the piece at <row>, <col>" + '\n' + "     where <direction> one of u(p), d(own), l(eft), r(right)");
        System.out.println("   hint - get a valid move, if one exists");
        System.out.println("   solve - solve the current puzzle");
        System.out.println("   quit - quit" + '\n');

        try {
            petesPike = new PetesPike(filename);
            
           
        } catch (IOException e) {
            System.out.println("Failed to load puzzle: " + e.getMessage()+ '\n');
            return;
        }
        playPetesPike(petesPike);
       
    }

    /**
     * Manages the main game loop for Pete's Pike. This method repeatedly displays the game board,
     * prompts the user for commands, processes these commands, and updates the game state accordingly.
     * The loop continues until the user decides to quit or the game reaches a conclusion (such as winning).
     *
     * @param petepike The instance of {@link PetesPike} representing the current game state.
     * @throws PetesPikeException if an error occurs during game command handling.
     */
    public void playPetesPike(PetesPike petepike) throws PetesPikeException {
        boolean start = true;
        petepike.printBoard();
        while (start) {
            
            
            System.out.println("Moves: " + petesPike.getMoveCount() + "\n");

            System.out.print("Commands: ");
            

            String command = scanner.nextLine();
            System.out.println();

            String[] tokens = command.strip().split(" ");
            if (tokens.length > 0 && !tokens[0].isEmpty()) { // check for non-empty command
                switch (tokens[0]) {
                    case HELP:
                        printHelp();
                        break;
                    case QUIT:
                        start = false; // Quit the game loop
                        break;
                    case MOVE:
                        handleMove(petepike, tokens); // Handle movement command
                        petepike.printBoard();
                        break;
                    case RESET:
                        petepike.reset(petepike); // Reset the game to its initial state
                        
                        petepike.printBoard(); 
                        break;
                    case NEW:
                        startGame(); // Start a new game with a different puzzle
                    case SOLVE:
                        petepike.solve(); // Automatically solve the puzzle
                        
                    case BOARD:
                        System.out.println();
                        petepike.printBoard(); // Display the current game board
                        System.out.println();
                        break;
                    case HINT:
                        System.out.println(petepike.getPossibleMoves()); // Show a possible valid move
                        break;
                    default:
                        System.out.println("Invalid command. Type 'help' for a list of commands." + '\n');
                        break;
                }
            } else if (tokens[0].isEmpty()) {
                System.out.println("No command entered. Type 'help' for a list of commands." + '\n');
            }
            if(gameWon == true && tokens[0] != null && tokens[0] != "reset"){
                System.out.println("There must be an active game to use this command");
                break;
            }
            if(petepike.isGoal()){
                System.out.println("Congratulations, you have scal the mountain!"+ '\n');
                gameWon = true;
            }
            
        }
        System.out.println("Good bye!" + '\n');
    }

    /**
     * Displays help information to the user, listing all available commands and their descriptions.
     * This method is typically called when the user enters the 'help' command or when the game starts
     * to provide guidance on how to interact with the game through the CLI.
     */
    private void printHelp() {
        // System.out.println("Enter one of the following commands: ");
        System.out.println("commands: ");

        System.out.println("help - this help menu");
        System.out.println("board - display current board");
        System.out.println("reset - resets the current puzzle");
        System.out.println("new <puzzle_filename> - start a new puzzle");
        System.out.println("move <row> <col> <direction> - moves the piece at <row>, <col> + '\n' + where <direction> one of u(p), d(own), l(eft), r(right)");
        System.out.println("hint - get a valid move, if one exists");
        // System.out.println("solve - solve the current puzzle");
        System.out.println("quit - quit");
    }


    /**
     * Handles the 'move' command entered by the user. This method parses the command parameters to extract
     * the row, column, and direction for the move. If the parameters are valid, it attempts to make the move
     * on the game board through the {@link PetesPike} instance.
     * 
     * If the direction is invalid or the coordinates are not specified correctly, it prints an error message.
     * If the move cannot be made due to game rules or logic, it catches and prints the exception message.
     *
     * @param ppl The {@link PetesPike} instance on which the move will be executed.
     * @param tokens An array of strings containing the command and its parameters.
     */
    private void handleMove(PetesPike ppl, String[] tokens) {
        // System.out.println(Arrays.toString(tokens));

        if(tokens.length == 4) {
            int row = Integer.parseInt(tokens[1]);
            int col = Integer.parseInt(tokens[2]);
            // System.out.println(tokens[3]);
            Position position = new Position(row, col);
            // Direction d = Direction.DOWN;
            try{
            if(tokens[3].equals("u")){
                petesPike.makeMove(new Move(position,petespike.model.Direction.UP));
                //petesPike.addMoveCount();
            }else if(tokens[3].equals("d")){
                petesPike.makeMove(new Move(position,petespike.model.Direction.DOWN));
                //petesPike.addMoveCount();
            }else if(tokens[3].equals("r")){
                petesPike.makeMove(new Move(position,petespike.model.Direction.RIGHT));
                //petesPike.addMoveCount();
            }else if(tokens[3].equals("l")){
                petesPike.makeMove(new Move(position,petespike.model.Direction.LEFT));
                //petesPike.addMoveCount();
            }

            else {
                System.out.println("invalid move");
            }
                 
            // petesPike.printBoard();
            } catch (PetesPikeException e) {
                System.out.println(e.getMessage());
            }

        } else {
            System.out.println("Usage: move <row> <col>");
        }
    }

    public static void main(String[] args) throws PetesPikeException {
        // "data/petes_pike_5_5_4_1.txt"
        // data/petes_pike_5_7_4_0.txt

        // data/petes_pike_5_5_4_0.txt
        // data/petes_pike_9_9_9_0.txt
        new PetesPikeCLI().startGame();
    }
}