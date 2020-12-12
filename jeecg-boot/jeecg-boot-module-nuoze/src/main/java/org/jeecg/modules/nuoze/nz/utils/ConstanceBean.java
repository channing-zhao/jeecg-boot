package org.jeecg.modules.nuoze.nz.utils;

import lombok.Data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class ConstanceBean {
    @Value("${jeecg.path.upload}")
    private String filePath;
    @Value("${bar.format}")
    private String format;
    @Value("${bar.domain}")
    private String domain;
}
