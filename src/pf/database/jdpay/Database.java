package pf.database.jdpay;

import framework.database.DatabaseFramework;
import org.apache.ibatis.session.SqlSessionFactory;

public class Database extends DatabaseFramework {
    public static void main(String[] args) throws Exception {

    }

    static {
        String mybatisConfig = "pf/database/jdpay/conf.xml";
        sqlSessionFactory_ = DatabaseFramework.buildSqlSessionFactory(mybatisConfig);
    }

    public static Database Instance() {
        return instance_;
    }

    private Database() {}

    protected SqlSessionFactory sqlSessionFactory() {
        return sqlSessionFactory_;
    }

    private static final Database instance_ = new Database();
    private static SqlSessionFactory sqlSessionFactory_;
}