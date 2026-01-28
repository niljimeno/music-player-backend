# Music Player Backend
Variables de entorno:
- MASTER_KEY: clave de administrador para registrar usuarios
- SECRET_KEY: clave para encriptar el token


## Rutas
### `/login`
Espera: json
```json
{
    { "username" : nombre_de_usuario },
    { "password" : clave_de_usuario }
}
```
