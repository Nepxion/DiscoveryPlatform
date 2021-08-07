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

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import com.nepxion.discovery.platform.server.entity.vo.AdminVo;
import com.nepxion.discovery.platform.server.constant.PlatformConstant;

public class JwtTool {
    private static final String ID = "i";
    private static final String MAX_LIVE = "mlt";

    public static String generateToken(AdminVo adminVo, String secret,
                                       Duration expireTime, Duration maxLiveTime) {
        expireTime = expireTime.compareTo(maxLiveTime) > 0 ? maxLiveTime : expireTime;
        Date iat = new Date();
        long now = iat.getTime();
        return JWT.create()
                .withAudience(PlatformConstant.PLATFORM)
                .withIssuedAt(iat)
                .withExpiresAt(new Date(now + expireTime.toMillis()))
                .withClaim(ID, adminVo.getId())
                .withClaim(MAX_LIVE, new Date(now + maxLiveTime.toMillis()))
                .sign(Algorithm.HMAC256(secret));
    }

    public static boolean verify(String token, String secret) {
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secret)).build();
        jwtVerifier.verify(token);
        return true;
    }

    public static long decodeToken(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        return decodedJWT.getClaims().get(ID).asLong();
    }

    public static String refreshTokenIfNecessary(String token, String secret,
                                                 Duration expireTime, Integer renewThreshold) {
        DecodedJWT jwt = JWT.decode(token);
        if (!checkThreshold(jwt, renewThreshold)) {
            return null;
        }
        try {
            Date maxLive = jwt.getClaims().get(MAX_LIVE).asDate();
            Date date = new Date(System.currentTimeMillis() + expireTime.toMillis());
            date = maxLive.before(date) ? maxLive : date;
            return JWT.create()
                    .withAudience(jwt.getAudience().toArray(new String[0]))
                    .withIssuedAt(jwt.getIssuedAt())
                    .withExpiresAt(date)
                    .withClaim(MAX_LIVE, maxLive)
                    .withClaim(ID, jwt.getClaims().get(ID).asLong())
                    .sign(Algorithm.HMAC256(secret));
        } catch (JWTCreationException e) {
            return null;
        }
    }

    private static boolean checkThreshold(DecodedJWT jwt, Integer renewThreshold) {
        long mlt = jwt.getClaims().get(MAX_LIVE).asDate().getTime();
        long exp = jwt.getExpiresAt().getTime();
        if (mlt == exp) {
            return false;
        }
        long iat = jwt.getIssuedAt().getTime();
        long cur = System.currentTimeMillis();
        // (cur - iat) / (exp - cur) >= 1
        return Math.floor((cur - iat) / (exp - cur)) >= renewThreshold;
    }
}