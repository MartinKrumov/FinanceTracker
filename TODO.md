## Tasks:
### Additional Improvements
1. Separate instance for the cache provider. 
2. Fix issue with JWT issuer
3. Translate exceptions in the FilterChain to the AdviceController ✓
4. Working with files, images (proper storage)
5. Reactive Programming and Messaging
   1. Webflux, R2DBC
   2. Kafka - Confluent course, others
   3. Messaging patterns
6. Use Test containers instead of in memory db
7. JsonSchema
8. Spring docs instead of spring fox, because of bug with 2.6 **PathMatchers**
9. Persistence Improvements
   1. Use sequences for entities in PostgreSQL
   2. EntityGraphs/Projections for fetching data from repos
   3. Entity versioning (Envers/Javers)
   4. Change liquibase scripts 
      1. Use preconditions
      2. Use other style for foreign key mappings
 ```xml
   <column name="employee_id" type="int">
       <constraints nullable="false" foreignKeyName="fk_address_employee" references="employee(id)"/>
   </column>
   ```

10. Deployment and Packaging
    1. CI/CD solution, GitActions?
    2. Use packeto for building images or [JLINK](https://levelup.gitconnected.com/java-developing-smaller-docker-images-with-jdeps-and-jlink-d4278718c550)
    3. Use new layered jar for creating [**Docker Images**](https://spring.io/blog/2020/01/27/creating-docker-images-with-spring-boot-2-3-0-m1)
       https://reflectoring.io/spring-boot-docker/
    4. Choose and configure container registry
11. Kubernetes:
    1. Rearrange packages (Improve docker k8s folders structure)
    2. Add [Scaffold](https://piotrminkowski.com/2020/02/14/local-java-development-on-kubernetes/), Kustomize, Helm or other solutions
    5. ~~**Add ConfigMaps/Secrets for deployments using Spring Cloud Refresh**~~ ✓
    ~~6. Fix volumes for PostgreSQL after K8s cluster restart
    7. ~~Mount init script for creating dbs if not exist on start up of PostgreSQL~~ ✓
    8. Find a way to import data with volume in keycloak without creating separate image ✓
12. Management
    1. Add it to README, chose between adoc or md
    2. Move from TODO file to issue for the tasks and group them by labels
    3. Improve the README and describe how to start the projects
13. Monitoring
    1. Integrate monitoring tools (Prometheus, Grafana)
14. [OpenApi3 + Gateway](https://piotrminkowski.com/2020/02/20/microservices-api-documentation-with-springdoc-openapi/)
---
WSL2
