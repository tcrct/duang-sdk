package com.duangframework.sdk;

import com.duangframework.sdk.annon.ApiParam;
import com.duangframework.sdk.common.AbstractSdkRequest;
import com.duangframework.sdk.common.SdkClient;
import com.duangframework.sdk.common.SdkDto;
import com.duangframework.sdk.common.SdkResponse;
import com.duangframework.sdk.enums.ContentType;
import com.duangframework.sdk.enums.HttpMethod;
import com.duangframework.sdk.utils.ClientKit;
import com.duangframework.sdk.utils.DuangId;
import com.duangframework.sdk.utils.SdkUtils;

import java.util.Map;


/**
 * Hello world!
 *
 */
public class Demo
{
    public static void main( String[] args ) {

        CreateUserDto createUserDto = new CreateUserDto();
        createUserDto.setAccount("laotang");
        createUserDto.setDepartment("研发部");
        createUserDto.setEmail("aa@admin.com");
        createUserDto.setPassword("111");
        createUserDto.setSex(0);
        ItemDto itemDto = new ItemDto();
        itemDto.setBody("这是一封迟来的告白！！！");
        itemDto.setId(new DuangId().toString());
        createUserDto.setItem(itemDto);

//        Demo.demo1(createUserDto);
        Demo.demo2(createUserDto);
    }


    private static void demo1(CreateUserDto createUserDto) {
        SdkResponse response = ClientKit.duang()
//                .uri("/signet-api/Account/create/{USERNAME}/{context_bady}")  // 如果在Dto里有指定了常量API,则不用设置
//                .method(HttpMethod.POST) // 不设置时，默认为POST
//                .conetnttype(ContentType.JSON) // 不设置时，默认为JSON
//                .headers(new HashMap<String, String>()) // 设置header头信息
                .params(createUserDto)
                .send();

        System.out.println(response.getResult());
    }

    private static void demo2(CreateUserDto createUserDto) {
        // 可以设置为全局变量
        SdkClient client = new SdkClient(SdkUtils.getEndPoint(), SdkUtils.getAppKey(), SdkUtils.getAppSecret());
        SdkResponse response = client.execute(new CreateUserRequest(createUserDto));
        System.out.println(response.getResult());
    }
}

/**
 * 创建用户DTO
 */
class CreateUserDto<T> extends SdkDto<T> {
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
    private T item;

    public CreateUserDto() {
    }

    public CreateUserDto(String account, String password, String email, String department, Integer sex) {
        this.account = account;
        this.password = password;
        this.email = email;
        this.department = department;
        this.sex = sex;
    }

    public T getItem() {
        return item;
    }

    public void setItem(T item) {
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

/**
 * 第二层DTO
 */
class ItemDto extends SdkDto {

    private String id;
    @ApiParam(label = "context_bady", name = "context")
    private String body;

    public ItemDto() {
    }

    public ItemDto(String id, String body) {
        this.id = id;
        this.body = body;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}

/**
 * 创建用户请求
 */
class CreateUserRequest extends AbstractSdkRequest {

    public CreateUserRequest(CreateUserDto dto) {
        super(dto);
    }

    public CreateUserRequest(Map<String,String> headerMap, CreateUserDto dto) {
        super(headerMap, dto);
    }


    /**
     * 请求的API地址
     */
    private static final String API = "/signet-api/Account/create/{USERNAME}/{context_bady}";

    @Override
    public String getRequestApi() {
        setRequestApi(API, sdkDto);
        return requestApi;
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.POST;
    }


    @Override
    public String getContentType() {
        return ContentType.JSON.getValue();
    }

    @Override
    public boolean isRestful() {
        return true;
    }
}
