package petespike.model;

public class Position {
    private int row;
    private int col;


     /**
     * Constructs a {@code Position} object with specified row and column.
     *
     * @param row the row coordinate of the position
     * @param col the column coordinate of the position
     */
    public Position(int row, int col){
        this.col = col;
        this.row = row;
    }


    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    @Override
    public String toString() {
        return "(" + row + ", " + col + ")";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + row;
        result = prime * result + col;
        return result;
    }
    /**
     * Compares this position with the specified object for equality. Two positions
     * are considered equal if and only if both the row and column of one are equal
     * to the row and column of the other.
     *
     * 
     * @return true if the given object represents a {@code Position} equivalent to this position, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Position) {
            Position other = (Position)obj;
            return this.getCol() == other.getCol() && this.getRow() == other.getRow();
        }else{
            return false;
        }
        
    }

    
    
    
    
}
