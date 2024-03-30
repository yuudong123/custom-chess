package com.yuudong123.chessgalltnmt.domain;

import java.util.ArrayList;

import com.yuudong123.chessgalltnmt.support.interceptor.IntegerValues;

public class Chessboard implements IntegerValues {

  public static Chessboard chessboard = new Chessboard();

  public String[][] board;
  public char turn;
  public String wPlayer;
  public String bPlayer;
  public double wTime;
  public double bTime;
  public boolean check;
  public boolean finish;
  public double add;
  public ArrayList<ChessDTO> gibo;

  public int[] wk;
  public int[] bk;
  public boolean isWhiteCastled;
  public boolean isBlackCastled;

  public Chessboard() {
    reset(null);
  }

  public void reset(ChessDTO dto) {
    board = new String[][] {
        { "wr", "wn", "wb", "--", "wq", "wk", "--", "wb", "wn", "wr" },
        { "wp", "wp", "wp", "--", "wp", "wp", "--", "wp", "wp", "wp" },
        { "--", "--", "--", "--", "--", "--", "--", "--", "--", "--" },
        { "--", "--", "--", "--", "--", "--", "--", "--", "--", "--" },
        { "--", "--", "--", "--", "--", "--", "--", "--", "--", "--" },
        { "--", "--", "--", "--", "--", "--", "--", "--", "--", "--" },
        { "--", "--", "--", "--", "--", "--", "--", "--", "--", "--" },
        { "--", "--", "--", "--", "--", "--", "--", "--", "--", "--" },
        { "bp", "bp", "bp", "--", "bp", "bp", "--", "bp", "bp", "bp" },
        { "br", "bn", "bb", "--", "bq", "bk", "--", "bb", "bn", "br" },
    };
    turn = 'w';
    check = false;
    finish = false;
    if (dto != null) {
      wTime = dto.getTime();
      bTime = dto.getTime();
      wPlayer = dto.getWPlayer();
      bPlayer = dto.getBPlayer();
      add = dto.getAdd();
    } else {
      wTime = 300;
      bTime = 300;
      add = 2;
    }
    wk = new int[] { F, 0 };
    bk = new int[] { F, board.length - 1 };
    isWhiteCastled = false;
    isBlackCastled = false;

    gibo = new ArrayList<ChessDTO>();
  }

  public void move(ChessDTO c) {
    board[c.getTr() - 1][c.getTf() - 1] = c.getPromotion().equals("--") ? c.getPiece() : c.getPromotion();
    board[c.getFr() - 1][c.getFf() - 1] = "--";

    if (c.getPiece().charAt(0) == 'w') {
      wTime += add;
    } else {
      bTime += add;
    }

    check = isCheck();
    finish = isFinish();

    turn = turn == 'w' ? 'b' : 'w';
    gibo.add(c);
  }

  public String getBoard() {
    String result = "";
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < board.length; j++) {
        result += board[i][j];
      }
    }
    return result;
  }

  // 변형
  public boolean isFinish() {
    boolean wkLive = false;
    boolean bkLive = false;

    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        if (!wkLive || !bkLive) {
          if (board[i][j].equals("wk"))
            wkLive = true;
          if (board[i][j].equals("bk"))
            bkLive = true;
        } else {
          return false;
        }
      }
    }
    return true;
  }

  public boolean isCheck() {
    if (isAttacked(wk[0], wk[1], 'w')) {
      return true;
    }

    if (isAttacked(bk[0], bk[1], 'b')) {
      return true;
    }
    return false;
  }

  // 변형룰
  // public boolean canCastle(char color, char side) {
  // int r = color == 'w' ? 0 : 7;
  // if (side == 'k') {
  // return !(isAttacked(r, 4, color) || isAttacked(r, 5, color) || isAttacked(r,
  // 6, color));
  // } else {
  // return !(isAttacked(r, 4, color) || isAttacked(r, 3, color) || isAttacked(r,
  // 2, color));
  // }
  // }

  private boolean isAttacked(int rank, int file, char color) {
    return isAttackedByRook(rank, file, color) ||
        isAttackedByBishop(rank, file, color) ||
        isAttackedByKnight(rank, file, color) ||
        isAttackedByPawn(rank, file, color);
  }

  private boolean isAttackedByRook(int rank, int file, char color) {
    int[][] directions = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };

    for (int[] dir : directions) {
      int x = rank + dir[0];
      int y = file + dir[1];

      while (x >= 0 && x < 8 && y >= 0 && y < 8) {
        if (!board[x][y].equals("--")) {
          if (board[x][y].charAt(0) != color && (board[x][y].charAt(1) == 'r' || board[x][y].charAt(1) == 'q')) {
            return true;
          } else {
            break;
          }
        }
        x += dir[0];
        y += dir[1];
      }
    }

    return false;
  }

  private boolean isAttackedByBishop(int rank, int file, char color) {
    int[][] directions = { { 1, 1 }, { 1, -1 }, { -1, 1 }, { -1, -1 } };

    for (int[] dir : directions) {
      int x = rank + dir[0];
      int y = file + dir[1];

      while (x >= 0 && x < 8 && y >= 0 && y < 8) {
        if (!board[x][y].equals("--")) {
          if (board[x][y].charAt(0) != color && (board[x][y].charAt(1) == 'b' || board[x][y].charAt(1) == 'q')) {
            return true;
          } else {
            break;
          }
        }
        x += dir[0];
        y += dir[1];
      }
    }

    return false;
  }

  private boolean isAttackedByKnight(int rank, int file, char color) {
    int[][] directions = { { -2, -1 }, { -2, 1 }, { -1, -2 }, { -1, 2 }, { 1, -2 }, { 1, 2 }, { 2, -1 }, { 2, 1 } };

    for (int[] dir : directions) {
      int x = rank + dir[0];
      int y = file + dir[1];

      if (x >= 0 && x < 8 && y >= 0 && y < 8) {
        if (board[x][y].charAt(0) != color && board[x][y].charAt(1) == 'n') {
          return true;
        }
      }
    }

    return false;
  }

  private boolean isAttackedByPawn(int rank, int file, char color) {
    int[][] directions = (color == 'w') ? new int[][] { { -1, -1 }, { -1, 1 } } : new int[][] { { 1, -1 }, { 1, 1 } };

    for (int[] dir : directions) {
      int x = rank + dir[0];
      int y = file + dir[1];

      if (x >= 0 && x < 8 && y >= 0 && y < 8) {
        if (board[x][y].charAt(0) != color && board[x][y].charAt(1) == 'p') {
          return true;
        }
      }
    }

    return false;
  }

}
