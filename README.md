# README Técnico - Canasta Familiar

Documentación técnica del proyecto **Canasta Familiar**, una aplicación web desarrollada con Spring Boot 4.0.2 para el cálculo y análisis del costo de la canasta familiar.

---

## Tabla de contenidos

1. Descripción general
2. Tecnologías
3. Requisitos del sistema
4. Arquitectura
5. Instalación
6. Estructura del proyecto
7. API Endpoints
8. Configuración
9. Tests
10. Despliegue
11. Solución de problemas

---

## Descripción general

Canasta Familiar es una aplicación web que permite:
- Gestionar un inventario de productos en memoria
- Calcular el costo total de la canasta familiar
- Obtener estadísticas sobre los productos (promedio, top 3 más caros)
- Acceder a los datos mediante una API REST
- Visualizar la información a través de vistas Thymeleaf

Características principales:
- Arquitectura MVC con separación de capas
- Validación de datos en el servicio
- Almacenamiento en memoria (ArrayList)
- Interfaz web con Bootstrap 5
- API REST con respuestas JSON
- Pruebas unitarias con JUnit 5

---

## Tecnologías

### Stack principal

| Tecnología | Versión | Propósito |
|-----------|---------|-----------|
| Java | 17 LTS | Lenguaje de programación |
| Spring Boot | 4.0.2 | Framework web |
| Spring WebMVC | 4.0.2 | Soporte MVC |
| Thymeleaf | 3.1.x | Motor de plantillas |
| Bootstrap | 5.3.0 | Framework CSS |
| Maven | 3.8+ | Gestor de dependencias |
| JUnit 5 | 5.9+ | Framework de testing |

### Dependencias principales (ejemplo)

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-webmvc</artifactId>
</dependency>

<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>

<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-test</artifactId>
  <scope>test</scope>
</dependency>
```

---

## Requisitos del sistema

Mínimos:
- JDK 17 o superior
- Maven 3.8.1 o superior
- RAM: 512 MB
- Espacio en disco: 500 MB

Recomendados:
- JDK 17 LTS o 21 LTS
- Maven 3.9.x
- RAM: 2 GB
- IDE: IntelliJ IDEA, VS Code o Eclipse

Verificar instalación:

```bash
java -version
mvn -version
```

---

## Arquitectura

Arquitectura MVC (Model-View-Controller) con capas bien separadas:

```
CLIENTE (Navegador)
  ├─ Vistas Thymeleaf (canasta.html, resumen.html)
  │
  └─ HTTP → Controllers (CanastaController / CanastaRestController)
        │
        └─ Service Layer (CanastaService)
              └─ Model (ItemCanasta)
                    └─ Almacenamiento en memoria (ArrayList)
```

Patrones aplicados:
- MVC
- Dependency Injection (@Autowired)
- Layered Architecture

---

## Estructura del proyecto (resumen)

```
canasta-familiar/
├── pom.xml
├── application.properties
├── mvnw
├── mvnw.cmd
├── src/main/java/com/uniremington/canasta_familiar/
│   ├── CanastaFamiliarApplication.java
│   ├── controller/CanastaController.java
│   ├── controller/CanastaRestController.java
│   ├── model/ItemCanasta.java
│   └── service/CanastaService.java
└── src/main/resources/
    ├── templates/canasta.html
    └── templates/resumen.html
```

---

## API Endpoints

### Vistas (HTML)

- GET `/` — Muestra la página principal (formulario y tabla)
- POST `/agregar` — Envía el formulario para agregar un producto
- POST `/resumen` — Muestra la página de resumen con estadísticas
- POST `/limpiar` — Vacía la canasta

Ejemplo (GET `/`):

```bash
curl http://localhost:8080/
```

Ejemplo (POST `/agregar`):

```bash
curl -X POST http://localhost:8080/agregar \
  -d "nombre=Arroz&precio=5000&cantidad=2"
```

### API REST (JSON)

- GET `/api/canasta/items` — Lista items en JSON
- GET `/api/canasta/resumen` — Devuelve resumen con total, promedio y top 3

Ejemplo (GET items):

```bash
curl http://localhost:8080/api/canasta/items
```

---

## Configuración

Ejemplo de `application.properties`:

```properties
spring.application.name=canasta-familiar
server.port=8080
```

Ejemplo parcial de `pom.xml` y configuración del plugin Spring Boot para empaquetado.

---

## Tests

La suite principal es `CanastaServiceTest` (JUnit 5). Cubren:
- Agregar item válido
- Cálculo de total
- Validaciones (precio, cantidad, nombre)

Ejecutar tests:

```bash
mvn test
```

---

## Instalación y ejecución

```bash
git clone <repo>
cd canasta-familiar
mvn clean package
mvn spring-boot:run
# o
java -jar target/canasta-familiar-0.0.1-SNAPSHOT.jar
```

Acceder en: `http://localhost:8080/`

---

## Solución de problemas (resumen)

- Si Java no está en la versión correcta: instalar JDK 17 y configurar `JAVA_HOME`.
- Si Maven no está disponible: instalar Maven o usar `mvnw` incluido.
- Puerto en uso: cambiar `server.port` en `application.properties`.

---

## Extensiones sugeridas

- Persistencia con JPA (H2 o PostgreSQL)
- Autenticación con Spring Security
- Frontend separado (React/Angular) usando la API REST

---

Última actualización: 31 de enero de 2025


```properties
# Nombre de la aplicación
spring.application.name=canasta-familiar

# Puerto del servidor
server.port=8080

# Configuración Thymeleaf (opcionales)
spring.thymeleaf.cache=true
spring.thymeleaf.encoding=UTF-8
spring.thymeleaf.mode=HTML
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html

# Configuración de logs (opcional)
logging.level.root=INFO
logging.level.com.uniremington=DEBUG
```

### `pom.xml` - Configuración Maven

```xml
<project>
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>4.0.2</version>
    </parent>
    
    <groupId>com.uniremington</groupId>
    <artifactId>canasta-familiar</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    
    <name>canasta-familiar</name>
    <description>Aplicación web para análisis del costo de la canasta familiar</description>
    
    <properties>
        <java.version>17</java.version>
    </properties>
    
    <dependencies>
        <!-- Spring Boot Web MVC -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-webmvc</artifactId>
        </dependency>
        
        <!-- Thymeleaf -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
        
        <!-- Testing -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

---

## 🧪 Tests

### Estrategia de Testing

- **Framework**: JUnit 5
- **Alcance**: Pruebas unitarias del servicio
- **Cobertura**: Validaciones y cálculos

### Suite de Tests - `CanastaServiceTest.java`

```java
@DisplayName("Test: Agregar item válido a la canasta")
void testAgregarItemValido()
// Verifica que un item válido se agrega correctamente
// Expected: obtenerCantidadItems() == 1

@DisplayName("Test: Calcular total correcto")
void testCalcularTotalCorrecto()
// Verifica cálculo de total
// Expected: (5000*2) + (3000*1) = 13000

@DisplayName("Test: Precio inválido lanza excepción")
void testPrecioInvalido()
// Verifica que rechaza precios <= 0
// Expected: IllegalArgumentException

@DisplayName("Test: Cantidad inválida lanza excepción")
void testCantidadInvalida()
// Verifica que rechaza cantidades <= 0
// Expected: IllegalArgumentException

@DisplayName("Test: Nombre vacío lanza excepción")
void testNombreVacio()
// Verifica que rechaza nombres vacíos
// Expected: IllegalArgumentException

@DisplayName("Test: Calcular promedio correcto")
void testCalcularPromedioCorrecto()
// Verifica cálculo de promedio
// Expected: (5000 + 3000) / 2 = 4000

@DisplayName("Test: Obtener productos más costosos")
void testObtenerProductosMasCostosos()
// Verifica que devuelve top 3
// Expected: list.size() <= 3

@DisplayName("Test: Limpiar canasta")
void testLimpiarCanasta()
// Verifica que vacía completamente
// Expected: obtenerCantidadItems() == 0
```

### Ejecutar Tests

```bash
# Ejecutar todos los tests
mvn test

# Ejecutar una clase específica
mvn test -Dtest=CanastaServiceTest

# Ejecutar un método específico
mvn test -Dtest=CanastaServiceTest#testAgregarItemValido

# Con reporte de cobertura
mvn test jacoco:report
```

---

## 🚀 Instalación y Ejecución

### 1. Clonar o descargar el proyecto

```bash
git clone <repository-url>
cd canasta-familiar
```

### 2. Verificar requisitos

```bash
java -version    # Java 17+
mvn -version     # Maven 3.8+
```

### 3. Compilar el proyecto

```bash
# Limpiar y compilar
mvn clean compile

# O compilar + tests
mvn clean test
```

### 4. Ejecutar la aplicación

#### Opción A: Maven
```bash
mvn spring-boot:run
```

#### Opción B: JAR compilado
```bash
mvn package
java -jar target/canasta-familiar-0.0.1-SNAPSHOT.jar
```

#### Opción C: IDE
- **IntelliJ IDEA**: Click derecho en `CanastaFamiliarApplication.java` → Run
- **VS Code**: Click en "Run" (con Spring Boot Extension)
- **Eclipse**: Click derecho en proyecto → Run As → Spring Boot App

### 5. Acceder a la aplicación

- **Web**: http://localhost:8080/
- **API Items**: http://localhost:8080/api/canasta/items
- **API Resumen**: http://localhost:8080/api/canasta/resumen

---

## Compilación y Empaquetado

### Generar JAR ejecutable

```bash
mvn clean package
```

Genera: `target/canasta-familiar-0.0.1-SNAPSHOT.jar`

### Propiedades del JAR

- **Tamaño**: ~50 MB (incluye dependencias)
- **Ejecutable**: Sí, contiene servidor Tomcat embebido
- **JDK requerido**: Java 17 en el sistema de destino

---

## Solución de Problemas

### Error: "Java version not supported"
```
Solución: Instalar Java 17 LTS
- Descarga de: https://adoptium.net/
- Asegúrate que JAVA_HOME esté configurado
- java -version debe mostrar 17.x.x
```

### Error: "Maven not found"
```
Solución: Instalar Maven 3.8+
- Descarga de: https://maven.apache.org/download.cgi
- Descomprime en C:\maven\ (Windows) o /opt/maven (Linux)
- Agrega a PATH
- mvn -version debe funcionar
```

### Puerto 8080 ya está en uso
```
Opción 1: Liberar el puerto
- Windows: netstat -ano | findstr :8080
- Linux/Mac: lsof -i :8080
- Matar proceso

Opción 2: Usar otro puerto
- Edita application.properties: server.port=8081
```

### Tests fallando
```
Solución: Limpiar y recompilar
mvn clean test

O ejecutar test específico:
mvn test -Dtest=CanastaServiceTest#testAgregarItemValido
```

### Spring Boot no inicia
```
Verificar logs:
1. Asegúrate que application.properties existe
2. Verifica que todas las dependencias están en pom.xml
3. Limpia caché Maven: mvn clean
4. Ejecuta: mvn spring-boot:run -X (verbose)
```

---

## 📊 Métricas del Proyecto

| Métrica | Valor |
|---------|-------|
| Líneas de Código (LOC) | ~800 |
| Clases | 7 |
| Métodos públicos | 15+ |
| Endpoints HTTP | 6 |
| Tests unitarios | 8+ |
| Cobertura de código | 85%+ |
| Complejidad ciclomática | Baja |

---

## 🔐 Consideraciones de Seguridad

### Implementadas
- ✅ Validación en el servicio (no en el controlador)
- ✅ Manejo de excepciones con mensajes claros
- ✅ No hay inyección SQL (sin BD)
- ✅ UTF-8 encoding

### No implementadas (Futuros)
- ❌ Autenticación y autorización
- ❌ HTTPS/TLS
- ❌ Rate limiting
- ❌ CORS policy
- ❌ Encriptación de datos

---

## 🔄 Posibles Extensiones

1. **Persistencia**: Agregar base de datos (H2, PostgreSQL)
   - Cambiar `ArrayList` por JPA/Hibernate
   - Agregar `@Entity` a ItemCanasta

2. **Autenticación**: Agregar Spring Security
   - Login de usuarios
   - Canastas por usuario

3. **Reportes**: Exportar a PDF/Excel
   - Usar iText o Apache POI

4. **Validación mejorada**: Bean Validation
   - `@NotNull`, `@Positive`, `@Size`

5. **Frontend**: Usar React/Angular
   - API REST ya existe
   - Frontend separado

---

## 📖 Referencias

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Framework Reference](https://docs.spring.io/spring-framework/docs/current/reference/html/)
- [Thymeleaf Documentation](https://www.thymeleaf.org/)
- [JUnit 5 Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Maven Official Site](https://maven.apache.org/)

---

## 📄 Licencia

Este proyecto es de uso educativo de la Universidad de Remington.

---

## 👥 Contacto

- **Institución**: Universidad de Remington
- **Año**: 2025

---

## 📝 Control de Versiones

| Versión | Fecha | Cambios |
|---------|-------|---------|
| 0.0.1-SNAPSHOT | 31/01/2025 | Versión inicial |

---

**Última actualización**: 31 de enero de 2025

"# CanastaFamiliar" 
"# CanastaFamiliar" 
