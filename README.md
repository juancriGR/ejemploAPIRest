#Ejemplo API REST

Creación de una api rest con spring a modo de tutorial. Se trata de una pequeña aplicación backend que expone una API REST que permite a un usuario gestionar una lista de tareas personalizada.
La aplicación debe permitir tanto la creación de tareas nuevas, como el borrado y la edición de tareas existentes. Asímismo, una tarea ya realizada debe poder marcarse como finalizada.

## Requisitos de la aplicación

La API se ha realizado con Java11 y SpringBoot. Para compilarla, hacemos uso de maven. Para poder ejecutarla, hace falta que exista una base de datos Mysql vacía en la máquina. 
La configuración de la base de datos (user y pass) se realiza en el fichero *`src/main/resources/application.yml`*:

```bash
spring:
	datasource:
		url: jdbc:mysql://localhost:3306/mydb?useUnicode=true&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC
		username: root
		password: root
```

Las aplicación está configurada para crear directamente las tablas necesarias para su funcionamiento:

```bash
jpa:
		hibernate:
			ddl-auto: update
```

La aplicación hace uso de Spring Security y de autentificación por JWT.
Para poder usar la api el usuario debe haber sido insertado previamente en base de datos con las contraseña cifrada (Bcript function de Spring).

## Uso de la aplicación

La aplicación permite el crear o modificar tareas (*apiejemplo/savetask),*  recuperar una lista de todas las tareas no finalizadas asignadas a un usuario *(apiejemplo/showtask),* marcar una tarea como finalizada *(apiejemplo/finisTask)* o borrar una tarea determinada. El contrato de interfaz de la api se puede encontrar en /ejemploapirest/src/main/resources/swagger-info.json.

### Logeandonos en la aplicación

Para hacer login, mandamos un JSON en una petición POST, con el usuario y pass a la dirección /login:

![images/Untitled.png](images/Untitled.png)

Si la autentificación ha sido existosa, tendremos como respuesta:

![images/Untitled%201.png](images/Untitled%201.png)

El token que recibimos como respuesta, tendremos que mandarlo en la cabecera (en formato Bearer) de todas las demás peticiones.

### Creando una nueva tarea

Para crear una nueva tarea, hacemos una petición POST, con el título de la tarea y la descripción en el body de la petición y el token en la cabecera. Por defecto, cualquier tarea nueva creada se marca como no finalizada:

![images/Untitled%202.png](images/Untitled%202.png)

![images/Untitled%203.png](images/Untitled%203.png)

Si la nota se ha creado correctamente, obtendremos la siguiente respuesta:

![images/Untitled%204.png](images/Untitled%204.png)

### Actualizando la descripción una tarea

Para modificar una nueva tarea, hacemos una petición POST, con el título de la tarea y la descripción modificada en el body de la petición y el token en la cabecera. 

![images/Untitled%202.png](images/Untitled%202.png)

![images/Untitled%205.png](images/Untitled%205.png)

Como respusta obtendremos:

![images/Untitled%206.png](images/Untitled%206.png)

### Obteniendo lista de tareas sin finalizar de un usuario

Para obtener una lista de las tareas no finalizadas de un usuario, hacemos una petición GET, con el token en la cabecera. El cuerpo puede ir vacío, no hay parámetros requeridos: 

![images/Untitled%207.png](images/Untitled%207.png)

Como respuesta obtendremos:

![images/Untitled%208.png](images/Untitled%208.png)

### Finalizando una tarea

Para finalizar una nueva tarea, hacemos una petición PUT, con el título de la tarea y marcándola como finalizada en el body de la petición y el token en la cabecera

![images/Untitled%209.png](images/Untitled%209.png)

![images/Untitled%2010.png](images/Untitled%2010.png)

Como respuesta tendremos:

![images/Untitled%2011.png](images/Untitled%2011.png)

### Borrando una tarea

Para borrar una tarea, hacemos una petición DELETE, con el token en la cabecera y el título de la tarea en el body de la petición:

![images/Untitled%2012.png](images/Untitled%2012.png)

![images/Untitled%2013.png](images/Untitled%2013.png)

Como respuesta tendremos:

![images/Untitled%2014.png](images/Untitled%2014.png)