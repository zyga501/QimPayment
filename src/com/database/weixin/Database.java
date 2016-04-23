package com.database.weixin;

import com.framework.database.DatabaseFramework;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.xml.crypto.Data;
import java.util.List;

public class Database extends DatabaseFramework {
    public static void main(String[] args) throws Exception {

    }

    static {
        String mybatisConfig = "com/database/weixin/conf.xml";
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
