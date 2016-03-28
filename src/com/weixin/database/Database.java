package com.weixin.database;

import com.framework.database.DatabaseFramework;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class Database extends DatabaseFramework {
    public static void main(String[] args) throws Exception {
        SqlSession sqlSession = Database.SqlSessionFactory().openSession(false);
        sqlSession.close();
    }

    public static SqlSessionFactory SqlSessionFactory() {
        return DatabaseFramework.SqlSessionFactory();
    }

    static {
        String mybatisConfig = "com/weixin/database/conf.xml";
        DatabaseFramework.buildSqlSessionFactory(mybatisConfig);
    }
}
