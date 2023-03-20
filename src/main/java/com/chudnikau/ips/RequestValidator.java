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

            if (blockRequestFromBlacklist(reqIp, blacklisted_ips)) {
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

    private boolean incorrectBlacklistSize(List<String> blacklisted_ips) {
        return blacklisted_ips.size() < 1 || blacklisted_ips.size() > 10;
    }

    private boolean incorrectIPsLength(List<String> list) {
        return list.stream().filter(ip -> ip.length() < 1 || ip.length() > 15).toList().size() > 0;
    }

    private boolean incorrectRequestSize(List<String> requests) {
        return requests.size() < 1 || requests.size() > 1000;
    }

    private boolean blockRequestFromBlacklist(String ip, List<String> blacklisted_ips) {
        for (String mask : blacklisted_ips) {
            String wildcardMask = mask.replaceAll("\\*", "[.0-9]+");
            if (Pattern.matches(wildcardMask, ip)) {
                return true;
            }
        }
        return false;
    }

    private boolean blockRequestByCondition(Integer unblockedLastRequests, Integer lastSec) {
        final int ALLOWED_LAST_UNBLOCKED_IPS = 2;
        final int ALLOWED_LAST_REQUEST_TIME = 5;

        return (unblockedLastRequests >= ALLOWED_LAST_UNBLOCKED_IPS && lastSec >= ALLOWED_LAST_REQUEST_TIME);
    }

}