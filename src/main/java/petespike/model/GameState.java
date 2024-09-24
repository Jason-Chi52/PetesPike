package petespike.model;
/**
 * The {@code GameState} enum represents the different states that the Pete's Pike game can be in at any given time.
 * This enum helps in managing the flow of the game and responding appropriately to different game conditions.
 */

public enum GameState {
    NEW,
    IN_PORGRESS,
    NO_MOVES,
    WON;
}
