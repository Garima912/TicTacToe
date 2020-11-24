import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.*;


// This class takes in the play levels of both the players. Each player then calls generateMoves on separate
// threads to request the best possible move to move ahead in the game.
public class StartGamePlay {
    private String initialBoardState;
    private ArrayList<String> gameBoard;
    private ArrayList<Node> trumpExpertMoves;
    private ArrayList<Node> bidenExpertMoves;
    private String trumpPlayLevel;
    private String bidenPlayLevel;
    private int trump = 1;
    private int biden = 0;
    private String winner = "";

    public String getWinner() {
        return winner;
    }

    public StartGamePlay(String trumpPlayLevel, String bidenPlayLevel) throws ExecutionException, InterruptedException {
        this.trumpPlayLevel = trumpPlayLevel;
        this.bidenPlayLevel = bidenPlayLevel;
        initialBoardState = "b b b b b b b b b";
        gameBoard = new ArrayList<>();
        trumpExpertMoves = new ArrayList<>();
        bidenExpertMoves = new ArrayList<>();
        gameBoard.add(initialBoardState);
        gameBegins();
        trumpExpertMoves.clear();
        bidenExpertMoves.clear();
    }

    public ArrayList<String> getGameBoard() {
        return gameBoard;
    }

    public void gameBegins() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future<ArrayList<Node>> trumpResults;
        Future<ArrayList<Node>> bidenResults;

        // player Trump is X and is also denoted as 1
        trumpResults = executorService.submit(new GenerateMoves(initialBoardState, trump));
        trumpExpertMoves = trumpResults.get();  //blocking call
        initialBoardState = pickAMove(trump);
        System.out.println(initialBoardState);
        gameBoard.add(initialBoardState);

        // player Biden is O and is also denoted as 0
        bidenResults = executorService.submit(new GenerateMoves(initialBoardState, biden));
        bidenExpertMoves = bidenResults.get();  // blocking call
        initialBoardState = pickAMove(biden);
        System.out.println(initialBoardState);
        gameBoard.add(initialBoardState);

        while(gameDoesNotEnd()) {  // keep the game going until there is a tie or someone wins
            // player Trump is X and is also denoted as 1
            if(bidenResults.isDone() && !checkForWinner("O")) {
                trumpResults = executorService.submit(new GenerateMoves(initialBoardState, trump));
                trumpExpertMoves = trumpResults.get();  //blocking call
                initialBoardState = pickAMove(trump);
                System.out.println(initialBoardState);
                gameBoard.add(initialBoardState);
            }

            // player Biden is O and is also denoted as 0
            if(trumpResults.isDone() && !checkForWinner("X")) {
                bidenResults = executorService.submit(new GenerateMoves(initialBoardState, biden));
                bidenExpertMoves = bidenResults.get();  // blocking call
                if(bidenExpertMoves.isEmpty()){
                    System.out.println("game ends");
                    winner = "tie";
                    break;
                }
                initialBoardState = pickAMove(biden);
                System.out.println(initialBoardState);
                gameBoard.add(initialBoardState);
            }

        }
        executorService.shutdown();

    }
    public Boolean checkForWinner(String move){

        String[] currentBoard = gameBoard.get(gameBoard.size()-1).split(" ");

        if((currentBoard[0].equals(move) && currentBoard[1].equals(move)  && currentBoard[2].equals(move)) )//horizontal top
        {
            if(move == "X"){
                winner = "trump";
            }else{
                winner = "biden";
            }
            return true;
        }

        if((currentBoard[3].equals(move) && currentBoard[4].equals(move) && currentBoard[5].equals(move)))//horizontal middle
        {
            if(move == "X"){
                winner = "trump";
            }else{
                winner = "biden";
            }
            return true;
        }

        if((currentBoard[6].equals(move) && currentBoard[7].equals(move) && currentBoard[8].equals(move)) )//horizontal bottom
        {
            if(move == "X"){
                winner = "trump";
            }else{
                winner = "biden";
            }
            return true;
        }

        if((currentBoard[0].equals(move) && currentBoard[3].equals(move) && currentBoard[6].equals(move)) )//vert right
        {
            if(move == "X"){
                winner = "trump";
            }else{
                winner = "biden";
            }
            return true;
        }

        if((currentBoard[1].equals(move) && currentBoard[4].equals(move) && currentBoard[7].equals(move)))//vert middle
        {
            if(move == "X"){
                winner = "trump";
            }else{
                winner = "biden";
            }
            return true;
        }

        if((currentBoard[2].equals(move) && currentBoard[5].equals(move) && currentBoard[8].equals(move)))//vert left
        {
            if(move == "X"){
                winner = "trump";
            }else{
                winner = "biden";
            }
            return true;
        }

        if((currentBoard[0].equals(move) && currentBoard[4].equals(move) && currentBoard[8].equals(move)) )// diag from top left
        {
            if(move == "X"){
                winner = "trump";
            }else{
                winner = "biden";
            }
            return true;
        }

        if((currentBoard[2].equals(move) && currentBoard[4].equals(move) && currentBoard[6].equals(move)) )// diag from top right
        {
            if(move == "X"){
                winner = "trump";
            }else{
                winner = "biden";
            }
            return true;
        }

        return false;  // no winner till now in the game
    }

    public Boolean gameDoesNotEnd(){

        if(bidenExpertMoves.isEmpty() || checkForWinner("X") || checkForWinner("O")){
            // when a game results in tie, Trump is the last to take any move
            System.out.println("game ends");
            return false;
        }
        System.out.println("game continues");
        return true;  // game continues

    }

    public String pickAMove(int player){
        Random pickRandomBestMove = new Random();
        ArrayList<Node> trumpWinningMoveNodes = new ArrayList<>();
        ArrayList<Node> trumpTieMoveNodes =  new ArrayList<>();
        ArrayList<Node> bidenWinningMoveNodes = new ArrayList<>();
        ArrayList<Node> bidenTieMoveNodes =  new ArrayList<>();
        String moveString = "";

        if(player == trump){
            Node trumpMoveNode;
            for( Node move: trumpExpertMoves){
                if(move.getMinMax() == 10){
                    trumpWinningMoveNodes.add(move);
                }
                if(move.getMinMax() == 0){
                    trumpTieMoveNodes.add(move);
                }
            }
            if(trumpPlayLevel == "Expert") {
                if (trumpWinningMoveNodes.isEmpty()) {
                    trumpMoveNode = trumpTieMoveNodes.get(pickRandomBestMove.nextInt(trumpTieMoveNodes.size()));
                }

                else {
                    trumpMoveNode = trumpWinningMoveNodes.get(pickRandomBestMove.nextInt(trumpWinningMoveNodes.size()));
                }
                moveString = getBoardString(trumpMoveNode.getInitStateString(), " ");
            }
            else if(trumpPlayLevel == "Advanced"){
                trumpMoveNode = trumpExpertMoves.get(pickRandomBestMove.nextInt(trumpExpertMoves.size()));
                moveString = getBoardString(trumpMoveNode.getInitStateString(), " ");
            }
            else if(trumpPlayLevel == "Novice"){
                trumpMoveNode = trumpExpertMoves.get(0);
                moveString = getBoardString(trumpMoveNode.getInitStateString(), " ");
            }

        }
        else if(player == biden){
            Node bidenMoveNode;
            for( Node move: bidenExpertMoves){
                if(move.getMinMax() == -10){
                    bidenWinningMoveNodes.add(move);
                }
                if(move.getMinMax() == 0){
                    bidenTieMoveNodes.add(move);
                }
            }
            if(bidenPlayLevel == "Expert") {
                if (bidenWinningMoveNodes.isEmpty()) {
                    bidenMoveNode = bidenTieMoveNodes.get(pickRandomBestMove.nextInt(bidenTieMoveNodes.size()));
                }

                else {
                    bidenMoveNode = bidenWinningMoveNodes.get(pickRandomBestMove.nextInt(bidenWinningMoveNodes.size()));
                }
                moveString = getBoardString(bidenMoveNode.getInitStateString(), " ");
            }
            else if(bidenPlayLevel == "Advanced"){
                bidenMoveNode = bidenExpertMoves.get(pickRandomBestMove.nextInt(bidenExpertMoves.size()));
                moveString = getBoardString(bidenMoveNode.getInitStateString(), " ");
            }
            else if(bidenPlayLevel == "Novice"){
                bidenMoveNode = bidenExpertMoves.get(0);
                moveString = getBoardString(bidenMoveNode.getInitStateString(), " ");
            }
        }

        return  moveString;
    }

    private static String getBoardString(String[] Arr, String delimiter) {
        StringBuilder  boardString = new StringBuilder();
        for (String x : Arr)
            boardString.append(x).append(delimiter);
        return boardString.substring(0, boardString.length() - 1);
    }

}  // StartGamePlay() ends

