package com.duangframework.sdk.dto.user;

import com.duangframework.sdk.annon.ApiParam;
import com.duangframework.sdk.common.SdkDto;

/**
 * 创建用户
 *
 * @author laotang
 * @since 1.0
 * @date 2019-01-04
 *
 */
public class CreateUserDto extends SdkDto {

    /**
     * 请求的API地址
     */
    private String REQUEST_API = "/signet-api/Account/create/{USERNAME}/{context_bady}";

    @ApiParam(label = "USERNAME", name = "username")
    private String account; // 帐号
    private String password;    //密码
    private String email;       // 邮件地址
    private String department; // 部门
    private Integer sex; // 性别(0男1女)
    private ItemDto item;

    public CreateUserDto() {
    }

    public CreateUserDto(String account, String password, String email, String department, Integer sex) {
        this.account = account;
        this.password = password;
        this.email = email;
        this.department = department;
        this.sex = sex;
    }

    public ItemDto getItem() {
        return item;
    }

    public void setItem(ItemDto item) {
        this.item = item;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }
}
