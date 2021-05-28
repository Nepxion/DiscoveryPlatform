package com.nepxion.discovery.platform.server.tool.common;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Ning Zhang
 * @version 1.0
 */

import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.nepxion.discovery.platform.server.constant.PlatformConstant;
import com.nepxion.discovery.platform.server.entity.vo.AdminVo;

public class JwtTool {
    private static final String SECRET = "nengapszsnuighag";
    private static final String ID = "i";

    public static String generateToken(AdminVo adminVo) {
        Date now = new Date();
        return JWT.create()
                .withAudience(PlatformConstant.PLATFORM)
                .withIssuedAt(new Date())
                .withExpiresAt(DateUtils.addDays(now, 7))
                .withClaim(ID, adminVo.getId())
                .sign(Algorithm.HMAC256(SECRET));
    }

    public static boolean verify(String token) {
        try {
            JWT.require(Algorithm.HMAC256(SECRET)).build();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static long decodeToken(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        return decodedJWT.getClaims().get(ID).asLong();
    }
}