package jyu.secret.model;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table SECRET.
 */
public class Secret {

    private Long id;
    private String title;
    private Long userId;
    private String name;
    private Long level;
    private String pwd;
    private java.util.Date createdDate;
    private java.util.Date updatedDate;

    public Secret() {
    }

    public Secret(Long id) {
        this.id = id;
    }

    public Secret(Long id, String title, Long userId, String name, Long level, String pwd, java.util.Date createdDate, java.util.Date updatedDate) {
        this.id = id;
        this.title = title;
        this.userId = userId;
        this.name = name;
        this.level = level;
        this.pwd = pwd;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getLevel() {
        return level;
    }

    public void setLevel(Long level) {
        this.level = level;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public java.util.Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(java.util.Date createdDate) {
        this.createdDate = createdDate;
    }

    public java.util.Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(java.util.Date updatedDate) {
        this.updatedDate = updatedDate;
    }

}
