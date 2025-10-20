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

# 3. Run the backend locally
mvn spring-boot:run

# 4. Execute a task
curl -X PUT http://localhost:8080/api/tasks/<task-id>/executions

---

⚙️ Task 3 – Kubernetes Deployment (Minikube)
🧠 Overview

The Spring Boot backend was containerized using Docker and deployed to Minikube along with MongoDB.
Both use the Deployment + Service pattern, and the API is exposed through NodePort 30080.

🧰 Steps to Run
# 1. Package the Spring Boot app
mvn clean package -DskipTests

# 2. Point Docker to Minikube’s internal daemon
& minikube -p minikube docker-env | Invoke-Expression

# 3. Build the image inside Minikube
docker build -t taskrunner-backend:latest .

# 4. Apply Kubernetes manifests
kubectl apply -f k8s/mongo-deployment.yml
kubectl apply -f k8s/taskrunner-deployment.yml

# 5. Verify deployments
kubectl get pods
kubectl get svc

# 6. Access the service
minikube service taskrunner-service --url

⚙️ Task 4 – Ingress & External Access
🧠 Overview

This task exposed the TaskRunner backend externally via a Kubernetes Ingress using Minikube’s NGINX Ingress controller.
The API became reachable at http://taskrunner.local/api/tasks
.

🧩 Steps Implemented
# 1. Enable Ingress in Minikube
minikube addons enable ingress


Created k8s/taskrunner-ingress.yml mapping taskrunner.local → taskrunner-service:8080

Updated local hosts file:

127.0.0.1 taskrunner.local


Verified routing with:

curl http://taskrunner.local/api/tasks

🧾 Current Status

✅ Ingress controller: Running

✅ Routing & DNS mapping: Working

✅ Backend pod & MongoDB connection: Stable

⚠️ Pending Fix: Spring Boot controller logs not visible → occasional 404 Not Found

⚙️ Task 5 – Helm Chart Packaging & Deployment
🧠 Overview

This task focused on converting all Kubernetes manifests into a Helm Chart for reusable and automated deployment of both backend and MongoDB.

🧩 Work Completed

Created Helm chart under helm/taskrunner-chart/

helm/taskrunner-chart/
├── Chart.yaml
├── values.yaml
└── templates/
    ├── deployment.yaml
    ├── service.yaml
    ├── mongo-deployment.yaml
    ├── mongo-service.yaml
    └── ingress.yaml


Parameterized values in values.yaml

Verified YAML syntax and template consistency

Built local Docker image (taskrunner-backend:latest)

Verified manual deployment in Kubernetes

⚠️ Environment Limitation

Helm CLI installation failed on the current Windows setup (no Windows binaries for latest version).
All chart templates are ready and validated, awaiting Helm installation for final deployment.
