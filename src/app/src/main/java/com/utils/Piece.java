package com.utils;

import java.util.Arrays;

public class Piece {
    private char[][] piece = new char[0][];
    private int maxCol = 0;

    public int getLength() {
        return piece.length;
    }

    public Piece() {
        this.piece = new char[0][];
    }

    public Piece(char[][] piece) {
        this.piece = piece;
    }

    public char[][] getPiece() {
        return piece;
    }

    public void addLineBelow(char[] newLine) {
        if (newLine.length > maxCol) {
            maxCol = newLine.length;
            expandAllRows();
        }
        piece = Arrays.copyOf(piece, piece.length + 1);
        piece[piece.length - 1] = Arrays.copyOf(newLine, maxCol);
        Arrays.fill(piece[piece.length - 1], newLine.length, maxCol, ' ');
    }

    private void expandAllRows() {
        for (int i = 0; i < piece.length; i++) {
            char[] expandedRow = Arrays.copyOf(piece[i], maxCol);
            Arrays.fill(expandedRow, piece[i].length, maxCol, ' ');
            piece[i] = expandedRow;
        }
    }

    public boolean validatePiece() {
        if (piece.length == 0 || piece[0].length == 0)
            return false;
        char characterPiece = '*';
        int[][] copyPiece = new int[piece.length][piece[0].length];
        for (int i = 0; i < piece[0].length; i++) {
            if (piece[0][i] != ' ') {
                characterPiece = piece[0][i];
            }
        }
        int num = 0;
        for (int i = 0; i < piece.length; i++) {
            for (int j = 0; j < piece[i].length; j++) {
                if (piece[i][j] != ' ') {
                    boolean topValid = i > 0 && piece[i - 1][j] == characterPiece;
                    boolean leftValid = j > 0 && piece[i][j - 1] == characterPiece;
                    boolean topLeftValid = i > 0 && j > 0 && piece[i - 1][j - 1] == characterPiece;
                    boolean topRightValid = i > 0 && j + 1 < piece[i].length
                            && piece[i - 1][j + 1] == characterPiece;
                    if (topValid || leftValid || topLeftValid || topRightValid) {
                        copyPiece[i][j] = num;
                    } else {
                        num++;
                        copyPiece[i][j] = num;
                    }
                }
            }
        }
        for (int[] row : copyPiece) {
            for (int i = 0; i < row.length; i++) {
                if (row[i] > 1) {
                    return false;
                }
            }
        }
        return true;
    }

    public Piece rotate90() {
        int rows = piece.length;
        int cols = piece[0].length;
        char[][] rotated = new char[cols][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                rotated[j][rows - 1 - i] = piece[i][j];
            }
        }
        return new Piece(rotated);
    }

    public Piece flipHorizontal() {
        int rows = piece.length;
        int cols = piece[0].length;
        char[][] flipped = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                flipped[i][cols - 1 - j] = piece[i][j];
            }
        }
        return new Piece(flipped);
    }

    public void printPiece() {
        for (char[] row : piece) {
            System.out.print("[");
            for (char c : row) {
                System.out.print(c);
                System.out.print(",");
            }
            System.out.print("]");
            System.out.println();
        }
    }
}
