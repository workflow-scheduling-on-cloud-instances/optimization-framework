package org.optframework;

import org.optframework.config.Config;
import org.optframework.core.Log;
import org.optframework.core.utils.Printer;

public class Main {
    public static void main(String[] args) throws Exception{
        Log.init();
        Log.logger.info("Main Flow is started");

        /**
         * Initializes Cloudsim Logger
         * */
        org.cloudbus.cloudsim.Log.init("cloudsim.log");

        org.cloudbus.spotsim.main.config.Config.load(null);

        /**
         * Loads configs from YAML file
         * */
        Config.initConfig();

        Printer.printSplitter();

        switch (Config.global.algorithm){
            case "sa":
                RunSAAlgorithm.runSA();
                break;
            case "hbmo":
                RunHBMOAlgorithm.runHBMO();
                break;
            case "hbmo-heft":
                RunHEFTWithHBMO.runHEFTWithHBMO();
                break;
        }
    }
}
