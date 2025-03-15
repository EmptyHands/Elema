package Server.Entitles;

/**
 * @author xiezhr
 * @create 2024-04-25 19:34
 */
public class Admin{
    String adminName;
    String password;

    public Admin(String adminName, String password){
        this.adminName = adminName;
        this.password = password;
    }

    public void setAdminName(String adminName){
        this.adminName = adminName;
    }

    public String getAdminName(){
        return adminName;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getPassword(){
        return password;
    }


}
