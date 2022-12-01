package org.simple.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName(value = "code_datasource")
public class DataSource {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String url;
    private String user;
    private String pwd;
    private String name;

}
