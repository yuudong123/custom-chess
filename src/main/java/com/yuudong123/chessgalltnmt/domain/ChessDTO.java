package com.yuudong123.chessgalltnmt.domain;

import lombok.Data;

@Data
public class ChessDTO {
  private String type;
  private String piece;
  private String password;
  private String wPlayer;
  private String bPlayer;
  private double time;
  private double add;
  private int ff;
  private int fr;
  private int tf;
  private int tr;
  private String promotion;
  private String capture;

  public ChessDTO() {
  }

  public ChessDTO(String piece, int ff, int fr, int tf, int tr) {
    this.piece = piece;
    this.ff = ff;
    this.fr = fr;
    this.tf = tf;
    this.tr = tr;
    this.capture = "--";
    this.promotion = "--";
  }
}