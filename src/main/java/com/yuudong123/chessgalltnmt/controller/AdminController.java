package com.yuudong123.chessgalltnmt.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.yuudong123.chessgalltnmt.domain.MemberVO;
import com.yuudong123.chessgalltnmt.enums.ResultType;
import com.yuudong123.chessgalltnmt.service.JsonService;

@Controller
@RequestMapping("admin")
@SessionAttributes("memberinfo")
public class AdminController {

  @Autowired
  JsonService jsonService;

  @GetMapping({ "/", "/main", "" })
  public String getMain() {
    return "/admin/main";
  }

  @GetMapping("/members")
  public ModelAndView getMembers() {
    ModelAndView mav = new ModelAndView();

    ArrayList<MemberVO> list = jsonService.selectMemberList();

    mav.setViewName("admin/members");
    mav.addObject("list", list);
    return mav;
  }

  @ResponseBody
  @PostMapping("/ban")
  public String postBan(@RequestBody String naverId) {
    String result = jsonService.toggleBan(naverId) == ResultType.SUCCESS ? "success" : "err_exception";
    return result;
  }

  @ResponseBody
  @PostMapping("/delete")
  public String postDelete(@RequestBody String naverId) {
    String result = jsonService.deleteMember(naverId) == ResultType.SUCCESS ? "success" : "err_exception";
    return result;
  }
}
