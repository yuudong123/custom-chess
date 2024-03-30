package com.yuudong123.chessgalltnmt.support.interceptor;

import java.util.Arrays;
import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.yuudong123.chessgalltnmt.domain.MemberVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminAccessInterceptor implements HandlerInterceptor {

  private final List<String> ADMIN_NAVER_ID = Arrays.asList(
      "chessgalltnmtadmin1",
      "chessgalltnmtadmin2"
      );

  @Override
  public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
      @NonNull Object handler) throws Exception {

    MemberVO memberinfo = (MemberVO) request.getSession().getAttribute("memberinfo");

    if (memberinfo != null && ADMIN_NAVER_ID.stream().anyMatch(matcher -> matcher.matches(memberinfo.getNaverId()))) {
      return true;
    }

    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    request.getRequestDispatcher("/error/no-permission").forward(request, response);
    return false;
  }
}
