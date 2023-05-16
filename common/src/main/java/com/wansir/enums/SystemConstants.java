package com.wansir.enums;


/**
 * @author wanlanfeng
 * @version 1.0
 * @description 文章有关的常量
 * @date 2023/5/11 9:21
 */
public class SystemConstants {

    /**
     *  文章是草稿：status=1
     */
    public static final int ARTICLE_STATUS_DRAFT = 1;
    /**
     *  文章已发布：status=0
     */
    public static final int ARTICLE_STATUS_NORMAL = 0;

    /**
     * 分类正常状态：0
     */
    public static final String STATUS_NORMAL = "0";
    /**
     * 友链状态为审核通过
     */
    public static final String LINK_STATUS_NORMAL = "0";

    /**
     * redis中保存的用户信息key的前缀
     */
    public static final String REDIS_LOGIN_KEY_PREFIX = "bloglogin:";
    /**
     * 评论类别：0表示文章评论
     */
    public static final String ARTICLE_COMMENT = "0";

    /**
     * 评论类别：1表示友链评论
     */
    public static final String LINK_COMMENT = "1";
    /**
     * 文章浏览量对应的redis键
     */
    public static final String ARTICLE_VIEWCOUNT_KEY = "article:viewCount";

    /**
     * 后台系统用户登录后保存到redis的key
     */
    public static final String ADMIN_USER_KEY = "adminlogin:";
    /**
     * 菜单
     */
    public static final String MENU = "C";
    /**
     * 按钮
     */
    public static final String BUTTON = "F";

    /**
     * 菜单根id
     */
    public static final Long MENU_ROOT_ID = 0L;

    /**
     * ADMIN表示用户是后台用户
     */
    public static final String ADMAIN = "1";
    /**
     * 正常角色的状态为0
     */
    public static final String ROLE_NOMAL_STATUS = "0";
}
