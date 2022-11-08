package com.minimi.domain.user.response;

import com.minimi.domain.user.entity.User;
import com.minimi.core.helper.EntityCovertFormHelper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoForm {
    private String name;
    private String nickName;
    private String email;
    private String mobile;
    private String password;
    private LocalDateTime createTime;

    public static UserInfoForm entityToForm(User user) {
        EntityCovertFormHelper entityCovertFormHelper = EntityCovertFormHelper.newInstance(user,
                u -> new UserInfoForm(
                        user.getNickName(),
                        user.getNickName(),
                        user.getEmail(),
                        user.getMobile(),
                        user.getPassword(),
                        user.getCreateTime()
                ));
        return (UserInfoForm) entityCovertFormHelper.convert();

    }

}
