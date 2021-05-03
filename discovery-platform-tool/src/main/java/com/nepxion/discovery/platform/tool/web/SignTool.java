package com.nepxion.discovery.platform.tool.web;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

import cn.hutool.core.util.NumberUtil;
import org.apache.commons.codec.binary.Base64;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;

public final class SignTool {
    private static final String TIMESTAMP_NAME = "_$_current__timestamp_$_";

    public static String sign(final Map<String, Object> params,
                              final String key,
                              final String iv) throws Exception {
        final String data = SignTool.createLinkString(params);
        final Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        final int blockSize = cipher.getBlockSize();
        final byte[] dataBytes = data.getBytes();
        int plaintextLength = dataBytes.length;
        if (plaintextLength % blockSize != 0) {
            plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
        }
        final byte[] plaintext = new byte[plaintextLength];
        System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
        final SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
        final IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
        final byte[] encrypted = cipher.doFinal(plaintext);
        return new String(Base64.encodeBase64(encrypted)).toLowerCase();
    }

    public static boolean checkSign(final String sign,
                                    final Map<String, Object> params,
                                    final String key,
                                    final String iv) throws Exception {
        if (ObjectUtils.isEmpty(sign)) {
            return false;
        }

        final Object timestamp = params.get(TIMESTAMP_NAME);
        if (null == timestamp) {
            return false;
        } else if (!NumberUtil.isLong(timestamp.toString())) {
            return false;
        }
        final Date date = new Date(Long.parseLong(timestamp.toString()));
        final long diff = Math.abs(new Date().getTime() - date.getTime());
        if (diff > 1000 * 60 * 5) {
            return false;
        }
        final String tSign = Objects.requireNonNull(SignTool.sign(params, key, iv)).toLowerCase();
        return tSign.equals(sign);
    }

    private static String createLinkString(final Map<String, Object> params) {
        Assert.notNull(params, "params cannot be null");
        params.entrySet().removeIf(next -> null == next.getValue());
        if (!params.containsKey(TIMESTAMP_NAME)) {
            params.put(TIMESTAMP_NAME, System.currentTimeMillis());
        }

        final List<String> keys = new ArrayList<>(params.keySet());
        Collections.sort(keys);
        final StringBuilder signStr = new StringBuilder();
        for (String key : keys) {
            if (null == params.get(key) || ObjectUtils.isEmpty(params.get(key).toString())) {
                continue;
            }
            signStr.append(key).append("=").append(params.get(key)).append("&");
        }
        if (signStr.length() > 0) {
            return signStr.deleteCharAt(signStr.length() - 1).toString();
        }
        return "";
    }
}