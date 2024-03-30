package com.yuudong123.chessgalltnmt.service;

import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.yuudong123.chessgalltnmt.domain.Chessboard;

@Service
public class ChessServiceNew {

  private String sendBoard(String move) {
    JsonObject data = new JsonObject();
    data.addProperty("type", "board");
    data.addProperty("board", Chessboard.chessboard.getBoard());
    data.addProperty("whiteTime", Chessboard.chessboard.wTime);
    data.addProperty("blackTime", Chessboard.chessboard.bTime);
    data.addProperty("wPlayer", Chessboard.chessboard.wPlayer);
    data.addProperty("bPlayer", Chessboard.chessboard.bPlayer);
    data.addProperty("check", Chessboard.chessboard.check);
    data.addProperty("turn", String.valueOf(Chessboard.chessboard.turn));
    data.addProperty("sound", move == null ? "--" : move);
    return data.toString();
  }
}
