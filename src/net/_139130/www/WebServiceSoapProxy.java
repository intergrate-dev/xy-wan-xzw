package net._139130.www;

public class WebServiceSoapProxy implements net._139130.www.WebServiceSoap {
  private String _endpoint = null;
  private net._139130.www.WebServiceSoap webServiceSoap = null;
  
  public WebServiceSoapProxy() {
    _initWebServiceSoapProxy();
  }
  
  public WebServiceSoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initWebServiceSoapProxy();
  }
  
  private void _initWebServiceSoapProxy() {
    try {
      webServiceSoap = (new net._139130.www.WebServiceLocator()).getWebServiceSoap();
      if (webServiceSoap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)webServiceSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)webServiceSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (webServiceSoap != null)
      ((javax.xml.rpc.Stub)webServiceSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public net._139130.www.WebServiceSoap getWebServiceSoap() {
    if (webServiceSoap == null)
      _initWebServiceSoapProxy();
    return webServiceSoap;
  }
  
  public net._139130.www.MOMsg[] getMOMessage(java.lang.String account, java.lang.String password, int pagesize) throws java.rmi.RemoteException{
    if (webServiceSoap == null)
      _initWebServiceSoapProxy();
    return webServiceSoap.getMOMessage(account, password, pagesize);
  }
  
  public net._139130.www.BusinessType[] getBusinessType(java.lang.String account, java.lang.String password) throws java.rmi.RemoteException{
    if (webServiceSoap == null)
      _initWebServiceSoapProxy();
    return webServiceSoap.getBusinessType(account, password);
  }
  
  public net._139130.www.AccountInfo getAccountInfo(java.lang.String account, java.lang.String password) throws java.rmi.RemoteException{
    if (webServiceSoap == null)
      _initWebServiceSoapProxy();
    return webServiceSoap.getAccountInfo(account, password);
  }
  
  public int modifyPassword(java.lang.String account, java.lang.String old_password, java.lang.String new_password) throws java.rmi.RemoteException{
    if (webServiceSoap == null)
      _initWebServiceSoapProxy();
    return webServiceSoap.modifyPassword(account, old_password, new_password);
  }
  
  public net._139130.www.GsmsResponse post(java.lang.String account, java.lang.String password, net._139130.www.MTPacks mtpack) throws java.rmi.RemoteException{
    if (webServiceSoap == null)
      _initWebServiceSoapProxy();
    return webServiceSoap.post(account, password, mtpack);
  }
  
  public int postSingle(java.lang.String account, java.lang.String password, java.lang.String mobile, java.lang.String content, java.lang.String subid) throws java.rmi.RemoteException{
    if (webServiceSoap == null)
      _initWebServiceSoapProxy();
    return webServiceSoap.postSingle(account, password, mobile, content, subid);
  }
  
  public int postMass(java.lang.String account, java.lang.String password, java.lang.String[] mobiles, java.lang.String content, java.lang.String subid) throws java.rmi.RemoteException{
    if (webServiceSoap == null)
      _initWebServiceSoapProxy();
    return webServiceSoap.postMass(account, password, mobiles, content, subid);
  }
  
  public int postGroup(java.lang.String account, java.lang.String password, net._139130.www.MessageData[] mssages, java.lang.String subid) throws java.rmi.RemoteException{
    if (webServiceSoap == null)
      _initWebServiceSoapProxy();
    return webServiceSoap.postGroup(account, password, mssages, subid);
  }
  
  public net._139130.www.MTResponse[] getResponse(java.lang.String account, java.lang.String password, int pageSize) throws java.rmi.RemoteException{
    if (webServiceSoap == null)
      _initWebServiceSoapProxy();
    return webServiceSoap.getResponse(account, password, pageSize);
  }
  
  public net._139130.www.MTReport[] getReport(java.lang.String account, java.lang.String password, int pageSize) throws java.rmi.RemoteException{
    if (webServiceSoap == null)
      _initWebServiceSoapProxy();
    return webServiceSoap.getReport(account, password, pageSize);
  }
  
  public net._139130.www.MTResponse[] findResponse(java.lang.String account, java.lang.String password, java.lang.String batchid, java.lang.String mobile, int pageindex, int flag) throws java.rmi.RemoteException{
    if (webServiceSoap == null)
      _initWebServiceSoapProxy();
    return webServiceSoap.findResponse(account, password, batchid, mobile, pageindex, flag);
  }
  
  public net._139130.www.MTReport[] findReport(java.lang.String account, java.lang.String password, java.lang.String batchid, java.lang.String mobile, int pageindex, int flag) throws java.rmi.RemoteException{
    if (webServiceSoap == null)
      _initWebServiceSoapProxy();
    return webServiceSoap.findReport(account, password, batchid, mobile, pageindex, flag);
  }
  
  public net._139130.www.MediaItems[] setMedias(java.lang.String fullPath) throws java.rmi.RemoteException{
    if (webServiceSoap == null)
      _initWebServiceSoapProxy();
    return webServiceSoap.setMedias(fullPath);
  }
  
  
}