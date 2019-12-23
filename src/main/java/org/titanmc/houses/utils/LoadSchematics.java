package org.titanmc.houses.utils;

import org.apache.commons.io.FileUtils;
import org.titanmc.houses.Core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class LoadSchematics {

    /**
     * This file loads schematics into the /schematic file if it is empty
     */

    //Constructor
    public LoadSchematics(Core instance){

        /**
         * Create fodler if doesn't exist
         */
        File schematicFolder = new File(instance.getDataFolder(),"schematics");
        if(!schematicFolder.exists()){
            schematicFolder.mkdir();
        }

        /**
         * See if folder is empty, if it is, put scehmatics into it from jar
         */
        if(schematicFolder.list().length == 0){
            for(int i = 1; i<=6;i++){
                try {
                    InputStream resource = instance.getResource("schematics/house"+i+".schematic");
                    File outputFile = new File(instance.getDataFolder() + File.separator +"schematics/house"+i+".schematic");
                    FileUtils.copyInputStreamToFile(resource, outputFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}