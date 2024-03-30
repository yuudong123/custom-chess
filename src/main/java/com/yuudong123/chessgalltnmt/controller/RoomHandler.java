package com.yuudong123.chessgalltnmt.controller;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.yuudong123.chessgalltnmt.domain.Chessboard;
import com.yuudong123.chessgalltnmt.service.ChessService;
import com.yuudong123.chessgalltnmt.service.JsonService;
import com.yuudong123.chessgalltnmt.support.Helper;
import com.yuudong123.chessgalltnmt.support.StringValues;

public class RoomHandler implements WebSocketHandler, StringValues {

  private static final Set<WebSocketSession> sessions = new HashSet<>();

  private static final ChessService chessService = new ChessService();

  private JsonService jsonService = new JsonService(new Helper());

  private ScheduledExecutorService scheduler;
  private JsonObject timeData = new JsonObject();
  private Gson gson = new Gson();

  @Override
  public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
    // String dataString = (String) message.getPayload();
    // if (type.equals("onopen")) {
    // sendBoard(session, null);
    // } else if (type.equals("move") && !Chessboard.chessboard.finish) {

    // if (chessService.isValidMove(MoveDTO move)) {
    // Chessboard.chessboard.move(move);
    // for (WebSocketSession webSocketSession : sessions) {
    // sendBoard(webSocketSession, sound);
    // if (Chessboard.chessboard.finish) {
    // stopTimer();
    // JsonObject dataFinish = new JsonObject();
    // dataFinish.addProperty("type", "finish");
    // dataFinish.addProperty("win", chessDTO.getPiece().charAt(0) == 'w' ? "white"
    // : "black");
    // webSocketSession.sendMessage(new TextMessage(dataFinish.toString()));
    // }
    // }
    // }
    // } else if (type.equals("reset")) {
    // Chessboard.chessboard.reset(chessDTO);
    // startTimer();
    // jsonService.setValidPlayer(chessDTO.getWPlayer(), chessDTO.getBPlayer());
    // for (WebSocketSession webSocketSession : sessions) {
    // sendBoard(webSocketSession, null);
    // }
    // }
  }

  @Override
  public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    sessions.add(session);
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
    sessions.remove(session);
  }

  @Override
  public boolean supportsPartialMessages() {
    return false;
  }

  private void startTimer() {
    stopTimer();
    scheduler = Executors.newSingleThreadScheduledExecutor();
    timeData.addProperty("type", "time");
    scheduler.scheduleAtFixedRate(() -> {
      if (Chessboard.chessboard.gibo.size() > 1) {
        if (Chessboard.chessboard.turn == 'w') {
          Chessboard.chessboard.wTime -= 1;
          if (Chessboard.chessboard.wTime == 0) {
            Chessboard.chessboard.finish = true;
            for (WebSocketSession webSocketSession : sessions) {
              JsonObject data = new JsonObject();
              data.addProperty("type", "finish");
              data.addProperty("win", "black");
              try {
                webSocketSession.sendMessage(new TextMessage(data.toString()));
              } catch (IOException e) {
                e.printStackTrace();
              }
            }
            stopTimer();
            return;
          }
        } else {
          Chessboard.chessboard.bTime -= 1;
          if (Chessboard.chessboard.bTime == 0) {
            Chessboard.chessboard.finish = true;
            for (WebSocketSession webSocketSession : sessions) {
              JsonObject data = new JsonObject();
              data.addProperty("type", "finish");
              data.addProperty("win", "white");
              try {
                webSocketSession.sendMessage(new TextMessage(data.toString()));
              } catch (Exception e) {
                e.printStackTrace();
              }
            }
            stopTimer();
            return;
          }
        }
        for (WebSocketSession webSocketSession : sessions) {
          timeData.addProperty("whiteTime", Chessboard.chessboard.wTime);
          timeData.addProperty("blackTime", Chessboard.chessboard.bTime);
          try {
            webSocketSession.sendMessage(new TextMessage(timeData.toString()));
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    }, 0, 1000, TimeUnit.MILLISECONDS);
  }

  private void stopTimer() {
    if (scheduler != null) {
      scheduler.shutdown();
      scheduler = null;
    }
  }

  private void sendBoard(WebSocketSession session, String move) {
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
    try {
      session.sendMessage(new TextMessage(data.toString()));
    } catch (IOException e) {
    }
  }
}
