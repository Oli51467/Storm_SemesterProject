package org.qdu.storm.jdbcUtils;

import org.apache.storm.trident.state.StateType;

/*
    连接JDBC配置信息
 */
public class JDBCStateConfig {

    private String url;
    private String driver;
    private String username;
    private String password;
    private String table;
    private int batchSize;
    private String cols;
    private String colVals;
    private int cacheSize = 100;
    private StateType type = StateType.OPAQUE;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public String getCols() {
        return cols;
    }

    public void setCols(String cols) {
        this.cols = cols;
    }

    public String getColVals() {
        return colVals;
    }

    public void setColVals(String colVals) {
        this.colVals = colVals;
    }

    public int getCacheSize() {
        return cacheSize;
    }

    public void setCacheSize(int cacheSize) {
        this.cacheSize = cacheSize;
    }

    public StateType getType() {
        return type;
    }

    public void setType(StateType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Test2StateConfig [url=" + url + ", driver=" + driver + ", username=" + username + ", password="
                + password + ", table=" + table + ", batchSize=" + batchSize + ", cols=" + cols
                + ", colVals=" + colVals + ", cacheSize=" + cacheSize + ", type=" + type + "]";
    }

}