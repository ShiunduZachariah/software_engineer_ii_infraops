### **1.	Application Setup: Student REST API**  
This project is a simple **Student REST API** built using **Spring Boot** and **MongoDB**. The API provides basic functionality to manage student data, including adding new students to the database and retrieving all students.

---

## **Features**
1. **POST /students**: Add a new student to the database.
2. **GET /students**: Retrieve all students from the database.

---

## **Prerequisites**
1. **Java**: Ensure Java 17+ is installed.
2. **Maven**: Ensure Maven is installed for dependency management.
3. **MongoDB**: Install and start a MongoDB instance running on `localhost:27017`.

---

## **Setup Instructions**
1. Clone the repository:
   ```bash
   git clone https://github.com/ShiunduZachariah/software_engineer_ii_infraops.git
   cd <repository-folder>
   ```

2. Build the project:
   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

4. The application will start on `http://localhost:8080`.

---

## **Endpoints**

### **1. POST /students**
Add a new student to the database.

- **URL**: `http://localhost:8080/students`
- **Method**: `POST`
- **Headers**:  
  - `Content-Type: application/json`
- **Body**: JSON object with the following fields:
  - `name` (String, Required): Name of the student.
  - `email` (String, Required): Email of the student.
  - `age` (Integer, Optional): Age of the student.

#### **Example Request Body**
```json
{
    "name": "John Doe",
    "email": "john.doe@example.com",
    "age": 20
}
```

#### **Example Response**
**Status Code**: `201 Created`  
**Response Body**:
```json
{
    "id": "63b1d1a4d1f4a6452345f1b2",
    "name": "John Doe",
    "email": "john.doe@example.com",
    "age": 20
}
```

---

### **2. GET /students**
Retrieve all students in the database.

- **URL**: `http://localhost:8080/students`
- **Method**: `GET`

#### **Example Response**
**Status Code**: `200 OK`  
**Response Body**:
```json
[
    {
        "id": "63b1d1a4d1f4a6452345f1b2",
        "name": "John Doe",
        "email": "john.doe@example.com",
        "age": 20
    },
    {
        "id": "63b1d1a4d1f4a6452345f1b3",
        "name": "Jane Smith",
        "email": "jane.smith@example.com",
        "age": 25
    }
]
```

---

## **Testing the API in Postman**

1. **Set up Postman**:
   - Download and install [Postman](https://www.postman.com/downloads/).
   - Create a new collection for the Student API.

2. **Add Requests**:
   - **POST /students**:
     - Select `POST` as the HTTP method.
     - Set the URL to `http://localhost:8080/students`.
     - In the "Body" tab, select `raw`, set the content type to `JSON`, and add the request body:
       ```json
       {
           "name": "John Doe",
           "email": "john.doe@example.com",
           "age": 20
       }
       ```
     - Send the request to add a new student.

   - **GET /students**:
     - Select `GET` as the HTTP method.
     - Set the URL to `http://localhost:8080/students`.
     - Send the request to fetch the list of students.

3. **View Results**:
   - Use the `GET /students` request to verify that the data added using the `POST` request appears in the database.

---

## **Reason for the POST Endpoint**
Initially, the database was empty, so the `GET /students` endpoint returned an empty array. The `POST /students` endpoint was introduced to allow users to add student data dynamically, which can then be retrieved using the `GET /students` endpoint.

---
![image](https://github.com/user-attachments/assets/1682ea63-b689-4b47-8d9f-e23544289b16)
![image](https://github.com/user-attachments/assets/c36e8ef0-e8aa-4414-989f-2ef24bfc1188)
![image](https://github.com/user-attachments/assets/e958e206-c793-4534-8c52-7745147bb28a)




### 2. Containerizing a Spring Boot Application with MongoDB using Docker


This document outlines the process of containerizing a Spring Boot application that uses MongoDB as its database using Docker. This setup is crucial for consistent deployments across different environments, including Kubernetes with Minikube.

**Objective:**

To create a Docker image for a Spring Boot application that can connect to a MongoDB database, either running locally or in a containerized environment.

**Prerequisites:**

*   **Java Development Kit (JDK):** Installed and configured.
*   **Maven or Gradle:** For building the Spring Boot application.
*   **Docker Desktop or Docker Engine:** Installed and running.
*   **Spring Boot Application:** A functional Spring Boot application that connects to MongoDB.

**Steps:**

1.  **Configure MongoDB Connection in Spring Boot:**

    Your Spring Boot application should be configured to connect to MongoDB using environment variables. This makes it flexible to connect to different MongoDB instances (local or containerized).

    **Example `application.properties`:**

    ```properties
    spring.application.name=student
    spring.data.mongodb.port=27017
    spring.data.mongodb.database=students
    spring.data.mongodb.username=${SPRING_DATA_MONGODB_USERNAME} # Use environment variables
    spring.data.mongodb.password=${SPRING_DATA_MONGODB_PASSWORD}
    spring.data.mongodb.host=${SPRING_DATA_MONGODB_HOST}
    ```

    This configuration uses environment variables `SPRING_DATA_MONGODB_HOST`, `SPRING_DATA_MONGODB_PORT`, `SPRING_DATA_MONGODB_USERNAME` and `SPRING_DATA_MONGODB_PASSWORD` for MongoDB connection details.

2.  **Create the Dockerfile:**

    Create a file named `Dockerfile` (without any extension) in the root directory of your Spring Boot project.

    ```dockerfile
    # Use a base image with Java 23 (or your project's JDK)
    FROM eclipse-temurin:23-jdk-alpine

    # Set the working directory inside the container
    WORKDIR /app

    # Copy the Maven Wrapper (if using Maven)
    COPY mvnw .
    COPY .mvn .mvn
    COPY pom.xml .

    # Resolve dependencies (using the Maven Wrapper)
    RUN chmod +x ./mvnw
    RUN ./mvnw dependency:go-offline

    # Copy the application source code
    COPY src ./src

    # Build the application (using the Maven Wrapper)
    RUN ./mvnw clean package -DskipTests

    # Copy the built JAR file
    COPY target/student-*.jar app.jar # Update with your JAR file name

    # Expose the application port
    EXPOSE 8080


    # Command to run the application
    CMD ["java", "-jar", "app.jar"]
    ```

    **Explanation of Dockerfile instructions:**

*   `FROM`: Specifies the base image. `eclipse-temurin:21-jdk-alpine` is a small and efficient base image with Java 21.
*   `WORKDIR`: Sets the working directory inside the container.
*   `COPY`: Copies files and directories from your local machine to the container.
*   `RUN`: Executes commands inside the container during the image build process.
*   `EXPOSE`: Exposes the specified port.
*   `CMD`: Specifies the command to run when the container starts.

3.  **Build the Docker Image:**

    Open a terminal in the root directory of your project (where the `Dockerfile` is located) and run:

    ```bash
    docker build -t <your-dockerhub-username>/<your-app-name>:<tag> .
    ```

    Replace:

    *   `<your-dockerhub-username>`: Your Docker Hub username (or other registry username).
    *   `<your-app-name>`: The name you want to give to your Docker image.
    *   `<tag>`: A tag for your image (e.g., `latest`, `1.0.0`).

4.  **Run the Docker Container (Connecting to a Local MongoDB):**

    To run the container and connect to a MongoDB instance running on your *local machine*, use the following command:

    ```bash
    docker run -p 8080:8080 \
        -e SPRING_DATA_MONGODB_HOST=localhost \
        -e SPRING_DATA_MONGODB_PORT=27017 \
        -e SPRING_DATA_MONGODB_DATABASE=students \
        -e SPRING_DATA_MONGODB_USERNAME=root \
        -e SPRING_DATA_MONGODB_PASSWORD=example \
        <your-dockerhub-username>/<your-app-name>:<tag>
    ```

    *   `-p 8080:8080`: Maps port 8080 on your host machine to port 8080 in the container.
    *   `-e`: Sets environment variables.

5.  **Run the Docker Container (Connecting to a Containerized MongoDB):**

    If you want to run MongoDB in a separate Docker container, you can use `docker-compose` or run the MongoDB container manually. Here's how to run it manually:

    ```bash
    docker run -d --name mongodb -p 27017:27017 \
        -e MONGO_INITDB_ROOT_USERNAME=root \
        -e MONGO_INITDB_ROOT_PASSWORD=example \
        mongo:latest
    ```

    Then, run your Spring Boot container, linking it to the MongoDB container:

    ```bash
    docker run -p 8080:8080 \
        --link mongodb:mongodb \ # Links the containers
        -e SPRING_DATA_MONGODB_HOST=mongodb \ # Use the container name as host
        -e SPRING_DATA_MONGODB_PORT=27017 \
        -e SPRING_DATA_MONGODB_DATABASE=students \
        -e SPRING_DATA_MONGODB_USERNAME=root \
        -e SPRING_DATA_MONGODB_PASSWORD=example \
        <your-dockerhub-username>/<your-app-name>:<tag>
    ```
    The `--link` option is now deprecated. It is better to use docker networks.

    ```bash
    docker network create my-network
    docker run -d --name mongodb --network my-network -p 27017:27017 \
        -e MONGO_INITDB_ROOT_USERNAME=root \
        -e MONGO_INITDB_ROOT_PASSWORD=example \
        mongo:latest
    docker run -p 8080:8080 --network my-network \
        -e SPRING_DATA_MONGODB_HOST=mongodb \
        -e SPRING_DATA_MONGODB_PORT=27017 \
        -e SPRING_DATA_MONGODB_DATABASE=students \
        -e SPRING_DATA_MONGODB_USERNAME=root \
        -e SPRING_DATA_MONGODB_PASSWORD=example \
        <your-dockerhub-username>/<your-app-name>:<tag>
    ```

6.  **Push the Docker Image (After Testing):**

    Once you've confirmed the container runs correctly, push it to your Docker Hub repository:

    ```bash
    docker push <your-dockerhub-username>/<your-app-name>:<tag>
    ```

![image](https://github.com/user-attachments/assets/bbd92998-799c-49a9-8022-9f04a4676175)
![image](https://github.com/user-attachments/assets/3c72dd44-c208-4331-8f51-42a456190c39)
![image](https://github.com/user-attachments/assets/ede37b9e-285f-4dda-ba9c-a244f3bba94f)
![image](https://github.com/user-attachments/assets/e610bac0-4bcc-402b-81dc-442a2f5c3063)



### 3. Setting up CI/CD with GitHub Actions for Docker Image Build and Push

This document describes how to set up a Continuous Integration/Continuous Deployment (CI/CD) pipeline using GitHub Actions to automatically build and push your Docker image to a container registry (Docker Hub in this example) whenever you push changes to your repository.

**Objective:**

To automate the Docker image build and push process using GitHub Actions, ensuring that your container image is always up-to-date in your container registry.

**Prerequisites:**

*   **GitHub Repository:** You have a GitHub repository containing your project and a `Dockerfile`.
*   **Docker Hub Account:** You have an account on Docker Hub (or another container registry like GitHub Container Registry).
*   **Docker Hub Credentials:** You have your Docker Hub username and a Docker Hub access token (recommended over your password).

**Steps:**

1.  **Create GitHub Secrets:**

    Go to your GitHub repository:

    *   Go to "Settings" -> "Secrets and variables" -> "Actions".
    *   Click "New repository secret".

    Create the following secrets:

    *   `DOCKER_USERNAME`: Your Docker Hub username.
    *   `DOCKER_PASSWORD`: Your Docker Hub access token.

2.  **Create the GitHub Actions Workflow File:**

    Create a file named `.github/workflows/docker-build-and-push.yml` (or any `.yml` file you prefer) in your repository.

    ```yaml
    name: CI/CD Pipeline for Docker

on:
  push:
    branches:
      - master # Or main, depending on your default branch

jobs:
  build-and-push:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout repository
        uses: actions/checkout@v3

      - name: Set up JDK 23
        uses: actions/setup-java@v3
        with:
          java-version: '23'
          distribution: 'temurin'

      - name: Cache Maven local repository # Improves build speed
        uses: actions/cache@v3
        with:
          path: ~/.m2/repository
          key: ${{ runner.os }}-maven-${{ hashFiles('**/pom.xml') }}
          restore-keys: |
            ${{ runner.os }}-maven-

      - name: Grant execute permission for mvnw
        run: chmod +x ./mvnw

      - name: Build with Maven
        run: ./mvnw clean package

      - name: Log in to Docker Hub
        uses: docker/login-action@v2
        with:
          username: ${{ secrets.DOCKER_USERNAME }}
          password: ${{ secrets.DOCKER_PASSWORD }}

      - name: Build and tag Docker image
        run: |
          docker build -t students-app:latest .
          docker tag students-app:latest ${{ secrets.DOCKER_USERNAME }}/students-app:latest

      - name: Push Docker image to Docker Hub
        run: docker push ${{ secrets.DOCKER_USERNAME }}/students-app:latest
    ```

    **Explanation of the Workflow File:**

    *   `name`: The name of your workflow.
    *   `on.push.branches`: Specifies the branches that trigger the workflow.
    *   `jobs.build-and-push`: Defines the job that will be executed.
    *   `runs-on`: Specifies the operating system for the runner (Ubuntu in this case).
    *   `permissions`: These are required for the `docker/build-push-action` to work correctly.
    *   `steps`: Defines the individual steps within the job:
        *   `actions/checkout@v3`: Checks out your code.
        *   `docker/setup-buildx-action@v2`: Sets up Docker Buildx, which is needed for building multi-platform images and caching.
        *   `docker/login-action@v2`: Logs in to Docker Hub using the secrets you created.
        *   `docker/build-push-action@v4`: Builds and pushes the Docker image.
            *   `context`: The build context (the current directory `.`).
            *   `push: true`: Enables pushing the image.
            *   `tags`: Tags the image with the commit SHA and `latest`. Replace `<your-app-name>` with your application's name.
            *   `build-args`: Adds build arguments for the date and version.
            *   `cache-from` and `cache-to`: Uses Buildx caching to speed up subsequent builds.
    *   `Move cache`: moves the new cache to the old cache location.

3.  **Commit and Push the Workflow File:**

    Commit the `.github/workflows/docker-build-and-push.yml` file to your repository and push the changes.

4.  **Trigger the Workflow:**

    The workflow will automatically be triggered whenever you push changes to the specified branch (e.g., `master`).

5.  **Monitor the Workflow Run:**

    Go to the "Actions" tab in your GitHub repository to monitor the progress of the workflow run.

![image](https://github.com/user-attachments/assets/f62ba279-b8cb-41e8-9638-8a189dd39792)
![image](https://github.com/user-attachments/assets/a80c5470-dd7a-49b3-8f28-5487572954b7)
![image](https://github.com/user-attachments/assets/354c4622-1779-4d71-856c-8c4758950742)
![image](https://github.com/user-attachments/assets/6a3797e1-445a-4bbf-96b8-4d201184c9fb)


### 4. Deploying a Spring Boot Application with MongoDB on Minikube

This document details the process of deploying a Dockerized Spring Boot application that uses MongoDB as its database to a local Kubernetes cluster using Minikube.

**Objective:**

To deploy and expose a containerized Spring Boot application connected to a MongoDB database using Kubernetes manifests on a local Minikube cluster, making it accessible locally.

**Prerequisites:**

*   **Minikube Installed:** Install Minikube using the official guide: [https://minikube.sigs.k8s.io/docs/start/](https://minikube.sigs.k8s.io/docs/start/) Verify installation: `minikube version`
*   **kubectl Installed:** Install the Kubernetes CLI: [https://kubernetes.io/docs/tasks/tools/](https://kubernetes.io/docs/tasks/tools/) Verify installation: `kubectl version --client`
*   **Docker Installed:** Install Docker Desktop or Docker Engine.
*   **Docker Image Built and Pushed:** Ensure your Spring Boot application is containerized and the Docker image is pushed to a container registry (e.g., Docker Hub). Verify by pulling the image: `docker pull <your-dockerhub-username>/<your-app-name>:<tag>`
*   **Spring Boot Application Configured:** Your Spring Boot application should be configured to connect to MongoDB using environment variables.

**Steps:**

1.  **Start Minikube:**

    ```bash
    minikube start
    ```

2.  **Create Kubernetes Manifests:**

    Create a directory named `k8s` in the root of your project. Inside this directory, create the following YAML files:

    **k8s/mongodb-deployment.yaml:**

    ```yaml
    apiVersion: apps/v1
    kind: Deployment
    metadata:
      name: mongodb
    spec:
      replicas: 1
      selector:
        matchLabels:
          app: mongodb
      template:
        metadata:
          labels:
            app: mongodb
        spec:
          containers:
          - name: mongodb
            image: mongo:latest
            ports:
            - containerPort: 27017
            env:
            - name: MONGO_INITDB_ROOT_USERNAME
              value: root # Change this for production!
            - name: MONGO_INITDB_ROOT_PASSWORD
              value: example # Change this for production!
    ```

    **k8s/mongodb-service.yaml:**

    ```yaml
    apiVersion: v1
    kind: Service
    metadata:
      name: mongodb-service
    spec:
      selector:
        app: mongodb
      ports:
        - protocol: TCP
          port: 27017
          targetPort: 27017
      type: ClusterIP # Only accessible within the cluster
    ```

    **k8s/deployment.yaml (Your Spring Boot Application):**

    ```yaml
    apiVersion: apps/v1
    kind: Deployment
    metadata:
      name: student-app
      labels:
        app: student-app
    spec:
      replicas: 1
      selector:
        matchLabels:
          app: student-app
      template:
        metadata:
          labels:
            app: student-app
        spec:
          containers:
            - name: student-app
              image: <your-dockerhub-username>/<your-app-name>:<tag> # Replace placeholders
              imagePullPolicy: IfNotPresent # Use Always for production
              ports:
                - containerPort: 8080
                  protocol: TCP
              env:
                - name: SPRING_DATA_MONGODB_HOST
                  value: mongodb-service # Connect using the service name
                - name: SPRING_DATA_MONGODB_PORT
                  value: "27017"
                - name: SPRING_DATA_MONGODB_DATABASE
                  value: students # Your database name
                - name: SPRING_DATA_MONGODB_USERNAME # Only if authentication is enabled
                  value: root # Change this for production!
                - name: SPRING_DATA_MONGODB_PASSWORD # Only if authentication is enabled
                  value: example # Change this for production!
              resources:
                requests:
                  cpu: 250m
                  memory: 512Mi
                limits:
                  cpu: 500m
                  memory: 1Gi
      restartPolicy: Always
    ```

    **k8s/service.yaml (Your Spring Boot Application):**

    ```yaml
    apiVersion: v1
    kind: Service
    metadata:
      name: student-app-service
    spec:
      selector:
        app: student-app
      ports:
        - protocol: TCP
          port: 8080 # Port exposed externally
          targetPort: 8080 # Port your app listens on inside the container
      type: NodePort # Important for local access
    ```

3.  **Apply the Manifests:**

    ```bash
    kubectl apply -f k8s/
    ```

4.  **Verify Deployment and Service Status:**

    ```bash
    kubectl get deployments
    kubectl get services
    kubectl get pods
    ```

    Check that the deployments are `READY` and the pods are `Running`. If there are issues, use `kubectl describe pod <pod-name>` or `kubectl logs <pod-name>` for more details.

5.  **Access the Application:**

    **Method 1: Using `minikube service`:**

    ```bash
    minikube service student-app-service --url
    ```

    This command will output the URL, including the Minikube IP and the NodePort, and may even open your default browser.

    **Method 2: Manual Method:**

    ```bash
    minikube ip # Get the Minikube IP (e.g., 192.168.49.2)
    kubectl get service student-app-service # Get the NodePort (e.g., 32000)
    # Construct the URL: http://<minikube-ip>:<node-port> (e.g., http://192.168.49.2:32000)
    ```

    Open your web browser and navigate to the constructed URL.

**Important Considerations:**

*   **MongoDB Credentials:** The MongoDB credentials (`root`/`example`) are for demonstration purposes *only*. **In production, use strong, unique credentials and manage them with Kubernetes Secrets.**
*   **Resource Limits:** Adjust the `resources` section in your application's deployment to match your application's needs.
*   **Image Pull Policy:** `IfNotPresent` is suitable for local development. In production, use `Always` or `IfNotPresent` combined with image tags for better control.
*   **Service Type:** `NodePort` is used for local access with Minikube. For production deployments on cloud providers, use `LoadBalancer`.
*   **Namespaces:** For more complex deployments, consider using Kubernetes namespaces to isolate your application and its resources.

**Troubleshooting:**

*   **ImagePullBackOff:** Verify the Docker image name and ensure it's pushed to your registry.
*   **CrashLoopBackOff:** Check pod logs: `kubectl logs <pod-name>`. This often indicates an error in your application's code or configuration (e.g., incorrect MongoDB connection details).
*   **Connection Refused (from application to MongoDB):** Ensure `SPRING_DATA_MONGODB_HOST` is set to `mongodb-service` in your application's deployment.
*   **Cannot Connect to Application from Browser:**
    *   Verify the Minikube IP and NodePort are correct.
    *   Check your local firewall to ensure it's not blocking connections to the NodePort on the Minikube IP.
    *   Restart Minikube (`minikube stop` then `minikube start`) to refresh the network configuration.

This comprehensive documentation should help you successfully deploy your Spring Boot application with MongoDB on Minikube. 

![image](https://github.com/user-attachments/assets/e411c343-4520-4d2b-a377-19818b62eca4)
![image](https://github.com/user-attachments/assets/071d1ffe-f137-4ab4-8856-3a2764eefaac)
![image](https://github.com/user-attachments/assets/edd84a16-805e-4275-831a-30485fe42765)
![image](https://github.com/user-attachments/assets/95e8f5d2-c372-4f49-85ab-eed07b954a9a)
![image](https://github.com/user-attachments/assets/e533bece-dd61-4fe2-a82f-3b144780998a)
![image](https://github.com/user-attachments/assets/afda699a-7a20-4ca7-ab76-0acf3ac8c184)
![image](https://github.com/user-attachments/assets/b240b8c5-1a46-4514-9820-9179642edc51)

### Thank You!
