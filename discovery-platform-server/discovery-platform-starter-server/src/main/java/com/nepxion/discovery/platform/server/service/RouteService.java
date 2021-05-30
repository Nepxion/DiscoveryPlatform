package com.nepxion.discovery.platform.server.service;

public interface RouteService {
    Integer getNextMaxCreateTimesInDayOfGateway();

    Integer getNextMaxCreateTimesInDayOfZuul();
}
