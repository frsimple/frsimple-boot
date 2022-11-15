package org.simple.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;


/**
 * 数据字典实体
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/7/23
 */

@Data
@TableName(value = "center_dictionary")
public class Dictionary {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String code;
    private String value;
    private String label;
    private LocalDateTime createtime;
}
