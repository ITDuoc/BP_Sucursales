API Sucursales Caso BookPoint
 SpringBoot + JPA + MYSQL


Query para crear Base de Datos requerida:
CREATE DATABASE sucursales_db;


Endpoints de los metodos existentes

Crear:
POST /api/v1/sucursales
http://localhost:8093/api/v1/sucursales

{
    "nombre":"Sucursal Santiago Centro",
    "telefono":"987654321",
    "direccion":"Alameda 123",
    "horario":"09:00 - 18:00"
}

Listar:
GET /api/v1/sucursales
http://localhost:8093/api/v1/sucursales

Buscar por ID:
GET /api/v1/sucursales/{id}
http://localhost:8093/api/v1/sucursales/1

Modificar:
PUT /api/v1/sucursales/{id}
http://localhost:8093/api/v1/sucursales/1

Eliminar:
DELETE /api/v1/sucursales/{id}
http://localhost:8093/api/v1/sucursales/1