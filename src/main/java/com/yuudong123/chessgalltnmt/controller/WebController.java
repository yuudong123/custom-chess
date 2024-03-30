package com.yuudong123.chessgalltnmt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.yuudong123.chessgalltnmt.support.Helper;

@Controller
@SessionAttributes("memberinfo")
public class WebController {

  @Autowired
  Helper helper;

  @GetMapping({ "/", "/index" })
  public String getIndex() {
    return "index";
  }

  @GetMapping("/login")
  public String getLogin() {
    return "login";
  }

  @GetMapping("/join")
  public String getJoin() {
    return "join";
  }

  @GetMapping("/edit")
  public String getEdit() {
    return "edit";
  }

  @GetMapping("/history")
  public String getHistory() {
    return "history";
  }

  @GetMapping("/room")
  public String getRoom(Model model) {
    // MemberVO memberVO = (MemberVO) model.getAttribute("memberinfo");
    // if (memberVO != null && memberVO.isVerified()) {
    return "room";
    // } else {
    // return "error/no-permission";
    // }
  }

  @GetMapping("/test")
  public String test() {
    return "test";
  }

}
