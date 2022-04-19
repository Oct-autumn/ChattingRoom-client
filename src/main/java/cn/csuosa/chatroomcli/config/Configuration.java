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
    public String serverURL = "127.0.0.1";
    public String serverPort = "8080";
    public String userNick = "";
    public String pwd = "";

    public void debug()
    {
        System.out.println(serverURL + ' ' + serverPort + '\n' + userNick + ' ' + pwd);
    }
}
