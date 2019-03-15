/**
 * WebServiceSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package net._139130.www;

public interface WebServiceSoap extends java.rmi.Remote {
    public net._139130.www.MOMsg[] getMOMessage(java.lang.String account, java.lang.String password, int pagesize) throws java.rmi.RemoteException;
    public net._139130.www.BusinessType[] getBusinessType(java.lang.String account, java.lang.String password) throws java.rmi.RemoteException;
    public net._139130.www.AccountInfo getAccountInfo(java.lang.String account, java.lang.String password) throws java.rmi.RemoteException;
    public int modifyPassword(java.lang.String account, java.lang.String old_password, java.lang.String new_password) throws java.rmi.RemoteException;
    public net._139130.www.GsmsResponse post(java.lang.String account, java.lang.String password, net._139130.www.MTPacks mtpack) throws java.rmi.RemoteException;
    public int postSingle(java.lang.String account, java.lang.String password, java.lang.String mobile, java.lang.String content, java.lang.String subid) throws java.rmi.RemoteException;
    public int postMass(java.lang.String account, java.lang.String password, java.lang.String[] mobiles, java.lang.String content, java.lang.String subid) throws java.rmi.RemoteException;
    public int postGroup(java.lang.String account, java.lang.String password, net._139130.www.MessageData[] mssages, java.lang.String subid) throws java.rmi.RemoteException;
    public net._139130.www.MTResponse[] getResponse(java.lang.String account, java.lang.String password, int pageSize) throws java.rmi.RemoteException;
    public net._139130.www.MTReport[] getReport(java.lang.String account, java.lang.String password, int pageSize) throws java.rmi.RemoteException;
    public net._139130.www.MTResponse[] findResponse(java.lang.String account, java.lang.String password, java.lang.String batchid, java.lang.String mobile, int pageindex, int flag) throws java.rmi.RemoteException;
    public net._139130.www.MTReport[] findReport(java.lang.String account, java.lang.String password, java.lang.String batchid, java.lang.String mobile, int pageindex, int flag) throws java.rmi.RemoteException;
    public net._139130.www.MediaItems[] setMedias(java.lang.String fullPath) throws java.rmi.RemoteException;
}
