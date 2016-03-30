package com.mhf.script.generator.util;

import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;

public class SaveProjectHelper {

    public void generateProject(ScriptGeneratorModel scriptGeneratorModel) throws IOException {
        Gson gson = new Gson();
        String json = gson.toJson(scriptGeneratorModel);
        Executor exec = new Executor();
        FileWriter writer = new FileWriter(exec.createOutputFile(scriptGeneratorModel.getFilePath(), scriptGeneratorModel.getScriptName() + "_PROJECT", ".json"));
        writer.write(json);
        writer.close();
    }

}
