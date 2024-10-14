package kim.kin.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;

/**
 * @author whoimi
 */
public class UserPermissionVO implements Serializable {

    private String name;

    private String path;

    private String redirect;

    private String component;

    private Boolean alwaysShow;

    private MetaVO meta;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<UserPermissionVO> children;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public Boolean getAlwaysShow() {
        return alwaysShow;
    }

    public void setAlwaysShow(Boolean alwaysShow) {
        this.alwaysShow = alwaysShow;
    }

    public MetaVO getMeta() {
        return meta;
    }

    public void setMeta(MetaVO meta) {
        this.meta = meta;
    }

    public List<UserPermissionVO> getChildren() {
        return children;
    }

    public void setChildren(List<UserPermissionVO> children) {
        this.children = children;
    }

    public UserPermissionVO() {
        super();
    }

    public UserPermissionVO(String name, String path, String redirect, String component, Boolean alwaysShow, MetaVO meta, List<UserPermissionVO> children) {
        this.name = name;
        this.path = path;
        this.redirect = redirect;
        this.component = component;
        this.alwaysShow = alwaysShow;
        this.meta = meta;
        this.children = children;
    }

    @Override
    public String toString() {
        return "UserPermissionVO{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", redirect='" + redirect + '\'' +
                ", component='" + component + '\'' +
                ", alwaysShow=" + alwaysShow +
                ", meta=" + meta +
                ", children=" + children +
                '}';
    }
}