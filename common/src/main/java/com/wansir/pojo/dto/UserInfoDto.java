package com.wansir.pojo.dto;


import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wanlanfeng
 * @version 1.0
 * @description TODO
 * @date 2023/5/11 14:02
 */
@Data
@Accessors(chain = true)
public class UserInfoDto {
        /**
         * 主键
         */
        private Long id;

        /**
         * 昵称
         */
        private String nickName;

        /**
         * 头像
         */
        private String avatar;

        private String sex;

        private String email;


}
