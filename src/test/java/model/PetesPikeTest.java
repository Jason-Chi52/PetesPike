package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import petespike.model.Direction;
import petespike.model.Move;
import petespike.model.PetesPike;
import petespike.model.PetesPikeException;
import petespike.model.Position;

public class PetesPikeTest {
    @Test
    public void position1() throws PetesPikeException, IOException {
    


        PetesPike petesPike = new PetesPike("data/petes_pike_5_5_4_1.txt");
        Position position = new Position(0, 1);
        
        Move move = new Move(position, Direction.DOWN);

        Position newPosition = new Position(2,1);
        Move newmove = new Move(newPosition, Direction.DOWN);
        petesPike.makeMove(move);
        int rowExpect = 2;
        int colExpect = 1;
    
        assertEquals(rowExpect, newPosition.getRow());
        assertEquals(colExpect, newPosition.getCol());
        assertEquals(position.equals(newPosition), false);
        assertEquals(move.equals(newmove), false);

    }    



    @Test
    public void test_1() {
        
        Position position = new Position(1, 2);
        Direction direction = Direction.DOWN;

        Move move = new Move(position, direction);

        int rowExpect = 1;
        int colExpect = 2;
        Direction expectDirection = Direction.DOWN;

        assertEquals(rowExpect, move.getPosition().getRow());
        assertEquals(colExpect, move.getPosition().getCol());
        assertEquals(expectDirection, move.getDirection());
    }    

    @Test
    public void test_2() throws PetesPikeException {
        
        String filename = "data/petes_pike_5_5_4_0.txt" ;
        PetesPike petesPike;
        try {
            petesPike = new PetesPike(filename);
            List<Move> movelist = petesPike.getPossibleMoves();
            List<Move> expectMovelist = new ArrayList<>();
            Move move1 = new Move(new Position(3,2 ), Direction.UP);
            Move move2 = new Move(new Position(0,2 ), Direction.DOWN);
            expectMovelist.add(move1);
            expectMovelist.add(move2);
            
            petesPike.makeMove(move2); //test make move
            char symbol = petesPike.getSymbolAt(new Position(2,2 ));
            char expectSymbol = '1';

            petesPike.reset(petesPike); //test reset and get mountaintop
            Position mountainTop = petesPike.getMountainTop();
            Position expectMT = new Position(2, 2);

            assertEquals(expectMovelist, movelist);
            assertEquals(symbol, expectSymbol);
            assertEquals(mountainTop, expectMT);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        

        
    }   
}
