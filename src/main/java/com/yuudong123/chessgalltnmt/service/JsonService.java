package com.yuudong123.chessgalltnmt.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.reflect.TypeToken;
import com.yuudong123.chessgalltnmt.domain.Chessboard;
import com.yuudong123.chessgalltnmt.domain.EmailCheckDTO;
import com.yuudong123.chessgalltnmt.domain.MemberVO;
import com.yuudong123.chessgalltnmt.enums.ResultType;
import com.yuudong123.chessgalltnmt.support.ChesscomApi;
import com.yuudong123.chessgalltnmt.support.Helper;
import com.yuudong123.chessgalltnmt.support.StringValues;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class JsonService implements StringValues {

  @Autowired
  private Helper helper;

  @Autowired
  private ChesscomApi chesscomApi;

  public JsonService(Helper helper) {
    this.helper = helper;
  }

  public ArrayList<MemberVO> selectMemberList() {
    ArrayList<MemberVO> memberList = helper.readJsonFile(MEMBER_JSON_PATH, new TypeToken<ArrayList<MemberVO>>() {
    }.getType());
    return memberList == null ? new ArrayList<MemberVO>() : memberList;
  }

  public MemberVO selectMember(String chesscomId, String password, boolean withRating) {
    ArrayList<MemberVO> memberList = selectMemberList();

    try {
      if (!helper.isValidString(chesscomId)
          || !helper.isValidString(password)) {
        return null;
      }
    } catch (Exception e) {
      return null;
    }

    MemberVO existMember = isWrongPassword(memberList, chesscomId, password);

    if (existMember == null) {
      return null;
    }

    if (withRating) {
      String[] ratings = chesscomApi.getStats(chesscomId).split(" ");
      existMember.setRapid(ratings[0]);
      existMember.setBlitz(ratings[1]);
      existMember.setBullet(ratings[2]);
    }

    return existMember;
  }

  public ResultType insertMember(MemberVO memberVO, int emailcode) {
    ArrayList<MemberVO> memberList = selectMemberList();

    try {
      if (!helper.isValidString(memberVO.getChesscomId())
          || !helper.isValidString(memberVO.getPassword())
          || !helper.isValidString(memberVO.getNaverId())
          || !helper.isValidString("" + emailcode)) {
        return ResultType.ERR_REGEX;
      }
    } catch (Exception e) {
      return ResultType.ERR_REGEX;
    }

    if (memberList.size() >= 19) {
      return ResultType.ERR_FULL;
    }

    ResultType resultD = isDuplicatedMember(memberList, memberVO);
    if (resultD != ResultType.SUCCESS) {
      return resultD;
    }

    ResultType resultE = isEmailCodeMatch(memberVO.getNaverId(), emailcode);
    if (resultE != ResultType.SUCCESS) {
      return resultE;
    }

    memberList.add(memberVO);
    helper.writeJsonFile(MEMBER_JSON_PATH, memberList);

    return ResultType.SUCCESS;
  }

  public ResultType updateMember(MemberVO memberinfo, String newChesscomId, String password) {
    ArrayList<MemberVO> memberList = selectMemberList();
    if (!memberinfo.getPassword().equals(password)) {
      return ResultType.ERR_WRONG_INFO;
    }

    MemberVO temp = new MemberVO();
    temp.setChesscomId(newChesscomId);
    ResultType result = isDuplicatedMember(memberList, temp);
    if (result == ResultType.ERR_DUPLICATED_CHESSCOM_ID) {
      return result;
    }

    for (int i = 0; i < memberList.size(); i++) {
      if (memberList.get(i).getChesscomId().equals(memberinfo.getChesscomId())) {
        memberList.get(i).setChesscomId(newChesscomId);
        helper.writeJsonFile(MEMBER_JSON_PATH, memberList);
        return ResultType.SUCCESS;
      }
    }
    return ResultType.ERR_EXCEPTION;
  }

  public ResultType deleteMember(String naverId) {
    ArrayList<MemberVO> memberList = selectMemberList();
    for (int i = 0; i < memberList.size(); i++) {
      if (memberList.get(i).getNaverId().equals(naverId)) {
        memberList.remove(i);
        helper.writeJsonFile(MEMBER_JSON_PATH, memberList);
      }
    }
    return ResultType.SUCCESS;
  }

  public ResultType toggleBan(String naverId) {
    ArrayList<MemberVO> memberList = selectMemberList();
    for (int i = 0; i < memberList.size(); i++) {
      if (memberList.get(i).getNaverId().equals(naverId)) {
        memberList.get(i).setBanned(!memberList.get(i).isBanned());
        helper.writeJsonFile(MEMBER_JSON_PATH, memberList);
      }
    }
    return ResultType.SUCCESS;
  }

  public void insertEmailcheck(String naverId) {
    int code = (int) (Math.random() * 100000);
    ArrayList<EmailCheckDTO> emailcheckList = selectEmailCheckList();
    for (EmailCheckDTO emailCheckDTO : emailcheckList) {
      if (emailCheckDTO.getId().equals(naverId)) {
        return;
      }
    }
    emailcheckList.add(new EmailCheckDTO(naverId, code));
    helper.writeJsonFile(EMAILCHECK_JSON_PATH, emailcheckList);
    helper.sendEmail(naverId + "@naver.com", "변형체스대회 신청 인증번호입니다. 본인이 아니라면 스팸처리해주세요.", "인증번호: " + code);
  }

  public void setValidPlayer(String white, String black) {
    boolean a = true;
    boolean b = true;
    boolean c = true;
    boolean d = true;
    ArrayList<MemberVO> memberList = selectMemberList();
    for (int i = 0; i < memberList.size(); i++) {
      if (a && memberList.get(i).getRole().equals("white")) {
        memberList.get(i).setRole("--");
        a = false;
      }
      if (b && memberList.get(i).getChesscomId().equals(white)) {
        memberList.get(i).setRole("white");
        Chessboard.chessboard.wPlayer = white;
        b = false;
      }
      if (c && memberList.get(i).getRole().equals("black")) {
        memberList.get(i).setRole("--");
        c = false;
      }
      if (d && memberList.get(i).getChesscomId().equals(black)) {
        memberList.get(i).setRole("black");
        Chessboard.chessboard.bPlayer = black;
        d = false;
      }
      if (!a && !b && !c && !d) {
        break;
      }
    }
    helper.writeJsonFile(MEMBER_JSON_PATH, memberList);
  }

  // private
  // private
  private ArrayList<EmailCheckDTO> selectEmailCheckList() {
    ArrayList<EmailCheckDTO> emailcheckList = helper.readJsonFile(EMAILCHECK_JSON_PATH,
        new TypeToken<ArrayList<EmailCheckDTO>>() {
        }.getType());
    return emailcheckList == null ? new ArrayList<EmailCheckDTO>() : emailcheckList;
  }

  private ResultType isDuplicatedMember(ArrayList<MemberVO> list, MemberVO data) {
    for (MemberVO item : list) {
      if (item.getChesscomId().equals(data.getChesscomId()))
        return ResultType.ERR_DUPLICATED_CHESSCOM_ID;
      if (item.getNaverId().equals(data.getNaverId()))
        return ResultType.ERR_DUPLICATED_NAVER_ID;
    }
    return ResultType.SUCCESS;
  }

  private MemberVO isWrongPassword(ArrayList<MemberVO> list, String chesscomId, String password) {
    for (MemberVO item : list) {
      if (item.getChesscomId().equals(chesscomId) && item.getPassword().equals(password)) {
        return item;
      }
    }
    return null;
  }

  private ResultType isEmailCodeMatch(String naverId, int code) {
    ArrayList<EmailCheckDTO> emailcheckList = selectEmailCheckList();
    for (int i = 0; i < emailcheckList.size(); i++) {
      if (emailcheckList.get(i).getId().equals(naverId)) {
        emailcheckList.remove(i);
        helper.writeJsonFile(EMAILCHECK_JSON_PATH, emailcheckList);
        return ResultType.SUCCESS;
      }
    }
    return ResultType.ERR_WRONG_INFO;
  }
}
