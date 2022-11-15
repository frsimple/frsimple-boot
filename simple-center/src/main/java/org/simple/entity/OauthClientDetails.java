package org.simple.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

/**
 * OauthClientDetails
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */
@Data
@TableName(value = "oauth_client_details")
public class OauthClientDetails {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String clientId;

    private String resourceIds;
    private String clientSecret;
    private String scope;
    private String authorizedGrantTypes;
    private String webServerRedirectUri;
    private String authorities;
    private Integer accessTokenValidity;
    private Integer refreshTokenValidity;
    private String additionalInformation;
    private Timestamp createTime;
    private Integer archived;
    private Integer trusted;
    private String autoapprove;


}
