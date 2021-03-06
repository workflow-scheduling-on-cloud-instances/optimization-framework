package org.optframework.automator;

import org.optframework.*;
import org.optframework.config.Config;
import org.optframework.core.Log;
import org.optframework.core.utils.CSVWriter;
import static org.optframework.automator.BudgetList.*;
import java.util.ArrayList;

/**
 * Budget Automator (Automates budget setup)
 * This Utility class facilitates getting the result process
 * Automates budget based on Evolutionary Multi-Objective Workflow Scheduling in Cloud paper
 *
 * todo: remove main method and make it configurable through the config file
 * */

public class BudgetAutomator implements GenericAutomator{

    public void run(){
        ArrayList<RunResult> runResultArrayList = new ArrayList<>(Config.automator.number_of_runs);

        double budgetList[] = null;
        GlobalAccess.solutionArrayListToCSV = new ArrayList<>();
        GlobalAccess.timeInMilliSecArrayList = new ArrayList<>();
        GlobalAccess.solutionRepository = new ArrayList<>();

        switch (Config.global.workflow_id){
            case 1: budgetList = inspiral1000; break;
            case 2: budgetList = inspiral100; break;
            case 3: budgetList = inspiral50; break;
            case 4: budgetList = inspiral30; break;
            //
            case 10: budgetList = montage1000; break;
            case 11: budgetList = montage100; break;
            case 12: budgetList = montage50; break;
            case 13: budgetList = montage25; break;
            //
            case 20: budgetList = sipht1000; break;
            case 21: budgetList = sipht100; break;
            case 22: budgetList = sipht60; break;
            case 23: budgetList = sipht30; break;
            //
            case 30: budgetList = epigenomics997; break;
            case 31: budgetList = epigenomics100; break;
            case 32: budgetList = epigenomics46; break;
            case 33: budgetList = epigenomics24; break;
            //
            case 40: budgetList = cybershake1000; break;
            case 41: budgetList = cybershake100; break;
            case 42: budgetList = cybershake50; break;
            case 43: budgetList = cybershake30; break;
        }

        if (budgetList == null){
            throw new RuntimeException("This type of workflow is not supported to be automated");
        }

        for (int i = 0; i < Config.automator.number_of_runs; i++) {
            RunResult runResult = new RunResult();

            for (double budget: budgetList){
                Config.global.budget = budget;
                //at the end of run** method the automator-specific static variables will be filled
                switch (Config.global.algorithm){
                    case "sa":
//                    RunSAAlgorithm.runSA();
                    case "hbmo":
//                    RunHBMOAlgorithm.runHBMO();
                    case "heft":
//                    RunHEFTAlgorithm.runSingleHEFT();
                    case "hbmo-heft":
//                    RunHEFTWithHBMO.runHEFTWithHBMO();
                    case "heft-example":
//                    RunHEFTExample.runHEFTExample();
                    case "pacsa":
//                    RunPACSAAlgorithm.runPACSA(0);
                    case "pacsa-plus":
//                    RunPACSAAlgorithm.runPACSA(1);
                    case "pso":
//                    RunPSOAlgorithm.runPSO(0);
                    case "zpso":
//                    RunPSOAlgorithm.runPSO(1);
                        throw new RuntimeException("This Algorithm does not support Automator");
                    case "iterative-grp-heft": RunIterativeGRPHEFTAlgorithm.runGRPHEFT();break;
                    case "grp-heft": RunGRPHEFTAlgorithm.runGRPHEFT();break;
                    case "grp-pacsa":
                        RunGRPPACSAAlgorithm.runGRPPACSA();
                        Log.logger.info("Results Successfully Generated");
                        break;
                }
            }
            runResult.solutionArrayListToCSV = GlobalAccess.solutionArrayListToCSV;
            runResult.timeInMilliSecArrayList = GlobalAccess.timeInMilliSecArrayList;
            runResultArrayList.add(runResult);

            GlobalAccess.solutionArrayListToCSV = new ArrayList<>();
            GlobalAccess.timeInMilliSecArrayList = new ArrayList<>();
            GlobalAccess.solutionRepository = new ArrayList<>();
        }
        CSVWriter.processResults(runResultArrayList, budgetList);
    }
}
