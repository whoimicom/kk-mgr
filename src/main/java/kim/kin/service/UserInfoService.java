package kim.kin.service;

import kim.kin.model.UserInfoDTO;
import kim.kin.model.UserInfo;

/**
 * @author choky
 */
public interface UserInfoService {
    UserInfo save(UserInfoDTO dto);
}
