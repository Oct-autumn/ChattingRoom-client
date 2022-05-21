package cn.csuosa.chatroomcli.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Configuration
{
    /**
     * 服务器URL
     */
    public String serverURL = "127.0.0.1";
    /**
     * 服务器端口
     */
    public String serverPort = "8080";
    /**
     * 用户名
     */
    public String userAccount = "";
    /**
     * 认证密码
     */
    public String pwd = "";

    public void debug()
    {
        System.out.println(serverURL + ' ' + serverPort + '\n' + userAccount + ' ' + pwd);
    }
}
