import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

public class TicTacToeTests {
    StartGamePlay ticTacToeGame;
    String trumpLvl ;
    String bidenLvl ;

    @Test
    void gameDoesNotEnds_1Test() throws ExecutionException, InterruptedException {
        trumpLvl = "Expert";
        bidenLvl = "Expert";
        ticTacToeGame = new StartGamePlay(trumpLvl, bidenLvl);
        assertEquals( false, ticTacToeGame.gameDoesNotEnd(),"game ends without actual completion.");
    }
    @Test
    void gameDoesNotEnds_2Test() throws ExecutionException, InterruptedException {
        trumpLvl = "Novice";
        bidenLvl = "Expert";
        ticTacToeGame = new StartGamePlay(trumpLvl, bidenLvl);
        assertEquals(false,ticTacToeGame.gameDoesNotEnd(),  "game ends without actual completion.");
    }

    @Test
    void winner_1Test() throws ExecutionException, InterruptedException {
        trumpLvl = "Expert";
        bidenLvl = "Expert";
        ticTacToeGame = new StartGamePlay(trumpLvl, bidenLvl);
        assertEquals("tie", ticTacToeGame.getWinner(), "game result incorrect");
    }

    @Test
    void winner_2Test() throws ExecutionException, InterruptedException {
        trumpLvl = "Novice";
        bidenLvl = "Expert";
        ticTacToeGame = new StartGamePlay(trumpLvl, bidenLvl);
        assertEquals("biden", ticTacToeGame.getWinner(), "game result incorrect");
    }

    @Test
    void getBoardTest() throws ExecutionException, InterruptedException {
        trumpLvl = "Advanced";
        bidenLvl = "Expert";
        ticTacToeGame = new StartGamePlay(trumpLvl, bidenLvl);
        assertNotNull(ticTacToeGame.getGameBoard(), "Board should not be null");
    }

}
