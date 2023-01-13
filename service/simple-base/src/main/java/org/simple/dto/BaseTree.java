package org.simple.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author yh_liu
 * @version v1.0
 * @since 2022-7-24
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class BaseTree<T> extends BaseEntity {

    /**
     * 节点父id
     */
    private String parentId;

    /**
     * 节点父名称
     */
    private String parentName;

    /**
     * 当前节点名称
     */
    private String name;

    /**
     * 设置children
     */
    private List<T> children;
}
