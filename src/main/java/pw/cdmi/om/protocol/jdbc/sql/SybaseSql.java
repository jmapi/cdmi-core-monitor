package pw.cdmi.om.protocol.jdbc.sql;

/************************************************************
 * Sybase的JDBC监控语句.<br/>
 * @author Roger
 * @version aws Service Platform, 2015年12月7日
 ************************************************************/
public enum SybaseSql {

    baseInfo(
        "基础信息",
        "select "
                + "(SELECT @@SERVERNAME) as fSvrName, "
                + "(SELECT @@VERSION) as fDBVersion, "
                + "(SELECT @@CPU_BUSY) as fCpuWorkTime, "
                + "(SELECT @@IDLE) as fDBFreeTime, "
                + "(select count(*) from master.dbo.sysdevices) as fDiskCount, "
                + "(select count(*) from master.dbo.sysdatabases) as fDBCount, "
                + "(select count(*) from master.dbo.sysengines) as fEngineCount, "
                + "(select count(e.status) from master.dbo.sysengines e where status = 'online') as fEngineCountOnline, "
                + "(select count(*) from master.dbo.sysservers) as fSvrCount, "
                + "(select count(*)from master..syslocks l,master..spt_values v,master..sysprocesses p where l.type = v.number and v.type = 'L' and l.spid = p.spid) as fLockCount, "
                + "count(*) as fTranCount from master.dbo.systransactions","SQL"),
    ioInfo("I/O信息","select " + "(SELECT @@pack_received) as fNetPackReads, "
            + "(SELECT @@pack_sent) as fNetPackWrites, " + "(SELECT @@PACKET_ERRORS) as fNetPackErrs, "
            + "(SELECT @@IO_BUSY) as fIoTime, " + "(SELECT @@TOTAL_READ) as fIoReads, "
            + "(SELECT @@TOTAL_WRITE) as fIoWrites, " + "@@TOTAL_ERRORS as fIoErrs","SQL"),
    memoryAndCacheConf(
        "内存缓存配置",
        "select "
                + "(select convert(decimal(10,0),c.value/512.00) from master.dbo.sysconfigures t ,master.dbo.syscurconfigs c where t.comment like '%total physical memory%' and c.config=t.config) as fTotalPhyMem, "
                + "(select convert(decimal(10,0),c.value/512.00) from master.dbo.sysconfigures t ,master.dbo.syscurconfigs c where t.comment like '%total logical memory%' and c.config=t.config) as fTotalLogicMem, "
                + "(select convert(decimal(10,0),c.value/512.00) from master.dbo.sysconfigures t ,master.dbo.syscurconfigs c where t.comment like '%total data cache size%' and c.config=t.config) as fTotalDataCache, "
                + "(select convert(decimal(10,0),c.value/512.00) from master.dbo.sysconfigures t ,master.dbo.syscurconfigs c where t.comment like '%Meta-Data Caches%' and c.config=t.config) as fMetadataCache, "
                + "(select convert(decimal(10,0),c.value/512.00) from master.dbo.sysconfigures t ,master.dbo.syscurconfigs c where t.comment like '%procedure cache size%' and c.config=t.config) as fProcCache, "
                + "(select convert(decimal(10,0),c.value*100/c.memory_used) from master.dbo.sysconfigures t ,master.dbo.syscurconfigs c where t.comment ='total data cache size' and c.config=t.config) as fDataHitRate, "
                + "convert(decimal(10,0),c.value*200/c.memory_used) as fProcHitRate from master.dbo.sysconfigures t ,master.dbo.syscurconfigs c where t.comment ='procedure cache size' and c.config=t.config",
        "SQL"),

    tableInfo("Table信息","SELECT USER_NAME(uid)ownerName, " + "O.name name, " + "rowcnt(doampg) rowNum, "
            + "S.name segmentName, " + "creation = O.crdate, " + "case sysstat2 & 57344  "
            + "when 32768 then 'datarows'  " + "when 16384 then 'datapages'  " + "else 'allpages' end  "
            + "FROM sysobjects O, sysindexes I, syssegments S  " + "WHERE O.type = 'U' AND  " + "O.id=I.id AND  "
            + "I.indid IN (0,1) AND  " + "I.segment=S.segment AND  " + "O.type!='S' " + "ORDER BY 3","SQL"),

    processesInfo("Processes信息","select " + "p.status as fStatus, " + "p.hostname as fHostName, " + "p.cmd as fCmd, "
            + "b.name as fDBName, " + "p.cpu as fAccuCpuTime, " + "p.memusage as fProcUsage, "
            + "p.network_pktsz as fNetPackSize, " + "p.priority as fPriority, "
            + "substring(convert(char,p.loggedindatetime, 112),1,4)+'-' + "
            + "substring(convert(char,p.loggedindatetime, 112),5,2)+'-' + "
            + "substring(convert(char,p.loggedindatetime, 112),7,2)+' ' + "
            + "(convert(char,p.loggedindatetime,108)) as fLoginDate, " + "p.ipaddr as fLoginIP "
            + "from master.dbo.sysprocesses p, master.dbo.sysdatabases b where p.dbid = b.dbid","SQL"),
    userInfo("用户信息","sp_helpuser","SQL"),
    remoteLoginInfo("远程登录信息","select * from master..sysservers","SQL"),
    deviceInfo("Device信息","sp_helpdevice","SP"),

    segmentInfo("Segment信息","sp_helpsegment","SQL"),

    lockInfo("lock信息","select l.spid, " + "locktype=convert(char(12),name), "
            + "dbname=convert(char(15),db_name(l.dbid)), " + "'table'=convert(char(15),object_name(l.id,l.dbid)), "
            + "class=convert(char(15),class), " + "hostname, cmd "
            + "from master..syslocks l,master..spt_values v,master..sysprocesses p "
            + "where l.type = v.number and v.type = 'L' and l.spid = p.spid ","SQL"),
    connectionInfo("数据库连接","select * from master..sysprocesses","SQL"),
    dbCapacity(
        "数据库容量",
        "select convert(char(16),db_name(data_segment.dbid)) name"
                + ",str(round(total_data_pages / ((1024.0 * 1024) / @@maxpagesize),2),10,2) \"data_total\""
                + ",str(round(free_data_pages / ((1024.0 * 1024) / @@maxpagesize),2),10,2) \"data_free\""
                + ",str( round(100.0 * free_data_pages / total_data_pages ,2),10,2) \"data_free_rate\""
                + ",str(round(total_log_pages / ((1024.0 * 1024) / @@maxpagesize),2),10,2) \"log_total\""
                + ",str(round(free_log_pages / ((1024.0 * 1024) / @@maxpagesize),2),10,2) \"log_free\""
                + ",str( round(100.0 * free_log_pages / total_log_pages,2),10,2) \"log_free_rate\""
                + " from (select dbid,sum(size) total_log_pages,lct_admin('logsegment_freepages', dbid ) free_log_pages"
                + " from master.dbo.sysusages where segmap & 4 = 4 group by dbid) log_segment"
                + ",(select dbid,sum(size) total_data_pages ,sum(curunreservedpgs(dbid, lstart, unreservedpgs)) free_data_pages"
                + " from master.dbo.sysusages where segmap <> 4 group by dbid ) data_segment "
                + "where data_segment.dbid = log_segment.dbid order by data_segment.dbid","SP"), ;

    private String text;

    private String sql;

    private String type;

    private SybaseSql(String text, String sql, String type) {
        this.text = text;
        this.sql = sql;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
