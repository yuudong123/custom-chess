package com.yuudong123.chessgalltnmt.domain;

import lombok.Data;

@Data
public class MoveDTO {
  private int ff;
  private int fr;
  private int tf;
  private int tr;
  private String move = "--";
}
