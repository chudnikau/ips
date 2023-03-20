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
        List<String> requests = List.of("123.1.23.34");

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
        List<String> requests = List.of("123.1.23.34");

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
        List<String> requests = List.of("123.1.23.34");

        List<Integer> result = requestValidator.validateRequests(
                blacklisted_ips,
                requests
        );

        List<Integer> expected = List.of();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void validateMaxBlackListMaskLength() {
        List<String> blacklisted_ips = List.of("255.255.255.255.255");
        List<String> requests = List.of("123.1.23.34");

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
        List<String> blacklisted_ips = List.of("1.2.3.4");
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
        List<String> blacklisted_ips = List.of("1.2.3.4");
        List<String> requests = List.of("255.255.255.255.255");

        List<Integer> result = requestValidator.validateRequests(
                blacklisted_ips,
                requests
        );

        List<Integer> expected = List.of();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void shouldMask1() {
        List<String> blacklisted_ips = List.of(
                "111.*.255",
                "12.");
        List<String> requests = List.of(
                "121.3.5.255",
                "12.13.5.255",
                "111.3.5.255",
                "121.3.5.255");

        List<Integer> result = requestValidator.validateRequests(
                blacklisted_ips,
                requests
        );

        List<Integer> expected = List.of(0, 0, 1, 0);

        Assert.assertEquals(expected, result);
    }

    @Test
    public void shouldMask2() {
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
    public void shouldMask3() {
        List<String> blacklisted_ips = List.of("*.35");
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

        List<Integer> expected = List.of(0, 0, 0, 0, 1, 0, 0);

        Assert.assertEquals(expected, result);
    }

    @Test
    public void shouldMask4() {
        List<String> blacklisted_ips = List.of("*.34");
        List<String> requests = List.of(
                "123.1.23.34",
                "121.1.23.34",
                "121.1.23.34",
                "34.1.23.34",
                "121.1.23.35",
                "12.1.23.34",
                "121.1.23.34");

        List<Integer> result = requestValidator.validateRequests(
                blacklisted_ips,
                requests
        );

        List<Integer> expected = List.of(1, 1, 1, 1, 0, 1, 1);

        Assert.assertEquals(expected, result);
    }

    @Test
    public void shouldMask5() {
        List<String> blacklisted_ips = List.of("34.*");
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

        List<Integer> expected = List.of(0, 0, 0, 1, 1, 0, 1);

        Assert.assertEquals(expected, result);
    }

    @Test
    public void shouldMask6() {
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
    public void shouldMask7() {
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

        List<Integer> result = requestValidator.validateRequests(
                blacklisted_ips,
                requests
        );

        List<Integer> expected = List.of(1, 0, 1, 0, 1, 1, 0, 0, 1, 1, 1, 0);

        Assert.assertEquals(expected, result);
    }

}
