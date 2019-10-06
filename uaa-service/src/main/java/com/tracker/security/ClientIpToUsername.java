package com.tracker.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
class ClientIpToUsername implements Serializable {

    private String ip;
    private String username;
}
