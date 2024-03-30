package com.yuudong123.chessgalltnmt.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("/error")
@SessionAttributes("memberinfo")
public class MyErrorController implements ErrorController {

  @RequestMapping("banned")
  public String banned() {
    return "error/banned";
  }

  @RequestMapping("no-permission")
  public String noPermission() {
    return "error/no-permission";
  }
}
