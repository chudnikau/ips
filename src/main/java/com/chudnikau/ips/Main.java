package com.chudnikau.ips;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<String> blacklisted_ips = List.of("*");
        List<String> requests = List.of("121.1.23.34");

        List<Integer> response = new RequestValidator().validateRequests(
                blacklisted_ips,
                requests
        );

        System.out.println(response);
    }
}