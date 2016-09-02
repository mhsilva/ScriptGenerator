/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mhf.script.generator.util;

import java.util.List;

/**
 *
 * @author Matheus
 */
public class ScriptGeneratorModel {

    private String filePath;
    private String scriptName;
    private String userExitName;
    private String functionality;
    private String order;
    private List<String> event;
    private String script;
    private String version;

    public ScriptGeneratorModel(String filePath, String scriptName, String userExitName, String functionality, String order, List<String> event, String script, String version) {
        this.filePath = filePath;
        this.scriptName = scriptName;
        this.userExitName = userExitName;
        this.functionality = functionality;
        this.order = order;
        this.event = event;
        this.script = script;
        this.version = version;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getScriptName() {
        return scriptName;
    }

    public void setScriptName(String scriptName) {
        this.scriptName = scriptName;
    }

    public String getUserExitName() {
        return userExitName;
    }

    public void setUserExitName(String userExitName) {
        this.userExitName = userExitName;
    }

    public String getFunctionality() {
        return functionality;
    }

    public void setFunctionality(String functionality) {
        this.functionality = functionality;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public List<String> getEvent() {
        return event;
    }

    public void setEvent(List<String> event) {
        this.event = event;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

}
