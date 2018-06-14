package org.optframework.core.utils;

import org.cloudbus.spotsim.enums.InstanceType;
import org.optframework.core.Job;
import org.optframework.core.Workflow;

import java.util.List;

public class PreProcessing {
    public static Workflow doPreProcessing(org.cloudbus.cloudsim.util.workload.Workflow workflow){
        double exeTime[] = new double[InstanceType.values().length];
        List<Job> jobList = null;

        for (org.cloudbus.cloudsim.util.workload.Job job : workflow.getJobList()){
            for (InstanceType type: InstanceType.values()){
                exeTime[type.getId()] = TaskUtility.executionTimeOnType(job,type);
            }
            jobList.add(new Job(job.getIntId(),
                    0,
                    0,
                    exeTime,
                    job.getEdgeInfo()));
        }

        return new Workflow(workflow.getWfDAG(),
                jobList,
                workflow.getJobList().size(),
                workflow.getDeadline(),
                workflow.getBudget(),
                0.0);
    }
}
