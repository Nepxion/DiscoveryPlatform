package com.nepxion.discovery.platform.server.tool;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

import java.time.Duration;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.nepxion.discovery.platform.server.constant.PlatformConstant;
import com.nepxion.discovery.platform.server.entity.vo.AdminVo;

public class JwtTool {
    private static final String SECRET = "nengapszsnuighag";
    private static final String ID = "i";
    private static final String MAX_LIVE = "mlh";
    private static final Integer EXPIRE_TIME_MINUTES = 30;
    private static final Integer MAX_LIVE_HOURS = 24;

    public static String generateToken(AdminVo adminVo) {
        Date iat = new Date();
        return JWT.create()
                .withAudience(PlatformConstant.PLATFORM)
                .withIssuedAt(iat)
                .withExpiresAt(DateUtils.addMinutes(iat, EXPIRE_TIME_MINUTES))
                .withClaim(ID, adminVo.getId())
                .withClaim(MAX_LIVE, DateUtils.addHours(iat, MAX_LIVE_HOURS))
                .sign(Algorithm.HMAC256(SECRET));
    }

    public static boolean verify(String token) {
        try {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
            jwtVerifier.verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static long decodeToken(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        return decodedJWT.getClaims().get(ID).asLong();
    }

    public static String refreshTokenIfNecessary(String token) {
        DecodedJWT jwt = JWT.decode(token);
        if (!checkThreshold(jwt)) {
            return null;
        }
        try {
            Date maxLive = jwt.getClaims().get(MAX_LIVE).asDate();
            Date date = new Date(System.currentTimeMillis() +
                    Duration.ofMinutes(EXPIRE_TIME_MINUTES).toMillis());
            date = maxLive.before(date) ? maxLive : date;
            return JWT.create()
                    .withAudience(jwt.getAudience().toArray(new String[0]))
                    .withIssuedAt(jwt.getIssuedAt())
                    .withExpiresAt(date)
                    .withClaim(MAX_LIVE, maxLive)
                    .withClaim(ID, jwt.getClaims().get(ID).asLong())
                    .sign(Algorithm.HMAC256(SECRET));
        } catch (JWTCreationException e) {
            return null;
        }
    }

    private static boolean checkThreshold(DecodedJWT jwt) {
        long mlh = jwt.getClaims().get(MAX_LIVE).asDate().getTime();
        long iat = jwt.getIssuedAt().getTime();
        long exp = jwt.getExpiresAt().getTime();
        // (exp - cur) / (exp - iat) < 1/2
        return exp != mlh && (exp + iat - 2 * System.currentTimeMillis() < 0);
    }

}