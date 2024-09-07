# TransferServiceFiduBogota

Este es un proyecto de ejemplo utilizando Spring Boot con Spring Web y Spring Data JPA. El propósito de este proyecto es proporcionar una base sólida para aplicaciones que requieran una configuración sencilla de un entorno local.

## Requisitos

Antes de comenzar, asegúrate de tener instalados los siguientes requisitos:


- [Java JDK 17](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html) o superior
- [Gradle](https://gradle.org/install/) (para gestionar dependencias y construir el proyecto)
- [MySQL](https://dev.mysql.com/downloads/mysql/) (o el sistema de base de datos de tu preferencia)

## Instalación y Configuración

### Clonar el Repositorio

Clona el repositorio a tu máquina local:


git clone https://github.com/oscargps/TransferServiceFiduBogota.git
cd TransferServiceFiduBogota

## Configurar la Base de Datos
Crea una base de datos en MySQL (o usa tu base de datos preferida). Puedes usar el siguiente comando SQL para crear la base de datos:

CREATE DATABASE nombre_de_la_base_de_datos;

### Configurar las Credenciales en application.properties:

Abre el archivo src/main/resources/application.properties y configura las credenciales de tu base de datos. Ejemplo de configuración para MySQL:

spring.datasource.url=jdbc:mysql://localhost:3306/nombre_de_la_base_de_datos
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true


Ajusta nombre_de_la_base_de_datos, tu_usuario y tu_contraseña según tu configuración.

## Construir y Ejecutar la Aplicación
### Construir el Proyecto:

Usa Gradle para construir el proyecto. Ejecuta el siguiente comando en la raíz del proyecto:

./gradlew build

### Ejecutar la Aplicación:

Después de construir el proyecto, puedes ejecutar la aplicación con el siguiente comando:

./gradlew bootRun


La aplicación debería estar disponible en http://localhost:8080 por defecto.