package com.example.awarehouse.exception;

import java.time.Instant;

public record BasicErrorDto(Instant timestamp, int statusCode, String statusText, String message, String path) {}
