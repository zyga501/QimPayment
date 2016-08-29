package pf.database.merchant;

import java.util.Date;

public class OAuthLogin {
    public static void main(String[] args) throws Exception {

    }

    public static OAuthLogin getOAuthLoginByOpenid(long id) {
        String statement = "pf.database.merchant.mapping.oAuthLogin.getOAuthLoginByOpenid";
        return Database.Instance().selectOne(statement, id);
    }

    public static OAuthLogin getOAuthLoginByRmdno(String subId) {
        String statement = "pf.database.merchant.mapping.oAuthLogin.getOAuthLoginByRmdno";
        return Database.Instance().selectOne(statement, subId);
    }

    public static boolean insertOAuthLogin(OAuthLogin oAuthLogin) {
            String statement = "pf.database.merchant.mapping.oAuthLogin.insertOAuthLogin";
            return Database.Instance().insert(statement, oAuthLogin)==1;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Date getInserttime() {
        return inserttime;
    }

    public void setInserttime(Date inserttime) {
        this.inserttime = inserttime;
    }

    public String getRmdno() {
        return rmdno;
    }

    public void setRmdno(String rmdno) {
        this.rmdno = rmdno;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;
    private String openid;
    private Date inserttime;
    private String rmdno;
}
