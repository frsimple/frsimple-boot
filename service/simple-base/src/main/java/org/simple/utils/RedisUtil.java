package org.simple.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * RedisUtil
 *
 * @author : yh_Liu
 * @version v1.0
 * @since : 2022-12-14
 */
@Component
@RequiredArgsConstructor
public class RedisUtil {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 给一个指定的 key 值附加过期时间
     *
     * @param key  key
     * @param time 过期时间
     * @return true已过期，false未过期
     */
    public boolean expire(String key, long time, TimeUnit timeUnit) {
        return Boolean.TRUE.equals(redisTemplate.expire(key, time, timeUnit));
    }

    /**
     * 根据key判断key是否存在
     *
     * @param key key
     * @return true存在 false不存在
     */
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * 移除指定key的过期时间
     *
     * @param key redis key
     * @return true成功，false失败
     */
    public boolean persist(String key) {
        return Boolean.TRUE.equals(redisTemplate.boundValueOps(key).persist());
    }

    //- - - - - - - - - - - - - - - - - - - - -  String类型 - - - - - - - - - - - - - - - - - - - -

    /**
     * 根据key获取值
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 根据key的过期时间
     *
     * @param key 键
     * @return 值
     */
    public Long getExpire(String key) {
        return key == null ? null : redisTemplate.opsForValue().getOperations().getExpire(key);
    }

    /**
     * 将值放入缓存
     *
     * @param key   键
     * @param value 值
     */
    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 将值放入缓存
     *
     * @param key   键
     * @param value 值
     */
    public void setObj(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 将值放入缓存并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) -1为无期限
     */
    public void set(String key, String value, long time) {
        if (time > 0) {
            redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
        } else {
            redisTemplate.opsForValue().set(key, value);
        }
    }

    /**
     * 批量添加key(重复的键会覆盖)
     *
     * @param keyAndValue 键值对
     */
    public void batchSet(Map<String, String> keyAndValue) {
        redisTemplate.opsForValue().multiSet(keyAndValue);
    }

    /**
     * 批量添加 key-value 只有在键不存在时,才添加
     * map 中只要有一个key存在,则全部不添加
     *
     * @param keyAndValue 键值对
     */
    public void batchSetIfAbsent(Map<String, String> keyAndValue) {
        redisTemplate.opsForValue().multiSetIfAbsent(keyAndValue);
    }

    /**
     * 对一个 key-value 的值进行加减操作,
     * 如果该 key 不存在 将创建一个key 并赋值该 number
     * 如果 key 存在,但 value 不是长整型 ,将报错
     *
     * @param key    键
     * @param number 操作数
     * @return 操作时间
     */
    public Long increment(String key, long number) {
        return redisTemplate.opsForValue().increment(key, number);
    }

    /**
     * 对一个 key-value 的值进行加减操作,
     * 如果该 key 不存在 将创建一个key 并赋值该 number
     * 如果 key 存在,但 value 不是 纯数字 ,将报错
     *
     * @param key    键
     * @param number 操作数
     * @return 操作结果
     */
    public Double increment(String key, double number) {
        return redisTemplate.opsForValue().increment(key, number);
    }

    //- - - - - - - - - - - - - - - - - - - - -  set类型 - - - - - - - - - - - - - - - - - - - -

    /**
     * 将数据放入set缓存
     *
     * @param key   键
     * @param value 值
     */
    public void sSet(String key, String value) {
        redisTemplate.opsForSet().add(key, value);
    }

    /**
     * 获取变量中的值
     *
     * @param key 键
     * @return 值
     */
    public Set<Object> members(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 随机获取变量中指定个数的元素
     *
     * @param key   键
     * @param count 个数
     */
    public void randomMembers(String key, long count) {
        redisTemplate.opsForSet().randomMembers(key, count);
    }

    /**
     * 随机获取变量中的元素
     *
     * @param key 键
     * @return 元素
     */
    public Object randomMember(String key) {
        return redisTemplate.opsForSet().randomMember(key);
    }

    /**
     * 弹出变量中的元素
     *
     * @param key 键
     * @return 元素
     */
    public Object pop(String key) {
        return redisTemplate.opsForSet().pop(key);
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true存在，false不存在
     */
    public boolean sHasKey(String key, Object value) {
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, value));
    }

    /**
     * 检查给定的元素是否在变量中。
     *
     * @param key 键
     * @param obj 元素对象
     * @return true存在，false不存在
     */
    public boolean isMember(String key, Object obj) {
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, obj));
    }

    /**
     * 转移变量的元素值到目的变量。
     *
     * @param key     键
     * @param value   元素对象
     * @param destKey 元素对象
     * @return true成功，false失败
     */
    public boolean move(String key, String value, String destKey) {
        return Boolean.TRUE.equals(redisTemplate.opsForSet().move(key, value, destKey));
    }

    /**
     * 批量移除set缓存中元素
     *
     * @param key    键
     * @param values 值
     */
    public void remove(String key, Object... values) {
        redisTemplate.opsForSet().remove(key, values);
    }

    /**
     * 通过给定的key求2个set变量的差值
     *
     * @param key     键
     * @param destKey 键
     * @return 差值
     */
    public Set<Object> difference(String key, String destKey) {
        return redisTemplate.opsForSet().difference(key, destKey);
    }


    //- - - - - - - - - - - - - - - - - - - - -  hash类型 - - - - - - - - - - - - - - - - - - - -

    /**
     * 加入缓存
     *
     * @param key 键
     * @param map 键
     */
    public void add(String key, Map<String, Object> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    /**
     * 获取变量中的键值对
     *
     * @param key 键
     */
    public Map<Object, Object> entries(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 获取key下的所有hashKey和value
     *
     * @param key 键
     * @return 键值对
     */
    public Map<Object, Object> getHashEntries(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 验证指定key下有没有指定的hashKey
     *
     * @param key     键
     * @param hashKey 哈希键
     * @return 是否存在
     */
    public boolean hashKey(String key, String hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    /**
     * 获取指定key的值string
     *
     * @param key  键
     * @param key2 键
     * @return 值
     */
    public String getMapString(String key, String key2) {
        return Objects.requireNonNull(redisTemplate.opsForHash().get(key, key2)).toString();
    }

    /**
     * 获取指定的值Int
     *
     * @param key  键
     * @param key2 键
     * @return 值
     */
    public Integer getMapInt(String key, String key2) {
        return (Integer) redisTemplate.opsForHash().get(key, key2);
    }

    /**
     * 弹出元素并删除
     *
     * @param key 键
     * @return 值
     */
    public String popValue(String key) {
        return Objects.requireNonNull(redisTemplate.opsForSet().pop(key)).toString();
    }

    /**
     * 删除指定hash的HashKey
     *
     * @param key 键
     */
    public void delete(String key) {
        if (hasKey(key)) {
            redisTemplate.delete(key);
        }
    }

    /**
     * 删除指定hash的HashKey
     *
     * @param key      键
     * @param hashKeys 哈希键
     */
    public void delete(String key, String... hashKeys) {
        if (hasKey(key)) {
            redisTemplate.opsForHash().delete(key, (Object) hashKeys);
        }
    }

    /**
     * 给指定hash的hashKey做增减操作
     *
     * @param key     键
     * @param hashKey 哈希键
     * @param number  数字
     * @return 操作后的值
     */
    public Long increment(String key, String hashKey, long number) {
        return redisTemplate.opsForHash().increment(key, hashKey, number);
    }

    /**
     * 给指定hash的hashKey做增减操作
     *
     * @param key     键
     * @param hashKey 哈希键
     * @param number  数字
     * @return 操作后的值
     */
    public Double increment(String key, String hashKey, Double number) {
        return redisTemplate.opsForHash().increment(key, hashKey, number);
    }

    /**
     * 获取key下的所有hashKey字段
     *
     * @param key 键
     * @return hashKey集合
     */
    public Set<Object> hashKeys(String key) {
        return redisTemplate.opsForHash().keys(key);
    }

    /**
     * 获取指定hash下面的键值对数量
     *
     * @param key 键
     * @return 数量
     */
    public Long hashSize(String key) {
        return redisTemplate.opsForHash().size(key);
    }

    //- - - - - - - - - - - - - - - - - - - - -  list类型 - - - - - - - - - - - - - - - - - - - -

    /**
     * 在变量左边添加元素值
     *
     * @param key   键
     * @param value 元素
     */
    public void leftPush(String key, Object value) {
        redisTemplate.opsForList().leftPush(key, value);
    }


    /**
     * 获取指定区间的值。
     *
     * @param key   键
     * @param start 开始位置
     * @param end   结束位置
     * @return 值列表
     */
    public List<Object> range(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * 把最后一个参数值放到指定集合的第一个出现中间参数的前面，
     * 如果中间参数值存在的话。
     *
     * @param key   键
     * @param pivot 中间参数
     * @param value 操作树
     */
    public void leftPush(String key, String pivot, String value) {
        redisTemplate.opsForList().leftPush(key, pivot, value);
    }

    /**
     * 向左边批量添加参数元素。
     *
     * @param key    键
     * @param values 元素集合
     */
    public void leftPushAll(String key, String... values) {
        redisTemplate.opsForList().leftPushAll(key, values);
    }

    /**
     * 向集合最右边添加元素。
     *
     * @param key   键
     * @param value 元素
     */
    public void leftPushAll(String key, String value) {
        redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * 向左边批量添加参数元素。
     *
     * @param key    键
     * @param values 元素集合
     */
    public void rightPushAll(String key, String... values) {
        redisTemplate.opsForList().rightPushAll(key, values);
    }

    /**
     * 向已存在的集合中添加元素。
     *
     * @param key   键
     * @param value 元素
     */
    public void rightPushIfPresent(String key, Object value) {
        redisTemplate.opsForList().rightPushIfPresent(key, value);
    }

    /**
     * 移除集合中的左边第一个元素。
     *
     * @param key 键
     */
    public void leftPop(String key) {
        redisTemplate.opsForList().leftPop(key);
    }

    /**
     * 移除集合中左边的元素在等待的时间里，如果超过等待的时间仍没有元素则退出。
     *
     * @param key     键
     * @param timeout 过期时间
     * @param unit    时间类型
     */
    public void leftPop(String key, long timeout, TimeUnit unit) {
        redisTemplate.opsForList().leftPop(key, timeout, unit);
    }

    /**
     * 移除集合中右边的元素。
     *
     * @param key 键
     */
    public void rightPop(String key) {
        redisTemplate.opsForList().rightPop(key);
    }

    /**
     * 移除集合中右边的元素在等待的时间里，如果超过等待的时间仍没有元素则退出。
     *
     * @param key     键
     * @param timeout 过期时间
     * @param unit    时间类型
     */
    public void rightPop(String key, long timeout, TimeUnit unit) {
        redisTemplate.opsForList().rightPop(key, timeout, unit);
    }
}