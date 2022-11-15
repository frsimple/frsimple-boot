package org.simple.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class MenuTree {

    private String id;
    private String parentId;
    private String path;
    private String component;
    private String name;
    private String redirect;
    private Map<String,String> meta;
    private List<MenuTree> children;

}
