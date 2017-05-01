package pw.cdmi.om.protocol.snmp.host;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import pw.cdmi.om.protocol.snmp.HostOS;
import pw.cdmi.om.protocol.snmp.SNMPClient;
import pw.cdmi.om.protocol.snmp.SNMPTarget;
import pw.cdmi.om.protocol.snmp.SNMPUtil;
import pw.cdmi.om.protocol.snmp.mib.MibDiskAndMemoEntry;
import pw.cdmi.om.protocol.snmp.mib.MibOID;
import pw.cdmi.om.protocol.snmp.mib.MibProcessorEntry;

/************************************************************
 * TODO(对类的简要描述说明 – 必须).
 * TODO(对类的作用含义说明 – 可选).
 * TODO(对类的使用方法说明 – 可选).
 * 
 * @author WUWEI
 * @version iSoc Service Platform, 2015年3月16日
 ************************************************************/
public class HostPerformance {

	private Map<String, Object> result;

	private HostOS hostOS;

	public HostPerformance(SNMPTarget target, HostOS os) throws IOException {
		MibOID[] mibOIDs;
		hostOS = os;
		if (os == HostOS.LINUX) {
			MibOID[] mibs = { MibProcessorEntry.ssCpuSystem, MibProcessorEntry.ssCpuUser, MibProcessorEntry.ssCpuIdle,
					MibDiskAndMemoEntry.memTotalReal, MibDiskAndMemoEntry.memAvailReal,
					MibDiskAndMemoEntry.hrStorageDescr, MibDiskAndMemoEntry.hrStorageAllocationUnits,
					MibDiskAndMemoEntry.hrStorageSize, MibDiskAndMemoEntry.hrStorageUsed };
			mibOIDs = mibs;

		}else{
			MibOID[] mibs = { MibProcessorEntry.hrProcessorLoad, MibDiskAndMemoEntry.hrStorageDescr,
					MibDiskAndMemoEntry.hrStorageAllocationUnits, MibDiskAndMemoEntry.hrStorageSize,
					MibDiskAndMemoEntry.hrStorageUsed };
			mibOIDs = mibs;
		}
		
		SNMPClient snmpClient = null;
		try {
			snmpClient = new SNMPClient(target);
			result = snmpClient.getMibField(mibOIDs);
		}catch(RuntimeException e) {
			throw new IOException(e);
		} finally {
			snmpClient.close();
		}
	}

	/**
	 * 获取linux系统的cpu使用情况
	 * 
	 * @return
	 */
	public CpuUsage getLinuxCpuUsage() {
		CpuUsage cpuUsed = new CpuUsage();
		cpuUsed.setSysCpuUsage(Integer.parseInt(result.get(MibProcessorEntry.ssCpuSystem.getOID()).toString()));
		cpuUsed.setUserCpuUsage(Integer.parseInt(result.get(MibProcessorEntry.ssCpuUser.getOID()).toString()));
		cpuUsed.setFreeCpuUsage(Integer.parseInt(result.get(MibProcessorEntry.ssCpuIdle.getOID()).toString()));
		cpuUsed
			.setOtherCpuUsage(100 - cpuUsed.getSysCpuUsage() - cpuUsed.getUserCpuUsage() - cpuUsed.getFreeCpuUsage());
		cpuUsed.setUsedCpuUsage(100 - cpuUsed.getFreeCpuUsage());
		return cpuUsed;
	}

	/**
	 * 获取Windows和AIX系统的CPU使用情况
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public CpuUsage getWindowsOrAixCpuUsage() {
		CpuUsage cpuUsage = new CpuUsage();
		Map<String, String> map = (Map<String, String>) result.get(MibProcessorEntry.hrProcessorLoad.getOID());
		Integer num = 0;
		for (Map.Entry<String, String> m : map.entrySet()) {
			num = num + Integer.parseInt(m.getValue());
		}
		if (map.size() > 0) {
			cpuUsage.setUsedCpuUsage((float) ((num / map.size())));

			cpuUsage.setFreeCpuUsage(100 - cpuUsage.getUsedCpuUsage());
		}
		return cpuUsage;
	}

	/**
	 * 获取Linux系统内存使用情况
	 * 
	 * @return
	 */
	public MemoryUsage getLinuxMemoryUsage() {
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		MemoryUsage memoryUsage = new MemoryUsage();
		memoryUsage.setTotal(Long.parseLong(result.get(MibDiskAndMemoEntry.memTotalReal.getOID()).toString()));
		memoryUsage.setFree(Long.parseLong(result.get(MibDiskAndMemoEntry.memAvailReal.getOID()).toString()));
		memoryUsage.setUsed(memoryUsage.getTotal() - memoryUsage.getFree());
		Float usage = Float.parseFloat(decimalFormat.format((float) memoryUsage.getUsed() / memoryUsage.getTotal()
				* 100));
		memoryUsage.setUsage(usage);
		return memoryUsage;
	}

	/**
	 * 获取Windows和Aix系统的内存使用情况
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public MemoryUsage getWindowsOrAixMemoryUsage() {
		MemoryUsage memory = new MemoryUsage();
		Map<String, String> hrStorageDescrMap = (Map<String, String>) result.get(MibDiskAndMemoEntry.hrStorageDescr
			.getOID());
		Map<String, String> hrStorageAllocationUnitsMap = (Map<String, String>) result
			.get(MibDiskAndMemoEntry.hrStorageAllocationUnits.getOID());
		Map<String, String> hrStorageSizeMap = (Map<String, String>) result.get(MibDiskAndMemoEntry.hrStorageSize
			.getOID());
		Map<String, String> hrStorageUsedMap = (Map<String, String>) result.get(MibDiskAndMemoEntry.hrStorageUsed
			.getOID());
		List<String> hrStorageDescrList = new ArrayList<String>();
		for (Map.Entry<String, String> map : hrStorageDescrMap.entrySet()) {
			hrStorageDescrList.add(map.getValue());
		}
		List<String> hrStorageAllocationUnitsList = new ArrayList<String>();
		for (Map.Entry<String, String> map : hrStorageAllocationUnitsMap.entrySet()) {
			hrStorageAllocationUnitsList.add(map.getValue());
		}
		List<String> hrStorageSizeList = new ArrayList<String>();
		for (Map.Entry<String, String> map : hrStorageSizeMap.entrySet()) {
			hrStorageSizeList.add(map.getValue());
		}
		List<String> hrStorageUsedList = new ArrayList<String>();
		for (Map.Entry<String, String> map : hrStorageUsedMap.entrySet()) {
			hrStorageUsedList.add(map.getValue());
		}
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		for (int i = 0; i < hrStorageDescrList.size(); i++) {
			if (hostOS == HostOS.WINDOWS) {
				if (hrStorageDescrList.get(i).contains("Physical Memory")) {

					// 总物理内存大小
					Long total = (Long.parseLong(hrStorageAllocationUnitsList.get(i)) * Long
						.parseLong(hrStorageSizeList.get(i))) / 1024;
					Long usedTotal = (Long.parseLong(hrStorageAllocationUnitsList.get(i)) * Long
						.parseLong(hrStorageUsedList.get(i))) / 1024;
					Float usage = (float) usedTotal / (float) total * 100;
					Long free = total - usedTotal;
					memory.setTotal(total);
					memory.setFree(free);
					memory.setUsed(usedTotal);
					memory.setUsage(Float.parseFloat(decimalFormat.format(usage)));

				}
			} else {
				if (hrStorageDescrList.get(i).toLowerCase().contains("system ram")) {

					// 总物理内存大小
					Long total = (Long.parseLong(hrStorageAllocationUnitsList.get(i)) * Long
						.parseLong(hrStorageSizeList.get(i))) / 1024;
					Long usedTotal = (Long.parseLong(hrStorageAllocationUnitsList.get(i)) * Long
						.parseLong(hrStorageUsedList.get(i))) / 1024;
					Float usage = (float) usedTotal / (float) total * 100;
					Long free = total - usedTotal;
					memory.setTotal(total);
					memory.setFree(free);
					memory.setUsed(usedTotal);
					memory.setUsage(Float.parseFloat(decimalFormat.format(usage)));

				}
			}

		}
		return memory;
	}

	/**
	 * 获取操作系统的磁盘使用情况
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DiskUsage> getDiskUsage() {
		List<DiskUsage> list = new ArrayList<DiskUsage>();

		Map<String, String> hrStorageDescrMap = (Map<String, String>) result.get(MibDiskAndMemoEntry.hrStorageDescr
			.getOID());
		Map<String, String> hrStorageAllocationUnitsMap = (Map<String, String>) result
			.get(MibDiskAndMemoEntry.hrStorageAllocationUnits.getOID());
		Map<String, String> hrStorageSizeMap = (Map<String, String>) result.get(MibDiskAndMemoEntry.hrStorageSize
			.getOID());
		Map<String, String> hrStorageUsedMap = (Map<String, String>) result.get(MibDiskAndMemoEntry.hrStorageUsed
			.getOID());

		List<String> hrStorageDescrList = new ArrayList<String>();
		for (Map.Entry<String, String> map : hrStorageDescrMap.entrySet()) {
			hrStorageDescrList.add(map.getValue());
		}
		List<String> hrStorageAllocationUnitsList = new ArrayList<String>();
		for (Map.Entry<String, String> map : hrStorageAllocationUnitsMap.entrySet()) {
			hrStorageAllocationUnitsList.add(map.getValue());
		}
		List<String> hrStorageSizeList = new ArrayList<String>();
		for (Map.Entry<String, String> map : hrStorageSizeMap.entrySet()) {
			hrStorageSizeList.add(map.getValue());
		}
		List<String> hrStorageUsedList = new ArrayList<String>();
		for (Map.Entry<String, String> map : hrStorageUsedMap.entrySet()) {
			hrStorageUsedList.add(map.getValue());
		}
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		for (int i = 0; i < hrStorageDescrList.size(); i++) {
			DiskUsage diskUsage = new DiskUsage();
			String diskName = ((hrStorageDescrList.get(i).split(":").length > 2) ? SNMPUtil
				.getChinese(hrStorageDescrList.get(i)) : hrStorageDescrList.get(i));
			if (diskName == null) {
				diskName = hrStorageDescrList.get(i);
			}
			if (diskName.indexOf("/") == 0 || diskName.contains("Label")) {
				int index = diskName.indexOf(":");
				diskUsage.setName(index == -1 ? diskName : diskName.substring(0, diskName.indexOf(":")));
				// 总磁盘大小
				Long total = (Long.parseLong(hrStorageAllocationUnitsList.get(i)) * Long.parseLong(hrStorageSizeList
					.get(i))) / 1024;
				Long usedTotal = (Long.parseLong(hrStorageAllocationUnitsList.get(i)) * Long
					.parseLong(hrStorageUsedList.get(i))) / 1024;
				Float usage = total != 0 ? ((float) usedTotal / (float) total) * 100 : 0;
				Long free = total - usedTotal;
				diskUsage.setTotal(total);
				diskUsage.setFree(free);
				diskUsage.setUsed(usedTotal);

				diskUsage.setUsage(Float.parseFloat(decimalFormat.format(usage)));
				list.add(diskUsage);
			}
		}

		return list;
	}

	/**
	 * 生成Linux性能的json对象
	 * 
	 * @return
	 */
	public String toJSONStringForLinux() {
		MemoryUsage memoryUsage = getLinuxMemoryUsage();
		JSONObject memoryUsageJsonName = new JSONObject();
		JSONObject memoryUsageJsonObject = new JSONObject();
		JSONArray memoryUsageJsonArray = new JSONArray();

		memoryUsageJsonObject.put("total", memoryUsage.getTotal() + "KB");
		memoryUsageJsonObject.put("free", memoryUsage.getFree() + "KB");
		memoryUsageJsonObject.put("used", memoryUsage.getUsed() + "KB");
		memoryUsageJsonObject.put("used%", memoryUsage.getUsage() + "%");
		memoryUsageJsonArray.add(memoryUsageJsonObject);
		memoryUsageJsonName.put("name", "memory");
		memoryUsageJsonName.put("content", memoryUsageJsonArray);

		CpuUsage linuxCpuUsage = getLinuxCpuUsage();
		JSONObject linuxCpuUsageJsonName = new JSONObject();
		JSONObject linuxCpuUsageJsonObject = new JSONObject();
		JSONArray linuxCpuUsageJsonArray = new JSONArray();
		linuxCpuUsageJsonObject.put("系统CPU百分比", linuxCpuUsage.getSysCpuUsage() + "%");
		linuxCpuUsageJsonObject.put("空闲CPU百分比", linuxCpuUsage.getFreeCpuUsage() + "%");
		linuxCpuUsageJsonObject.put("用户CPU百分比", linuxCpuUsage.getUserCpuUsage() + "%");
		linuxCpuUsageJsonObject.put("其他百分比", linuxCpuUsage.getOtherCpuUsage() + "%");
		linuxCpuUsageJsonArray.add(linuxCpuUsageJsonObject);
		linuxCpuUsageJsonObject.put("used%", linuxCpuUsage.getUsedCpuUsage() + "%");
		linuxCpuUsageJsonName.put("name", "cpu");
		linuxCpuUsageJsonName.put("content", linuxCpuUsageJsonArray);

		List<DiskUsage> diskUsageList = getDiskUsage();
		JSONObject diskUsageJsonName = new JSONObject();

		JSONArray diskUsageJsonArray = new JSONArray();
		List<String> directoryList = new ArrayList<String>();
		for (DiskUsage diskUsage : diskUsageList) {
			String diskName = diskUsage.getName();
			if (diskName.contains("/")) {
				// 是否计算目录大小
				boolean iscount = true;
				for (String str : directoryList) {
					if (diskName.contains(str)) {
						iscount = false;
						break;
					}
				}
				if (!diskName.equals("/")) {
					directoryList.add(diskName);
				}
				if (iscount) {
					JSONObject diskUsageJsonObject = new JSONObject();
					diskUsageJsonObject.put("name", diskUsage.getName());
					diskUsageJsonObject.put("total", diskUsage.getTotal() + "KB");
					diskUsageJsonObject.put("free", diskUsage.getFree() + "KB");
					diskUsageJsonObject.put("used", diskUsage.getUsed() + "KB");
					diskUsageJsonObject.put("used%", diskUsage.getUsage() + "%");
					diskUsageJsonArray.add(diskUsageJsonObject);
				}
			}

		}
		diskUsageJsonName.put("name", "disk");
		diskUsageJsonName.put("content", diskUsageJsonArray);

		JSONArray workstationStatusJsonArray = new JSONArray();
		workstationStatusJsonArray.add(linuxCpuUsageJsonName);
		workstationStatusJsonArray.add(memoryUsageJsonName);
		workstationStatusJsonArray.add(diskUsageJsonName);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("WorkstationStatus", workstationStatusJsonArray);
		return jsonObject.toString();

	}

	/**
	 * 生成Winodw和Aix性能的json对象
	 * 
	 * @return
	 */
	public String toJSONStringForWindowsOrAix() {
		MemoryUsage memoryUsage = getWindowsOrAixMemoryUsage();
		JSONObject memoryUsageJsonName = new JSONObject();
		JSONObject memoryUsageJsonObject = new JSONObject();
		JSONArray memoryUsageJsonArray = new JSONArray();
		memoryUsageJsonObject.put("total", memoryUsage.getTotal() + "KB");
		memoryUsageJsonObject.put("free", memoryUsage.getFree() + "KB");
		memoryUsageJsonObject.put("used", memoryUsage.getUsed() + "KB");
		memoryUsageJsonObject.put("used%", memoryUsage.getUsage() + "%");
		memoryUsageJsonName.put("name", "memory");
		memoryUsageJsonArray.add(memoryUsageJsonObject);
		memoryUsageJsonName.put("content", memoryUsageJsonArray);

		CpuUsage windowsCpuUsage = getWindowsOrAixCpuUsage();
		JSONObject windowsCpuUsageJsonName = new JSONObject();
		JSONObject windowsCpuUsageJsonObject = new JSONObject();
		JSONArray windowsCpuUsageJsonArray = new JSONArray();
		windowsCpuUsageJsonObject.put("使用CPU百分比", windowsCpuUsage.getUsedCpuUsage() + "%");
		windowsCpuUsageJsonObject.put("空闲CPU百分比", windowsCpuUsage.getFreeCpuUsage() + "%");

		windowsCpuUsageJsonArray.add(windowsCpuUsageJsonObject);
		windowsCpuUsageJsonName.put("name", "cpu");
		windowsCpuUsageJsonName.put("content", windowsCpuUsageJsonArray);
		List<DiskUsage> diskUsageList = getDiskUsage();
		JSONObject diskUsageJsonName = new JSONObject();

		JSONArray diskUsageJsonArray = new JSONArray();
		for (DiskUsage diskUsage : diskUsageList) {
			JSONObject diskUsageJsonObject = new JSONObject();
			diskUsageJsonObject.put("name", diskUsage.getName());
			diskUsageJsonObject.put("total", diskUsage.getTotal() + "KB");
			diskUsageJsonObject.put("free", diskUsage.getFree() + "KB");
			diskUsageJsonObject.put("used", diskUsage.getUsed() + "KB");
			diskUsageJsonObject.put("used%", diskUsage.getUsage() + "%");
			diskUsageJsonArray.add(diskUsageJsonObject);
		}
		diskUsageJsonName.put("name", "disk");
		diskUsageJsonName.put("content", diskUsageJsonArray);

		JSONArray workstationStatusJsonArray = new JSONArray();
		workstationStatusJsonArray.add(windowsCpuUsageJsonName);
		workstationStatusJsonArray.add(memoryUsageJsonName);
		workstationStatusJsonArray.add(diskUsageJsonName);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("WorkstationStatus", workstationStatusJsonArray);
		return jsonObject.toString();

	}

}
