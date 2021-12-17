package edu.puc.core2.engine.rmi;

import edu.puc.core2.execution.callback.MatchCallbackType;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RemoteCOREInterface extends Remote {
    String RMI_NAME = "RemoteEngineStub";

    boolean addQuery(String name, String query, MatchCallbackType callbackType, Object... args) throws RemoteException;

    boolean addQuery(String name, String query) throws RemoteException;

    String getQuery(String name) throws RemoteException;

    List<String> listQueries() throws RemoteException;

    boolean removeQuery(String name) throws RemoteException;

    List<String> listStreams() throws RemoteException;

    }