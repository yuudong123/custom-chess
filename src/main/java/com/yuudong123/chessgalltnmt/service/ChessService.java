package com.yuudong123.chessgalltnmt.service;

import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.yuudong123.chessgalltnmt.domain.ChessDTO;
import com.yuudong123.chessgalltnmt.domain.Chessboard;

@Service
public class ChessService {
  public boolean isValidMove(ChessDTO chessDTO) {
    String piece = chessDTO.getPiece();

    if (piece.charAt(0) == Chessboard.chessboard.turn) {
      switch (piece.charAt(1)) {
        case 'p':
          if (pawn(chessDTO)) {
            return true;
          }
          break;
        case 'n':
          if (knight(chessDTO)) {
            return true;
          }
          break;
        case 'b':
          if (bishop(chessDTO)) {
            return true;
          }
          break;
        case 'r':
          if (rook(chessDTO)) {
            return true;
          }
          break;
        case 'q':
          if (queen(chessDTO)) {
            return true;
          }
          break;
        case 'k':
          if (king(chessDTO)) {
            return true;
          }
          break;
        default:
          break;
      }
    }
    return false;
  }

  private boolean pawn(ChessDTO chessDTO) {
    String piece = chessDTO.getPiece();
    String capture = chessDTO.getCapture();
    int ff = chessDTO.getFf();
    int fr = chessDTO.getFr();
    int tf = chessDTO.getTf();
    int tr = chessDTO.getTr();

    // 원본
    // if (piece.equals("wp")) {
    // if (fr < tr) {
    // if (fr == 2) {
    // if (tr != 1 && tr != 3 && tr != 4) {
    // return false;
    // }
    // } else {
    // if (tr - fr != 1) {
    // return false;
    // }
    // }
    // } else {
    // return false;
    // }
    // } else if (piece.equals("bp")) {
    // if (fr > tr) {
    // if (fr == 7) {
    // if (tr != 6 && tr != 5) {
    // return false;
    // }
    // } else {
    // if (fr - tr != 1) {
    // return false;
    // }
    // }
    // } else {
    // return false;
    // }
    // }

    if (piece.equals("wp")) {
      if (fr < tr) {
        if (fr == 2) {
          if (tr != 1 && tr != 3 && tr != 4) {
            return false;
          }
        } else {
          if (tr - fr != 1) {
            return false;
          }
        }
      } else if (fr > tr) {
        if (fr - tr != 1) {
          return false;
        }
      } else {
        return false;
      }
    } else if (piece.equals("bp")) {
      if (fr > tr) {
        if (fr == 7) {
          if (tr != 6 && tr != 5) {
            return false;
          }
        } else {
          if (fr - tr != 1) {
            return false;
          }
        }
      } else if (fr < tr) {
        if (fr - tr != -1) {
          return false;
        }
      } else {
        return false;
      }
    }

    if (Chessboard.chessboard.gibo.size() != 0) {
      ChessDTO prevMove = Chessboard.chessboard.gibo.get(Chessboard.chessboard.gibo.size() - 1);
      if (prevMove.getPiece().charAt(1) == 'p'
          && ((piece.charAt(0) == 'w' && fr == 5) || (piece.charAt(0) == 'b' && fr == 4))
          && (prevMove.getFr() - prevMove.getTr()) % 2 == 0) {

        if ((ff - tf == 1 || ff - tf == -1) && prevMove.getTf() == tf) {
          Chessboard.chessboard.board[fr - 1][tf - 1] = "--";
          return true;
        }
      }
    }

    if (!capture.equals("--")) {
      if (ff - tf != 1 && ff - tf != -1) {
        return false;
      }
    } else {
      if (ff != tf) {
        return false;
      }
    }
    return true;
  }

  private boolean knight(ChessDTO chessDTO) {
    int ff = chessDTO.getFf();
    int fr = chessDTO.getFr();
    int tf = chessDTO.getTf();
    int tr = chessDTO.getTr();

    return (((ff - tf == 2 || tf - ff == 2) && (fr - tr == 1 || tr - fr == 1))
        || ((ff - tf == 1 || tf - ff == 1) && (fr - tr == 2 || tr - fr == 2)));
  }

  private boolean bishop(ChessDTO chessDTO) {
    int ff = chessDTO.getFf();
    int fr = chessDTO.getFr();
    int tf = chessDTO.getTf();
    int tr = chessDTO.getTr();

    if (Math.abs(ff - tf) != Math.abs(fr - tr)) {
      return false;
    }

    int fileMod = ff > tf ? -1 : 1;
    int rankMod = fr > tr ? -1 : 1;

    while (ff + fileMod != tf) {
      if (!Chessboard.chessboard.board[fr - 1 + rankMod][ff - 1 + fileMod].equals("--"))
        return false;
      ff += fileMod;
      fr += rankMod;
    }

    return true;
  }

  private boolean rook(ChessDTO chessDTO) {
    int ff = chessDTO.getFf();
    int fr = chessDTO.getFr();
    int tf = chessDTO.getTf();
    int tr = chessDTO.getTr();

    if ((ff != tf) && (fr != tr))
      return false;

    if (ff == tf) {
      int rankMod = fr > tr ? -1 : 1;
      while (fr + rankMod != tr) {
        if (!Chessboard.chessboard.board[fr - 1 + rankMod][ff - 1].equals("--"))
          return false;
        fr += rankMod;
      }
    } else {
      int fileMod = ff > tf ? -1 : 1;
      while (ff + fileMod != tf) {
        if (!Chessboard.chessboard.board[fr - 1][ff - 1 + fileMod].equals("--"))
          return false;
        ff += fileMod;
      }
    }
    return true;
  }

  private boolean queen(ChessDTO chessDTO) {
    return rook(chessDTO) || bishop(chessDTO);
  }

  private boolean king(ChessDTO chessDTO) {
    String piece = chessDTO.getPiece();
    int ff = chessDTO.getFf();
    int fr = chessDTO.getFr();
    int tf = chessDTO.getTf();
    int tr = chessDTO.getTr();

    if (!Chessboard.chessboard.isWhiteCastled) {
      if (ff == 5 && fr == 1 && tr == 1 && piece.equals("wk")) {
        if (tf == 7
            && (Chessboard.chessboard.board[0][5].equals("--") || Chessboard.chessboard.board[0][5].equals("wp"))
            && (Chessboard.chessboard.board[0][6].equals("--") || Chessboard.chessboard.board[0][6].equals("wp"))) {
          Chessboard.chessboard.board[0][5] = "wr";
          Chessboard.chessboard.board[0][7] = "--";
          Chessboard.chessboard.isWhiteCastled = true;
          return true;

        }
        if (tf == 3
            && (Chessboard.chessboard.board[0][1].equals("--") || Chessboard.chessboard.board[0][1].equals("wp"))
            && (Chessboard.chessboard.board[0][2].equals("--") || Chessboard.chessboard.board[0][2].equals("wp"))
            && (Chessboard.chessboard.board[0][3].equals("--") || Chessboard.chessboard.board[0][3].equals("wp"))) {
          Chessboard.chessboard.board[0][3] = "wr";
          Chessboard.chessboard.board[0][0] = "--";
          Chessboard.chessboard.isWhiteCastled = true;
          return true;

        }
      }
    }
    if (!Chessboard.chessboard.isBlackCastled && piece.equals("bk")) {
      if (ff == 5 && fr == 8 && tr == 8 && piece.equals("bk")) {
        if (tf == 7
            && (Chessboard.chessboard.board[7][5].equals("--") || Chessboard.chessboard.board[7][5].equals("bp"))
            && (Chessboard.chessboard.board[7][6].equals("--") || Chessboard.chessboard.board[7][6].equals("bp"))) {
          Chessboard.chessboard.board[7][5] = "br";
          Chessboard.chessboard.board[7][7] = "--";
          Chessboard.chessboard.isBlackCastled = true;
          return true;
        }
        if (tf == 3
            && (Chessboard.chessboard.board[7][1].equals("--") || Chessboard.chessboard.board[7][1].equals("bp"))
            && (Chessboard.chessboard.board[7][2].equals("--") || Chessboard.chessboard.board[7][2].equals("bp"))
            && (Chessboard.chessboard.board[7][3].equals("--") || Chessboard.chessboard.board[7][3].equals("bp"))) {
          Chessboard.chessboard.board[7][3] = "br";
          Chessboard.chessboard.board[7][0] = "--";
          Chessboard.chessboard.isBlackCastled = true;
          return true;
        }
      }
    }

    if (Math.abs(ff - tf) > 1 || Math.abs(fr - tr) > 1) {
      return false;
    }

    if (piece.equals("wk")) {
      Chessboard.chessboard.wk = new int[] { tr - 1, tf - 1 };
    } else if (piece.equals("bk")) {
      Chessboard.chessboard.bk = new int[] { tr - 1, tf - 1 };
    }

    return true;
  }
}
