### Tasks:

1. Refactor yaml properties files for dev and default ✓
    * Datasource and IDP and other properties ✓
1. Use Graceful Shutdown ✓
    * ```server.shutdown=graceful``` ✓
    * ```spring.lifecycle.timeout-per-shutdown-phase=20s``` ✓
1. Configure [**Liveness** and **Readiness**](https://spring.io/blog/2020/03/25/liveness-and-readiness-probes-with-spring-boot) probes for k8s deployments ✓
    * Liveness - ```/actuator/health/liveness``` ✓
    * Readiness - ```/actuator/health/readiness``` ✓
    * [Resources](https://docs.spring.io/spring-boot/docs/2.3.0.BUILD-SNAPSHOT/reference/html/production-ready-features.html#production-ready-kubernetes-probes)
1. Use new layered jar for creating [**Docker Images**](https://spring.io/blog/2020/01/27/creating-docker-images-with-spring-boot-2-3-0-m1)
   https://reflectoring.io/spring-boot-docker/
#### Additional Improvements
1. Change liquibase scripts
    * Use other style for foreign key mappings
    ```xml
   <column name="employee_id" type="int">
       <constraints nullable="false" foreignKeyName="fk_address_employee" references="employee(id)"/>
   </column>
   ```
    * Use preconditions
1. Fix volumes for PostgreSQL after K8s cluster restart
1. Mount init script for creating dbs if not exist on start up of PostgreSQL ✓
1. Find a way to import data with volume in keycloak without creating separate image ✓
1. **Add ConfigMaps/Secrets for deployments using Spring Cloud Refresh**
1. Separate instance for the cache provider
1. Integrate monitoring tools (Prometheus, Grafana)
1. Fix issue with JWT issuer
1. Entity versioning (Envers/Javers)
1. Translate exceptions in the FilterChain to the AdviceController ✓
1. Use sequences for entities in PostgreSQL
1. EntityGraphs/Projections for fetching data from repos
1. Working with files, images (proper storage)
1. Webflux, R2DBC
1. Use Test containers instead of in memory db
1. Improve docker k8s folders structure and add it to README, chose between adoc or md
1. Move from TODO file to issues for the tasks and group them by labels
1. Use packeto for building images 
1. Improve the README and describe how to start the projects

---
WSL2
