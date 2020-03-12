package com.luoben.myblog.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * mybatis自定义类型转换器
 *
 * java( boolean) -> db(int)
 */
public class MybatisTypeHandler extends BaseTypeHandler<Boolean> {


    /**
     * java -> DB
     * @param preparedStatement
     * @param i
     * @param parameter
     * @param jdbcType
     * @throws SQLException
     */
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Boolean parameter, JdbcType jdbcType) throws SQLException {
        if(parameter) {
            preparedStatement.setInt(i, 1);
        }else {
            preparedStatement.setInt(i, 0);
        }
    }

    /**
     * db -> java
     * 根据列名获取值
     * @param resultSet
     * @param columnName
     * @return
     * @throws SQLException
     */
    @Override
    public Boolean getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        int value = resultSet.getInt(columnName) ;
        return value == 1?true:false ;
    }

    /**
     * 根据索引获取值
     * @param resultSet
     * @param columnIndex
     * @return
     * @throws SQLException
     */
    @Override
    public Boolean getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {

        int value = resultSet.getInt(columnIndex) ;
        return value == 1?true:false ;
    }

    /**
     * 存储过程拿到值，封装
     * @param callableStatement
     * @param columnIndex
     * @return
     * @throws SQLException
     */
    @Override
    public Boolean getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
        int value = callableStatement.getInt(columnIndex);
        return value == 1?true:false ;
    }
}
