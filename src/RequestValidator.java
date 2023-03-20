import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class RequestValidator {

    public List<Integer> validateRequests(List<String> blacklisted_ips, List<String> requests) {
        // Write your code here
        List<Integer> result = new ArrayList<>();
        int unblockedLastRequests = 0;
        int startLastRequestedSec = 0;

        for (int i = 0; i < requests.size(); i++) {
            String reqIp = requests.get(i);

            int lastRequestSec = (i - startLastRequestedSec + 1);

            if (blockRequestFromBlacklist(reqIp, blacklisted_ips)) {
                result.add(1);
                continue;
            }

            if (blockRequestByCondition(unblockedLastRequests, lastRequestSec)) {
                result.add(1);
                unblockedLastRequests = 0;
                startLastRequestedSec = i;
                continue;
            }

            result.add(0);
            unblockedLastRequests++;
        }

        return result;
    }

    private boolean blockRequestFromBlacklist(String ip, List<String> blacklisted_ips) {
        for (String mask : blacklisted_ips) {
            if (Pattern.matches(mask, ip)) {
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
