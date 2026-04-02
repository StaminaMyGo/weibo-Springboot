package com.wei.it.weibo.web.interceptor;

import com.wei.it.weibo.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.HandlerInterceptor;

//@Service // 业务层对象放到Spring容器
//@Controller // 界面层对象放到Spring容器
//@Repository // 数据库对象放到Spring容器
@Component
public class JwtInterceptor  implements HandlerInterceptor {
    @Value("${my.jwt_pwd}")
    private String jwtPwd=null;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equalsIgnoreCase("options")) {
            return true;
        }
        if (request.getMethod().equalsIgnoreCase("get")) {
            return true;
        }
        response.setContentType("application/json;charset=utf-8");
        String jwt = request.getHeader("Authorization");
        if(jwt==null){
            response.getWriter().print("""
                    "code":4001,
                    "msg":"请先登录",
                    "data":null
                    """);
            return false;
        }
        jwt=jwt.substring(7);
        Claims claims = null;
        try{
            claims=Jwts.parser().setSigningKey(this.jwtPwd).parseClaimsJws(jwt).getBody();
        }catch (Exception e){
            e.printStackTrace();
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write("{\"code\":4001,\"msg\":凭证无效，过期或被篡改，请重新登录,\"data\":null,}");
            return false;
        }
        // 获取当前登录User的对象

        User u = new User();
        u.setId((Integer) claims.get("id"));
        u.setNickName(claims.get("nickName").toString());
        u.setLoginName(claims.get("loginName").toString());
        u.setPhoto(claims.get("photo").toString());
        u.setAttentionCount((Integer) claims.get("attentionCount"));
        u.setScore((Integer) claims.get("score"));
        request.setAttribute("auth", u);


        return true;
    }
}
