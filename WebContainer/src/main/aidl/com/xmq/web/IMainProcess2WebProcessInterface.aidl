// IMainProcess2WebProcessInterface.aidl
package com.xmq.web;

// Declare any non-default types here with import statements

interface IMainProcess2WebProcessInterface {

//    void handleMainProcessCommand(String action, String jsonParams);

    void response(String actionName, String response);
}