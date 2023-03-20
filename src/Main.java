import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        test1();
        test2();
    }

    private static void test1() {
        List<String> blacklisted_ips = Arrays.asList(
                "111.*.255",
                "12.");
        List<String> requests = Arrays.asList(
                "121.3.5.255",
                "12.13.5.255",
                "111.3.5.255",
                "121.3.5.255");

        RequestValidator requestValidator = new RequestValidator();

        List<Integer> result = requestValidator.validateRequests(
                blacklisted_ips,
                requests
        );

        List<Integer> response = Arrays.asList(0, 0, 1, 0);

        // switch on -enableassertions
        assert result.equals(response);
    }

    private static void test2() {
        List<String> blacklisted_ips = Arrays.asList(
                "\\*111.*",
                "123.*",
                "34.*");
        List<String> requests = Arrays.asList(
                "123.1.23.34",
                "121.1.23.34",
                "121.1.23.34",
                "34.1.23.34",
                "121.1.23.34",
                "12.1.23.34",
                "121.1.23.34");

        RequestValidator requestValidator = new RequestValidator();

        List<Integer> result = requestValidator.validateRequests(
                blacklisted_ips,
                requests
        );

        List<Integer> response = Arrays.asList(1, 0, 0, 1, 1, 0, 0);

        // switch on -enableassertions
        assert result.equals(response);
    }
}