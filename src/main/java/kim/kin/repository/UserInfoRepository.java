package kim.kin.repository;

import kim.kin.model.UserInfo;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * @author choky
 */
@Repository
public interface UserInfoRepository extends PagingAndSortingRepository<UserInfo, Long> , JpaSpecificationExecutor<UserInfo> {

    /**
     * 根据用户名查找用户
     *
     * @param username 用户名
     * @return
     */
    Optional<UserInfo> findByUsername(String username);


}
