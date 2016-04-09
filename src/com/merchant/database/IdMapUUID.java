package com.merchant.database;

import org.apache.ibatis.session.SqlSession;

public class IdMapUUID {
    public static void main(String[] args) throws Exception {
        SqlSession sqlSession = com.merchant.database.Database.SqlSessionFactory().openSession();
        String statement = "com.merchant.database.mapping.idMapUUID.getMappingByUUID";
        IdMapUUID idMapUUID = sqlSession.selectOne(statement, "C1D9902E-91F7-4096-BA8A-633F32CCD171");
        sqlSession.close();
    }

    public static IdMapUUID getMappingByUUID(String odod) {
        SqlSession sqlSession = com.merchant.database.Database.SqlSessionFactory().openSession();
        String statement = "com.merchant.database.mapping.idMapUUID.getMappingByUUID";
        IdMapUUID idMapUUID = sqlSession.selectOne(statement, odod);
        sqlSession.close();
        return idMapUUID;
    }

    public long getId() {
        return id_;
    }

    public void setId(long id) {
        this.id_ = id;
    }

    public String getUuid() {
        return uuid_;
    }

    public void setUuid(String uuid) {
        this.uuid_ = uuid;
    }

    private long id_;
    private String uuid_;
}
