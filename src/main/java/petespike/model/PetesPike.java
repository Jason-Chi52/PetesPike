package petespike.model;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

import backtracker.Backtracker;

/**
 * Represents the game board and logic for Pete's Pike. This class handles game state,
 * move validation, and interactions with the game observer.
 */
public class PetesPike {
    private final char MOUNTAINTOP_SYMBOL = 'T';
    public final char EMPTY_SYMBOL  = '-';
    public final char PETE_SYMBOL  = 'P';
    public final Set<Character> GOAT_SYMBOLS = Set.of('0', '1', '2', '3', '4', '5', '6', '7', '8');
    private char [][] board ; 
    private char [][] initialboard;
    private Map< Character, Position > elements = new HashMap<>();
    private int moveCount ;
    private int rows; // Store the number of rows
    private int cols; // Store the number of columns
    private Position mountainTopPosition; 
    private Position petePosition;
    private PetesPikeObserver observer;
    private PetesPikeSolver petesPikeSolver;


       /**
     * Constructs a new game board by copying the state from another PetesPike instance.
     * This constructor is typically used for creating a new game state based on an existing one.
     * @param other The PetesPike instance to copy from.
     */
    protected PetesPike(PetesPike other){
        this.rows = other.rows;
        this.cols = other.cols;
        // Deep copy of the board 
        this.board = new char[other.rows][other.cols];
        for (int i = 0; i < other.rows; i++) {
            for(int j = 0  ; j < other.cols ;j++){
                this.board[i][j] = other.board[i][j];
            }
        }
        // Deep copy of the initialboard 
        this.initialboard = new char [other.rows][other.cols];
        for (int i = 0; i < other.rows; i++) {
            for(int j = 0  ; j < other.cols ;j++){
                this.initialboard[i][j] = other.initialboard[i][j];
            }
        }
        this.petePosition = new Position(other.petePosition.getRow(), other.petePosition.getCol());
        this.mountainTopPosition = new Position(other.mountainTopPosition.getRow(), other.mountainTopPosition.getCol());
        // Deep copy of the elements map
        this.elements = new HashMap<>();
        for (Map.Entry<Character, Position> entry : other.elements.entrySet()) {
            this.elements.put(entry.getKey(), new Position(entry.getValue().getRow(), entry.getValue().getCol()));
        }
        // Set observer to null as specified
        this.observer = null;
    }
    /**
     * Registers an observer for receiving notifications about board changes.
     * @param observer The observer to register.
     */
    public void registerObserver(PetesPikeObserver observer){
        this.observer = observer;
    }

    /**
     * Notifies the registered observer about a change in the game board.
     * @param from The position from which a move was initiated.
     * @param to The direction in which the move was made.
     */
    private void notifyObserver(Position from , Direction to){
        if(observer != null){
            observer.boardChanged(from , to);
        }
    }

        /**
     * Validates a move based on the current game state.
     * @param move The move to validate.
     * @return true if the move is valid, false otherwise.
     * @throws PetesPikeException if the move is invalid.
     */
    public boolean isValid(Move move) throws PetesPikeException{
        Position position = move.getPosition();
        Position nextPosition =null ;
        Move nextmove =null;
        
        int row = position.getRow();
        int col = position.getCol();
        // Position from = new Position(row, col);
        Direction direction = move.getDirection();
        char symbolAtPosition = getSymbolAt(position);
        // System.out.println(position);
        // System.out.println(symbolAtPosition);

        if (symbolAtPosition == EMPTY_SYMBOL) {
            return false;
            // throw new PetesPikeException("There is no piece at the specified position." + '\n');
        }
        if(row>this.rows || col>this.cols){
            return false;
            // throw new PetesPikeException("The given position is invalid." + '\n');
        }
        if(direction == Direction.DOWN || direction == Direction.UP){
            for(char key : elements.keySet()){
                Position tempKeyP = elements.get(key);
                if(tempKeyP.getCol()==col && key!=symbolAtPosition){
                    if(direction == Direction.UP && tempKeyP.getRow()-row < 1 
                    && !GOAT_SYMBOLS.contains(this.board[tempKeyP.getRow()+1][tempKeyP.getCol()])
                    && this.board[tempKeyP.getRow()+1][tempKeyP.getCol()]!= PETE_SYMBOL){
                        boolean noSymbsBelow = true;
                        for(int i = tempKeyP.getRow()+2; i < row; i++){
                            if(GOAT_SYMBOLS.contains(this.board[i][tempKeyP.getCol()])){
                                noSymbsBelow = false;
                            }     
                        }
                        if(noSymbsBelow){
                            row = tempKeyP.getRow()+1;
                            nextPosition = new Position(row, col);
                            nextmove = new Move(nextPosition, Direction.UP);
                        }    
                    }
                    if(direction == Direction.DOWN && tempKeyP.getRow()-row >1 
                    && !GOAT_SYMBOLS.contains(this.board[tempKeyP.getRow()-1][tempKeyP.getCol()])
                    && this.board[tempKeyP.getRow()-1][tempKeyP.getCol()]!= PETE_SYMBOL){
                        boolean noSymbsBelow = true;
                        for(int i = tempKeyP.getRow()-2; i > row; i--){
                            if(GOAT_SYMBOLS.contains(this.board[i][tempKeyP.getCol()])){
                                noSymbsBelow = false;
                            }     
                        }
                        if(noSymbsBelow){
                            row = tempKeyP.getRow()-1;
                            nextPosition = new Position(row, col);
                            nextmove = new Move(nextPosition, Direction.DOWN);
                        }

                    }
                }
            }
        }
        if(direction == Direction.RIGHT || direction == Direction.LEFT){
            for(char key : elements.keySet()){
                Position tempKeyP = elements.get(key);
                if(elements.get(key).getRow()==row && key!=symbolAtPosition){ 
                    if(direction == Direction.RIGHT && elements.get(key).getCol()-col >1 
                    && !GOAT_SYMBOLS.contains(this.board[tempKeyP.getRow()][tempKeyP.getCol()-1])
                    && this.board[tempKeyP.getRow()][tempKeyP.getCol()-1]!= PETE_SYMBOL){
                        boolean noSymbsBelow = true;
                        for(int i = tempKeyP.getCol()-2; i > col; i--){
                            if(GOAT_SYMBOLS.contains(this.board[tempKeyP.getRow()][i])){
                                noSymbsBelow = false;
                            }     
                        }
                        if(noSymbsBelow){
                        col = elements.get(key).getCol()-1;
                        nextPosition = new Position(row, col);
                        nextmove = new Move(nextPosition, Direction.RIGHT);
                        }
                    }
                    if(direction == Direction.LEFT && elements.get(key).getCol()-col <1 
                    && !GOAT_SYMBOLS.contains(this.board[tempKeyP.getRow()][tempKeyP.getCol()+1])
                    && this.board[tempKeyP.getRow()][tempKeyP.getCol()+1]!= PETE_SYMBOL){
                        boolean noSymbsBelow = true;
                        for(int i = tempKeyP.getCol()+2; i < col; i++){
                            if(GOAT_SYMBOLS.contains(this.board[tempKeyP.getRow()][i])){
                                noSymbsBelow = false;
                            }     
                        }
                        if(noSymbsBelow){
                        col = elements.get(key).getCol()+1;
                        nextPosition = new Position(row, col);
                        nextmove = new Move(nextPosition, Direction.LEFT);
                        }
                    }
                }
            }
        }
        // else if( this.getGameState() == GameState.WON){
        //     System.out.println("There must be an active game to use this command");
     
        // }

        if (nextmove == null){
            return false;
            // throw new PetesPikeException("There is no piece to stop the move in the given direction."+ '\n');
        }
        this.elements.remove(symbolAtPosition);
        this.elements.put(symbolAtPosition,nextPosition);
        this.board[position.getRow()][position.getCol()] = '-';
        this.board[row][col] = symbolAtPosition;
        return true;

    }
    /**
     * Initializes the game board from a specified file.
     * @param filename The path to the file containing the board layout.
     * @throws IOException if there is an issue reading the file.
     */
    public PetesPike(String filename) throws IOException{
        try(
        FileReader file = new FileReader(filename);
        BufferedReader reader = new BufferedReader(file);){
        
        String head = reader.readLine (); 
        String[]lines = head.strip().split(" ");
        this.rows = Integer.parseInt(lines[0]);
        this.cols = Integer.parseInt(lines[1]);
        board = new char[this.rows][this.cols];
        initialboard = new char[this.rows][this.cols]; // Initialize the initialboard
        int row=0;
        String line = reader.readLine (); 
        while(line != null){
            //System.out.println(line);
            for(int col =0;col <line.length();col++){
                this.board[row][col] = line.charAt(col);
                this.initialboard[row][col] = line.charAt(col); // stores the initialboard for reset function
                
                if(GOAT_SYMBOLS.contains(this.board[row][col]) || line.charAt(col) ==PETE_SYMBOL ){
                    this.elements.put( this.board[row][col],new Position(row, col)) ;
                }
                if(line.charAt(col) ==MOUNTAINTOP_SYMBOL){
                    this.mountainTopPosition = new Position(row, col);
                }
                if(line.charAt(col) ==PETE_SYMBOL){
                    this.petePosition = new Position(row, col);
                }

            }
            line = reader.readLine();
            
            row++;
        }
        }
    }

    public char getBoard(int row, int col){
        return this.board[row][col];
    }
    
    public Map< Character, Position > getElement(){
        return this.elements;
    }
    public int addMoveCount(){
        return this.moveCount++;
    }
    public int getMoveCount(){
        return this.moveCount;
    }

    public int getRows(){
        return this.rows;
    }

    public int getCol(){
        return this.cols;
    }

    public boolean isGoal(){
        Position pete = elements.get('P');
        return pete.equals(mountainTopPosition);
    } 

    public Position getMountainTop(){
        return this.mountainTopPosition;
    }

    public Position getPetePosition(){
        return this.petePosition;
    }
    
    
    public GameState getGameState(){
        Position pete = elements.get('P');
        if(mountainTopPosition.equals(pete))
            
            return GameState.WON;
        return GameState.IN_PORGRESS;
    }

    public void printBoard(){
        System.out.print(" ");
        IntStream.rangeClosed(0, cols-1)
            .forEach(n->System.out.print(  " "+n));
        System.out.println();
        for (int row = 0; row < this.rows; row++){
            System.out.print(row);
            for (int col = 0; col < cols; col++) {
                
                System.out.print(" " +board[row][col]);
            }
            System.out.println(); 
        }
    }

    /**
     * Executes a move on the game board and updates the game state accordingly.
     * @param move The move to make.
     * @return The resulting PetesPikeSolver instance, if any.
     * @throws PetesPikeException if the move is invalid or cannot be made.
     */
    public PetesPikeSolver makeMove(Move move)throws PetesPikeException{
        Position position = move.getPosition();
        Position nextPosition =null ;
        Move nextmove =null;
        if(isGoal()){
            System.out.print(("Congratulations, you have scal the mountain"+ '\n'));
            
            return petesPikeSolver;
        }
        
        // if(position.getRow() == 0){
        //     return null;
        // }
        int row = position.getRow();
        int col = position.getCol();
        Position from = new Position(row, col);
        Direction direction = move.getDirection();
        char symbolAtPosition = getSymbolAt(position);
        // System.out.println(position);
        // System.out.println(symbolAtPosition);

        if (symbolAtPosition == EMPTY_SYMBOL) {

            throw new PetesPikeException("There is no piece at the specified position." + '\n');
        }
        if(row>this.rows || col>this.cols){
            throw new PetesPikeException("The given position is invalid." + '\n');
        }
        if(board[row][col]=='T'){
            throw new PetesPikeException("can't move the mountain." + '\n');
        }
        if(direction == Direction.DOWN || direction == Direction.UP){
            for(char key : elements.keySet()){
                Position tempKeyP = elements.get(key);
                if(tempKeyP.getCol()==col && key!=symbolAtPosition){
                    if(direction == Direction.UP && tempKeyP.getRow()-row < 1 
                    && !GOAT_SYMBOLS.contains(this.board[tempKeyP.getRow()+1][tempKeyP.getCol()])
                    && this.board[tempKeyP.getRow()+1][tempKeyP.getCol()]!= PETE_SYMBOL){
                        boolean noSymbsBelow = true;
                        for(int i = tempKeyP.getRow()+2; i < row; i++){
                            if(GOAT_SYMBOLS.contains(this.board[i][tempKeyP.getCol()])){
                                noSymbsBelow = false;
                            }     
                        }
                        if(noSymbsBelow){
                            row = tempKeyP.getRow()+1;
                            nextPosition = new Position(row, col);
                            nextmove = new Move(nextPosition, Direction.UP);
                        }    
                    }
                    if(direction == Direction.DOWN && tempKeyP.getRow()-row >1 
                    && !GOAT_SYMBOLS.contains(this.board[tempKeyP.getRow()-1][tempKeyP.getCol()])
                    && this.board[tempKeyP.getRow()-1][tempKeyP.getCol()]!= PETE_SYMBOL){
                        boolean noSymbsBelow = true;
                        for(int i = tempKeyP.getRow()-2; i > row; i--){
                            if(GOAT_SYMBOLS.contains(this.board[i][tempKeyP.getCol()])){
                                noSymbsBelow = false;
                            }     
                        }
                        if(noSymbsBelow){
                            row = tempKeyP.getRow()-1;
                            nextPosition = new Position(row, col);
                            nextmove = new Move(nextPosition, Direction.DOWN);
                        }

                    }
                }
            }
        }
        if(direction == Direction.RIGHT || direction == Direction.LEFT){
            for(char key : elements.keySet()){
                Position tempKeyP = elements.get(key);
                if(elements.get(key).getRow()==row && key!=symbolAtPosition){ 
                    if(direction == Direction.RIGHT && elements.get(key).getCol()-col >1 
                    && !GOAT_SYMBOLS.contains(this.board[tempKeyP.getRow()][tempKeyP.getCol()-1])
                    && this.board[tempKeyP.getRow()][tempKeyP.getCol()-1]!= PETE_SYMBOL){
                        boolean noSymbsBelow = true;
                        for(int i = tempKeyP.getCol()-2; i > col; i--){
                            if(GOAT_SYMBOLS.contains(this.board[tempKeyP.getRow()][i])){
                                noSymbsBelow = false;
                            }     
                        }
                        if(noSymbsBelow){
                        col = elements.get(key).getCol()-1;
                        nextPosition = new Position(row, col);
                        nextmove = new Move(nextPosition, Direction.RIGHT);
                        }
                    }
                    if(direction == Direction.LEFT && elements.get(key).getCol()-col <1 
                    && !GOAT_SYMBOLS.contains(this.board[tempKeyP.getRow()][tempKeyP.getCol()+1])
                    && this.board[tempKeyP.getRow()][tempKeyP.getCol()+1]!= PETE_SYMBOL){
                        boolean noSymbsBelow = true;
                        for(int i = tempKeyP.getCol()+2; i < col; i++){
                            if(GOAT_SYMBOLS.contains(this.board[tempKeyP.getRow()][i])){
                                noSymbsBelow = false;
                            }     
                        }
                        if(noSymbsBelow){
                        col = elements.get(key).getCol()+1;
                        nextPosition = new Position(row, col);
                        nextmove = new Move(nextPosition, Direction.LEFT);
                        }
                    }
                }
            }
        }
        // else if( this.getGameState() == GameState.WON){
        //     System.out.println("There must be an active game to use this command");
     
        // }
        
        if (nextmove == null){
            throw new PetesPikeException("There is no piece to stop the move in the given direction."+ '\n');
        }
        this.moveCount++;
        this.elements.remove(symbolAtPosition);
        this.elements.put(symbolAtPosition,nextPosition);
        this.board[position.getRow()][position.getCol()] = '-';
        this.board[row][col] = symbolAtPosition;

        if(this.board[getMountainTop().getRow()][getMountainTop().getCol()]=='-')
            this.board[getMountainTop().getRow()][getMountainTop().getCol()]='T';
        notifyObserver(from , nextmove.getDirection());
        return null; // remove possible
        
    }
    
    /**
     * Returns the symbol at a specific position on the board.
     * @param position The position to query.
     * @return The symbol at the given position.
     */

    public char getSymbolAt(Position position){
        if (position.getRow() >= 0 && position.getRow() < this.rows &&
        position.getCol() >= 0 && position.getCol() < cols) {
        return board[position.getRow()][position.getCol()];
    }
        return EMPTY_SYMBOL; // or throw an exception if the position is out of bounds
    } 

    


   /**
     * Generates a list of all possible moves based on the current game board state. This method checks
     * for every character on the board (both Pete and goats) and determines if they can move to a new position
     * based on the game rules. Moves are considered possible if there's no adjacent character in the intended
     * direction and the movement does not result in placing a character outside the bounds of the board.
     *
     * @return A list of {@link Move} objects, each representing a valid move that can be made given the
     * current board state. If no moves are possible, returns an empty list.
     */
    public List<Move> getPossibleMoves(){
        List<Move> moveList = new ArrayList<>();
        Position p = new Position(0, 0);
        Position pp = new Position(0, 0);
        for(char key : elements.keySet()){
            for(char key2 : elements.keySet()){
                p =this.elements.get(key);
                if(key2==key)
                    continue;
                pp = this.elements.get(key2);
                Direction d;
                Position temp;
                Move move =null;
                if(p.getCol()==pp.getCol()){
                    
                    if(p.getRow()-pp.getRow()>1 && !GOAT_SYMBOLS.contains(this.board[pp.getRow()+1][pp.getCol()]) && this.board[pp.getRow()+1][pp.getCol()]!= PETE_SYMBOL ){//make sure the other one is not next to it
                        d = Direction.UP;
                        temp = new Position(p.getRow(), p.getCol());
                        move = new Move(temp,d);
                    }if (p.getRow()-pp.getRow() < 1 && !GOAT_SYMBOLS.contains(this.board[pp.getRow()-1][pp.getCol()]) && this.board[pp.getRow()-1][pp.getCol()]!= PETE_SYMBOL){
                        d = Direction.DOWN;
                        temp = new Position(p.getRow(), p.getCol());
                        move = new Move(temp,d);
                    }
                    //move = new Move(temp,d);
                    
                }else if(p.getRow()==pp.getRow()){
                    // Direction d;
                    // Position temp;
                    if(p.getCol()-pp.getCol()>1 && !GOAT_SYMBOLS.contains(this.board[pp.getRow()][pp.getCol()+1]) && this.board[pp.getRow()][pp.getCol()+1]!= PETE_SYMBOL){ //make sure the other one is not next to it
                        d = Direction.LEFT;
                        temp = new Position(p.getRow(), p.getCol());
                        move = new Move(temp,d);
                    }if(p.getCol()-pp.getCol()< 1 && !GOAT_SYMBOLS.contains(this.board[pp.getRow()][pp.getCol()-1]) && this.board[pp.getRow()][pp.getCol()-1]!= PETE_SYMBOL){
                        d = Direction.RIGHT;
                        temp = new Position(p.getRow(), p.getCol());
                        move = new Move(temp,d);
                    }
                    
                }
               
               
                // System.out.println(moveList.toString());
                if(!moveList.contains(move) && move !=null){
                    moveList.add(move);
                }
                
            }
        }
        return moveList; 
    }

    /**
     * Resets the game to its initial state.
     * @param ppl The PetesPike instance to reset to.
     */
    public void reset(PetesPike ppl) {
        int row=0;
        while(row<rows){
            //System.out.println(line);
            for(int col =0;col <cols;col++){
                this.board[row][col] = this.initialboard[row][col];
                
                if(GOAT_SYMBOLS.contains(this.board[row][col]) || this.board[row][col] ==PETE_SYMBOL ){
                    this.elements.put( this.board[row][col],new Position(row, col)) ;
                }
                if(this.board[row][col] ==PETE_SYMBOL){
                    this.petePosition = new Position(row, col);
                }

            }
            row++;
        }
        
        

        this.moveCount = 0;
        
    }
    
    /**
     * Attempts to solve the game using the backtracking algorithm.
     * @throws PetesPikeException if the solution process fails.
     */
    public void solve() throws PetesPikeException{
        PetesPikeSolver pps = new PetesPikeSolver(this);
        Backtracker<PetesPikeSolver> backtracker = new Backtracker<>(false);
        PetesPikeSolver solution = backtracker.solve(pps);
        // System.out.println(solution);
        List<Move> ppSolved = solution.getMoves();
        for(Move move: ppSolved){
            
            this.makeMove(new Move(move.getPosition(), move.getDirection()));
            if(!isGoal()){
                printBoard();
            }
            
            System.out.println();
            
        }
        // System.out.println("Congratulations, you have scal the mountain!"+ '\n');
    }


    /**
     * The main method serves as the entry point for the Pete's Pike game application.
     * This method initializes the game with a predefined board configuration file,
     * displays the initial board setup, calculates and displays possible moves,
     * makes a move, and then prints the updated board. It handles any exceptions
     * that occur during the game initialization or gameplay.
     *
     * @param args Command line arguments, not used in this implementation.
     * @throws IOException If there is an issue with reading the board configuration file.
     */
    public static void main(String[] args) throws IOException {
        
        try{
            // Scanner scanner = new Scanner(System.in);
            // System.out.println("Puzzle filename: ");
            // String filename = scanner.nextLine();
            String filename = "data/petes_pike_5_5_4_0.txt" ;
            PetesPike petesPike = new PetesPike(filename);
            
            petesPike.printBoard();
            System.out.println(petesPike.getPossibleMoves());
            Position position = new Position(0, 2);
            //System.out.println(petesPike.getSymbolAt(position));

            Move move = new Move(position, Direction.DOWN);
            petesPike.makeMove(move);
            petesPike.printBoard();
            //System.out.println(petesPike.getPossibleMoves());
        }catch (PetesPikeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    
    }
}
    

