# CAC-UPB — Centro de Atención al Cliente

Sistema de gestión de citas y turnos para el CAC-UPB, desarrollado en Java con JavaFX y RMI.

---

## Cómo correr el proyecto

**Requisitos:** Java 17, Maven 3.8+

```bash
# Compilar
mvn clean compile

# Correr servidor
mvn javafx:run

# Correr cliente (en otra terminal)
mvn javafx:run -Djavafx.args=client

# Empaquetar JAR
mvn package

# Ejecutar JAR — servidor
java -jar target/cac-upb.jar

# Ejecutar JAR — cliente
java -jar target/cac-upb.jar client

# Correr tests
mvn test
```

---

## Estructuras de datos utilizadas

### `LinkedList<E>` — Lista enlazada simple
**Ubicación:** `structures/linkedlist/singly/LinkedList.java`

| Clase | Uso |
|---|---|
| `GestorClientes` | Lista completa de clientes registrados |
| `GestorCitas` | Citas activas e historial |
| `GestorTickets` | Tickets activos en el sistema |
| `GestorBancos` | Lista de bancos de servicio |
| `GestorReportes` | Reportes de citas no asistidas y completadas |
| `BancoServicio` | Lista de monitores suscritos por banco |
| `History` | Log cronológico de eventos del servidor |

**Por qué:** Las operaciones principales son agregar al final e iterar en orden. La lista enlazada es O(1) para insertar y recorre secuencialmente sin desperdicio de memoria.

---

### `QueueList<E>` — Cola FIFO enlazada
**Ubicación:** `structures/queue/QueueList.java`

| Clase | Uso |
|---|---|
| `ColaBancaria` | Cola de tickets por banco (orden de llegada) |

**Por qué:** Los tickets se atienden en orden de llegada (primero en llegar, primero en salir). La lista enlazada evita el tamaño fijo del arreglo.

---

### `PriorityQueueClass<E>` — Cola con prioridad
**Ubicación:** `structures/model/PriorityQueue/PriorityQueueClass.java`

| Clase | Uso |
|---|---|
| `ColaPrioridad` | Cola de tickets con prioridad (bancos de Reclamos y Asesorías) |

**Por qué:** Clientes premium y mayores de 60 años tienen prioridad de atención según las reglas de negocio del sistema. La prioridad se calcula automáticamente desde `Cliente.getPrioridad()`.

**Prioridad:**
- Cliente estándar < 60 años → 0
- Cliente estándar > 60 años → 1
- Cliente premium < 60 años → 2
- Cliente premium > 60 años → 3

---

### `StackList<E>` — Pila LIFO enlazada
**Ubicación:** `structures/stack/StackList.java`

| Clase | Uso |
|---|---|
| `History` | Pila de acciones del servidor (para deshacer) |
| `GestorReportes` | Historial de citas (la más reciente primero) |

**Por qué:** La pila es la estructura natural para "deshacer" — siempre se revierte la última acción. En reportes, mostrar la cita más reciente primero (LIFO) es el comportamiento esperado.

---

### `BinaryTree<E>` — Árbol binario de búsqueda
**Ubicación:** `structures/tree/BinaryTree.java`

| Clase | Uso |
|---|---|
| `GestorClientes` | Búsqueda rápida de clientes por ID |
| `AdminController` | Gestión administrativa de clientes |

**Por qué:** Buscar un cliente en una lista enlazada es O(n). Con el árbol binario la búsqueda es O(log n), lo que importa cuando hay muchos clientes registrados.

---

## Arquitectura del proyecto

```
edu.co.diegoxs96/
├── App.java                    ← Punto de entrada (server/client)
├── Launcher.java               ← Wrapper para JavaFX
├── Environment/                ← Configuración (IP, puerto, servicio)
├── Client/
│   ├── Controller/ClientController   ← Conexión RMI al servidor
│   └── View/ClientView               ← Vista del cliente (JavaFX)
└── Server/
    ├── Model/
    │   ├── Usuario.java          ← Clase base abstracta
    │   ├── Cliente.java          ← Extiende Usuario
    │   ├── Administrador.java    ← Extiende Usuario
    │   ├── OperadorAtencion.java ← Extiende Usuario
    │   ├── Cita.java             ← Solicitud de atención
    │   ├── Ticket.java           ← Turno físico
    │   ├── BancoServicio.java    ← Cola independiente por banco
    │   ├── ColaBancaria.java     ← Adapta QueueList → IColaBancaria
    │   ├── ColaPrioridad.java    ← Adapta PriorityQueueClass → IColaBancaria
    │   ├── TicketInterface.java  ← Contrato RMI
    │   ├── interfaces/           ← IUsuario, IColaBancaria, INotificable, IComparable
    │   ├── Monitor/              ← MonitorSala, MonitorCelular
    │   ├── Service/TicketService ← Fachada RMI
    │   ├── History/              ← Historial de acciones (LinkedList + StackList)
    │   └── observer/             ← Patrón Observer
    ├── Controller/
    │   ├── GestorClientes        ← CRUD clientes (LinkedList + BinaryTree)
    │   ├── GestorCitas           ← Ciclo de vida de citas (LinkedList)
    │   ├── GestorTickets         ← Emisión de tickets
    │   ├── GestorBancos          ← Gestión de bancos (LinkedList)
    │   ├── GestorReportes        ← Reportes (StackList)
    │   ├── MaquinaEntradaController ← Flujo del kiosco
    │   ├── AdminController       ← Acciones admin (BinaryTree)
    │   ├── LoginController       ← Autenticación
    │   └── ServerController      ← Conecta modelo con vista JavaFX
    ├── View/ServerView           ← Vista del servidor (JavaFX)
    └── Factory/ServerFactory     ← Ensambla el servidor completo
```

---

## Reglas de negocio implementadas

- Un cliente sin cita vigente **no puede** obtener ticket en el kiosco
- Las citas canceladas **no** se guardan en el historial
- Un cliente **no puede** modificar su cita si ya tiene ticket emitido
- No se pueden registrar dos clientes con el mismo número de identificación
- Cada banco gestiona su cola de forma **independiente**
- Los clientes premium y mayores de 60 años tienen **prioridad** de atención

---

## Integrantes

- Diego — Backend, estructuras de datos
- [Compañero] — Vistas JavaFX
