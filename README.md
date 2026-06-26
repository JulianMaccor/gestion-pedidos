# Gestion de Pedidos API


Es una API con el objetivo de gestionar pedidos y stock de productos de musica para una tienda en av. Olmos.

## Tecnologías

- Java 21
- Spring Boot 4.1.0
- Springdoc 2.8.13
- Maven
- Open API y Swagger

## Requisitos previos

- Open JDK 21
- Maven

## Instalación y ejecución

1. Clonar el repositorio: `git clone https://github.com/JulianMaccor/gestion-pedidos.git`
2. Entrar a la carpeta: `cd gestion-pedidos`
3. Ejecutar GestionPedidosApplication
4. Queda disponible en la siguiente URL: http://localhost:8080/

## Endpoints principales

- GET /productos -> Traer lista de los productos
- POST /productos -> Crear un producto
- GET /productos/{Id} -> Buscar un producto por ID