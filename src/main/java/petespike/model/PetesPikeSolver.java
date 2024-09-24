package petespike.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import java.io.IOException;
// import java.util.*;

import backtracker.Backtracker;
import backtracker.Configuration;

/**
 * This class represents a solver for the Pete's Pike problem using a backtracking algorithm.
 * It implements the Configuration interface to provide specific configurations for the backtracking algorithm.
 */
public class PetesPikeSolver implements Configuration<PetesPikeSolver>{

    private PetesPike petesPike;
    private List<Move> moves;


    /**
     * Constructs a PetesPikeSolver with a given PetesPike instance.
     * Initializes an empty list of moves as the starting state of the solver.
     * 
     * @param petesPike the PetesPike instance this solver will handle
     */
    public PetesPikeSolver(PetesPike petesPike){
        this.petesPike = petesPike;
        this.moves = new ArrayList<>();  // Initialize with empty list for the initial state.
    }

    /**
     * Constructs a PetesPikeSolver with a given PetesPike instance and an initial list of moves.
     * This constructor allows specifying an initial path of moves for the solver.
     * 
     * @param petesPike the PetesPike instance this solver will handle
     * @param moves the initial list of moves to set in this solver
     */
    public PetesPikeSolver(PetesPike petesPike, List<Move> moves){
            this.petesPike = petesPike;
            this.moves = new ArrayList<>(moves);  // Copy current moves and add new ones as we progress.
    }

    /**
     * Returns the current PetesPike instance being solved.
     * 
     * @return the current instance of PetesPike
     */
    public PetesPike getPetesPike() {
        return petesPike;
    }

        /**
     * Returns the list of moves made so far in solving the PetesPike.
     * 
     * @return the list of moves
     */
    public List<Move> getMoves() {
        return this.moves;
    }

    /**
     * Sets the list of moves to a new list.
     * This method can be used to reset the move list or replace it with a different one.
     * 
     * @param moves the new list of moves to be used by this solver
     */
    public void setMoves(List<Move> moves){
        this.moves = moves;
    }

    /**
     * Generates a collection of all possible successors from the current configuration.
     * Each successor represents a valid state of the problem after making a possible move.
     * 
     * @return a collection of PetesPikeSolver instances, each representing a successor state
     */
    @Override
    public Collection<PetesPikeSolver> getSuccessors() {
        List<PetesPikeSolver> successors = new ArrayList<>();
        List<Move> possibleMoves = this.petesPike.getPossibleMoves();
        for(Move move : possibleMoves){
            try {
                PetesPike pp = new PetesPike(this.petesPike);
                pp.makeMove(move);
                List<Move> newMovesList = new ArrayList<>(this.moves);  // Copy current move list
                newMovesList.add(move);  // Add the new move
                PetesPikeSolver solver = new PetesPikeSolver(pp, newMovesList);
            
                successors.add(solver);
            } catch (PetesPikeException e) {
                e.printStackTrace();
            }
        }
        return successors;
    }
    /**
     * Checks if the current configuration is valid.
     * 
     * @return true if the current configuration is valid, false otherwise
     */
    @Override
    public boolean isValid() {
        return this.petesPike.getPossibleMoves() != null;

    }
    /**
     * Represents if Pete reached MountainTop
     * 
     * @return true if the current configuration is valid, false otherwise
     */
    @Override
    public boolean isGoal() {
        // petesPike.printBoard();

        Position pete = this.petesPike.getElement().get('P');
        return pete.equals(this.petesPike.getMountainTop());
    }


    /**
     * Solves the problem starting from the provided PetesPikeSolver instance.
     * Uses recursive backtracking to find a solution if it exists.
     * 
     * @param petesPike the initial solver configuration to start the solving process
     * @return the solved PetesPikeSolver instance if a solution is found, null otherwise
     */
    public PetesPikeSolver solve(PetesPikeSolver petesPike){
        if (petesPike.isGoal()) {
            return petesPike;
            
        }

        Collection<PetesPikeSolver> successors = petesPike.getSuccessors();
        for (PetesPikeSolver successor : successors) {
            if (successor.isValid()) {
                PetesPikeSolver sol = solve(successor);
                if (sol != null) { 
                    return sol;
                }
            }
        }
    
        return null;
    }

      /**
     * Overloaded method to solve the problem with a debug flag to enable debugging outputs.
     * 
     * @param petesPike the initial PetesPike instance
     * @param debug a boolean flag to enable or disable debugging outputs
     * @return the solved PetesPikeSolver instance if a solution is found, null otherwise
     */

    public PetesPikeSolver solve(PetesPike petesPike, boolean debug){
        PetesPikeSolver solver = new PetesPikeSolver(petesPike);
        Backtracker<PetesPikeSolver> backtracker = new Backtracker<>(debug);
        return backtracker.solve(solver);
        
    }


    // toString for moves
    @Override
    public String toString(){
        return "Moves: " + moves.toString();
    }

    public static void main(String[] args) throws IOException {
        // PetesPike petesPike = new PetesPike("data/petes_pike_5_5_4_0.txt");
        // PetesPikeSolver petesPikeSolver = new PetesPikeSolver(petesPike);
        // petesPikeSolver.solve(petesPike, false);
        


        //Create a new PetesPike game
        PetesPike petesPike = new PetesPike("data/petes_pike_5_5_5_0.txt");

  
        //Implement petesPikeSolver for the current petesPike solver.
        PetesPikeSolver petesPikeSolver = new PetesPikeSolver(petesPike);

        // Implement BackTracker
        Backtracker<PetesPikeSolver> backtracker = new Backtracker<>(false);

        // Save solution from BackTracker
        PetesPikeSolver solution = backtracker.solve(petesPikeSolver);
        // System.out.println(solution);
        if(solution == null){
            // if solution fails, print nope.
            System.out.println("Nope !!");
        }else{
            // if success, print solution
            System.out.println(solution);
        }
    }

    
}
