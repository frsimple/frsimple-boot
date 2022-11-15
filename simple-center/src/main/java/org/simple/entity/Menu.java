package org.simple.entity;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName(value = "center_menu", autoResultMap = true)
public class Menu extends Model<Menu> {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String name;
    private String type;
    private String authcode;
    private String path;
    private String parentid;
    private String component;
    private String sort;
    private String status;
    private LocalDateTime createtime;
    private LocalDateTime updatetime;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private JSONObject meta;

}
