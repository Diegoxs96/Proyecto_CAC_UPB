# Documentación técnica — CAC-UPB
## Centro de Atención al Cliente UPB

---

## Índice

1. [Arquitectura general](#1-arquitectura-general)
2. [Interfaces del sistema](#2-interfaces-del-sistema)
3. [Modelos](#3-modelos)
4. [Estructuras de datos propias](#4-estructuras-de-datos-propias)
5. [Gestores (lógica de negocio)](#5-gestores-lógica-de-negocio)
6. [Controladores de infraestructura](#6-controladores-de-infraestructura)
7. [Monitores](#7-monitores)
8. [Historial y Observer](#8-historial-y-observer)
9. [Comunicación RMI](#9-comunicación-rmi)
10. [Flujos principales paso a paso](#10-flujos-principales-paso-a-paso)
11. [Reglas de negocio implementadas](#11-reglas-de-negocio-implementadas)
12. [Resumen de estructuras de datos por clase](#12-resumen-de-estructuras-de-datos-por-clase)

---

## 1. Arquitectura general

El sistema sigue una arquitectura **Cliente-Servidor con RMI** (Remote Method Invocation).

```
┌─────────────────────────────────────────────────────────────────┐
│  SERVIDOR                                                       │
│                                                                 │
│  ServerController → Server (gestores) → TicketService (RMI)    │
│                          │                                      │
│              ┌───────────┼───────────┐                         │
│         GestorClientes  GestorCitas  GestorBancos               │
│         GestorTickets   GestorReportes  AdminController         │
│         LoginController  MaquinaEntradaController               │
│         PerfilController  History                               │
└─────────────────────────────────────────────────────────────────┘
                           │  Java RMI (puerto 1099)
┌─────────────────────────────────────────────────────────────────┐
│  CLIENTE                                                        │
│                                                                 │
│  ClientController ──RMI──► TicketInterface                     │
│  ClientView (JavaFX)                                            │
└─────────────────────────────────────────────────────────────────┘
```

El punto de entrada es `App.java`, que detecta si debe lanzar el servidor o el cliente mediante el argumento `client`:

- Sin argumentos  → lanza `ServerController` (modo servidor)
- Con `client`    → lanza `ClientController` + `ClientView` (modo cliente)

La configuración de red (IP, puerto, nombre del servicio) se lee desde variables de entorno o propiedades del sistema en `Environment` (patrón Singleton).

---

## 2. Interfaces del sistema

### `IUsuario`
**Paquete:** `Server.Model.interfaces`

Contrato base para todos los tipos de usuario del sistema.

| Método | Parámetros | Retorno | Descripción |
|---|---|---|---|
| `autenticar` | `String contrasena` | `boolean` | Valida la contraseña del usuario |
| `getNombreCompleto` | — | `String` | Retorna nombres + apellidos |

---

### `IColaBancaria`
**Paquete:** `Server.Model.interfaces`

Contrato que deben implementar todas las colas usadas por los bancos de servicio. Permite que `BancoServicio` no dependa de si la cola es FIFO o con prioridad.

| Método | Parámetros | Retorno | Descripción |
|---|---|---|---|
| `encolar` | `Ticket t` | `void` | Agrega un ticket al final o según prioridad |
| `desencolar` | — | `Ticket` | Extrae y retorna el siguiente ticket |
| `verFrente` | — | `Ticket` | Consulta el siguiente sin extraerlo |
| `estaVacia` | — | `boolean` | Indica si no hay tickets en espera |
| `getTamano` | — | `int` | Número de tickets en la cola |
| `obtenerPosicion` | `int ticketId` | `int` | Posición de un ticket en la cola (1-based), -1 si no existe |

Implementaciones: `ColaBancaria` (FIFO) y `ColaPrioridad` (por prioridad del cliente).

---

### `INotificable`
**Paquete:** `Server.Model.interfaces`

Permite a los monitores (sala y celular) recibir eventos del servidor de forma uniforme.

| Método | Parámetros | Retorno | Descripción |
|---|---|---|---|
| `notificarTurno` | `Ticket t` | `void` | Notifica que es el turno del ticket |
| `notificarCambioEstado` | `int estado` | `void` | Notifica cambio de estado (0=esperando, 1=atendiendo, 2=atendido, 3=cancelado) |

Implementaciones: `MonitorSala`, `MonitorCelular`, `MonitorCelularView`.

---

### `IComparable<T>`
**Paquete:** `Server.Model.interfaces`

Contrato genérico de comparación para las estructuras de datos.

| Método | Parámetros | Retorno | Descripción |
|---|---|---|---|
| `comparar` | `T t` | `int` | Retorna negativo, 0 o positivo según el orden |

---

### `TicketInterface`
**Paquete:** `Server.Model`  
Extiende: `java.rmi.Remote`

Contrato RMI expuesto al cliente remoto.

| Método | Parámetros | Retorno | Descripción |
|---|---|---|---|
| `emitirTicket` | `int clienteId, int citaId` | `Ticket` | Emite un ticket para la cita indicada. Retorna null si la cita no existe o no es vigente |
| `consultarPosicion` | `int ticketId` | `int` | Posición del ticket en la cola de su banco |
| `verFrente` | `int bancoId` | `Ticket` | Ticket al frente de la cola del banco indicado |

---

## 3. Modelos

### `Usuario` (abstracta)
**Paquete:** `Server.Model`  
Implementa: `IUsuario`, `Serializable`

Clase base para todos los actores del sistema. Define los campos y comportamientos comunes.

**Atributos:**

| Campo | Tipo | Visibilidad | Descripción |
|---|---|---|---|
| `id` | `int` | `protected` | Identificador interno auto-generado |
| `numeroIdentificacion` | `String` | `protected` | Documento de identidad (cédula, etc.) |
| `nombres` | `String` | `protected` | Primer y segundo nombre |
| `apellidos` | `String` | `protected` | Apellidos |
| `contrasena` | `String` | `protected` | Contraseña de acceso |

**Métodos:**

| Método | Parámetros | Retorno | Descripción |
|---|---|---|---|
| `autenticar` | `String contrasena` | `boolean` | Compara la contraseña recibida con la almacenada |
| `getNombreCompleto` | — | `String` | Concatena nombres + " " + apellidos |
| `getId` | — | `int` | Retorna el id interno |
| `getNumeroIdentificacion` | — | `String` | Retorna el documento de identidad |
| `getNombres` | — | `String` | Retorna los nombres |
| `getApellidos` | — | `String` | Retorna los apellidos |

---

### `Cliente`
**Paquete:** `Server.Model`  
Extiende: `Usuario`  
Implementa: `Comparable<Cliente>`, `Serializable`

Representa al usuario final que solicita citas y obtiene tickets.

**Constantes:**

| Constante | Valor | Significado |
|---|---|---|
| `TIPO_ESTANDAR` | `0` | Cliente sin beneficio de prioridad por tipo |
| `TIPO_PREMIUM` | `1` | Cliente con prioridad aumentada |

**Atributos propios:**

| Campo | Tipo | Descripción |
|---|---|---|
| `edad` | `int` | Edad del cliente |
| `direccion` | `String` | Dirección de residencia |
| `tipoCliente` | `int` | 0=estándar, 1=premium |

**Métodos:**

| Método | Parámetros | Retorno | Descripción |
|---|---|---|---|
| `getEdad` | — | `int` | Retorna la edad |
| `getDireccion` | — | `String` | Retorna la dirección |
| `getTipoCliente` | — | `int` | Retorna el tipo (0 o 1) |
| `esMayorDe60` | — | `boolean` | true si edad > 60 |
| `esPremium` | — | `boolean` | true si tipoCliente == TIPO_PREMIUM |
| `getPrioridad` | — | `int` | Calcula prioridad: premium=2, estándar=0, +1 si mayor de 60. Rango 0-3 |
| `tieneCitaVigente` | `GestorCitas gestorCitas` | `boolean` | Consulta si tiene al menos una cita en estado PENDIENTE |
| `compareTo` | `Cliente o` | `int` | Compara por id (para BinaryTree) |

**Lógica de prioridad:**
```
Cliente estándar  < 60 años  → prioridad 0
Cliente estándar  > 60 años  → prioridad 1
Cliente premium   < 60 años  → prioridad 2
Cliente premium   > 60 años  → prioridad 3
```

---

### `Administrador`
**Paquete:** `Server.Model`  
Extiende: `Usuario`

Representa al usuario con acceso total al sistema.

**Atributos propios:**

| Campo | Tipo | Descripción |
|---|---|---|
| `nivelAcceso` | `int` | Nivel de permisos del administrador |

**Métodos:**

| Método | Retorno | Descripción |
|---|---|---|
| `getNivelAcceso` | `int` | Retorna el nivel de acceso |

---

### `OperadorAtencion`
**Paquete:** `Server.Model`  
Extiende: `Usuario`

Representa al empleado que atiende clientes en un banco de servicio.

**Atributos propios:**

| Campo | Tipo | Descripción |
|---|---|---|
| `bancoAsignado` | `BancoServicio` | Banco donde trabaja el operador |
| `disponible` | `boolean` | true si puede atender en este momento |

**Métodos:**

| Método | Parámetros | Retorno | Descripción |
|---|---|---|---|
| `getBancoAsignado` | — | `BancoServicio` | Retorna el banco asignado |
| `setBancoAsignado` | `BancoServicio b` | `void` | Asigna el banco al operador |
| `isDisponible` | — | `boolean` | Indica si el operador está libre |
| `llamarSiguiente` | — | `Ticket` | Extrae el siguiente ticket del banco (vía `BancoServicio.llamarSiguiente()`), lo marca como ATENDIENDO y pone disponible=false. Retorna null si no hay tickets o el operador está ocupado |
| `finalizarAtencion` | `Ticket t` | `void` | Marca el ticket como ATENDIDO (completar()) y pone disponible=true |

---

### `Cita`
**Paquete:** `Server.Model`  
Implementa: `Serializable`, `Comparable<Cita>`

Solicitud de atención registrada por un cliente.

**Constantes de tipo:**

| Constante | Valor | Significado |
|---|---|---|
| `TIPO_RECLAMO` | `0` | Reclamación sobre un producto o servicio |
| `TIPO_DEVOLUCION` | `1` | Devolución de un producto |
| `TIPO_ASESORIA` | `2` | Asesoría general |

**Constantes de estado:**

| Constante | Valor | Significado |
|---|---|---|
| `ESTADO_PENDIENTE` | `0` | Cita registrada, aún no atendida |
| `ESTADO_COMPLETADA` | `1` | Cita atendida exitosamente |
| `ESTADO_NO_ASISTIDA` | `2` | El cliente no se presentó |
| `ESTADO_CANCELADA` | `3` | Cita cancelada (no va al historial) |

**Atributos:**

| Campo | Tipo | Descripción |
|---|---|---|
| `id` | `int` | Identificador único auto-generado |
| `cliente` | `Cliente` | Cliente dueño de la cita |
| `fechaHora` | `String` | Fecha y hora programada |
| `lugar` | `String` | Ubicación o sala de atención |
| `tipoCita` | `int` | Tipo (0, 1 o 2) |
| `descripcionMotivo` | `String` | Descripción del motivo de la cita |
| `estado` | `int` | Estado actual (0-3) |
| `ticket` | `Ticket` | Ticket emitido para esta cita (null si no se ha emitido) |

**Métodos:**

| Método | Parámetros | Retorno | Descripción |
|---|---|---|---|
| `estaVigente` | — | `boolean` | true si estado == ESTADO_PENDIENTE |
| `puedeModificarse` | — | `boolean` | true si ticket==null Y estado==PENDIENTE (no se puede modificar si ya tiene ticket) |
| `cancelar` | — | `void` | Cambia estado a ESTADO_CANCELADA |
| `confirmar` | — | `void` | Cambia estado a ESTADO_COMPLETADA |
| `marcarNoAsistida` | — | `void` | Cambia estado a ESTADO_NO_ASISTIDA |
| `setFechaHora` | `String v` | `void` | Actualiza fecha y hora |
| `setLugar` | `String v` | `void` | Actualiza el lugar |
| `setTipoCita` | `int v` | `void` | Actualiza el tipo |
| `setDescripcionMotivo` | `String v` | `void` | Actualiza la descripción |
| `setTicket` | `Ticket t` | `void` | Asocia el ticket emitido |
| `compareTo` | `Cita o` | `int` | Compara por id |

---

### `Ticket`
**Paquete:** `Server.Model`  
Implementa: `Serializable`, `Comparable<Ticket>`

Turno físico asignado a una cita en un banco de servicio.

**Constantes de estado:**

| Constante | Valor | Significado |
|---|---|---|
| `ESTADO_ESPERANDO` | `0` | En cola, esperando turno |
| `ESTADO_ATENDIENDO` | `1` | Siendo atendido en este momento |
| `ESTADO_ATENDIDO` | `2` | Atención finalizada |
| `ESTADO_CANCELADO` | `3` | Ticket cancelado |

**Atributos:**

| Campo | Tipo | Descripción |
|---|---|---|
| `id` | `int` | Identificador único |
| `numeroTurno` | `int` | Número visible al cliente (T001, T002...) |
| `cita` | `Cita` | Cita asociada |
| `prioridad` | `int` | Prioridad heredada del cliente (0-3) |
| `estado` | `int` | Estado actual (0-3) |
| `horaEmision` | `String` | Hora en que se emitió |
| `horaVencimiento` | `String` | Hora límite (reservado para implementación futura) |
| `bancoAsignado` | `BancoServicio` | Banco donde debe presentarse |

**Métodos:**

| Método | Parámetros | Retorno | Descripción |
|---|---|---|---|
| `llamar` | — | `void` | Cambia estado a ESTADO_ATENDIENDO |
| `completar` | — | `void` | Cambia estado a ESTADO_ATENDIDO |
| `setEstado` | `int estado` | `void` | Setter directo de estado |
| `setHoraVencimiento` | `String hora` | `void` | Establece la hora de vencimiento |
| `compareTo` | `Ticket o` | `int` | Compara por prioridad DESCENDENTE (mayor prioridad = primero) |

---

### `BancoServicio`
**Paquete:** `Server.Model`

Unidad de atención con su propia cola de tickets independiente.

**Atributos:**

| Campo | Tipo | Descripción |
|---|---|---|
| `id` | `int` | Identificador único del banco |
| `nombre` | `String` | Nombre descriptivo ("Banco Reclamos", etc.) |
| `ubicacion` | `String` | Sala y ventanilla física |
| `capacidadMaxima` | `int` | Máximo de tickets en cola simultáneos |
| `cola` | `IColaBancaria` | Cola de tickets (FIFO o con prioridad) |
| `operador` | `OperadorAtencion` | Operador asignado |
| `activo` | `boolean` | Si el banco está en operación |
| `monitores` | `LinkedList<INotificable>` | Monitores suscritos (sala y celular) |

**Métodos:**

| Método | Parámetros | Retorno | Descripción |
|---|---|---|---|
| `addMonitor` | `INotificable monitor` | `void` | Suscribe un monitor para recibir notificaciones |
| `agregarTicket` | `Ticket t` | `void` | Encola el ticket (delega a cola.encolar) |
| `llamarSiguiente` | — | `Ticket` | Extrae el siguiente ticket, lo marca como ATENDIENDO y notifica a todos los monitores suscritos |
| `verFrente` | — | `Ticket` | Peek de la cola sin extraer |
| `getPersonasEnEspera` | — | `int` | Tamaño actual de la cola |
| `estaDisponible` | — | `boolean` | true si activo Y cola < capacidadMaxima |
| `obtenerPosicion` | `int ticketId` | `int` | Posición del ticket en la cola |
| `mostrarCola` | — | `void` | Imprime estado del banco en consola |
| `setOperador` | `OperadorAtencion o` | `void` | Asigna operador |
| `setActivo` | `boolean v` | `void` | Activa o desactiva el banco |

**Nota sobre notificaciones:** Cuando `llamarSiguiente()` extrae un ticket, itera automáticamente `monitores` y llama `notificarTurno(ticket)` en cada uno. El operador no necesita notificar manualmente.

---

### `ColaBancaria`
**Paquete:** `Server.Model`  
Implementa: `IColaBancaria`  
Usa: `QueueList<Ticket>` (estructura propia)

Adaptador FIFO. Traduce los métodos en español del diagrama (`encolar`, `desencolar`, `verFrente`) a los métodos en inglés de `QueueList` (`insert`, `extract`, `peek`).

| Método | Delegado a |
|---|---|
| `encolar(Ticket t)` | `queue.insert(t)` |
| `desencolar()` | `queue.extract()` |
| `verFrente()` | `queue.peek()` |
| `estaVacia()` | `queue.isEmpty()` |
| `getTamano()` | `queue.size()` |
| `obtenerPosicion(int id)` | Itera con `queue.iterator()` contando hasta encontrar el id |

---

### `ColaPrioridad`
**Paquete:** `Server.Model`  
Implementa: `IColaBancaria`  
Usa: `PriorityQueueClass<Ticket>` respaldado por `LinkedList<Ticket>` (estructuras propias)

Cola con prioridad. Los tickets con mayor valor en `prioridad` se atienden primero. Tiene un método adicional:

| Método | Parámetros | Descripción |
|---|---|---|
| `encolar(Ticket t, int prioridad)` | `Ticket, int` | Encola con prioridad explícita (sobrecarga) |

---

## 4. Estructuras de datos propias

Todas viven en el paquete `structures/` y fueron desarrolladas previamente por el equipo.

### `LinkedList<E>`
**Paquete:** `structures.linkedlist.singly`

Lista enlazada simple. Es la estructura más usada en el proyecto.

| Método | Retorno | Descripción |
|---|---|---|
| `add(E element)` | `boolean` | Agrega al final |
| `addFirst(E element)` | `boolean` | Agrega al inicio |
| `remove(E element)` | `boolean` | Elimina la primera ocurrencia |
| `contains(E element)` | `boolean` | Busca linealmente |
| `peek()` | `E` | Ver primero sin remover |
| `peekLast()` | `E` | Ver último sin remover |
| `poll()` | `E` | Extrae el primero |
| `pollLast()` | `E` | Extrae el último |
| `toArray()` | `E[]` | Convierte a arreglo |
| `size()` | `int` | Número de elementos |
| `isEmpty()` | `boolean` | Si está vacía |
| `clear()` | `boolean` | Vacía la lista |
| `iterator()` | `Iterator<E>` | Iterador para recorrer |
| `sort(ToIntFunction<E>)` | `boolean` | Ordena con comparador (bubble sort) |

**Complejidad:** O(1) insertar/eliminar extremos, O(n) búsqueda y acceso por posición.

---

### `QueueList<E>`
**Paquete:** `structures.queue`

Cola FIFO implementada con nodos enlazados. Sin límite de tamaño.

| Método | Retorno | Descripción |
|---|---|---|
| `insert(E element)` | `E` | Encola al final |
| `extract()` | `E` | Desencola del frente |
| `peek()` | `E` | Ver frente sin remover |
| `isEmpty()` | `boolean` | Si está vacía |
| `size()` | `int` | Tamaño actual |
| `contains(E element)` | `boolean` | Búsqueda lineal |
| `iterator()` | `Iterator<E>` | Iterador para recorrer |

---

### `StackList<E>`
**Paquete:** `structures.stack`

Pila LIFO respaldada por `LinkedList`. Inserta y extrae del inicio de la lista.

| Método | Retorno | Descripción |
|---|---|---|
| `push(E element)` | `boolean` | Apila en la cima |
| `pop()` | `E` | Desapila de la cima |
| `peek()` | `E` | Ver cima sin remover |
| `isEmpty()` | `boolean` | Si está vacía |
| `size()` | `int` | Número de elementos |
| `clear()` | `boolean` | Vacía la pila |

---

### `PriorityQueueClass<E>`
**Paquete:** `structures.model.PriorityQueue`

Cola con prioridad respaldada por `AbstractList<E>` (en la práctica, una `LinkedList`).

| Método | Retorno | Descripción |
|---|---|---|
| `insert(E element)` | `boolean` | Inserta sin prioridad explícita |
| `insert(int priority, E element)` | `boolean` | Inserta con prioridad numérica |
| `peek()` | `E` | Ver el de mayor prioridad sin remover |
| `poll()` | `E` | Extrae el de mayor prioridad |
| `isEmpty()` | `boolean` | Si está vacía |
| `size()` | `int` | Tamaño actual |
| `iterator()` | `Iterator<E>` | Iterador para recorrer |

---

### `BinaryTree<E>`
**Paquete:** `structures.tree`

Árbol binario de búsqueda. Requiere que `E` implemente `Comparable<E>`.

| Método | Retorno | Descripción |
|---|---|---|
| `insert(E element)` | `boolean` | Inserta comparando menor/mayor. Ignora duplicados |
| `remove(E element)` | `boolean` | Elimina el nodo, reemplaza por sucesor si tiene dos hijos |
| `search(E element)` | `boolean` | Búsqueda O(log n) |
| `inOrder()` | `List<E>` | Recorrido izquierda-raíz-derecha (orden ascendente) |
| `preOrder()` | `List<E>` | Recorrido raíz-izquierda-derecha |
| `postOrder()` | `List<E>` | Recorrido izquierda-derecha-raíz |
| `size()` | `int` | Número de elementos |
| `isEmpty()` | `boolean` | Si no tiene elementos |
| `searchWithMetrics(E element)` | `SearchResult` | Búsqueda con métricas (pasos, tiempo en ns) |

**Complejidad:** O(log n) promedio para insert/search/remove. O(n) en el peor caso (árbol degenerado).

---

## 5. Gestores (lógica de negocio)

### `GestorClientes`
**Paquete:** `Server.Controller`

Administra el registro y búsqueda de clientes. Usa dos estructuras simultáneamente para optimizar operaciones distintas.

**Estructuras internas:**
- `LinkedList<Cliente> clientes` — listado completo para iterar
- `BinaryTree<Cliente> arbol` — búsqueda O(log n) por id

**Atributos:**

| Campo | Tipo | Descripción |
|---|---|---|
| `clientes` | `LinkedList<Cliente>` | Lista completa de clientes |
| `arbol` | `BinaryTree<Cliente>` | Árbol para búsqueda rápida |
| `contadorId` | `int` | Auto-incremento para asignar ids |

**Métodos:**

| Método | Parámetros | Retorno | Descripción |
|---|---|---|---|
| `registrarCliente` | `String numeroId, String nombres, String apellidos, String contrasena, int edad, String direccion, int tipo` | `Cliente` | Verifica que no exista ya ese `numeroId`. Si hay duplicado retorna null y loguea aviso. Si es nuevo, crea el Cliente, lo agrega a la lista Y al árbol, e incrementa el contador |
| `buscarPorId` | `int id` | `boolean` | Búsqueda O(log n) en el árbol. Crea un Cliente "sonda" temporal con el id buscado |
| `buscarPorIdentificacion` | `String num` | `Cliente` | Búsqueda lineal O(n) en la lista por número de documento. Retorna el objeto Cliente completo o null |
| `listarTodos` | — | `LinkedList<Cliente>` | Retorna la referencia a la lista completa |
| `eliminarCliente` | `int id` | `boolean` | Itera la lista hasta encontrar el cliente, lo elimina de la lista Y del árbol. Retorna false si no existe |
| `total` | — | `int` | Tamaño actual de la lista |

---

### `GestorCitas`
**Paquete:** `Server.Controller`

Maneja el ciclo de vida completo de las citas.

**Estructuras internas:**
- `LinkedList<Cita> citas` — citas en estado PENDIENTE
- `LinkedList<Cita> historico` — citas COMPLETADAS o NO_ASISTIDAS

**Regla clave:** Las citas CANCELADAS nunca van al historial (se eliminan de `citas` y se descartan).

**Métodos:**

| Método | Parámetros | Retorno | Descripción |
|---|---|---|---|
| `solicitarCita` | `Cliente cliente, String fechaHora, String lugar, int tipoCita, String descripcion` | `Cita` | Crea la cita con estado PENDIENTE, la agrega a `citas`, registra evento en `History` |
| `modificarCita` | `int id, String fechaHora, String lugar, int tipoCita, String descripcion` | `boolean` | Busca la cita. Si no existe o `puedeModificarse()` es false (tiene ticket o no está pendiente) retorna false. Si puede, actualiza los campos y registra en History |
| `cancelarCita` | `int id` | `boolean` | Busca la cita. Si no existe o no está vigente retorna false. Si está vigente, llama `cita.cancelar()`, la elimina de `citas` (NO va al historial) y registra en History |
| `buscarCita` | `int id` | `Cita` | Itera `citas` linealmente hasta encontrar el id. Retorna null si no existe |
| `listarPorCliente` | `int clienteId` | `LinkedList<Cita>` | Filtra `citas` retornando solo las del cliente indicado |
| `completar` | `Cita c` | `void` | Llama `c.confirmar()`, la mueve de `citas` a `historico`, registra en History |
| `noAsistida` | `Cita c` | `void` | Llama `c.marcarNoAsistida()`, la mueve a `historico`, registra en History |
| `listarNoAsistidas` | — | `LinkedList<Cita>` | Filtra `historico` retornando solo las con ESTADO_NO_ASISTIDA |
| `getHistorico` | — | `LinkedList<Cita>` | Retorna el historial completo |
| `getCitas` | — | `LinkedList<Cita>` | Retorna las citas activas |

---

### `GestorTickets`
**Paquete:** `Server.Controller`

Emite tickets y los asigna al banco con menor carga disponible.

**Estructuras internas:**
- `LinkedList<Ticket> ticketsActivos` — todos los tickets en el sistema

**Atributos:**

| Campo | Tipo | Descripción |
|---|---|---|
| `ticketsActivos` | `LinkedList<Ticket>` | Todos los tickets emitidos |
| `gestorBancos` | `GestorBancos` | Referencia para buscar banco disponible |
| `contadorTurno` | `int` | Número de turno visible (T001...) |
| `contadorId` | `int` | Id interno del ticket |

**Métodos:**

| Método | Parámetros | Retorno | Descripción |
|---|---|---|---|
| `emitirTicket` | `Cita cita` | `Ticket` | Busca banco disponible vía `GestorBancos.obtenerDisponible(tipoCita)`. Si no hay banco retorna null. Crea el Ticket con la prioridad del cliente (`cita.getCliente().getPrioridad()`), lo encola en el banco, lo agrega a `ticketsActivos`, y asocia el ticket a la cita (`cita.setTicket(t)`) |
| `calcularPrioridad` | `Cliente c` | `int` | Delega a `c.getPrioridad()` |
| `asignarBanco` | `Ticket t` | `BancoServicio` | Busca banco disponible para el tipo de cita del ticket |
| `buscarTicket` | `int id` | `Ticket` | Búsqueda lineal en `ticketsActivos` |
| `getTicketsActivos` | — | `LinkedList<Ticket>` | Retorna todos los tickets activos |

---

### `GestorBancos`
**Paquete:** `Server.Controller`

Gestiona el conjunto de bancos de servicio.

**Estructuras internas:**
- `LinkedList<BancoServicio> bancos`

**Métodos:**

| Método | Parámetros | Retorno | Descripción |
|---|---|---|---|
| `agregarBanco` | `BancoServicio b` | `void` | Agrega banco al final de la lista |
| `buscarPorId` | `int id` | `BancoServicio` | Búsqueda lineal por id. Usado por `TicketService.verFrente()` |
| `obtenerDisponible` | `int tipoCita` | `BancoServicio` | Itera todos los bancos buscando el activo, disponible y con menor carga (`getPersonasEnEspera()`). Retorna el que tenga la cola más corta. El parámetro `tipoCita` está disponible para filtrado futuro |
| `listarActivos` | — | `LinkedList<BancoServicio>` | Filtra y retorna solo los bancos con `activo==true` |
| `mostrarEstadoGeneral` | — | `void` | Llama `mostrarCola()` en cada banco (imprime en consola) |

---

### `GestorReportes`
**Paquete:** `Server.Controller`

Genera reportes del historial de citas.

**Estructuras internas:**
- `StackList<Cita> historico` — pila LIFO para ver la cita más reciente primero

**Métodos:**

| Método | Parámetros | Retorno | Descripción |
|---|---|---|---|
| `cargarHistorico` | — | `void` | Limpia la pila y la recarga iterando `GestorCitas.getHistorico()`. La última cita procesada queda en la cima |
| `reporteNoAsistidas` | — | `LinkedList<Cita>` | Filtra el historial de GestorCitas retornando citas con ESTADO_NO_ASISTIDA |
| `reporteCompletadas` | — | `LinkedList<Cita>` | Filtra el historial retornando citas con ESTADO_COMPLETADA |
| `verHistorico` | — | `StackList<Cita>` | Retorna la pila (la cima es la cita más reciente) |

---

### `AdminController`
**Paquete:** `Server.Controller`

Operaciones exclusivas del Administrador. Mantiene su propio `BinaryTree` para búsquedas administrativas independientes del `GestorClientes`.

**Estructuras internas:**
- `BinaryTree<Cliente> arbolClientes`

**Métodos:**

| Método | Parámetros | Retorno | Descripción |
|---|---|---|---|
| `addCliente` | `Cliente c` | `boolean` | Inserta en el árbol administrativo |
| `findCliente` | `Cliente c` | `boolean` | Búsqueda O(log n) |
| `removeCliente` | `Cliente c` | `boolean` | Elimina del árbol |
| `configurarBanco` | `BancoServicio b` | `void` | Delega `agregarBanco` a `GestorBancos` |
| `generarReporte` | — | `void` | Llama `GestorBancos.mostrarEstadoGeneral()` |

---

### `LoginController`
**Paquete:** `Server.Controller`

Autentica los tres tipos de usuario.

**Métodos:**

| Método | Parámetros | Retorno | Descripción |
|---|---|---|---|
| `login` | `String numeroIdentificacion, String contrasena` | `Usuario` | Busca el cliente por número de identificación en `GestorClientes`. Si lo encuentra, llama `autenticar(contrasena)`. Retorna el objeto Usuario si es correcto, null si no existe o la contraseña es incorrecta |
| `autenticarAdmin` | `Administrador admin, String contrasena` | `boolean` | Delega a `admin.autenticar(contrasena)` |
| `autenticarOperador` | `OperadorAtencion op, String contrasena` | `boolean` | Delega a `op.autenticar(contrasena)` |

---

### `MaquinaEntradaController`
**Paquete:** `Server.Controller`

Implementa el módulo de kiosco de entrada. Orquesta el flujo completo desde que el cliente llega hasta que obtiene su ticket.

**Métodos:**

| Método | Parámetros | Retorno | Descripción |
|---|---|---|---|
| `procesarEntrada` | `String numeroIdentificacion` | `Ticket` | Flujo completo: (1) Busca cliente por identificación en `GestorClientes`. Si no existe → null + log. (2) Verifica `cliente.tieneCitaVigente(gestorCitas)`. Si no tiene → null + log. (3) Toma la primera cita pendiente del cliente con `listarPorCliente().peek()`. (4) Llama `GestorTickets.emitirTicket(cita)`. Si no hay banco disponible → null + log. (5) Imprime confirmación con número de turno, banco y posición en cola |

---

### `PerfilController`
**Paquete:** `Server.Controller`

Consulta y muestra el perfil de un cliente.

**Métodos:**

| Método | Parámetros | Retorno | Descripción |
|---|---|---|---|
| `obtenerPerfil` | `String numeroIdentificacion` | `Cliente` | Delega `buscarPorIdentificacion` a `GestorClientes` |
| `citasActivas` | `int clienteId` | `LinkedList<Cita>` | Retorna citas PENDIENTES del cliente vía `GestorCitas.listarPorCliente` |
| `historialCliente` | `int clienteId` | `LinkedList<Cita>` | Filtra `GestorCitas.getHistorico()` retornando solo las citas del cliente |
| `mostrarPerfil` | `String numeroIdentificacion` | `void` | Imprime en consola: datos personales, lista de citas activas con fecha/lugar, historial con estado de cada cita |

---

## 6. Controladores de infraestructura

### `ServerController`
**Paquete:** `Server.Controller`

Conecta el modelo (`Server`) con la vista JavaFX (`ServerView`).

**Métodos:**

| Método | Parámetros | Descripción |
|---|---|---|
| `show` | `Stage stage` | Construye la ventana JavaFX y registra los handlers de los botones |
| `inicializarBancos` | — | (privado) Crea los 3 bancos por defecto: "Banco Reclamos" con ColaPrioridad, "Banco Devoluciones" con ColaBancaria, "Banco Asesorías" con ColaPrioridad. Adjunta un MonitorSala a cada banco. Los agrega a GestorBancos |

**Flujo al presionar "Iniciar":**
1. Llama `inicializarBancos()` (crea y configura los 3 bancos)
2. Llama `model.deploy()` (levanta el registro RMI y publica el servicio)
3. Muestra resultado en la consola de la vista

**Flujo al presionar "Detener":**
1. Llama `model.undeploy()` (elimina el servicio del registro RMI)
2. Muestra confirmación en la vista

---

### `ClientController`
**Paquete:** `Client.Controller`

Gestiona la interacción del cliente con el servidor vía RMI.

**Atributos:**

| Campo | Tipo | Descripción |
|---|---|---|
| `view` | `ClientView` | Vista JavaFX del cliente |
| `service` | `TicketInterface` | Proxy RMI hacia el servidor |

**Métodos:**

| Método | Descripción |
|---|---|
| `init` | Llama `conectar()` para obtener el proxy RMI. Registra el handler del botón: lee ID de cliente y número de cita, llama `service.emitirTicket()`, y muestra número de turno + banco + posición. Maneja NumberFormatException y errores RMI |
| `conectar` | (privado) Lee IP/puerto/servicio de `Environment`. Hace `Naming.lookup()` para obtener el proxy RMI. Si falla, muestra "Sin conexión" en la vista |

---

## 7. Monitores

### `MonitorSala`
**Paquete:** `Server.Model.Monitor`  
Implementa: `INotificable`

Monitor físico en pantalla de sala. Imprime actualizaciones en consola (tu compañero conectará esto a la vista JavaFX).

| Método | Descripción |
|---|---|
| `notificarTurno(Ticket t)` | Imprime número de turno y banco cuando es llamado |
| `notificarCambioEstado(int estado)` | Imprime el nuevo estado |
| `mostrarCola(IColaBancaria cola)` | Imprime cuántas personas hay en espera |
| `actualizarPantalla()` | Imprime nombre del banco, personas en espera y próximo turno |

---

### `MonitorCelular`
**Paquete:** `Server.Model.Monitor`  
Implementa: `INotificable`

Monitor en el teléfono del cliente. Cada cliente tiene el suyo.

| Método | Descripción |
|---|---|
| `notificarTurno(Ticket t)` | Imprime que es el turno del cliente con banco y número |
| `notificarCambioEstado(int estado)` | Imprime el nuevo estado al cliente |
| `consultarPosicion(int ticketId)` | Consulta la posición del ticket en la cola del banco asignado vía `banco.obtenerPosicion()` |
| `mostrarInfoTurno()` | Imprime número de turno, banco y estado actual |
| `setTicket(Ticket ticket)` | Asocia el ticket emitido con el monitor del cliente |

---

### `MonitorCelularView`
**Paquete:** `Server.View`  
Implementa: `INotificable`

Vista JavaFX del monitor celular. Envuelve a `MonitorCelular` y actualiza la pantalla en el hilo de JavaFX (`Platform.runLater`).

| Método | Descripción |
|---|---|
| `build(Stage stage)` | Construye la ventana con número de turno grande, nombre del banco, posición y estado. Fondo oscuro (#1a1a2e) |
| `notificarTurno(Ticket t)` | Actualiza `labelTurno` con el número de turno, `labelBanco` con el banco. Cambia el estado a "¡Es su turno!" |
| `notificarCambioEstado(int estado)` | Actualiza `labelEstado` con mensaje legible según el código (0-3) |
| `actualizarPosicion(int pos)` | Actualiza `labelPosicion`. Si pos==0 muestra "Próximo en ser atendido" |

**Uso:**
```java
MonitorCelular monitor = new MonitorCelular(cliente);
MonitorCelularView vista = new MonitorCelularView(monitor);
banco.addMonitor(vista);   // Se suscribe al banco
vista.build(new Stage());  // Muestra la ventana
```

---

## 8. Historial y Observer

### `History` (Singleton)
**Paquete:** `Server.Model.History`

Registra automáticamente todos los eventos del servidor.

**Estructuras internas:**
- `LinkedList<String> log` — log cronológico (append-only)
- `StackList<Action> actions` — pila de acciones para deshacer (LIFO)

**Acceso:** `History.getInstance()`

| Método | Parámetros | Retorno | Descripción |
|---|---|---|---|
| `record` | `String type, String description` | `void` | Crea un `Action` con timestamp, lo apila en `actions`, lo agrega al `log` como String formateado, y llama `notifyObservers()` |
| `undo` | — | `Action` | Desapila y retorna la última acción (la más reciente) |
| `getLogs` | — | `String[]` | Retorna el log completo como array (cronológico) |
| `actionCount` | — | `int` | Número de acciones en la pila |
| `logCount` | — | `int` | Número de entradas en el log |
| `attach` | `Observer o` | `void` | Suscribe un Observer |
| `detach` | `Observer o` | `void` | Desuscribe un Observer |
| `notifyObservers` | — | `void` | Llama `update()` en todos los observers suscritos |

**Evento registrado automáticamente por `GestorCitas`:**
- `solicitarCita` → `"CITA"`, `"Cita N registrada para Nombre Cliente"`
- `modificarCita` → `"CITA"`, `"Cita N modificada."`
- `cancelarCita` → `"CITA"`, `"Cita N cancelada."`
- `completar` → `"CITA"`, `"Cita N completada."`
- `noAsistida` → `"CITA"`, `"Cita N no asistida."`

### `Action`
**Paquete:** `Server.Model.History`

| Campo | Tipo | Descripción |
|---|---|---|
| `type` | `String` | Categoría del evento ("CITA", "TICKET", etc.) |
| `description` | `String` | Descripción legible |
| `timestamp` | `String` | Fecha y hora en formato `yyyy-MM-dd HH:mm:ss` |

---

## 9. Comunicación RMI

### `TicketService`
**Paquete:** `Server.Model.Service`  
Extiende: `UnicastRemoteObject`  
Implementa: `TicketInterface`

Fachada RMI. No tiene lógica propia — delega todo a los gestores.

| Método | Lógica |
|---|---|
| `emitirTicket(int clienteId, int citaId)` | Busca la cita en `GestorCitas`. Si no existe o no es vigente → null. Si el cliente de la cita no coincide con clienteId → null. Si todo ok → llama `GestorTickets.emitirTicket(cita)` |
| `consultarPosicion(int ticketId)` | Busca el ticket en `GestorTickets`. Si no existe o no tiene banco → -1. Si existe → `banco.obtenerPosicion(ticketId)` |
| `verFrente(int bancoId)` | Busca el banco en `GestorBancos.buscarPorId(bancoId)`. Si existe → `banco.verFrente()`. Si no → null |

### `Server` (despliegue)

| Método | Descripción |
|---|---|
| `deploy()` | Crea el registro RMI con `LocateRegistry.createRegistry(port)`. Si ya existe el puerto, usa `getRegistry`. Crea `TicketService` pasándole los 4 gestores. Lo publica con `Naming.rebind(uri, service)` |
| `undeploy()` | Llama `Naming.unbind(uri)` para retirar el servicio del registro |

**URI del servicio:** `//localhost:1099/TicketService` (configurable via variables de entorno `SERVER_IP`, `SERVER_PORT`, `SERVICE_NAME`).

---

## 10. Flujos principales paso a paso

### Flujo A: Registro de cliente

1. `GestorClientes.registrarCliente("123456789", "Diego", "Lopez", "pass", 25, "Calle 1", TIPO_ESTANDAR)`
2. Verifica: `buscarPorIdentificacion("123456789")` → null (no existe)
3. Crea `Cliente(id=1, ...)` con `contadorId++`
4. `clientes.add(cliente)` → agrega a la lista enlazada
5. `arbol.insert(cliente)` → inserta en el árbol binario
6. Retorna el cliente creado

### Flujo B: Solicitud de cita

1. `GestorCitas.solicitarCita(cliente, "2026-05-10 09:00", "Sala A", TIPO_RECLAMO, "Producto defectuoso")`
2. Crea `Cita(id=1, cliente, ...)` con estado PENDIENTE
3. `citas.add(cita)` → agrega a la lista de citas activas
4. `History.getInstance().record("CITA", "Cita 1 registrada para Diego Lopez")`
5. Retorna la cita creada

### Flujo C: Obtención de ticket en el kiosco

1. `MaquinaEntradaController.procesarEntrada("123456789")`
2. `GestorClientes.buscarPorIdentificacion("123456789")` → Cliente encontrado
3. `cliente.tieneCitaVigente(gestorCitas)` → `gestorCitas.listarPorCliente(1).isEmpty()` → false → tiene cita
4. `gestorCitas.listarPorCliente(1).peek()` → primera cita pendiente
5. `GestorTickets.emitirTicket(cita)`
   - `GestorBancos.obtenerDisponible(TIPO_RECLAMO)` → BancoServicio con menor carga
   - `prioridad = cliente.getPrioridad()` → 0 (estándar < 60)
   - Crea `Ticket(id=1, turno=1, cita, prioridad=0, banco)`
   - `banco.agregarTicket(ticket)` → `cola.encolar(ticket)`
   - `ticketsActivos.add(ticket)`
   - `cita.setTicket(ticket)`
6. Imprime: "Ticket emitido: T1 | Banco: Banco Reclamos | Posición: 1"

### Flujo D: Llamar siguiente turno (operador)

1. `OperadorAtencion.llamarSiguiente()`
2. `bancoAsignado.llamarSiguiente()`
3. `cola.desencolar()` → extrae ticket de la cola
4. `ticket.llamar()` → estado = ESTADO_ATENDIENDO
5. Itera `monitores`:
   - `MonitorSala.notificarTurno(ticket)` → imprime/actualiza pantalla sala
   - `MonitorCelular.notificarTurno(ticket)` → avisa al cliente en celular
6. `disponible = false` en el operador
7. Retorna el ticket

### Flujo E: Finalizar atención

1. `OperadorAtencion.finalizarAtencion(ticket)`
2. `ticket.completar()` → estado = ESTADO_ATENDIDO
3. `disponible = true`
4. (Opcional) `GestorCitas.completar(cita)` → mueve la cita al historial

### Flujo F: Login de cliente (RMI)

1. `ClientController.init()` llama `conectar()`
2. `Environment.getInstance()` → ip="localhost", port=1099, service="TicketService"
3. `Naming.lookup("//localhost:1099/TicketService")` → obtiene proxy `TicketInterface`
4. Usuario presiona botón con id="1" y cita="1"
5. `service.emitirTicket(1, 1)` → llamada remota al servidor
6. Servidor ejecuta `TicketService.emitirTicket(1, 1)` → delega a gestores → retorna Ticket
7. Cliente muestra "Ticket T1 emitido | Banco: Banco Reclamos | Posición: 1"

---

## 11. Reglas de negocio implementadas

| Regla | Dónde se verifica |
|---|---|
| Sin cita vigente → no puede obtener ticket | `MaquinaEntradaController.procesarEntrada()` → `cliente.tieneCitaVigente()` |
| Cita con ticket no puede modificarse | `GestorCitas.modificarCita()` → `cita.puedeModificarse()` |
| Citas canceladas no van al historial | `GestorCitas.cancelarCita()` — elimina de `citas`, no agrega a `historico` |
| No se registran clientes duplicados | `GestorClientes.registrarCliente()` → `buscarPorIdentificacion()` |
| Prioridad: premium + mayor de 60 | `Cliente.getPrioridad()` → valor 0 a 3 |
| Cada banco gestiona su cola independientemente | `BancoServicio` contiene su propia instancia de `IColaBancaria` |
| Notificación automática al llamar turno | `BancoServicio.llamarSiguiente()` → itera y notifica `monitores` |

---

## 12. Resumen de estructuras de datos por clase

| Clase | Estructura | Tipo | Motivo de elección |
|---|---|---|---|
| `GestorClientes` | `LinkedList<Cliente>` | Lista enlazada | Listado completo, recorrido lineal para iterar clientes |
| `GestorClientes` | `BinaryTree<Cliente>` | Árbol BST | Búsqueda O(log n) por id en accesos frecuentes |
| `GestorCitas` | `LinkedList<Cita>` (citas) | Lista enlazada | Citas activas, se agregan/eliminan dinámicamente |
| `GestorCitas` | `LinkedList<Cita>` (historico) | Lista enlazada | Historial cronológico, append-only |
| `GestorTickets` | `LinkedList<Ticket>` | Lista enlazada | Seguimiento de todos los tickets activos |
| `GestorBancos` | `LinkedList<BancoServicio>` | Lista enlazada | Recorrido para encontrar banco disponible |
| `GestorReportes` | `StackList<Cita>` | Pila LIFO | La cita más reciente en la cima, ideal para reportes invertidos |
| `BancoServicio` | `LinkedList<INotificable>` | Lista enlazada | Monitores suscritos, se notifican en secuencia |
| `ColaBancaria` | `QueueList<Ticket>` | Cola FIFO | Atención en orden de llegada |
| `ColaPrioridad` | `PriorityQueueClass<Ticket>` | Cola con prioridad | Premium y mayores de 60 se atienden primero |
| `AdminController` | `BinaryTree<Cliente>` | Árbol BST | Búsqueda administrativa independiente O(log n) |
| `History` | `LinkedList<String>` | Lista enlazada | Log cronológico de eventos (append-only) |
| `History` | `StackList<Action>` | Pila LIFO | Historial de acciones para deshacer (la más reciente en cima) |
