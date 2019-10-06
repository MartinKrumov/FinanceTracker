package com.tracker.common.util.mail;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.thymeleaf.context.Context;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MailMessage {

    private String to;
    private MailType mailType;
    private Context context;
}
