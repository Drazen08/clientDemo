package com.hema.client;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.List;


public class WsItem {

	/**
	 * processId : 3539313546121216
	 * X-AUTH-USER : MzEwMTYwODU4NDIxNzY4OA==
	 * X-AUTH-TENANT : Mw==
	 * permissions : ["PERMISSION_COMMON","PERMISSION_WORKSHEET","PERMISSION_DASHBOARD","PERMISSION_DATASOURCE","PERMISSION_FILTER","PERMISSION_PROCESS","PERMISSION_RULE_MANAGE","PERMISSION_POINT","PERMISSION_COMPONENT","PERMISSION_COMPONENT_TYPE","PERMISSION_RULE_ALLOT","NO_DIFFERENT_DATA_PERMISSION","PERMISSION_DASHBOARD_MANAGE","PERMISSION_DASHBOARD_PREVIEW","PERMISSION_DASHBOARD_FILTER","PERMISSION_BIG_SCREEN_MANAGE","PERMISSION_BIG_SCREEN_PREVIEW","PERMISSION_PROCESS_MANAGE","PERMISSION_PROCESS_PREVIEW","PERMISSION_REPORT_MANAGE","PERMISSION_REPORT_PREVIEW","PERMISSION_WORKSHEET_MANAGE","PERMISSION_WORKSHEET_PREVIEW","PERMISSION_ASSETS_MANAGE","PERMISSION_ASSETS_PREVIEW","PERMISSION_DEVICE_MANAGE","PERMISSION_DEVICE_PREVIEW","PERMISSION_DL_MANAGE","PERMISSION_DL_PREVIEW","PERMISSION_PLATFORM_MANAGER","PERMISSION_RULES_ENGINE","PERMISSION_COMPONENT_MANAGE"]
	 * groupIds : []
	 * orgIds : ["3"]
	 * isSuperAdmin : true
	 */


	private String processId;
	@SerializedName("X-AUTH-USER")
	private String XAUTHUSER;
	@SerializedName("X-AUTH-TENANT")
	private String XAUTHTENANT;
	private boolean isSuperAdmin = true;
	private List<String> permissions = Arrays.asList(WsClient.per);
	private List<?> groupIds;
	private List<String> orgIds;

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getXAUTHUSER() {
		return XAUTHUSER;
	}

	public void setXAUTHUSER(String XAUTHUSER) {
		this.XAUTHUSER = XAUTHUSER;
	}

	public String getXAUTHTENANT() {
		return XAUTHTENANT;
	}

	public void setXAUTHTENANT(String XAUTHTENANT) {
		this.XAUTHTENANT = XAUTHTENANT;
	}

	public boolean isIsSuperAdmin() {
		return isSuperAdmin;
	}

	public void setIsSuperAdmin(boolean isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}

	public List<String> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}

	public List<?> getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(List<?> groupIds) {
		this.groupIds = groupIds;
	}

	public List<String> getOrgIds() {
		return orgIds;
	}

	public void setOrgIds(List<String> orgIds) {
		this.orgIds = orgIds;
	}
}
