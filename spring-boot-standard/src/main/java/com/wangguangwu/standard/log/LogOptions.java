package com.wangguangwu.standard.log;

import lombok.*;

/**
 * @author wangguangwu
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LogOptions {

    private boolean url;

    private boolean request;

    private boolean response;

    public static LogOptions all() {
        return LogOptions.builder()
                .url(true)
                .request(true)
                .response(true)
                .build();
    }

    public static LogOptions none() {
        return LogOptions.builder()
                .url(false)
                .request(false)
                .response(false)
                .build();
    }
}
