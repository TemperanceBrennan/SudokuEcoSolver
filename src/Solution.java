
import java.util.*;

public class Solution {

    static HashSet<Character> AllowedCharAll = new HashSet<Character>();


    public class SudokuCell {

        char CellChar = '.';
        int X = -1;
        int Y = -1;
        HashSet<Character> AllowedChars = new HashSet<Character>();
        // Horizontal rivals
        HashSet<SudokuCell> HRivals = new HashSet<SudokuCell>();
        // Vertical rivals
        HashSet<SudokuCell> VRivals = new HashSet<SudokuCell>();
        // Box 3x3 rivals
        HashSet<SudokuCell> BRivals = new HashSet<SudokuCell>();

        public SudokuCell(char cellChar) {
            this.CellChar = cellChar;
            AllowedCharAll.add('1');
            AllowedCharAll.add('2');
            AllowedCharAll.add('3');
            AllowedCharAll.add('4');
            AllowedCharAll.add('5');
            AllowedCharAll.add('6');
            AllowedCharAll.add('7');
            AllowedCharAll.add('8');
            AllowedCharAll.add('9');
            if (cellChar == '.') {
                for (char character : AllowedCharAll) {
                    this.AllowedChars.add(character);
                }
            } else {
                this.AllowedChars.add(cellChar);
            }
        }

        private void addHRival(SudokuCell cellChar) {
            this.HRivals.add(cellChar);
        }

        private void addVRival(SudokuCell cellChar) {
            this.VRivals.add(cellChar);
        }

        private void addBRival(SudokuCell cellChar) {
            this.BRivals.add(cellChar);
        }

        private void removeOption(char removedOption) {
            this.AllowedChars.remove(removedOption);
        }

        private boolean straighten() {
            boolean changed = false;
            if (this.AllowedChars.size() == 1) {
                for (char ch : this.AllowedChars)
                    this.CellChar = ch;
                changed = true;
                this.purgeDirectly();
            }
            return changed;
        }


        private boolean tryCompareAndPurge(SudokuCell cell, SudokuCell rival) {
            boolean changed = false;
            if (rival.AllowedChars.contains(cell.CellChar)) {
                rival.removeOption(cell.CellChar);
                changed = true;
                rival.straighten();
            }
            return changed;
        }


        private boolean purgeDirectly() {
            boolean changed = false;
            for (SudokuCell rivalCell : this.HRivals) {
                changed = changed | tryCompareAndPurge(this, rivalCell);
                changed = changed | tryCompareAndPurge(rivalCell, this);
            }
            for (SudokuCell rivalCell : this.VRivals) {
                changed = changed | tryCompareAndPurge(this, rivalCell);
                changed = changed | tryCompareAndPurge(rivalCell, this);
            }
            for (SudokuCell rivalCell : this.BRivals) {
                changed = changed | tryCompareAndPurge(this, rivalCell);
                changed = changed | tryCompareAndPurge(rivalCell, this);
            }
            return changed;

        }

    }

    public class SudokuBoard {

        private SudokuCell[][] Cells = new SudokuCell[9][9];
        private SudokuLine[] HLines = new SudokuLine[9];
        private SudokuLine[] VLines = new SudokuLine[9];
        private SudokuLine[] BLines = new SudokuLine[9];

        private SudokuBoard(char[][] board) {

            for (int x = 0; x < 9; x++)
                for (int y = 0; y < 9; y++) {
                    SudokuCell newCell = new SudokuCell(board[x][y]);
                    this.Cells[x][y] = newCell;
                    this.Cells[x][y].X = x;
                    this.Cells[x][y].Y = y;
                }

            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    for (int k = 0; k < 9; k++) {
                        if (k != i)
                            this.Cells[i][j].addHRival(this.Cells[k][j]);
                        if (k != j)
                            this.Cells[i][j].addHRival(this.Cells[i][k]);
                    }
                }
            }

            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    for (int m = (i / 3) * 3; m < (i / 3 + 1) * 3; m++) {
                        for (int n = (j / 3) * 3; n < (j / 3 + 1) * 3; n++) {
                            if (i != m & j != n)
                                this.Cells[i][j].addHRival(this.Cells[m][n]);
                        }
                    }
                }
            }

            for (int x = 0; x < 9; x++) {
                SudokuLine hLine = new SudokuLine();
                this.HLines[x] = hLine;
                for (int y = 0; y < 9; y++) {
                    this.HLines[x].addCell(Cells[x][y]);
                }
            }

            for (int y = 0; y < 9; y++) {
                SudokuLine vLine = new SudokuLine();
                this.VLines[y] = vLine;
                for (int x = 0; x < 9; x++) {
                    this.HLines[y].addCell(Cells[x][y]);
                }
            }

            for (int x = 0; x < 9; x = x + 3)
                for (int y = 0; y < 9; y = y + 3) {

                    for (int i = 0; i < 3; i++) {
                        SudokuLine bLine = new SudokuLine();
                        this.BLines[x + i] = bLine;
                        for (int j = 0; j < 3; j++) {
                            this.BLines[x + i].addCell(Cells[x + i][y + j]);
                        }
                    }
                }


        }

        private void boardOut(char[][] board) {
            for (int i = 0; i < 9; i++)
                for (int j = 0; j < 9; j++)
                    board[i][j] = this.Cells[i][j].CellChar;

        }

        private boolean doDirectPurge() {
            boolean changed = false;
            for (int x = 0; x < 9; x++)
                for (int y = 0; y < 9; y++)
                    changed = changed | this.Cells[x][y].purgeDirectly();
            return changed;

        }


        private boolean doBruteForceOne() {
            boolean changed = false;
            for (int x = 0; x < 9; x++)
                for (int y = 0; y < 9; y++) {
                    if (this.Cells[x][y].AllowedChars.size() > 1)
                        for (char ch : this.Cells[x][y].AllowedChars) {
                            char[][] newBoard = new char[9][9];
                            this.boardOut(newBoard);
                            newBoard[x][y] = ch;
                            SudokuBoard newSudokuBoard = new SudokuBoard(newBoard);
                            boolean innerChanged = true;
                            while (innerChanged) {
                                innerChanged = newSudokuBoard.doDirectPurge();
                            }
                            if (newSudokuBoard.isSolved()) {
                                this.Cells = newSudokuBoard.Cells;
                                changed = true;
                            }

                        }

                }
            return changed;
        }

        private boolean doRecurrentBruteForce(int maxDepth) {
            if (maxDepth == 0) return false;
            boolean changed = false;
            for (int x = 0; x < 9; x++)
                for (int y = 0; y < 9; y++) {
                    if (this.Cells[x][y].AllowedChars.size() > 1)
                        for (char ch : this.Cells[x][y].AllowedChars) {
                            char[][] newBoard = new char[9][9];
                            this.boardOut(newBoard);
                            newBoard[x][y] = ch;
                            SudokuBoard newSudokuBoard = new SudokuBoard(newBoard);
                            boolean innerChanged = true;
                            while (innerChanged) {
                                innerChanged = newSudokuBoard.doDirectPurge();
                                innerChanged = innerChanged |
                                        newSudokuBoard.doRecurrentBruteForce(maxDepth - 1);
                            }
                            if (newSudokuBoard.isSolved()) {
                                this.Cells = newSudokuBoard.Cells;
                                changed = true;
                            }

                        }

                }
            return changed;
        }


        private boolean isSolved() {
            boolean solved = true;
            for (int x = 0; x < 9; x++)
                for (int y = 0; y < 9; y++)
                    if (this.Cells[x][y].CellChar == '.')
                        solved = false;
            if (solved) {
                for (int i = 0; i < 9; i++) {
                    solved = solved & HLines[i].isCorrectLine();
                    solved = solved & VLines[i].isCorrectLine();
                    solved = solved & BLines[i].isCorrectLine();
                    if (!solved) return false;
                }

            }
            return solved;
        }


        private boolean doLineBreak() {
            boolean changed = false;
            /// TODO
            return changed;
        }


        private void solveIt() {
            boolean changed = true;
            while (changed) {
                changed = this.doDirectPurge();
                changed = changed | this.doBruteForceOne();
            }
            int upToDepth = 1;
            while (!this.isSolved() & upToDepth < 3) {
                do {
                    changed = changed | this.doRecurrentBruteForce(upToDepth);
                    changed = this.doDirectPurge();
                } while (changed);
                upToDepth++;
            }

        }

        public ArrayList<SudokuCell> recurrentBreakLine(ArrayList<SudokuCell> variedCells) {
            ArrayList<SudokuCell> newFilling = new ArrayList<SudokuCell>();
            int solutions = 0;
            if (variedCells.size() == 0) return newFilling;
            char[][] board = new char[9][9];
            this.boardOut(board);
            for (SudokuCell cell : variedCells) {
                SudokuCell newCell = new SudokuCell(cell.CellChar);
                newCell.X = cell.X;
                newCell.Y = cell.Y;
                newCell.AllowedChars = new HashSet<Character>();
                for (Character allowedCharacter : cell.AllowedChars)
                    newCell.AllowedChars.add(allowedCharacter);
                newFilling.add(newCell);
            }
            variedCells.remove(0);
            for (Character ch : newFilling.get(0).AllowedChars) {
                SudokuCell tempCell = new SudokuCell(ch);
                tempCell.X = newFilling.get(0).X;
                tempCell.Y = newFilling.get(0).Y;
                tempCell.AllowedChars = new HashSet<Character>();
                tempCell.AllowedChars.add(ch);
                tempCell.CellChar = ch;
                SudokuBoard tempBoard = new SudokuBoard(board);
                tempBoard.Cells[tempCell.X][tempCell.Y] = tempCell;
                if (variedCells.size() == 0) {
                    if (tempBoard.isValid()) {
                        solutions++;
                        newFilling = variedCells;
                    }

                } else {
                    newFilling = recurrentBreakLine(variedCells);
                }

            }

            /// TODO

            if (solutions != 1) return null;
            return newFilling;
        }

        private boolean isValid() {
            boolean correct = true;
            for (int i = 0; i < 9; i++) {
                correct = correct & HLines[i].isCorrectLine();
                correct = correct & VLines[i].isCorrectLine();
                correct = correct & BLines[i].isCorrectLine();
                if (!correct) return false;
            }


            return correct;
        }


    }

    public class SudokuLine {

        HashSet<SudokuCell> Cells = new HashSet<SudokuCell>();

        private void addCell(SudokuCell cell) {
            this.Cells.add(cell);
        }

        private boolean isCorrectLine() {
            boolean correct = true;
            HashSet<Character> metChars = new HashSet<Character>();
            for (SudokuCell cell : this.Cells)
                if (cell.CellChar != '.')
                    if (metChars.contains(cell.CellChar)) {
                        return false;
                    } else {
                        metChars.add(cell.CellChar);
                    }
            return correct;
        }


        private int breakLineSolveSolutions() {
            int solutions = 0;
            ArrayList<SudokuCell> variedCells = new ArrayList<SudokuCell>();
            for (SudokuCell cell : this.Cells)
                if (cell.AllowedChars.size() > 1) {
                    variedCells.add(cell);
                }
            //ArrayList<SudokuCell> fixedCells = recurrentBreakLine(variedCells);

            //if (fixedCells != null) solutions = 1;
            return solutions;
        }

    }


    public void solveSudoku(char[][] board) {
        SudokuBoard sudokuBoard = new SudokuBoard(board);
        sudokuBoard.solveIt();
        sudokuBoard.boardOut(board);
    }

}