package com.yuudong123.chessgalltnmt.support;

public interface StringValues {

  /* json 파일 경로 */
  String APPLYLIST_JSON_PATH = "E:/database/applylist.json";
  String MEMBER_JSON_PATH = "E:/database/member.json";
  String EMAILCHECK_JSON_PATH = "E:/database/emailcheck.json";

  /* 정규표현식 */
  String NAVER_ID_REGEX = "^[a-z0-9-_]{5,20}$";
  String INPUT_REGEX = "^[a-zA-Z0-9_-]{1,20}$";

  /* URL */
  String CHESSCOM_API_URL = "https://api.chess.com/pub/player/";
}
