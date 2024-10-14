package kim.kin.model;


import java.util.List;

/**
 * @author whoimi
 */
public class UserInfoDTO {
	private String username;
	private String password;
	private Boolean enabled;
	private String avatar;
	private String introduction;
	private String roles;

	private List<UserPermissionVO> vo;

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public List<UserPermissionVO> getVo() {
		return vo;
	}

	public void setVo(List<UserPermissionVO> vo) {
		this.vo = vo;
	}

	@Override
	public String toString() {
		return "UserDTO{" +
				"username='" + username + '\'' +
				", password='" + password + '\'' +
				", enabled=" + enabled +
				", avatar='" + avatar + '\'' +
				", introduction='" + introduction + '\'' +
				", roles='" + roles + '\'' +
				'}';
	}
}
