## 📸 Screenshots (With Date & Name)

| Step | Description | Screenshot |
|------|--------------|-------------|
| 1 | Mongo container running | ![Docker PS](screenshots/1_docker_ps.png) |
| 2 | Spring Boot app running | ![Spring Boot](screenshots/2_mvn_run.png) |
| 3 | Tasks list (after creation & execution) | ![GET tasks](screenshots/3_get_tasks.png) |
| 4 | Task created | ![PUT task](screenshots/4_put_task.png) |
| 5 | Execution simulated | ![Execution](screenshots/5_execute_task.png) |

---

## ⚙️ Task 2 – Kubernetes Pod Execution (Fabric8 Client)

### 🧠 Overview
This phase extends the backend to execute task commands inside a **real Kubernetes pod** using the Fabric8 Java Client.  
Each execution dynamically creates a short-lived BusyBox pod, runs the task’s command, retrieves logs, and then deletes the pod.

### 🧰 Steps to Run
```bash
# 1. Ensure Minikube is running
minikube start

# 2. Verify cluster
kubectl get nodes

# 3. Run the backend
mvn spring-boot:run

# 4. Execute a task
curl -X PUT http://localhost:8080/api/tasks/<task-id>/executions
