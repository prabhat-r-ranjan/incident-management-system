# Incident Management System - DevOps Learning Journey

## Project Overview

Built an enterprise-grade Incident Management System using Spring Boot, Next.js, Kubernetes and Azure. The objective was not only to build the application but also to implement production-style DevOps practices such as CI/CD, GitOps, Monitoring, Scaling, Health Checks and Persistent Storage.

---

# Technology Stack

* Spring Boot
* Next.js
* PostgreSQL
* Docker
* Azure Kubernetes Service (AKS)
* Azure Container Registry (ACR)
* GitHub Actions
* ArgoCD
* Kustomize
* NGINX Ingress Controller
* Prometheus
* Grafana

---

# DevOps Features Implemented

## 1. Docker

Implemented Dockerfiles for both Backend and Frontend.

Topics covered:

* Dockerfile
* Multi-stage build
* Docker Build
* Docker Images
* Containerization

---

## 2. Azure Container Registry (ACR)

Implemented private image registry.

Topics covered:

* Create ACR
* Docker Login
* Push Images
* SHA Tagging
* Latest Tag
* Pull Images from AKS

Flow:

Developer
↓
Docker Build
↓
Push Image to ACR

---

## 3. GitHub Actions (CI)

Implemented CI pipeline for Backend and Frontend.

Pipeline:

Code Push
↓
GitHub Actions
↓
Build Application
↓
Build Docker Image
↓
Push Image to ACR

Topics covered:

* GitHub Actions
* CI Pipeline
* Docker Build Automation
* Image Tagging

---

## 4. Kubernetes Basics

Implemented application deployment on AKS.

Topics covered:

* Pods
* ReplicaSet
* Deployment
* Services

---

## 5. Kubernetes Services

Implemented different service types.

Topics covered:

* ClusterIP
* LoadBalancer

Understanding:

ClusterIP

* Internal communication

LoadBalancer

* External communication

---

## 6. ConfigMap

Externalized application configuration.

Example:

* Database URL

Benefits:

* Configuration separated from application

---

## 7. Secret

Stored sensitive information.

Examples:

* Database Username
* Database Password

Benefits:

* Secure secret management

---

## 8. NGINX Ingress Controller

Implemented single entry point for application.

Before

Frontend LoadBalancer
Backend LoadBalancer

After

Internet
↓
Ingress
↓
/ → Frontend
/api → Backend

Topics covered:

* NGINX Ingress
* Path Based Routing
* ClusterIP Services
* Single External IP

Frontend API configuration changed from:

http://LoadBalancerIP:8081/api

to

/api

---

## 9. ArgoCD (GitOps)

Implemented GitOps deployment.

Flow:

Git Push
↓
ArgoCD
↓
Automatic Sync
↓
Deployment

Benefits:

* No manual kubectl apply
* Declarative deployment

---

## 10. Kustomize

Implemented Kubernetes manifest management.

Topics covered:

* Base configuration
* Resource management
* Deployment organization

Managed Resources:

* Backend
* Frontend
* PostgreSQL
* Ingress
* PVC

---

## 11. Monitoring

Implemented monitoring stack.

Tools:

* Prometheus
* Grafana
* Spring Boot Actuator

Topics covered:

* Metrics Collection
* Prometheus Endpoint
* ServiceMonitor
* Grafana Dashboard

---

## 12. Horizontal Pod Autoscaler (HPA)

Implemented CPU based auto scaling.

Topics covered:

* CPU Requests
* CPU Limits
* Metrics Server
* CPU Utilization
* Scheduler
* Scale Up
* Scale Down
* CPU Throttling

Configuration:

Request CPU : 100m

Limit CPU : 500m

Target Utilization : 30%

Min Replicas : 1

Max Replicas : 5

Validation:

Generated load using PowerShell.

Observed automatic scaling from 1 Pod to multiple Pods.

---

## 13. Readiness Probe

Purpose:

Determines whether Pod is ready to receive traffic.

Implemented using:

Spring Boot Actuator

Endpoint:

/actuator/health

Behavior:

If Readiness fails

* Pod is NOT restarted
* Service removes Pod from Endpoints
* Traffic is NOT sent

Validation:

Observed startup failure followed by automatic recovery once application became healthy.

---

## 14. Liveness Probe

Purpose:

Checks whether application is alive.

Behavior:

If Liveness fails

* Kubernetes restarts container

Difference

Readiness

→ Controls Traffic

Liveness

→ Restarts Container

---

## 15. Persistent Volume (PV) & Persistent Volume Claim (PVC)

Problem:

PostgreSQL data was lost whenever Pod was recreated.

Solution:

Implemented Persistent Storage.

Architecture

Postgres Pod
↓
PVC
↓
Azure Managed Disk

Topics covered:

* Persistent Volume
* Persistent Volume Claim
* Volume Mounts
* PGDATA configuration
* Azure Disk

Validation:

* Created incidents
* Deleted PostgreSQL Pod
* New Pod started
* Data remained intact

---

## 16. Spring Boot Actuator

Implemented health endpoints.

Endpoints:

* /actuator/health
* /actuator/prometheus

Used by:

* Readiness Probe
* Liveness Probe
* Prometheus

---

## 17. PostgreSQL on Kubernetes

Implemented:

* Deployment
* Service
* Secret
* PVC
* Persistent Storage

---

## 18. Azure Kubernetes Service (AKS)

Topics covered:

* Cluster Creation
* Node Pool
* Start / Stop Cluster
* Scaling
* Cost Optimization

---

## 19. Azure Cost Optimization

Learned:

* Stop AKS when not in use
* Stop Virtual Machine
* Monitor Azure Cost
* Resource Optimization

---

# Overall Architecture

Developer
↓
GitHub
↓
GitHub Actions
↓
Azure Container Registry
↓
ArgoCD
↓
AKS
↓
Ingress Controller
↓
Frontend
↓
Backend
↓
PostgreSQL
↓
Persistent Volume (Azure Disk)

Monitoring Flow

Spring Boot Actuator
↓
Prometheus
↓
Grafana

Scaling Flow

Metrics Server
↓
HPA
↓
Pods

---

# Topics Remaining

* Rolling Update
* Rollback
* Azure Key Vault
* Namespaces (Dev / QA / Prod)
* Azure Monitor (Optional)
* Network Policies (Optional)

---

# Summary

This project demonstrates practical implementation of:

* Docker
* Kubernetes
* AKS
* GitHub Actions
* Azure Container Registry
* ArgoCD
* GitOps
* Kustomize
* Ingress
* HPA
* Readiness Probe
* Liveness Probe
* Persistent Volume
* PostgreSQL
* Prometheus
* Grafana
* Spring Boot Actuator

The project follows a production-style cloud-native architecture with automated CI/CD, GitOps deployment, health monitoring, auto scaling and persistent storage.
