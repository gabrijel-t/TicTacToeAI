package tictactoe;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        boolean appRunning = true;
        boolean inGame = false;
        Scanner scan = new Scanner(System.in);
        while(appRunning){
            System.out.println("Input command: ");
            String input = scan.nextLine();
            String mode = "";
            String player1 = "";
            String player2 = "";

            if(input.split(" ").length == 1){
                mode = input.split(" ")[0];
            }
            if(input.split(" ").length == 3){
                String [] data = input.split(" ");
                mode = data[0];
                player1 = data[1];
                player2 = data[2];
            }

            boolean badInput= false;

            switch (mode){
                case "start":
                    inGame = true;
                    for(String player : new String[] {player1, player2}){
                        switch (player){
                            case "user": case "easy": case "medium": case "hard":
                                inGame = true;
                                break;
                            default:
                                badInput = true;
                                inGame = false;
                        }
                    }
                    break;
                case "exit":
                    appRunning = false;
                    badInput = false;
                    break;
                default:
                    badInput = true;
                    inGame = false;
            }
            if(badInput){
                System.out.println("Bad parameters! ");
                inGame = false;
            }

            String [][] gameBoard = initiateGameBoard();
            if(inGame){
                printGameBoard(gameBoard);
            }
            
            String result;
            
            while(inGame){
                switch (player1){
                    case "user":
                        //Ask the user coordinates
                        userMove(gameBoard, 1);
                        break;
                    case "easy":
                        //make the random easy move
                        easyMove(gameBoard, 1);
                        break;
                    case "medium":
                        //medium level move
                        mediumMove(gameBoard, 1);
                        break;
                    case "hard":
                        //difficult move
                        hardMove(gameBoard, 1);
                        break;
                }
                printGameBoard(gameBoard);
                
                result = checkWinner(gameBoard);
                if(result.contains("wins") || result.contains("Draw")){
                    inGame = false;
                    System.out.println(result);
                }else{
                    inGame = true;
                }
                
                if(!inGame){
                    break;
                }
                switch (player2){
                    case "user":
                        userMove(gameBoard, 2);
                        break;
                    case "easy":
                        easyMove(gameBoard, 2);
                        break;
                    case "medium":
                        //medium level move
                        mediumMove(gameBoard, 2);
                        break;
                    case "hard":
                        //difficult move
                        hardMove(gameBoard, 2);
                        break;
                }
                printGameBoard(gameBoard);

                if(result.contains("wins") || result.contains("Draw")){
                    inGame = false;
                    System.out.println(result);
                }else{
                    inGame = true;
                }
            }
        }
    }

    public static void printGameBoard(String [][] field){
        System.out.println("---------");
        for(String[] s: field){
            System.out.print("| ");
            for(String str: s){
                System.out.print(str+" ");
            }System.out.println("|");
        }
        System.out.println("---------");
    }

    public static String[][] initiateGameBoard(){
        String [][] gameBoard = new String[3][3];
        for(int row = 0; row < 3; row++){
            for(int col = 0; col < 3; col++){
                gameBoard[row][col] = " ";
            }
        }
        return gameBoard;
    }

    public static void userMove(String[][] gameBoard, int player){
        boolean validMove = false;
        Scanner scan = new Scanner(System.in);
        do {
            System.out.println("Enter the coordinates: ");
            while (!scan.hasNextInt()) {
                System.out.println("You should enter numbers! ");
                scan.next();
            }

            int x = scan.nextInt();
            int y = scan.nextInt();
            if (x > 0 && x <= 3 && y > 0 && y <= 3) {

                if (gameBoard[x - 1][y - 1].equals(" ")) {
                    if(player == 1) {
                        gameBoard[x - 1][y - 1] = "X";
                        validMove = true;
                    }else if(player == 2){
                        gameBoard[x - 1][y - 1] = "O";
                        validMove = true;
                    }
                } else {
                    System.out.println("This cell is occupied! Choose another one! ");
                }
            } else {
                System.out.println("Coordinates should be from 1 to 3!");
            }

        } while (!validMove);
    }

    public static void easyMove(String[][] gameBoard, int player){
        System.out.println("Making move level \"easy\"");
        Random random = new Random();
        int choice;
        do{
            choice = random.nextInt(9);
        }while(!gameBoard[choice/3][choice%3].equals(" "));

        if(player == 1){
            gameBoard[choice/3][choice%3] = "X";
        }else if(player == 2){
            gameBoard[choice/3][choice%3] = "O";
        }
    }

    public static void mediumMove(String [][] g, int player){
        System.out.println("Making move level \"medium\"");
        boolean choiceMade = false;
        String symbol;
        String enemySymbol;
        int [] coordinate = new int[2];

        if(player == 1){
            symbol = "X";
            enemySymbol = "O";
        }else{
            symbol = "O";
            enemySymbol = "X";
        }

        int [] bestMove = seekWin(g, symbol);
        if(bestMove[0] != -1){
            choiceMade = true;
            coordinate = bestMove;
        }

        if(!choiceMade){
            bestMove = seekWin(g, enemySymbol);
            if(bestMove[0] != -1){
                choiceMade = true;
                coordinate = bestMove;
            }
        }

        if(!choiceMade){
            Random random = new Random();
            int choice;
            do{
                choice = random.nextInt(9);
            }while(!g[choice/3][choice%3].equals(" "));

            g[choice/3][choice%3] = symbol;

        }else{
            g[coordinate[0]][coordinate[1]] = symbol;
        }
    }

    public static void hardMove(String [][] gameBoard, int player){
        System.out.println("Making move level \"hard\"");

        String symbol = "";
        if(player == 1){
            symbol = "X";
        }else if(player == 2){
            symbol = "O";
        }

        Move bestMove = minimax(gameBoard, player, player);

        gameBoard[bestMove.index[0]][bestMove.index[1]] = symbol;
    }

    public static int [] seekWin(String[][] g, String symbol){
        int row = -1;
        int col = -1;

        //Control of the diagonals
        if(g[0][0].equals(g[2][2]) && g[1][1].equals(" ") || g[2][0].equals(g[0][2]) && g[1][1].equals(" ")){
            row = 1;
            col = 1;
        }else if(g[1][1].equals(symbol)){
            if(g[0][0].equals(symbol) && g[2][2].equals(" ")){
                row = 2;
                col = 2;
            }else if(g[2][2].equals(symbol) && g[0][0].equals(" ")){
                row = 0;
                col = 0;
            }else if(g[2][0].equals(symbol) && g[0][2].equals(" ")){
                row = 0;
                col = 2;
            }else if(g[0][2].equals(symbol) && g[2][0].equals(" ")){
                row = 2;
                col = 0;
            }
        }

        //control of the other cells
        for(int i = 0; i< 3; i++){
            if(g[i][0].equals(symbol) && g[i][1].equals(symbol) && g[i][2].equals(" ")){
                row = i;
                col = 2;
            }else if(g[i][1].equals(symbol) && g[i][2].equals(symbol) && g[i][0].equals(" ")){
                row = i;
                col = 0;
            }else if(g[i][2].equals(symbol) && g[i][0].equals(symbol) && g[i][1].equals(" ")){
                row = i;
                col = 1;
            }else if(g[0][i].equals(symbol) && g[1][i].equals(symbol) && g[2][i].equals(" ")){
                row = 2;
                col = i;
            }else if(g[1][i].equals(symbol) && g[2][i].equals(symbol) && g[0][i].equals(" ")){
                row = 0;
                col = i;
            }else if(g[2][i].equals(symbol) && g[0][i].equals(symbol) && g[1][i].equals(" ")){
                row = 1;
                col = i;
            }
        }
        return new int[] {row, col};
    }

    public static String checkWinner(String[][] gameBoard){
        String out = "";
        boolean win = false;
        int empty = 0;
        for(String [] str : gameBoard){
            for(String s : str){
                if(s.equals(" ")){
                    empty++;
                }
            }
        }
        for(int row =0; row < 3; row++){
            if(gameBoard[row][0].equals(gameBoard[row][1]) && gameBoard[row][0].equals(gameBoard[row][2]) && (gameBoard[row][0].equals("O") || gameBoard[row][0].equals("X"))){
                out = gameBoard[row][0] + " wins";
                win = true;
            }
            if(gameBoard[0][row].equals(gameBoard[1][row]) && gameBoard[0][row].equals(gameBoard[2][row]) && (gameBoard[0][row].equals("O") || gameBoard[0][row].equals("X"))){
                out = gameBoard[0][row] + " wins";
                win = true;
            }
        }
        if(win){
            return out;
        }
        if(gameBoard[0][0].equals(gameBoard[1][1]) && gameBoard[0][0].equals(gameBoard[2][2]) && (gameBoard[0][0].equals("O") || gameBoard[0][0].equals("X"))){
            out = gameBoard[0][0] + " wins";
            win = true;
        }else if(gameBoard[0][2].equals(gameBoard[1][1]) && gameBoard[0][2].equals(gameBoard[2][0]) && (gameBoard[2][0].equals("O") || gameBoard[2][0].equals("X"))){
            out = gameBoard[0][2] + " wins";
            win = true;
        }
        if(win){
            return out;
        }
        if(empty >0){
            out = "Game not finished!";
        }else{
            out = "Draw";
        }
        return out;
    }

    public static boolean winningConditions(String[][] g, char p){
        String player = String.valueOf(p);
        return (g[0][0].equals(player) && g[0][1].equals(player) && g[0][2].equals(player) ||
                g[1][0].equals(player) && g[1][1].equals(player) && g[1][2].equals(player) ||
                g[2][0].equals(player) && g[2][1].equals(player) && g[2][2].equals(player) ||
                g[0][0].equals(player) && g[1][0].equals(player) && g[2][0].equals(player) ||
                g[0][1].equals(player) && g[1][1].equals(player) && g[2][1].equals(player) ||
                g[0][2].equals(player) && g[1][2].equals(player) && g[2][2].equals(player) ||
                g[0][0].equals(player) && g[1][1].equals(player) && g[2][2].equals(player) ||
                g[0][2].equals(player) && g[1][1].equals(player) && g[2][0].equals(player));
    }

    /*
    Implement the minimax algorithm described on the free code camp
    https://www.freecodecamp.org/news/how-to-make-your-tic-tac-toe-game-unbeatable-by-using-the-minimax-algorithm-9d690bad4b37/
     */

    public static Move minimax(String [][] gameBoard, int callingPlayer, int currentPlayer){
        char enemySymbol = 0, callingSymbol = 0;
        if(callingPlayer == 1){
            callingSymbol = 'X';
            enemySymbol = 'O';
        }else if(callingPlayer == 2){
            callingSymbol = 'O';
            enemySymbol = 'X';
        }

        char symbol = 0;
        int enemyNumber = 0;
        if(currentPlayer == 1){
            symbol = 'X';
            enemyNumber = 2;
        }else if(currentPlayer == 2){
            symbol = 'O';
            enemyNumber = 1;
        }

        int [][] availableSpots = emptyIndexes(gameBoard);
        if(winningConditions(gameBoard, enemySymbol)){
            return new Move(-10);
        }else if(winningConditions(gameBoard, callingSymbol)){
            return new Move(10);
        }else if(!areEmptyIndexes(gameBoard)){
            return new Move(0);
        }

        List <Move> moves = new ArrayList<>();

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(availableSpots[i][j] == 1){
                    Move move = new Move();
                    move.index = new int[]{i, j};
                    gameBoard[i][j] = String.valueOf(symbol);
                    Move result = minimax(gameBoard, callingPlayer, enemyNumber);
                    move.score = result.score;
                    gameBoard[i][j] = " ";
                    moves.add(move);
                }
            }
        }

        int bestMove = 0;

        if(callingPlayer == currentPlayer){
            int bestScore = Integer.MIN_VALUE;
            for(int i = 0; i< moves.size(); i++){
                if(moves.get(i).score > bestScore){
                    bestScore = moves.get(i).score;
                    bestMove = i;
                }
            }
        }else {
            int bestScore = Integer.MAX_VALUE;
            for(int i = 0; i< moves.size(); i++){
                if(moves.get(i).score < bestScore){
                    bestScore = moves.get(i).score;
                    bestMove = i;
                }
            }
        }
        return moves.get(bestMove);
    }

    public static int[][] emptyIndexes(String [][] gameBoard){
        int [][] g = new int[gameBoard.length][gameBoard.length];
        for(int i = 0; i< gameBoard.length; i++){
            for(int j = 0; j< gameBoard.length; j++){
                if(gameBoard[i][j].equals(" ")){
                    g[i][j] = 1;
                }else{
                    g[i][j] = 0;
                }
            }
        }
        return g;
    }

    public static boolean areEmptyIndexes(String[][] gameBoard){
        boolean empty = false;
        for(String[] str: gameBoard){
            for(String s: str){
                if(s.equals(" ")){
                    empty = true;
                    break;
                }
            }
        }
        return empty;
    }
}
class Move{

    int score;
    int [] index;

    public Move(){

    }

    public Move(int score){
        this.score = score;
    }
}
