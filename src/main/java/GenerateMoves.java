import java.util.ArrayList;
import java.util.concurrent.Callable;

//This class implements callabale and returns the moves List  for the board returned by the AI_MinMax class
public class GenerateMoves implements Callable<ArrayList<Node>> {

    String currentBoard;  // string of the current game board
    int player;  // player playing the move

    public GenerateMoves(String currentBoard, int player) {
        this.currentBoard = currentBoard;
        this.player = player;
    }

    @Override
    public ArrayList<Node> call() throws Exception {

        AI_MinMax algorithm = new AI_MinMax(currentBoard, player);
        return algorithm.getMovesList();   // returns the list containing multiple moves
    }
}
