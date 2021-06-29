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

import org.apache.commons.lang3.StringUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.nepxion.discovery.platform.server.constant.PlatformConstant;
import com.nepxion.discovery.platform.server.entity.vo.AdminVo;

public class JwtTool {
    private static final String ID = "i";
    private static final String MAX_LIVE = "mlt";

    private static final String SECRET;
    private static final Duration EXPIRE_TIME;
    private static final Duration MAX_LIVE_TIME;
    private static final Integer RENEW_THRESHOLD;

    public static final String SECRET_KEY = "platform.server.auth.token.secret";
    public static final String EXPIRE_TIME_KEY = "platform.server.auth.token.expireTime";
    public static final String MAX_LIVE_TIME_KEY = "platform.server.auth.token.maxLiveTime";
    public static final String RENEW_THRESHOLD_KEY = "platform.server.auth.token.renewThreshold";

    static {
        String secret = System.getProperty(SECRET_KEY);
        String expireTime = System.getProperty(EXPIRE_TIME_KEY);
        String maxLiveTime = System.getProperty(MAX_LIVE_TIME_KEY);
        String renewThreshold = System.getProperty(RENEW_THRESHOLD_KEY);
        SECRET = StringUtils.isEmpty(secret) ? "nengapszsnuighag": secret;
        EXPIRE_TIME = Duration.parse(expireTime);
        MAX_LIVE_TIME = Duration.parse(maxLiveTime);
        RENEW_THRESHOLD = Integer.valueOf(renewThreshold);
    }

    public static String generateToken(AdminVo adminVo) {
        Date iat = new Date();
        long now = iat.getTime();
        return JWT.create()
                .withAudience(PlatformConstant.PLATFORM)
                .withIssuedAt(iat)
                .withExpiresAt(new Date(now + EXPIRE_TIME.toMillis()))
                .withClaim(ID, adminVo.getId())
                .withClaim(MAX_LIVE, new Date(now + MAX_LIVE_TIME.toMillis()))
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
            Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME.toMillis());
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
        long mlt = jwt.getClaims().get(MAX_LIVE).asDate().getTime();
        long exp = jwt.getExpiresAt().getTime();
        if (mlt == exp) {
            return false;
        }
        long iat = jwt.getIssuedAt().getTime();
        long cur = System.currentTimeMillis();
        // (cur - iat) / (exp - cur) >= 1
        return Math.floor((cur - iat) / (exp - cur)) >= RENEW_THRESHOLD;
    }

}