package org.optframework;

import org.cloudbus.spotsim.enums.AZ;
import org.cloudbus.spotsim.enums.OS;
import org.cloudbus.spotsim.enums.Region;
import org.optframework.config.Config;
import org.optframework.config.StaticProperties;
import org.optframework.core.*;
import org.optframework.core.sa.SimulatedAnnealingAlgorithm;
import org.optframework.core.utils.PopulateWorkflow;
import org.optframework.core.utils.PreProcessor;
import org.optframework.core.utils.Printer;

/**
 * @author Hessam Modabberi hessam.modaberi@gmail.com
 * @version 1.0.0
 */

public class RunSAAlgorithm implements StaticProperties {

    public static void runSA(){
        Log.logger.info("<<<<<<<<< SA Algorithm is started >>>>>>>>>");

        Workflow workflow = PreProcessor.doPreProcessing(PopulateWorkflow.populateWorkflowWithId(Config.global.budget, 0, Config.global.workflow_id));

        computeCoolingFactor(workflow.getJobList().size());

        Log.logger.info("Maximum number of instances: " + M_NUMBER + " Number of different types of instances: " + N_TYPES + " Number of tasks: "+ workflow.getJobList().size());

        /**
         * Assumptions:
         * Region: europe
         * Availability Zone: A
         * OS type: Linux System
         * */
        InstanceInfo instanceInfo[] = InstanceInfo.populateInstancePrices(Region.EUROPE , AZ.A, OS.LINUX);

        workflow.setBeta(Beta.computeBetaValue(workflow, instanceInfo, M_NUMBER));

        SimulatedAnnealingAlgorithm saAlgorithm = new SimulatedAnnealingAlgorithm(workflow, instanceInfo);

        double fitnessValueList[] = new double[Config.sa_algorithm.getNumber_of_runs()];

        for (int i = 0; i < Config.sa_algorithm.getNumber_of_runs(); i++) {
            long start = System.currentTimeMillis();

            Solution solution = saAlgorithm.runAlgorithm();
            fitnessValueList[i] = solution.fitnessValue;

            long stop = System.currentTimeMillis();

            Printer.printSolution(solution, instanceInfo,stop-start);
        }

        double sum = 0.0;
        double max = 0.0;
        double min = 999999999999.9;
        for (double value : fitnessValueList){
            sum += value;
            if (value > max){
                max = value;
            }
            if (value < min){
                min = value;
            }
        }
        Printer.printSplitter();
        Log.logger.info("Average fitness value: " + sum / Config.sa_algorithm.getNumber_of_runs());

        Log.logger.info("Max fitness: " + max + " Min fitness: "+ min);
    }

    static void computeCoolingFactor(int numberOfTasks){
        if (!Config.sa_algorithm.force_cooling){
            if (numberOfTasks >= 10){
                Config.sa_algorithm.cooling_factor = 1 - 1 / (double)numberOfTasks;
            }else {
                Config.sa_algorithm.cooling_factor = 0.9;
            }
        }
    }
}
