package org.simple.dto;

import lombok.Data;

@Data
public class EmailParams {

    private String title;
    private String content;
    private String tos;
    private boolean isHtml;
}
