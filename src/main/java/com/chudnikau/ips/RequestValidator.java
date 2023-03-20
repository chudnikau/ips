package com.chudnikau.ips;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class RequestValidator {

    public List<Integer> validateRequests(List<String> blacklisted_ips, List<String> requests) {
        List<Integer> result = new ArrayList<>();

        boolean incorrectInput =
                incorrectIPsLength(blacklisted_ips) ||
                        incorrectIPsLength(requests) ||
                        incorrectBlacklistSize(blacklisted_ips) ||
                        incorrectRequestSize(requests);

        if (incorrectInput) {
            return result;
        }

        int unblockedLastRequestsCount = 0;
        int startLastRequestedSec = 0;

        for (int i = 0; i < requests.size(); i++) {
            String reqIp = requests.get(i);

            int lastRequestSec = (i - startLastRequestedSec + 1);

            if (blockIpFromBlacklist(reqIp, blacklisted_ips)) {
                result.add(1);
                continue;
            }

            if (blockRequestByCondition(unblockedLastRequestsCount, lastRequestSec)) {
                result.add(1);
                unblockedLastRequestsCount = 0;
                startLastRequestedSec = i;
                continue;
            }

            result.add(0);
            unblockedLastRequestsCount++;
        }

        return result;
    }

    private boolean incorrectBlacklistSize(List<String> list) {
        return list.size() < 1 || list.size() > 10;
    }

    private boolean incorrectIPsLength(List<String> list) {
        return list.stream().filter(ip -> ip.length() < 1 || ip.length() > 15).toList().size() > 0;
    }

    private boolean incorrectRequestSize(List<String> list) {
        return list.size() < 1 || list.size() > 1000;
    }

    private boolean blockIpFromBlacklist(String ip, List<String> blacklist) {
        for (String mask : blacklist) {
            String wildcardMask = mask.replaceAll("\\*", "[.0-9]+");
            if (Pattern.matches(wildcardMask, ip)) {
                return true;
            }
        }
        return false;
    }

    private boolean blockRequestByCondition(Integer unblockedLastIPs, Integer lastSeconds) {
        return (unblockedLastIPs >= 2 && lastSeconds >= 5);
    }

}
