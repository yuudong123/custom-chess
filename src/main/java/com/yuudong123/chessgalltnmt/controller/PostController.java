package com.yuudong123.chessgalltnmt.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.yuudong123.chessgalltnmt.domain.MemberVO;
import com.yuudong123.chessgalltnmt.enums.ResultType;
import com.yuudong123.chessgalltnmt.service.JsonService;
import com.yuudong123.chessgalltnmt.support.Helper;
import com.yuudong123.chessgalltnmt.support.StringValues;

@SessionAttributes("memberinfo")
@Controller
public class PostController implements StringValues {

  @Autowired
  JsonService jsonService;
  @Autowired
  Helper helper;

  @ResponseBody
  @PostMapping("/join")
  public String postJoin(String chesscomId, String naverId, int emailcode, String password) {
    MemberVO memberVO = new MemberVO(chesscomId, naverId, password, false, new Date(), false);
    ResultType result = jsonService.insertMember(memberVO, emailcode);

    switch (result) {
      case SUCCESS:
        return "<script>alert('등록되었습니다.'); location.href='http://chessgall.ddns.net:2385/'</script>";
      case ERR_DUPLICATED_CHESSCOM_ID:
        return "<script>alert('이미 등록된 체스닷컴 ID입니다.'); location.href='http://chessgall.ddns.net:2385/join'</script>";
      case ERR_DUPLICATED_NAVER_ID:
        return "<script>alert('이미 등록된 네이버 ID입니다.'); location.href='http://chessgall.ddns.net:2385/join'</script>";
      case ERR_WRONG_INFO:
        return "<script>alert('인증번호를 확인하세요'); location.href='http://chessgall.ddns.net:2385/join'</script>";
      case ERR_FULL:
        return "<script>alert('신청 마감입니다.'); location.href='http://chessgall.ddns.net:2385/join'</script>";
      case ERR_REGEX:
        return "<script>alert('알파벳 대소문자, 숫자, _ , - , 1~20자 확인'); location.href='http://chessgall.ddns.net:2385/join'</script>";
      case ERR_EXCEPTION:
      default:
        return "<script>alert('오류가 발생했습니다. 잠시후 다시 시도해주세요.'); location.href='http://chessgall.ddns.net:2385/'</script>";
    }
  }

  @PostMapping("/login")
  public String postLogin(Model model, String chesscomId, String password) {
    MemberVO existMember = jsonService.selectMember(chesscomId, password, false);
    if (existMember == null) {
      return "redirect:/login";
    } else {
      model.addAttribute("memberinfo", existMember);
      return "/index";
    }
  }

  @RequestMapping("/logout")
  public String logout(SessionStatus status) {
    status.setComplete();
    return "redirect:/index";
  }

  @ResponseBody
  @PostMapping("/emailcheck")
  public String postEmailcheck(String naverId) {
    if (naverId.matches(NAVER_ID_REGEX)) {
      jsonService.insertEmailcheck(naverId);
      return "success";
    }
    return "invalid-id";
  }

  @ResponseBody
  @PostMapping("/edit/save")
  public String postEditSave(String newChesscomId, String password, Model model) {
    MemberVO memberinfo = (MemberVO) model.getAttribute("memberinfo");
    ResultType result = jsonService.updateMember(memberinfo, newChesscomId, password);
    switch (result) {
      case ERR_DUPLICATED_CHESSCOM_ID:
        return "<script>alert('이미 등록된 체스닷컴 ID입니다.'); location.href='http://chessgall.ddns.net:2385/edit'</script>";
      case ERR_WRONG_INFO:
        return "<script>alert('비밀번호를 확인하세요.'); location.href='http://chessgall.ddns.net:2385/edit'</script>";
      case SUCCESS:
        model.addAttribute("memberinfo", jsonService.selectMember(newChesscomId, password, false));
        return "<script>alert('저장되었습니다.'); location.href='http://chessgall.ddns.net:2385/edit'</script>";
      default:
        return "<script>location.href='http://chessgall.ddns.net:2385/edit'</script>";
    }
  }

  @ResponseBody
  @PostMapping("/bye")
  public String bye(Model model, SessionStatus status) {
    MemberVO memberinfo = (MemberVO) model.getAttribute("memberinfo");
    ResultType result = ResultType.ERR_EXCEPTION;
    if (memberinfo != null) {
      result = jsonService.deleteMember(memberinfo.getNaverId());
    }
    switch (result) {
      case SUCCESS:
        status.setComplete();
        return "<script>location.href='http://chessgall.ddns.net:2385/'</script>";
      default:
        return "<script>alert('오류가 발생했습니다.'); location.href='http://chessgall.ddns.net:2385/edit'</script>";
    }
  }
}
