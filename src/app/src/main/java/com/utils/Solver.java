package com.utils;

import java.util.Arrays;
import java.util.List;

public class Solver {
    public char[][] board;
    public long casesTried;

    public Solver(char[][] board, long casesTried) {
        this.board = board;
        this.casesTried = casesTried;
    }

    public Solver solveBoard(char[][] board, List<List<Piece>> allPiecesList) {
        char[][] workingBoard = new char[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            workingBoard[i] = Arrays.copyOf(board[i], board[i].length);
        }
        boolean[] usedPieces = new boolean[allPiecesList.size()];
        long[] caseCounter = new long[1];
        boolean solved = solve(workingBoard, allPiecesList, usedPieces, 0, caseCounter);
        return new Solver(solved ? workingBoard : board, caseCounter[0]);
    }

    private boolean solve(char[][] board, List<List<Piece>> allPieces, boolean[] usedPieces, int piecesPlaced,
            long[] caseCounter) {
        if (piecesPlaced == allPieces.size()) {
            return true;
        }
        for (int pieceIndex = 0; pieceIndex < allPieces.size(); pieceIndex++) {
            if (usedPieces[pieceIndex])
                continue;
            for (Piece piece : allPieces.get(pieceIndex)) {
                char[][] pieceMatrix = piece.getPiece();
                for (int row = 0; row <= board.length - pieceMatrix.length; row++) {
                    for (int col = 0; col <= board[0].length - pieceMatrix[0].length; col++) {
                        caseCounter[0]++;
                        boolean canPlace = true;
                        if (pieceMatrix.length + row > board.length || pieceMatrix[0].length + col > board[0].length) {
                            canPlace = false;
                        }
                        for (int i = 0; i < pieceMatrix.length && canPlace; i++) {
                            for (int j = 0; j < pieceMatrix[0].length && canPlace; j++) {
                                if (pieceMatrix[i][j] != ' ' && board[row + i][col + j] != ' ') {
                                    canPlace = false;
                                }
                            }
                        }
                        if (canPlace) {
                            for (int i = 0; i < pieceMatrix.length; i++) {
                                for (int j = 0; j < pieceMatrix[0].length; j++) {
                                    if (pieceMatrix[i][j] != ' ') {
                                        board[row + i][col + j] = pieceMatrix[i][j];
                                    }
                                }
                            }
                            usedPieces[pieceIndex] = true;
                            if (solve(board, allPieces, usedPieces, piecesPlaced + 1, caseCounter)) {
                                return true;
                            }
                            for (int i = 0; i < pieceMatrix.length; i++) {
                                for (int j = 0; j < pieceMatrix[0].length; j++) {
                                    if (pieceMatrix[i][j] != ' ') {
                                        board[row + i][col + j] = ' ';
                                    }
                                }
                            }
                            usedPieces[pieceIndex] = false;
                        }
                    }
                }
            }
        }
        return false;
    }
}