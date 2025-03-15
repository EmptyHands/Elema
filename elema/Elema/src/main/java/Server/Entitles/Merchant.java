package Server.Entitles;

/**
 * @author xiezhr
 * @create 2024-04-27 17:46
 */
public class Merchant {
    int businessId;
    String password;

    public Merchant(int businessId, String password){
        this.businessId = businessId;
        this.password = password;
    }

    public String getPassword(){
        return password;
    }

}
