# Pruebas de Integración con Spring Boot Test — Esqueleto

Este proyecto es un **esqueleto listo** para que los estudiantes practiquen **pruebas de integración** en Spring Boot:
- **Capas**: controlador, servicio, repositorio, entidad `Producto`.
- **Base de datos en memoria H2** (perfil de test).
- **Tipos de prueba** incluidos:
  - `@DataJpaTest` (repositorio)
  - `@SpringBootTest` (servicio)
  - `@SpringBootTest` + `@AutoConfigureMockMvc` (controlador REST)

## Requisitos
- Java 17+
- Maven 3.9+

## Ejecutar la app
```bash
mvn spring-boot:run
```

## Ejecutar pruebas
```bash
mvn clean test
```

## Generar reporte HTML de ejecucion de Prueba 
```bash
mvn surefire-report:report
```

## Generar reporte de Cobertura
```bash
mvn jacoco:report
```

## Estructura clave
```
src/
 ├─ main/
 │   ├─ java/com/example/productos/...   # código fuente App
 │   └─ resources/application.properties # configuración H2 runtime
 └─ test/
     ├─ java/com/example/productos/...   # pruebas (repo, servicio, controller)
     └─ resources/application-test.properties
target/
 ├─ site/jacoco/index.html
 └─ reports/surefire.html
```

## Actividades Realizadas
1. Se agregaron validaciones (Bean Validation) y probar errores 400.
2. Se añadieron endpoints PUT/PATCH y sus pruebas.
3. Se modifico el codigo para que las clases de las pruebas del controlador fueran publicas y se pudiese acceder a ellas
4. Se configuraron plugins para generar reportes HTML de ejecucion de Pruebas con surefire y para medir la cobertura con JaCoCo.
5. Se completa la creación de casos de pruebas para cubrir el 100% de codigo ejecutado
5. Se integro un pipeline CI (GitHub Actions) que ejecute `mvn test` y que publica los reportes en github.
6. 
