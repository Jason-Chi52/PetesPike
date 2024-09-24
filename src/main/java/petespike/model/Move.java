package petespike.model;

public class Move {
    private Position position;
    private Direction direction;


    /**
     * Constructs a new Move object with a specified position and direction.
     * 
     * @param position the starting position of the move
     * @param direction the direction in which the move is made
     */
    public Move(Position position, Direction direction) {
        this.position = position;
        this.direction = direction;
    }
    /**
     * Returns the position associated with this move.
     * 
     * @return the position of the move
     */
    public Position getPosition() {
        return this.position;
    }
    /**
     * Returns the direction of the move.
     * 
     * @return the direction in which the move was made
     */
    public Direction getDirection() {
        return this.direction;
    }

    /**
     * Returns a string representation of the move, combining both the position and direction.
     * Useful for debugging and logging moves.
     * 
     * @return a string representation of the move, formatted as "position direction"
     */

    public String toString(){
        return this.position + " " + this.direction;
    }
    
    /**
     * Compares this Move object to another object for equality. Two Move objects are considered equal
     * if they have the same position and direction.
     * 
     * @param obj the object to compare with this Move
     * @return true if the specified object is a Move and has the same position and direction, false otherwise
     */

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Move) {
            Move other = (Move)obj;
            return this.getPosition().equals(other.getPosition())&& this.getDirection() == other.getDirection();
        }else{
            return false;
        }
        
    }
}
