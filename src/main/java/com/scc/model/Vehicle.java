package com.scc.model;
public record Vehicle(
        int id,
        String modelName,
        String plateNo,
        double dailyRate,
        String status
) { }
