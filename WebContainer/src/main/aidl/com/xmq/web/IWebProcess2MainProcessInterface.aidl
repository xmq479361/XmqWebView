// IWebProcess2MainProcessInterface.aidl
package com.xmq.web;

// Declare any non-default types here with import statements

interface IWebProcess2MainProcessInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void handleWebProcessCommand(String action, String jsonParams);

}