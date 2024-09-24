package petespike.model;

public interface PetesPikeObserver {
        /**
     * This method is called to notify the observer that the board has changed.
     * It provides the details of the move that was made, including the starting position and the direction of the move.
     *
     * @param from The position from which a move was initiated.
     * @param to The direction in which the move was made.
     */

    public void boardChanged(Position from , Direction to);
    
}
    

