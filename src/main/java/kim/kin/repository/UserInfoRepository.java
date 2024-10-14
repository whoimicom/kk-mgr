package kim.kin.repository;

import kim.kin.model.UserInfo;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;


/**
 * @author choky
 */
@Repository
public interface UserInfoRepository extends CrudRepository<UserInfo, Long>  {

    /**
     * 根据用户名查找用户
     *
     * @param username 用户名
     * @return
     */
    Optional<UserInfo> findByUsername(String username);

    Optional<UserInfo> findByPassword(String password);
    Optional<UserInfo> findByEmail(String email);

}
