import java.time.LocalTime;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        char[][] board1 = {
                {'5','3','.','.','7','.','.','.','.'},
                {'6','.','.','1','9','5','.','.','.'},
                {'.','9','8','.','.','.','.','6','.'},
                {'8','.','.','.','6','.','.','.','3'},
                {'4','.','.','8','.','3','.','.','1'},
                {'7','.','.','.','2','.','.','.','6'},
                {'.','6','.','.','.','.','2','8','.'},
                {'.','.','.','4','1','9','.','.','5'},
                {'.','.','.','.','8','.','.','7','9'}
        };

        char[][] board2 = {
                {'.','.','9','7','4','8','.','.','.'},
                {'7','.','.','.','.','.','.','.','.'},
                {'.','2','.','1','.','9','.','.','.'},
                {'.','.','7','.','.','.','2','4','.'},
                {'.','6','4','.','1','.','5','9','.'},
                {'.','9','8','.','.','.','3','.','.'},
                {'.','.','.','8','.','3','.','2','.'},
                {'.','.','.','.','.','.','.','.','6'},
                {'.','.','.','2','7','5','9','.','.'}
        };

        char[][] board3 = {
                {'.','.','.','2','.','.','.','6','3'},
                {'3','.','.','.','.','5','4','.','1'},
                {'.','.','1','.','.','3','9','8','.'},
                {'.','.','.','.','.','.','.','9','.'},
                {'.','.','.','5','3','8','.','.','.'},
                {'.','3','.','.','.','.','.','.','.'},
                {'.','2','6','3','.','.','5','.','.'},
                {'5','.','3','7','.','.','.','.','8'},
                {'4','7','.','.','.','1','.','.','.'}
        };

        System.out.println("__________________________________________");

        System.out.println("*Easy task is set ...");
        setTask(board1);

        System.out.println("*Medium task is set ...");
        setTask(board2);

        System.out.println("*Hard task is set ...");
        setTask(board3);



        System.out.println("Finished.");

        System.out.println();

    }


    private static void setTask(char[][] selectedBoard){

        long startTime = System.currentTimeMillis();
        Solution newSolution = new Solution();
        newSolution.solveSudoku(selectedBoard);
        //System.out.println();
        System.out.println();
        for (int i=0; i<9;i++){
            System.out.print("   ");
            for (int j=0;j<9; j++){
                System.out.print(" ");
                System.out.print(selectedBoard[i][j]);
                System.out.print(" ");
            }
            System.out.println();
        }
        System.out.println();

        long endTime = System.currentTimeMillis();
        System.out.print((endTime - startTime)/1000);
        System.out.print(".");
        int durationFraction = (int)(endTime - startTime) % 1000;
        Integer millisInt=(Integer)durationFraction;
        String millis=millisInt.toString();
        while (millis.length()<3) millis="0"+millis;
        System.out.print(millis);
        System.out.println(" seconds");
        System.out.println();
    }





}