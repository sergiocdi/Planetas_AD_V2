# Planetas AD v2 - API REST

Este proyecto es una API REST desarrollada con Spring Boot para la gestión de un juego de planetas, usuarios y partidas. Permite la autenticación de usuarios mediante JWT, la gestión de planetas (compra y ranking) y el registro de partidas con sus participantes.

## 🚀 Tecnologías Utilizadas

*   **Java 21**: Lenguaje de programación.
*   **Spring Boot 4.0.2**: Framework principal.
*   **Spring Data JPA**: Para la persistencia de datos.
*   **Spring Security & JWT**: Para la seguridad y autenticación.
*   **MySQL**: Base de datos relacional.
*   **Lombok**: Para reducir el código repetitivo (getters, setters, etc.).
*   **Maven**: Gestor de dependencias y construcción.

## 🏗️ Arquitectura del Proyecto

El proyecto sigue una arquitectura en capas:

1.  **Domain (Entidades)**: Definición de los objetos de negocio (`Usuario`, `Planeta`, `Partida`, `Participante`).
2.  **Repository**: Interfaces que extienden `JpaRepository` para la comunicación con la base de datos.
3.  **Service**: Lógica de negocio de la aplicación.
4.  **Web (Controllers)**: Endpoints REST que exponen las funcionalidades al exterior.
5.  **Config**: Configuraciones de seguridad (JWT) y otros beans del sistema.

## 🔐 Seguridad y Autenticación

La API utiliza **JSON Web Tokens (JWT)** para proteger los endpoints.
*   El login se realiza en `/api/auth/login`.
*   Si las credenciales son válidas, el servidor devuelve un token.
*   Este token debe enviarse en la cabecera `Authorization` como `Bearer <token>` en las peticiones que requieran autenticación.

## 📡 Endpoints Principales

### Autenticación (`/api/auth`)
*   `POST /login`: Autentica a un usuario y devuelve el token JWT.
*   `POST /logout`: Finaliza la sesión (informativo).

### Usuarios (`/api/usuarios`)
*   `POST /`: Crea un nuevo usuario.
*   `GET /`: Lista todos los usuarios.
*   `GET /{id}`: Obtiene detalles de un usuario.
*   `GET /{id}/planetas`: Obtiene los planetas de un usuario (Requiere Token).
*   `POST /{id}/comprar-planeta`: Permite a un usuario comprar un nuevo planeta (Requiere Token).

### Planetas (`/api/planetas`)
*   `GET /`: Lista todos los planetas.
*   `GET /ranking`: Devuelve el ranking de planetas por victorias.
*   `POST /`: Crea un planeta (Requiere Token).
*   `PUT /{id}`: Actualiza un planeta.
*   `DELETE /{id}`: Elimina un planeta.

### Partidas (`/api/partidas`)
*   `POST /guardar`: Guarda una partida y sus participantes.
*   `POST /finalizar`: Finaliza una partida indicando el ganador.
*   `POST /finalizar-con-posiciones`: Finaliza una partida con detalles de posiciones (Requiere Token).

## 🗄️ Configuración de la Base de Datos

El archivo `src/main/resources/application.properties` contiene la configuración de conexión:
```properties
spring.datasource.url=jdbc:mysql://<HOST>:<PORT>/proyectomisiles
spring.datasource.username=<USER>
spring.datasource.password=<PASSWORD>
```

## 📦 Despliegue en Azure (Máquina Virtual)

Para desplegar este proyecto en una máquina virtual de Azure como un archivo WAR:

1.  **Generar el archivo WAR**:
    Ejecuta el siguiente comando en la raíz del proyecto:
    ```bash
    ./mvnw clean package -DskipTests
    ```
    El archivo generado se encontrará en `target/Planetas_AD_v2-0.0.1-SNAPSHOT.war`.

2.  **Preparar la VM en Azure**:
    *   Crea una MV con Linux o Windows.
    *   Instala un servidor de aplicaciones como **Apache Tomcat 10+**.
    *   Asegúrate de tener instalada la **JRE 21**.

3.  **Desplegar**:
    *   Copia el archivo `.war` a la carpeta `webapps/` de tu instalación de Tomcat.
    *   Tomcat desplegará automáticamente la aplicación.

4.  **Base de Datos**:
    *   Asegúrate de que la base de datos MySQL esté accesible desde la MV y que los datos en `application.properties` sean correctos para el entorno de producción.

## 🛠️ Ejecución en Local

1.  Clonar el repositorio.
2.  Configurar MySQL con una base de datos llamada `proyectomisiles`.
3.  Ejecutar el proyecto con Maven:
    ```bash
    ./mvnw spring-boot:run
    ```
