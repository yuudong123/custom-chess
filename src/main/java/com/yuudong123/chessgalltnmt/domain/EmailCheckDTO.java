package com.yuudong123.chessgalltnmt.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailCheckDTO {
  private String id;
  private int code;
}
