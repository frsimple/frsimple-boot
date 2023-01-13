package org.simple.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.simple.dto.BaseTree;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author yh_liu
 * @since 2022-7-24
 */
public class TreeUtil<T extends BaseTree<T>> {

    /**
     * 递归查询父节点
     *
     * @param <T>     实体
     * @param data    条件的的数据
     * @param dataAll 所有的数据
     * @return 节点信息
     */
    public static <T> JSONArray treeWhere(List<T> data, List<T> dataAll) {
        String id = "id";
        String parentId = "parentId";
        JSONArray jsonArray = JSONUtil.parseArray(data);
        JSONArray result = new JSONArray();
        if (jsonArray.size() == dataAll.size()) {
            return jsonArray;
        }
        for (Object o : jsonArray) {
            JSONObject json = (JSONObject) o;
            if (result.stream().noneMatch(t -> t.equals(json))) {
                result.add(json);
            }
            if (!ObjectUtil.isNull(parentId)) {
                result(dataAll, json, result, id, parentId);
            }
        }
        return result;
    }

    /**
     * 递归查询父节点
     *
     * @param dataAll  所有数据
     * @param json     当前对象
     * @param result   结果数据
     * @param id       id
     * @param parentId parentId
     * @return 节点数据
     */
    private static <T> JSONArray result(List<T> dataAll, JSONObject json, JSONArray result, String id, String parentId) {
        JSONArray jsonArray = JSONUtil.parseArray(JSONUtil.toJsonStr(dataAll));
        for (Object o : jsonArray) {
            JSONObject aVal = (JSONObject) o;
            String ids = aVal.getStr(id);
            String parentIds = aVal.getStr(parentId);
            if (ObjectUtil.isNotNull(json.getStr(parentId))) {
                if (json.getStr(parentId).equals(ids)) {
                    if (result.stream().noneMatch(t -> t.equals(aVal))) {
                        result.add(aVal);
                    }
                    if (ObjectUtil.isNull(parentIds)) {
                        break;
                    }
                    result(dataAll, aVal, result, id, parentId);
                }
            }
        }
        return result;
    }

    /**
     * 递归查找所有菜单的子菜单
     *
     * @param root 父级菜单
     * @param all  所有菜单
     * @return 子菜单
     */
    public List<T> getChildren(T root, List<T> all) {
        return all.stream()
                .filter(node -> Objects.equals(node.getParentId(), root.getId()))
                .peek(node -> {
                    node.setParentName(root.getName());
                    node.setChildren(getChildren(node, all));
                })
                .collect(Collectors.toList());
    }

    /**
     * 转化tree
     *
     * @param list 父级菜单
     * @return 子菜单
     */
    public List<T> buildTree(List<T> list, String rootId) {
        return list.stream()
                .filter(node -> ObjectUtil.isNull(node.getParentId()) || ObjectUtil.isNull(node.getParentId()) || rootId.equals(node.getParentId()))
                .peek(node -> node.setChildren(getChildren(node, list)))
                .collect(Collectors.toList());
    }

    /**
     * 转化tree
     *
     * @param list 父级菜单
     * @return 子菜单
     */
    public List<T> buildTree(List<T> list) {
        return list.stream()
                .filter(node -> ObjectUtil.isNull(node.getParentId()) || ObjectUtil.isNull(node.getParentId()) || "0".equals(node.getParentId()))
                .peek(node -> node.setChildren(getChildren(node, list)))
                .collect(Collectors.toList());
    }
}
