package com.yuudong123.chessgalltnmt.domain;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberVO implements Serializable {
  private String chesscomId;
  private String naverId;
  private String password;
  private boolean isBanned;
  private Date joinAt;
  private boolean verified;
  
  private String rapid;
  private String blitz;
  private String bullet;
  private String role = "--";

  public MemberVO(String chesscomId, String naverId, String password, boolean isBanned, Date joinAt, boolean verified) {
    this.chesscomId = chesscomId;
    this.naverId = naverId;
    this.password = password;
    this.isBanned = isBanned;
    this.joinAt = joinAt;
    this.verified = verified;
  }

}

