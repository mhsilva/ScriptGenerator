/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mhf.script.generator.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Matheus
 */
public class Executor {

    private FileOutputStream outputStream;

    private ScriptTemplate scriptTemplate;

    public void execute(ScriptGeneratorModel baseModel) throws IOException {
        scriptTemplate = new ScriptTemplate();
        outputStream = new FileOutputStream(createOutputFile(baseModel.getFilePath(), baseModel.getScriptName()));
        String formatedHeader = MessageFormat.format(scriptTemplate.getHeaderData(), baseModel.getScriptName(), baseModel.getUserExitName(), baseModel.getFunctionality(), baseModel.getOrder());
        writeToFile(formatedHeader);
        StringBuffer procedureCalls = new StringBuffer();
        for (String chunk : scriptHandler(baseModel.getScript(), procedureCalls)) {
            writeToFile(chunk);
        }
        writeToFile(scriptTemplate.getHeaderProcedures());
        StringBuffer ueUeEvent = new StringBuffer();
        writeToFile(eventHandler(baseModel.getEvent(), ueUeEvent).toString());
        writeToFile(procedureCalls.toString());
        writeToFile(MessageFormat.format(scriptTemplate.getStaticInsert(), baseModel.getVersion()));
        writeToFile(ueUeEvent.toString());
        writeToFile(scriptTemplate.getFooterData());
        outputStream.close();
    }

    public File createOutputFile(String path,
        String scriptName) throws IOException {
        return this.createOutputFile(path, scriptName, ".sql");
    }

    public File createOutputFile(String path,
        String scriptName,
        String ext) throws IOException {
        path = path + "/" + scriptName + ext;
        System.out.println(path);
        File file = new File(path);
        file.createNewFile();
        return file;
    }

    public List<String> scriptHandler(String script,
        StringBuffer procedureCalls) {
        int size = 30000;
        List<String> scriptSplit = new ArrayList<>();

        int sourceNumber = 1;
        for (int start = 0; start < script.length(); start += size) {
            scriptSplit.add("  v_s_source" + sourceNumber + "             varchar2(32000) := '" + script.substring(start, Math.min(script.length(), start + size)).replaceAll("'", "'|| chr(39) ||'")
                .replaceAll("&&", "'|| chr(38) || chr(38) ||'").replaceAll("@Override", "'|| chr(64) ||'Override").replaceAll("@CompileStatic", "'|| chr(64) ||'CompileStatic") + "';\n");
            procedureCalls.append("    PRC_ADD_SOURCE(v_s_source");
            procedureCalls.append(sourceNumber++);
            procedureCalls.append(");\n");
        }

        return scriptSplit;
    }

    public StringBuffer eventHandler(List<String> eventsOpFunc,
        StringBuffer ueUeEvent) {
        StringBuffer sb = new StringBuffer();
        for (String eventOpFunc : eventsOpFunc) {
            String[] split = eventOpFunc.split("-");
            String opFunc = split[0];
            String eventName = split[1];
            sb.append(MessageFormat.format(scriptTemplate.getInsertEvent(), opFunc, eventName));
            ueUeEvent.append(MessageFormat.format(scriptTemplate.getInsertUserExitXEvent(), eventName, opFunc));
        }
        return sb;
    }

    public void writeToFile(String text) throws IOException {
        outputStream.write(text.getBytes());
    }

}
