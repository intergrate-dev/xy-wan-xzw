package com.founder.mobileinternet.cmsinterface.service.cms;

public interface PermissionService {
  public String getUserPermission(String uid, String siteID);

  public String getAllPermission(String siteID);

}
