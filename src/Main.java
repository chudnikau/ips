import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        test1();
        test2();
        test3();
        test4();
        test5();
        test6();
        test7();
    }

    private static void test1() {
        List<String> blacklisted_ips = List.of(
                "111.*.255",
                "12.");
        List<String> requests = List.of(
                "121.3.5.255",
                "12.13.5.255",
                "111.3.5.255",
                "121.3.5.255");

        RequestValidator requestValidator = new RequestValidator();

        List<Integer> result = requestValidator.validateRequests(
                blacklisted_ips,
                requests
        );

        List<Integer> response = List.of(0, 0, 1, 0);

        assert result.equals(response);
    }

    private static void test2() {
        List<String> blacklisted_ips = List.of(
                "*111.*",
                "123.*",
                "34.*");
        List<String> requests = List.of(
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

        List<Integer> response = List.of(1, 0, 0, 1, 1, 0, 0);

        assert result.equals(response);
    }

    private static void test3() {
        List<String> blacklisted_ips = List.of(
                "*.35");
        List<String> requests = List.of(
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

        List<Integer> response = List.of(0, 0, 0, 0, 1, 0, 0);

        assert result.equals(response);
    }

    private static void test4() {
        List<String> blacklisted_ips = List.of(
                "*.34");
        List<String> requests = List.of(
                "123.1.23.34",
                "121.1.23.34",
                "121.1.23.34",
                "34.1.23.34",
                "121.1.23.35",
                "12.1.23.34",
                "121.1.23.34");

        RequestValidator requestValidator = new RequestValidator();

        List<Integer> result = requestValidator.validateRequests(
                blacklisted_ips,
                requests
        );

        List<Integer> response = List.of(1, 1, 1, 1, 0, 1, 1);

        assert result.equals(response);
    }

    private static void test5() {
        List<String> blacklisted_ips = List.of(
                "34.*");
        List<String> requests = List.of(
                "123.1.23.34",
                "121.1.23.34",
                "121.1.23.34",
                "34.1.34.34",
                "121.1.34.35",
                "12.1.23.34",
                "34.1.23.34");

        RequestValidator requestValidator = new RequestValidator();

        List<Integer> result = requestValidator.validateRequests(
                blacklisted_ips,
                requests
        );

        List<Integer> response = List.of(0, 0, 0, 1, 1, 0, 1);

        assert result.equals(response);
    }

    private static void test6() {
        List<String> blacklisted_ips = List.of(
                "*");
        List<String> requests = List.of(
                "123.1.23.34",
                "121.1.23.34",
                "121.1.23.34",
                "34.1.34.34",
                "121.1.34.35",
                "12.1.23.34",
                "34.1.23.34");

        RequestValidator requestValidator = new RequestValidator();

        List<Integer> result = requestValidator.validateRequests(
                blacklisted_ips,
                requests
        );

        List<Integer> response = List.of(1, 1, 1, 1, 1, 1, 1);

        assert result.equals(response);
    }

    private static void test7() {
        List<String> blacklisted_ips = List.of(
                "*.1.*.34");
        List<String> requests = List.of(
                "123.1.23.34",
                "121.1.23.35",
                "121.1.23.34",
                "34.1.34.100",
                "121.1.1.1",
                "12.1.23.34",
                "34.1.23.44",
                "34.1.23.45",
                "100.1.23.34",
                "200.2.56.34",
                "34.1.23.34",
                "34.1.23.55"
                );

        RequestValidator requestValidator = new RequestValidator();

        List<Integer> result = requestValidator.validateRequests(
                blacklisted_ips,
                requests
        );

        List<Integer> response = List.of(1, 0, 1, 0, 1, 1, 0, 0, 1, 1, 1, 0);

        assert result.equals(response);
    }
}