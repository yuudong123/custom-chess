package com.yuudong123.chessgalltnmt.support;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

@Component
public class ChesscomApi implements StringValues {

  public String getStats(String chesscomId) {
    String url = CHESSCOM_API_URL + chesscomId + "/stats";

    RestTemplate restTemplate = new RestTemplate();
    Gson gson = new Gson();
    String jsonResponse = restTemplate.getForObject(url, String.class);
    String result = "";
    JsonObject json = gson.fromJson(jsonResponse, JsonObject.class);
    if (json.has("code")) {
      return "- - -";
    }
    if (json.has("chess_rapid")) {
      JsonObject chessRapid = json.getAsJsonObject("chess_rapid");
      if (chessRapid.has("best")) {
        result += chessRapid.get("rating").getAsString() + " ";
      } else {
        result += "- ";
      }
    } else {
      result += "- ";
    }
    if (json.has("chess_blitz")) {
      JsonObject chessRapid = json.getAsJsonObject("chess_blitz");
      if (chessRapid.has("best")) {
        result += chessRapid.get("rating").getAsString() + " ";
      } else {
        result += "- ";
      }
    } else {
      result += "- ";
    }
    if (json.has("chess_bullet")) {
      JsonObject chessRapid = json.getAsJsonObject("chess_bullet");
      if (chessRapid.has("best")) {
        result += chessRapid.get("rating").getAsString();
      } else {
        result += "-";
      }
    } else {
      result += "-";
    }
    return result;
  }

  public boolean isVerified(String chesscomId) {
    String url = CHESSCOM_API_URL + chesscomId;
    RestTemplate restTemplate = new RestTemplate();
    Gson gson = new Gson();
    String jsonResponse = restTemplate.getForObject(url, String.class);
    JsonObject json = gson.fromJson(jsonResponse, JsonObject.class);
    if (json.has("name")){
      String name = json.get("name").getAsString();
      return name.contains("체갤변형룰대회");
    }
    return false;
  }
}