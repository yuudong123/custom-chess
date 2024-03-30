package com.yuudong123.chessgalltnmt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.JsonObject;
import com.yuudong123.chessgalltnmt.domain.Chessboard;

import org.springframework.web.bind.annotation.PostMapping;

@RequestMapping("chess")
@Controller
public class ChessPostController {

  @PostMapping("/initialize")
  public String initialize() {
    JsonObject data = new JsonObject();
    data.addProperty("type", "board");
    data.addProperty("board", Chessboard.chessboard.getBoard());
    data.addProperty("whiteTime", Chessboard.chessboard.wTime);
    data.addProperty("blackTime", Chessboard.chessboard.bTime);
    data.addProperty("wPlayer", Chessboard.chessboard.wPlayer);
    data.addProperty("bPlayer", Chessboard.chessboard.bPlayer);
    data.addProperty("check", Chessboard.chessboard.check);
    data.addProperty("turn", String.valueOf(Chessboard.chessboard.turn));
    data.addProperty("sound", "--");

    return data.toString();
  }

}
