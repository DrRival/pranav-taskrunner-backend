package com.pranav.taskrunner.service;

import com.pranav.taskrunner.model.TaskExecution;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.client.*;

import java.time.Instant;
import java.util.UUID;

public class KubernetesExecutor {

    private final KubernetesClient client = new KubernetesClientBuilder().build();

    public TaskExecution runCommand(String command) {
        String podName = "task-runner-" + UUID.randomUUID().toString().substring(0, 6);

        //Build a short-lived BusyBox pod that runs the command
        Pod pod = new PodBuilder()
                .withNewMetadata()
                    .withName(podName)
                .endMetadata()
                .withNewSpec()
                    .withContainers(new ContainerBuilder()
                        .withName("runner")
                        .withImage("busybox:1.35")
                        .withCommand("/bin/sh", "-c", command + " ; echo EXIT:$?")
                        .withTty(false)
                        .build())
                    .withRestartPolicy("Never")
                .endSpec()
                .build();

        client.pods().inNamespace("default").create(pod);

        Instant start = Instant.now();
        String output = "";

        //Wait until the pod finishes and grab its logs
        for (int i = 0; i < 60; i++) {
            Pod current = client.pods().inNamespace("default").withName(podName).get();
            if (current == null) break;
            String phase = current.getStatus().getPhase();
            if ("Succeeded".equals(phase) || "Failed".equals(phase)) {
                output = client.pods().inNamespace("default").withName(podName).getLog();
                break;
            }
            try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
        }

        //Delete the pod after it completes
        //client.pods().inNamespace("default").withName(podName).delete();

        Instant end = Instant.now();
        TaskExecution exec = new TaskExecution();
        exec.setStartTime(start);
        exec.setEndTime(end);
        exec.setOutput(output);

        return exec;
    }
}
