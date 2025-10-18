## 📸 Screenshots (With Date & Name)

| Step | Description | Screenshot |
|------|--------------|-------------|
| 1 | Mongo container running | ![Docker PS](screenshots/1_docker_ps.png) |
| 2 | Spring Boot app running | ![Spring Boot](screenshots/2_mvn_run.png) |
| 3 | Tasks list (after creation & execution) | ![GET tasks](screenshots/3_get_tasks.png) |
| 4 | Task created | ![PUT task](screenshots/4_put_task.png) |
| 5 | Execution simulated | ![Execution](screenshots/5_execute_task.png) |
---

## ☸️ Task 3 – Kubernetes Deployment (Minikube)

### 🧠 Overview
The Spring Boot backend is containerized using Docker and deployed in Minikube along with MongoDB.  
The setup uses a Deployment + Service pattern for both components and exposes the API through NodePort 30080.

### 🧰 Steps to Build and Deploy

```bash
# 1. Package the Spring Boot app
mvn clean package -DskipTests

# 2. Point Docker to Minikube's internal daemon
& minikube -p minikube docker-env | Invoke-Expression

# 3. Build image inside Minikube
docker build -t taskrunner-backend:latest .

# 4. Apply Kubernetes manifests
kubectl apply -f k8s/mongo-deployment.yml
kubectl apply -f k8s/taskrunner-deployment.yml

# 5. Verify
kubectl get pods
kubectl get svc

# 6. Access the service
minikube service taskrunner-service --url


