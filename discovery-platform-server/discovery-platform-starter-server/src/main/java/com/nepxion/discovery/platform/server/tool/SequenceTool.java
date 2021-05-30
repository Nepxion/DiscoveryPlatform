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

import com.nepxion.discovery.platform.server.exception.PlatformException;

public class SequenceTool {
    public static final int MAX_SEQUENCE = 99999;
    public static final int MAX_ID_LENGTH = 4;
    public static final long MAX_TOLERATE_TIME_DIFFERENCE_MILLISECONDS = 1000;

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

    public static boolean waitTolerateTimeDifferenceIfNeed(long currentMilliseconds) {
        if (lastMilliseconds <= currentMilliseconds) {
            return false;
        } else {
            long timeDifferenceMilliseconds = lastMilliseconds - currentMilliseconds;
            if (timeDifferenceMilliseconds < MAX_TOLERATE_TIME_DIFFERENCE_MILLISECONDS) {
                throw new PlatformException(String.format("Clock is moving backwards, last time is %s milliseconds, current time is %s milliseconds", lastMilliseconds, currentMilliseconds));
            }
            CommonTool.sleep(timeDifferenceMilliseconds);
            return true;
        }
    }

    public static long waitUntilNextTime(long lastTime) {
        long result;
        for (result = System.currentTimeMillis(); result <= lastTime; result = System.currentTimeMillis()) {
        }
        return result;
    }

    public static String getSequenceId(int nextMaxCreateTimesInDay) {
        return getSequenceId(null, nextMaxCreateTimesInDay);
    }

    public static String getSequenceId(String prefix, int nextMaxCreateTimesInDay) {
        if (StringUtils.isEmpty(prefix)) {
            return String.format("%s-%s", DateTool.getDataSequence(), StringUtils.leftPad(String.valueOf(nextMaxCreateTimesInDay), MAX_ID_LENGTH, "0"));
        }
        return String.format("%s-%s-%s", prefix, DateTool.getDataSequence(), StringUtils.leftPad(String.valueOf(nextMaxCreateTimesInDay), MAX_ID_LENGTH, "0"));
    }
}