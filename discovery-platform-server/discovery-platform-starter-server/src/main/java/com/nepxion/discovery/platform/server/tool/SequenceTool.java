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

import org.apache.commons.lang3.StringUtils;

import com.nepxion.discovery.platform.server.exception.BusinessException;

public final class SequenceTool {
    private static final int MAX_SEQUENCE = 99999;
    private static final long MAX_TOLERATE_TIME_DIFFERENCE_MILLISECONDS = 1000;
    private static long lastMilliseconds;
    private static long sequence = 0;

    public synchronized static long getSequence() {
        long currentMilliseconds = System.currentTimeMillis();
        if (waitTolerateTimeDifferenceIfNeed(currentMilliseconds)) {
            currentMilliseconds = System.currentTimeMillis();
        }

        if (lastMilliseconds == currentMilliseconds) { //同毫秒内的序列号递增
            sequence++;
            if (sequence >= MAX_SEQUENCE) {
                currentMilliseconds = waitUntilNextTime(currentMilliseconds);
            }
        } else {
            sequence = 0;
        }
        lastMilliseconds = currentMilliseconds;
        return Long.parseLong(String.valueOf(currentMilliseconds).concat(StringUtils.leftPad(String.valueOf(sequence), 5, "0")));
    }

    private static boolean waitTolerateTimeDifferenceIfNeed(long currentMilliseconds) {
        if (lastMilliseconds <= currentMilliseconds) {
            return false;
        } else {
            long timeDifferenceMilliseconds = lastMilliseconds - currentMilliseconds;
            if (timeDifferenceMilliseconds < MAX_TOLERATE_TIME_DIFFERENCE_MILLISECONDS) {
                throw new BusinessException(String.format("Clock is moving backwards, last time is %s milliseconds, current time is %s milliseconds", lastMilliseconds, currentMilliseconds));
            }
            CommonTool.sleep(timeDifferenceMilliseconds);
            return true;
        }
    }

    private static long waitUntilNextTime(long lastTime) {
        long result;
        for (result = System.currentTimeMillis(); result <= lastTime; result = System.currentTimeMillis()) {
        }
        return result;
    }
}