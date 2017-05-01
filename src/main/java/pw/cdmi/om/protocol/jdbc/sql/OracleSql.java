package pw.cdmi.om.protocol.jdbc.sql;


/************************************************************
 * ORACLE相关SQL语句
 * 
 * @author NIXIAOJUN
 * @version iSoc Service Platform, 2015年9月23日
 ************************************************************/
public enum OracleSql {

    Version("版本","SELECT * FROM v$version"),
    InstanceInfo(
        "实例信息",
        "SELECT instance_name AS \"Instance Name\", to_char (instance_number) AS \"Instance Number\", host_name AS \"Host Name\", version AS \"Oracle Version\", TO_CHAR ( startup_time, 'mm/dd/yyyy HH24:MI:SS' ) AS \"Start Time\", ROUND( TO_CHAR (SYSDATE - startup_time), 2 ) AS \"Uptime (in days)\", parallel AS \"RAC?\", STATUS AS \"Instance Status\", logins AS \"Logins\", DECODE( archiver, 'FAILED', archiver, archiver ) AS \"Archiver\" FROM gv$instance ORDER BY instance_number"),
    DatabaseInfo(
        "数据库信息",
        "SELECT NAME AS \"Database Name\", dbid AS \"Database ID\", TO_CHAR ( created, 'mm/dd/yyyy HH24:MI:SS' ) AS \"Creation Date\", log_mode AS \"Log Mode\", open_mode AS \"Open Mode\", force_logging \"Force Logging\", controlfile_type AS \"Controlfile Type\" FROM v$database"),
    SGA(
        "SGA统计",
        "SELECT NAME AS \"Pool Name\", TO_CHAR(VALUE,'999,999,999,999') AS \"Bytes\" FROM v$sga;SELECT TO_CHAR(SUM(VALUE),'999,999,999,999') AS \"total: Bytes\" FROM v$sga"),
    SGAState(
        "SGA详情",
        "SELECT pool AS \"Pool Name\", NAME AS \"Component Name\", TO_CHAR(bytes, '999,999,999,999') AS  \"Bytes\" FROM v$sgastat WHERE bytes > 1048576 ORDER BY 1 DESC, 3 DESC"),
    SharedPoolMemory(
        "共享池剩余内存",
        "SELECT 'SharePoolCheckMemory:' check_item, decode( sign((bytes / 1024 / 1024) - 300) ,- 1, 'fatal:Less300M', 'good' ) check_result FROM v$sgastat WHERE pool = 'shared pool' AND NAME = 'free memory';"),
    UndoTableSpace(
        "UNDO表空间",
        "SELECT 'UNDOTableSpace:' check_item, decode( sign(90 -(a.bytes / b.bytes) * 100) ,- 1, 'fatal', 'good' ) check_result FROM ( SELECT sum(bytes) bytes FROM dba_undo_extents WHERE tablespace_name = ( SELECT VALUE FROM v$parameter WHERE NAME = 'undo_tablespace' ) AND ( STATUS = 'ACTIVE' OR STATUS = 'UNEXPIRED' )) a, ( SELECT sum(bytes) bytes FROM dba_data_files WHERE tablespace_name = 'UNDOTBS1' ) b"),
    ControlFile("控制文件信息",
        "SELECT c. NAME NAME, DECODE( c. STATUS, NULL, 'VALID', c. STATUS ) STATUS FROM v$controlfile c ORDER BY c. NAME"),
    TableSpace(
        "表空间信息",
        "SELECT d.STATUS AS \"Status\", d.tablespace_name AS \"Tablespace Name\", d.contents AS \"TS Type\", d.extent_management AS \"Ext. Mgt.\", d.segment_space_management AS \"Seg. Mgt.\", TO_CHAR(NVL (a.bytes, 0) / 1024 / 1024,'999,999,999') AS \"Tablespace Size\", TO_CHAR(NVL (f.bytes, 0) / 1024 / 1024, '999,999,999') AS \"Free (in M)\", TO_CHAR(NVL (a.bytes - NVL(f.bytes, 0), 0) / 1024 / 1024,'999,999,999') AS \"Used(in M)\", TRUNC ( NVL ((a.bytes - NVL(f.bytes, 0)) / a.bytes * 100, 0 )) AS \"Pct. Used\" FROM sys.dba_tablespaces d, ( SELECT tablespace_name, sum(bytes) bytes FROM dba_data_files GROUP BY tablespace_name ) a, ( SELECT tablespace_name, sum(bytes) bytes FROM dba_free_space GROUP BY tablespace_name ) f WHERE d.tablespace_name = a.tablespace_name (+) AND d.tablespace_name = f.tablespace_name (+) AND NOT ( d.extent_management LIKE 'LOCAL' AND d.contents LIKE 'TEMPORARY' ) UNION ALL SELECT d.STATUS AS \"STATUS\", d.tablespace_name AS \"Tablespace Name\", d.contents AS \"TS Type\", d.extent_management AS \"Ext. Mgt.\", d.segment_space_management \"Seg. Mgt.\", TO_CHAR(NVL (a.bytes, 0) / 1024 / 1024,'999,999,999') AS \"Tablespace Size\", TO_CHAR(NVL (a.bytes - NVL(t.bytes, 0), 0) / 1024 / 1024,'999,999,999') AS \"Free (in M)\", TO_CHAR(NVL (t.bytes, 0) / 1024 / 1024,'999,999,999') AS \"Used(in M)\", TRUNC (NVL(t.bytes / a.bytes * 100, 0)) AS \"Pct. Used\" FROM sys.dba_tablespaces d, ( SELECT tablespace_name, sum(bytes) bytes FROM dba_temp_files GROUP BY tablespace_name ) a, ( SELECT tablespace_name, sum(bytes_cached) bytes FROM v$temp_extent_pool GROUP BY tablespace_name ) t WHERE d.tablespace_name = a.tablespace_name (+) AND d.tablespace_name = t.tablespace_name (+) AND d.extent_management LIKE 'LOCAL' AND d.contents LIKE 'TEMPORARY' ORDER BY 3, 9 DESC;"),
    DataFileStatus(
        "数据文件状态",
        "SELECT 'DataFileStatusCheck:' check_item, decode( sign(0 -(a.count + b.count)) ,- 1, 'fatal', 'good' ) check_result FROM ( SELECT count(*) count FROM v$datafile WHERE STATUS NOT IN ('ONLINE', 'SYSTEM')) a, ( SELECT count(*) count FROM v$tempfile WHERE STATUS <> 'ONLINE' ) b"),
    DataFileAutoExtensible(
        "数据文件自动扩展",
        "SELECT 'DataFileAutoInspectionCheck:' check_item, decode( sign(0 -(a.count + b.count)) ,- 1, 'fatal', 'good' ) check_result FROM ( SELECT count(*) count FROM dba_data_files WHERE autoextensible = 'YES' ) a, ( SELECT count(*) count FROM dba_temp_files WHERE autoextensible = 'YES' ) b"),
    Jobs(
        "JOBS",
        "SELECT TO_CHAR(job,'999,999') AS \"Job ID\", log_user AS \"User\", what AS \"What\", NVL ( TO_CHAR ( next_date, 'mm/dd/yyyy HH24:MI:SS' ), ' ' ) AS \"Next Run Date\", INTERVAL AS \"Interval\", NVL ( TO_CHAR ( last_date, 'mm/dd/yyyy HH24:MI:SS' ), ' ' ) AS \"Last Run Date\", failures AS \"Failures\", broken AS \"Broken?\" FROM dba_jobs ORDER BY job"),
    JobsState("JOBS状态","SELECT 'abnormal jobs:' || count(*) FROM dba_jobs WHERE broken = 'Y' AND failures > 0"),
    TempTableSpace(
        "临时表空间信息",
        "SELECT s.tablespace_name AS \"Tablespace Name\", TO_CHAR((used_blocks * block_size) / 1024 / 1024, '999,999') AS \"Used Size\", TO_CHAR((free_blocks * block_size) / 1024 / 1024, '999,999') AS \"Free Size\", TO_CHAR((total_blocks * block_size) / 1024 / 1024, '999,999') AS \"Total Size\", TO_CHAR((max_used_blocks * block_size) / 1024 / 1024, '999,999') AS \"Max Used Size\", TO_CHAR((max_sort_blocks * block_size) / 1024 / 1024,'999,999') AS \"Max Sort Size\" FROM v$sort_segment s, dba_tablespaces t WHERE s.tablespace_name = t.tablespace_name"),
    CurrentSessions(
        "当前会话",
        "SELECT TO_CHAR (a.count) AS \"Current No. of Processes\", TO_CHAR (b. VALUE) AS \"Max No. of Processes\", TO_CHAR ( ROUND(100 *(a.count / b. VALUE), 2)) AS \"% Usage\" FROM ( SELECT count(*) count FROM v$session ) a, ( SELECT VALUE FROM v$parameter WHERE NAME = 'processes' ) b"),
    Directories(
        "目录信息",
        "SELECT OWNER AS \"Owner\", directory_name AS \"Directory Name\", directory_path AS \"Directory Path\" FROM dba_directories ORDER BY OWNER, directory_name"),
    UnableExpandObject(
        "无法扩展对象",
        "SELECT 'Oracle_011:UnableExpandObjectCheck:' check_item, decode( sign(0 - count(*)) ,- 1, 'fatal', 'good' ) check_result FROM dba_segments ds, ( SELECT max(bytes) / 1024 / 1024 max, sum(bytes) / 1024 / 1024 sum, tablespace_name FROM dba_free_space GROUP BY tablespace_name ) dfs WHERE ( ds.next_extent / 1024 / 1024 > nvl (dfs.max, 0) OR ds.extents >= ds.max_extents ) AND ds.tablespace_name = dfs.tablespace_name (+) AND ds. OWNER NOT IN ('SYS','SYSTEM')"),
    UserAccounts(
        "用户账户",
        "SELECT DISTINCT a.username Username, DECODE( a.account_status, 'OPEN', a.account_status, a.account_status ) AS \"Account Status\", NVL ( TO_CHAR ( a.expiry_date, 'mm/dd/yyyy HH24:MI:SS' ), ' ' ) AS \"Expire Date\", a.default_tablespace AS \"Default Tbs.\", a.temporary_tablespace AS \"Temp Tbs.\", TO_CHAR ( a.created, 'mm/dd/yyyy HH24:MI:SS' ) AS \"Created On\", a.PROFILE AS \"Profile\", NVL (DECODE(p.sysdba, 'TRUE', 'TRUE', ''), ' ' ) AS \"SYSDBA\", NVL ( DECODE(p.sysoper, 'TRUE', 'TRUE', ''), ' ' ) AS \"SYSOPER\" FROM dba_users a, v$pwfile_users p WHERE p.username (+) = a.username ORDER BY username"),
    InvalidIndexes(
        "无效索引",
        "SELECT OWNER Owner, index_name AS \"Index Name\", index_type AS \"Index Type\", STATUS AS \"Status\" FROM dba_indexes WHERE STATUS = 'UNUSABLE' UNION ALL SELECT index_owner Owner, index_name AS \"Index Name\", 'Index Partition' AS \"Index Type\", STATUS AS \"Status\" FROM dba_ind_partitions WHERE STATUS = 'UNUSABLE' UNION ALL SELECT index_owner Owner, index_name AS \"Index Name\", 'Index Subpartition' AS \"Index Type\", STATUS AS \"Status\" FROM dba_ind_subpartitions WHERE STATUS = 'UNUSABLE' ORDER BY OWNER;SELECT COUNT(index_name) AS \"Grand Total:Index Name\" from (SELECT index_name FROM dba_indexes WHERE STATUS = 'UNUSABLE' UNION ALL SELECT index_name FROM dba_ind_partitions WHERE STATUS = 'UNUSABLE' UNION ALL SELECT index_name FROM dba_ind_subpartitions WHERE STATUS = 'UNUSABLE')"),
    InvalidIndexCheck(
        "无效索引检查",
        "SELECT 'Oracle_010:InvalidIndexCheck:' check_item, decode( sign( 0 - (a.count + b.count + c.count)) ,- 1, 'fatal', 'good' ) check_result FROM ( SELECT count(*) count FROM dba_indexes WHERE STATUS <> 'VALID' AND partitioned <> 'YES' ) a, ( SELECT count(*) count FROM dba_ind_partitions WHERE STATUS <> 'USABLE' ) b, ( SELECT count(*) count FROM dba_ind_subpartitions WHERE STATUS <> 'USABLE' ) c"),
    DataFiel(
        "数据文件信息",
        "SELECT /*+ ordered */ d.tablespace_name AS \"Tablespace Name File Class\", d.file_name AS \"Filename\", TO_CHAR(d.bytes/1024/1024,'999,999,999') AS \"File Size\", NVL(d.autoextensible, ' ') AS \"Autoextensible\", TO_CHAR(d.increment_by * e.value/1024/1024,'999,999,999') AS \"Next\", TO_CHAR(d.maxbytes/1024/1024,'999,999,999') AS \"Max\" FROM sys.dba_data_files d, v$datafile v, (SELECT value FROM v$parameter  WHERE name = 'db_block_size') e WHERE (d.file_name = v.name) UNION SELECT d.tablespace_name AS \"Tablespace Name File Class\" , d.file_name AS \"Filename\", TO_CHAR(d.bytes/1024/1024,'999,999,999') AS \"File Size\", NVL(d.autoextensible, ' ') AS \"Autoextensible\", TO_CHAR(d.increment_by * e.value/1024/1024,'999,999,999') AS \"Next\", TO_CHAR(d.maxbytes/1024/1024,'999,999,999') AS \"Max\" FROM sys.dba_temp_files d, (SELECT value FROM v$parameter  WHERE name = 'db_block_size') e UNION SELECT '[ ONLINE REDO LOG ]', a.member , TO_CHAR(b.bytes/1024/1024,'999,999,999') , null, null, null FROM v$logfile a, v$log b WHERE a.group# = b.group# UNION SELECT '[ CONTROL FILE    ]', a.name, null, null, null, null FROM v$controlfile a ORDER BY 1,2;SELECT TO_CHAR(SUM(filesize),'999,999,999') AS \"Total:File Size\" FROM (SELECT /*+ ordered */ d.tablespace_name AS \"Tablespace Name File Class\", d.file_name AS \"Filename\", d.bytes/1024/1024 filesize, NVL(d.autoextensible, ' ') AS \"Autoextensible\", TO_CHAR(d.increment_by * e.value/1024/1024,'999,999,999') AS \"Next\", TO_CHAR(d.maxbytes/1024/1024,'999,999,999') AS \"Max\" FROM sys.dba_data_files d, v$datafile v, (SELECT value FROM v$parameter  WHERE name = 'db_block_size') e WHERE (d.file_name = v.name) UNION SELECT d.tablespace_name AS \"Tablespace Name File Class\" , d.file_name AS \"Filename\", d.bytes/1024/1024 filesize, NVL(d.autoextensible, ' ') AS \"Autoextensible\", TO_CHAR(d.increment_by * e.value/1024/1024,'999,999,999') AS \"Next\", TO_CHAR(d.maxbytes/1024/1024,'999,999,999') AS \"Max\" FROM sys.dba_temp_files d, (SELECT value FROM v$parameter  WHERE name = 'db_block_size') e UNION SELECT '[ ONLINE REDO LOG ]', a.member , b.bytes/1024/1024 filesize, null, null, null FROM v$logfile a, v$log b WHERE a.group# = b.group# UNION SELECT '[ CONTROL FILE    ]', a.name, null, null, null, null FROM v$controlfile a ORDER BY 1,2)"),
    OnlineLog(
        "在线重做日志信息",
        "SELECT to_char(f.group#) AS \"Group\", to_char(l.thread#) Thread, f.member AS Member, f.type AS \"Redo Type\", l.status AS \"Group Status\", f.status \"Member Status\", TO_CHAR(l.bytes/1024/1024,'999,999') AS \"Size(M)\", l.archived AS \"Archived?\" FROM v$logfile f, v$log l WHERE f.group# = l.group# ORDER BY f.group# , f.member"),
    LogFileStatus(
        "日志文件状态",
        "SELECT 'Abnormal state of logs:'||count(*) FROM  v$logfile  f WHERE  f.type <> 'ONLINE' OR  f.status IN ('INVALID','DELETED');"),
    InvalidObjects(
        "无效的对象",
        "SELECT owner AS \"Owner\",object_type AS \"Object  Type\",object_name AS \"Object  Name\",status AS \"Status\" FROM dba_objects WHERE status <> 'VALID' ORDER BY owner,object_type ,object_name;SELECT COUNT(object_name) AS \"Grand Total:Object  Name\" FROM (SELECT owner,object_type,object_name,status FROM dba_objects WHERE status <> 'VALID' ORDER BY owner,object_type ,object_name)"),
    UnextendObjects(
        "不能扩展的对象",
        "SELECT ds.owner AS \"Owner\", ds.tablespace_name AS \"Tablespace Name\", ds.segment_name AS \"Segment Name\", ds.segment_type AS \"Segment Type\",TO_CHAR(ds.next_extent,'999,999,999,999') AS \"'Next Extent\" , TO_CHAR(NVL(dfs.max, 0),'999,999,999,999') AS \"Max. Piece Size\", TO_CHAR(NVL(dfs.sum, 0),'999,999,999,999') AS \"Sum of Bytes\",TO_CHAR(ds.extents,'999,999,999,999') AS \"Num. of Extents\", TO_CHAR(ds.max_extents,'999,999,999,999') AS \"Max Extents\" FROM dba_segments ds, (select max(bytes)/1024/1024 max, sum(bytes)/1024/1024 sum, tablespace_name from dba_free_space group by  tablespace_name) dfs WHERE (ds.next_extent/1024/1024 > nvl(dfs.max, 0) OR  ds.extents >= ds.max_extents)  AND ds.tablespace_name = dfs.tablespace_name (+) AND ds.owner NOT IN ('SYS','SYSTEM') ORDER BY ds.owner, ds.tablespace_name,ds.segment_name");

    private String text;

    private String sql;

    private OracleSql(String text, String sql) {
        this.sql = sql;
        this.text = text;
    }

    public String getSql() {
        return sql;
    }

    public String getText() {
        return text;
    }

}
