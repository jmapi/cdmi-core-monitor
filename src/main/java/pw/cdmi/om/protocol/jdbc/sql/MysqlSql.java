package pw.cdmi.om.protocol.jdbc.sql;

/************************************************************
 * Mysql数据库的JDBC监控语句.<br/>
 * @author Roger
 * @version CDMI Service Platform, 2016年1月8日
 ************************************************************/
public enum MysqlSql {

    // 没有容量的概念，只有当前数据库的大小(存了多少数据)
    dbSize("数据库大小",
        "select table_schema ,sum(DATA_LENGTH) as data_size,sum(INDEX_LENGTH) as index_size,(sum(DATA_LENGTH)+sum(INDEX_LENGTH)) as total_size,count(table_schema) as table_num,engine from information_schema.tables group by table_schema;"),
    tableSize(
        "表大小",
        "select table_schema,table_name,sum(DATA_LENGTH),sum(INDEX_LENGTH) from information_schema.tables where table_schema=? group by table_name;"),

    version("版本","select version();"),

    systemTime("系统时间","select now();"),
    
    globalVariables("globalVariables","show global variables;"),
    globalStatus("globalStatus","show global status;"),
    
    connections("数据库连接信息","show status like '%connections';"),

    // QPS = Questions(or Queries) / seconds
    QPS("QPS(每秒Query量)","show  global  status like 'Question%';"),

    // TPS = (Com_commit + Com_rollback) / seconds
    TPS_commit("TPS(每秒事务量)","show global status like 'Com_commit';"),
    TPS_rollback("TPS(每秒事务量)","show global status like 'Com_rollback';"),

    //主键击中率
    // key_buffer_read_hits = (1-key_reads / key_read_requests) * 100%
    keyBuffer_read("key Buffer 命中率","show global status like 'Key_read%';"),
    // key_buffer_write_hits = (1-key_writes / key_write_requests) * 100%
    keyBuffer_write("key Buffer 命中率","show global status like 'key_write%';"),

    // innodb_buffer_read_hits = (1 - innodb_buffer_pool_reads / innodb_buffer_pool_read_requests) * 100%
    innoDBBuffer("InnoDB Buffer命中率","show status like 'innodb_buffer_pool_read%';"),

    // Query_cache_hits = (Qcahce_hits / (Qcache_hits + Qcache_inserts )) * 100%;
    queryCache("Query Cache命中率","show status like 'Qcache%'; "), 
    
    //比较 open_tables  与 opend_tables 值 
    tableCache("Table Cache状态量","show global  status like 'open%';"),
    
    //Thread_cache_hits = (1 - Threads_created / connections ) * 100%
    threadCache("Thread Cache 命中率","show global status like 'Thread%';"),
    threadCache_Connections("Thread Cache 命中率","show global status like 'Connections';"),
    
    //Table_locks_waited/Table_locks_immediate=0.3%  如果这个比值比较大的话，说明表锁造成的阻塞比较严重 
    //Innodb_row_lock_waits innodb行锁，太大可能是间隙锁造成的
    lockState("锁定状态","show global  status like '%lock%';"),
    
//    slaveStatus("备机状态","show slave status;"),
    
    //Created_tmp_disk_tables/Created_tmp_tables比值最好不要超过10%，如果Created_tmp_tables值比较大， 
    //可能是排序句子过多或者是连接句子不够优化
    tmpTable("临时表状况","show status like 'Create_tmp%';"),
    
    //如果Binlog_cache_disk_use值不为0 ，可能需要调大 binlog_cache_size大小
    binlogCache("Binlog Cache 使用状况"," show status like 'Binlog_cache%';"),
    
    //Innodb_log_waits值不等于0的话，表明 innodb log  buffer 因为空间不足而等待 
    innodbLogWaits("Innodb_log_waits 量","show status like 'innodb_log_waits';"),
    
    
    //各个线程的独享内存
    threadMemory("线程独享内存","SHOW VARIABLES LIKE ?;"),
    
    currentConfMemory("当前配置的最大使用内存","SET @kilo_bytes = 1024;"
            + "SET @mega_bytes = @kilo_bytes * 1024;"
            + "SET @giga_bytes = @mega_bytes * 1024;"
            + "SET @innodb_buffer_pool_size = 2 * @giga_bytes;"
            + "SET @innodb_additional_mem_pool_size = 16 * @mega_bytes;"
            + "SET @innodb_log_buffer_size = 8 * @mega_bytes;"
            + "SET @thread_stack = 192 * @kilo_bytes;"
            + "SELECT"
            + "( @@key_buffer_size + @@query_cache_size + @@tmp_table_size"
            + "+ @innodb_buffer_pool_size + @innodb_additional_mem_pool_size"
            + "+ @innodb_log_buffer_size"
            + "+ @@max_connections * ("
            + "@@read_buffer_size + @@read_rnd_buffer_size + @@sort_buffer_size"
            + "+ @@join_buffer_size + @@binlog_cache_size + @thread_stack"
            + ") ) / @giga_bytes AS MAX_MEMORY_GB;"),
    
    
    ;
    private String text;

    private String sql;
    
    private MysqlSql(String text, String sql) {
        this.text = text;
        this.sql = sql;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
    
    

}
