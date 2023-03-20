package com.chudnikau.ips;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class RequestValidatorTest {

    private final RequestValidator requestValidator = new RequestValidator();

    @Test
    public void validateEmptyBlackList() {
        List<String> blacklisted_ips = List.of();
        List<String> requests = List.of("255.255.255.255");

        List<Integer> result = requestValidator.validateRequests(
                blacklisted_ips,
                requests
        );

        List<Integer> expected = List.of();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void validateSizeBlackList() {
        List<String> blacklisted_ips = List.of(
                "*.0", "*.1", "*.2", "*.3", "*.4", "*.5", "*.6", "*.7", "*.8", "*.9", "*.0"
        );
        List<String> requests = List.of("255.255.255.255");

        List<Integer> result = requestValidator.validateRequests(
                blacklisted_ips,
                requests
        );

        List<Integer> expected = List.of();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void validateMinBlackListMaskLength() {
        List<String> blacklisted_ips = List.of("");
        List<String> requests = List.of("255.255.255.255");

        List<Integer> result = requestValidator.validateRequests(
                blacklisted_ips,
                requests
        );

        List<Integer> expected = List.of();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void validateMaxBlackListMaskLength() {
        List<String> blacklisted_ips = List.of("255.255.255.255.0");
        List<String> requests = List.of("255.255.255.255");

        List<Integer> result = requestValidator.validateRequests(
                blacklisted_ips,
                requests
        );

        List<Integer> expected = List.of();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void validateEmptyRequestList() {
        List<String> blacklisted_ips = List.of("*");
        List<String> requests = List.of();

        List<Integer> result = requestValidator.validateRequests(
                blacklisted_ips,
                requests
        );

        List<Integer> expected = List.of();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void validateSizeRequestList() {
        List<String> blacklisted_ips = List.of("*");
        List<String> requests = new ArrayList<>();

        Stream.generate(() -> new Random().nextInt(127))
                .limit(1001)
                .forEach(val -> requests
                        .add(String.format("%d.%d.%d.%d", val + 5, val + 10, val + 15, val + 20)));

        List<Integer> result = requestValidator.validateRequests(
                blacklisted_ips,
                requests
        );

        List<Integer> expected = List.of();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void validateMinRequestMaskLength() {
        List<String> blacklisted_ips = List.of("255.255.255.255");
        List<String> requests = List.of("");

        List<Integer> result = requestValidator.validateRequests(
                blacklisted_ips,
                requests
        );

        List<Integer> expected = List.of();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void validateMaxRequestMaskLength() {
        List<String> blacklisted_ips = List.of("255.255.255.255");
        List<String> requests = List.of("255.255.255.255.0");

        List<Integer> result = requestValidator.validateRequests(
                blacklisted_ips,
                requests
        );

        List<Integer> expected = List.of();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void shouldMultipleBlackListMasks() {
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

        List<Integer> result = requestValidator.validateRequests(
                blacklisted_ips,
                requests
        );

        List<Integer> expected = List.of(1, 0, 0, 1, 1, 0, 0);

        Assert.assertEquals(expected, result);
    }

    @Test
    public void shouldStartsWildcard() {
        List<String> blacklisted_ips = List.of("*.34");
        List<String> requests = List.of(
                "255.255.255.34",
                "255.255.255.34",
                "255.255.255.34",
                "255.255.255.34",
                "255.255.255.35",
                "255.255.255.34",
                "255.255.255.34");

        List<Integer> result = requestValidator.validateRequests(
                blacklisted_ips,
                requests
        );

        List<Integer> expected = List.of(1, 1, 1, 1, 0, 1, 1);

        Assert.assertEquals(expected, result);
    }

    @Test
    public void shouldEndsWildcard() {
        List<String> blacklisted_ips = List.of("34.*");
        List<String> requests = List.of(
                "123.255.255.255",
                "121.255.255.255",
                "121.255.255.255",
                "34.255.255.255",
                "121.255.255.255",
                "12.255.255.255",
                "34.255.255.255");

        List<Integer> result = requestValidator.validateRequests(
                blacklisted_ips,
                requests
        );

        List<Integer> expected = List.of(0, 0, 0, 1, 1, 0, 1);

        Assert.assertEquals(expected, result);
    }

    @Test
    public void shouldWildcard() {
        List<String> blacklisted_ips = List.of("*");
        List<String> requests = List.of(
                "123.1.23.34",
                "121.1.23.34",
                "121.1.23.34",
                "34.1.34.34",
                "121.1.34.35",
                "12.1.23.34",
                "34.1.23.34");

        List<Integer> result = requestValidator.validateRequests(
                blacklisted_ips,
                requests
        );

        List<Integer> expected = List.of(1, 1, 1, 1, 1, 1, 1);

        Assert.assertEquals(expected, result);
    }

    @Test
    public void shouldComplexWildcard() {
        List<String> blacklisted_ips = List.of("*.1.*.34");
        List<String> requests = List.of(
                "123.1.23.34",
                "255.1.255.35",
                "255.1.255.34",
                "255.1.255.100",
                "255.1.255.1",
                "255.1.255.34",
                "255.1.255.44",
                "255.1.255.45",
                "255.1.255.34",
                "255.2.255.34",
                "255.1.255.34",
                "255.1.255.55"
        );

        List<Integer> result = requestValidator.validateRequests(
                blacklisted_ips,
                requests
        );

        List<Integer> expected = List.of(1, 0, 1, 0, 1, 1, 0, 0, 1, 1, 1, 0);

        Assert.assertEquals(expected, result);
    }

    @Test
    public void shouldWildcardAndNumber() {
        List<String> blacklisted_ips = List.of("2*.*1*.*12");

        List<String> requests = List.of(
                "255.211.255.212",
                "255.111.255.212",
                "255.255.255.212",
                "200.255.011.212"
        );

        List<Integer> result = requestValidator.validateRequests(
                blacklisted_ips,
                requests
        );

        List<Integer> expected = List.of(1, 1, 0, 1);

        Assert.assertEquals(expected, result);
    }

}
