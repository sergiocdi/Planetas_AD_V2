# Diagramas UML - Planetas AD v2

## 1. Diagrama de Casos de Uso

```
┌─────────────────────────────────────────────────────────────────────────────────────────┐
│                            SISTEMA PLANETAS AD v2                                       │
│                                                                                         │
│  ┌──────────────────────┐  ┌──────────────────────┐  ┌──────────────────────────────┐   │
│  │    AUTENTICACIÓN     │  │  GESTIÓN USUARIOS    │  │    GESTIÓN PLANETAS          │   │
│  │                      │  │                      │  │                              │   │
│  │  ○ UC-01 Registrarse │  │  ○ UC-04 Consultar   │  │  ○ UC-08 Comprar Planeta     │   │
│  │  ○ UC-02 Login       │  │         Perfil       │  │  ○ UC-09 Ver Mis Planetas    │   │
│  │  ○ UC-03 Logout      │  │  ○ UC-05 Actualizar  │  │  ○ UC-10 Ver Todos Planetas  │   │
│  │                      │  │         Perfil       │  │  ○ UC-11 Ver Ranking         │   │
│  │                      │  │  ○ UC-06 Eliminar    │  │  ○ UC-12 Actualizar Planeta  │   │
│  │                      │  │         Cuenta       │  │  ○ UC-13 Eliminar Planeta    │   │
│  │                      │  │  ○ UC-07 Listar      │  │                              │   │
│  │                      │  │         Usuarios     │  │                              │   │
│  └──────────────────────┘  └──────────────────────┘  └──────────────────────────────┘   │
│                                                                                         │
│  ┌──────────────────────────────┐  ┌────────────────────────────────────────────────┐   │
│  │      GESTIÓN PARTIDAS        │  │         ESTADÍSTICAS (MongoDB)                 │   │
│  │                              │  │                                                │   │
│  │  ○ UC-14 Guardar Partida     │  │  ○ UC-19 Copiar Partidas a MongoDB             │   │
│  │  ○ UC-15 Finalizar Partida   │  │  ○ UC-20 Ver Partidas Históricas               │   │
│  │  ○ UC-16 Finalizar con       │  │  ○ UC-21 Ver Usuario Top                       │   │
│  │         Posiciones           │  │  ○ UC-22 Ver Tipo Planeta Top                  │   │
│  │  ○ UC-17 Ver Partidas        │  │  ○ UC-23 Ver Ranking Usuarios                  │   │
│  │  ○ UC-18 Ver Detalle Partida │  │  ○ UC-24 Ver Ranking Tipos Planeta             │   │
│  │                              │  │                                                │   │
│  └──────────────────────────────┘  └────────────────────────────────────────────────┘   │
│                                                                                         │
└─────────────────────────────────────────────────────────────────────────────────────────┘

     👤                    👤                      🎮                    👤
   Usuario              Usuario               Sistema de            Administrador
   No Auth.            Autenticado              Juego

     │                     │                       │                      │
     ├──► UC-01, UC-02     │                       │                      │
     │                     │                       │                      │
     │                     ├──► UC-03 a UC-06      │                      │
     │                     ├──► UC-08 a UC-13      │                      │
     │                     ├──► UC-16 a UC-18      │                      │
     │                     ├──► UC-20 a UC-24      │                      │
     │                     │                       │                      │
     │                     │                       ├──► UC-14, UC-15      │
     │                     │                       │                      │
     │                     │                       │                      ├──► UC-07
     │                     │                       │                      ├──► UC-19
```

### Matriz Actor - Caso de Uso

| Caso de Uso | No Auth | Auth | Sistema | Admin |
|-------------|:-------:|:----:|:-------:|:-----:|
| UC-01 Registrarse | ✓ | | | |
| UC-02 Login | ✓ | | | |
| UC-03 Logout | | ✓ | | |
| UC-04 Consultar Perfil | | ✓ | | |
| UC-05 Actualizar Perfil | | ✓ | | |
| UC-06 Eliminar Cuenta | | ✓ | | |
| UC-07 Listar Usuarios | | | | ✓ |
| UC-08 Comprar Planeta | | ✓ | | |
| UC-09 Ver Mis Planetas | | ✓ | | |
| UC-10 Ver Todos Planetas | | ✓ | | |
| UC-11 Ver Ranking | | ✓ | | |
| UC-12 Actualizar Planeta | | ✓ | | |
| UC-13 Eliminar Planeta | | ✓ | | |
| UC-14 Guardar Partida | | | ✓ | |
| UC-15 Finalizar Partida | | | ✓ | |
| UC-16 Finalizar con Posiciones | | ✓ | | |
| UC-17 Ver Partidas | | ✓ | | |
| UC-18 Ver Detalle Partida | | ✓ | | |
| UC-19 Copiar a MongoDB | | | | ✓ |
| UC-20 Ver Partidas Históricas | | ✓ | | |
| UC-21 Ver Usuario Top | | ✓ | | |
| UC-22 Ver Tipo Planeta Top | | ✓ | | |
| UC-23 Ver Ranking Usuarios | | ✓ | | |
| UC-24 Ver Ranking Tipos Planeta | | ✓ | | |

---

## 2. Diagrama de Clases (Simplificado)

```
┌─────────────────────────────────────────────────────────────────────────────────────────┐
│                                    CAPA DE DOMINIO                                       │
├─────────────────────────────────────────────────────────────────────────────────────────┤
│                                                                                          │
│    ┌─────────────────┐         ┌─────────────────┐         ┌─────────────────┐          │
│    │   <<abstract>>  │         │                 │         │                 │          │
│    │    Auditable    │         │     Usuario     │         │     Planeta     │          │
│    ├─────────────────┤         ├─────────────────┤         ├─────────────────┤          │
│    │ - createdAt     │         │ - id: Long      │◄────────│ - id: Long      │          │
│    │ - updatedAt     │         │ - nombre        │    1    │ - nombre        │          │
│    └────────▲────────┘         │ - apellidos     │    │    │ - tipo          │          │
│             │                  │ - nickname      │    │    │ - vidas: 5      │          │
│             │                  │ - password      │    │    │ - victorias     │          │
│    ┌────────┴────────┐         │ - email         │    │    │ - usuario       │          │
│    │   Extienden:    │         │ - monedas       │    │    └─────────────────┘          │
│    │  - Usuario      │         └─────────────────┘    │           0..*                   │
│    │  - Planeta      │                │               │                                  │
│    │  - Partida      │                │ 1             │                                  │
│    │  - Participante │                │               │                                  │
│    └─────────────────┘                ▼               │                                  │
│                                ┌─────────────────┐    │    ┌─────────────────┐          │
│                                │  Participante   │────┘    │     Partida     │          │
│                                ├─────────────────┤         ├─────────────────┤          │
│                                │ - id: Long      │◄────────│ - id: Long      │          │
│                                │ - partida       │    1    │ - estado        │          │
│                                │ - usuario       │    │    │ - numeroRonda   │          │
│                                │ - planeta       │    │    └─────────────────┘          │
│                                │ - vida          │    │                                  │
│                                └─────────────────┘  0..*                                │
│                                                                                          │
└─────────────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────────────────┐
│                              DOCUMENTOS MONGODB                                          │
├─────────────────────────────────────────────────────────────────────────────────────────┤
│                                                                                          │
│    ┌─────────────────────┐              ┌─────────────────────┐                         │
│    │   <<Document>>      │              │                     │                         │
│    │    PartidaDoc       │              │   ParticipanteDoc   │                         │
│    ├─────────────────────┤    1    0..* ├─────────────────────┤                         │
│    │ - id: String        │──────────────│ - participanteId    │                         │
│    │ - partidaIdMysql    │              │ - usuarioId         │                         │
│    │ - estado            │              │ - usuarioNickname   │                         │
│    │ - numeroRonda       │              │ - planetaId         │                         │
│    │ - participantes[]   │              │ - planetaNombre     │                         │
│    │ - fechaCreacion     │              │ - planetaTipo       │                         │
│    │ - fechaCopiado      │              │ - vida              │                         │
│    └─────────────────────┘              │ - ganador           │                         │
│                                         └─────────────────────┘                         │
│                                                                                          │
└─────────────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────────────────┐
│                                CAPA DE SERVICIOS                                         │
├─────────────────────────────────────────────────────────────────────────────────────────┤
│                                                                                          │
│  ┌───────────────┐  ┌───────────────┐  ┌───────────────┐  ┌───────────────────────┐    │
│  │  AuthService  │  │UsuarioService │  │PlanetaService │  │    PartidaService     │    │
│  ├───────────────┤  ├───────────────┤  ├───────────────┤  ├───────────────────────┤    │
│  │ authenticate  │  │ create        │  │ create        │  │ guardarPartida        │    │
│  │ generateToken │  │ list          │  │ list          │  │ finalizarPartida      │    │
│  │ validateToken │  │ get           │  │ ranking       │  │ finalizarConPosiciones│    │
│  │ encode        │  │ update        │  │ listByUsuario │  │ obtenerTodas          │    │
│  └───────────────┘  │ delete        │  │ update        │  │ obtenerPorId          │    │
│                     └───────────────┘  │ delete        │  └───────────────────────┘    │
│                                        └───────────────┘                                │
│                                                                                          │
│  ┌───────────────────────────────────────────────────────────────────────────────┐     │
│  │                          EstadisticasService                                   │     │
│  ├───────────────────────────────────────────────────────────────────────────────┤     │
│  │ copiarPartidasAMongo | listarTodasLasPartidas | obtenerUsuarioConMasVictorias │     │
│  │ obtenerTipoPlanetaConMasVictorias | obtenerRankingUsuarios | rankingTipos     │     │
│  └───────────────────────────────────────────────────────────────────────────────┘     │
│                                                                                          │
└─────────────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────────────────┐
│                              CAPA DE CONTROLADORES                                       │
├─────────────────────────────────────────────────────────────────────────────────────────┤
│                                                                                          │
│  ┌────────────────┐  ┌────────────────┐  ┌────────────────┐  ┌────────────────┐        │
│  │ AuthController │  │UsuarioController│  │PlanetaController│ │PartidaController│       │
│  │  /api/auth     │  │  /api/usuarios │  │  /api/planetas │  │  /api/partidas │        │
│  ├────────────────┤  ├────────────────┤  ├────────────────┤  ├────────────────┤        │
│  │ POST /login    │  │ POST /         │  │ POST /         │  │ POST /guardar  │        │
│  │ POST /logout   │  │ GET /          │  │ GET /          │  │ POST /finalizar│        │
│  └────────────────┘  │ GET /{id}      │  │ GET /ranking   │  │ GET /          │        │
│                      │ PUT /{id}      │  │ PUT /{id}      │  │ GET /{id}      │        │
│                      │ DELETE /{id}   │  │ DELETE /{id}   │  └────────────────┘        │
│                      │ GET /{id}/plan │  └────────────────┘                             │
│                      │ POST /{id}/comp│                                                 │
│                      └────────────────┘                                                 │
│                                                                                          │
│  ┌─────────────────────────────────────────────────────────────────────────────────┐   │
│  │                    EstadisticasController  /api/estadisticas                     │   │
│  ├─────────────────────────────────────────────────────────────────────────────────┤   │
│  │ POST /copiar | GET /partidas | GET /usuario-top | GET /tipo-planeta-top         │   │
│  │ GET /ranking-usuarios | GET /ranking-tipos-planeta                              │   │
│  └─────────────────────────────────────────────────────────────────────────────────┘   │
│                                                                                          │
└─────────────────────────────────────────────────────────────────────────────────────────┘
```

---

## 3. Relaciones entre Entidades

```
                                    MYSQL
    ┌─────────────────────────────────────────────────────────────────┐
    │                                                                 │
    │      ┌─────────┐           ┌─────────────┐                     │
    │      │ Usuario │◄──────────│  Planeta    │                     │
    │      └────┬────┘   1    *  └─────────────┘                     │
    │           │                      │                              │
    │           │ 1                    │ 1                            │
    │           │                      │                              │
    │           ▼ *                    ▼ *                            │
    │      ┌─────────────┐        ┌─────────────┐                    │
    │      │Participante │───────►│   Partida   │                    │
    │      └─────────────┘  *   1 └─────────────┘                    │
    │                                                                 │
    └─────────────────────────────────────────────────────────────────┘
                          │
                          │ Sincronización
                          ▼
    ┌─────────────────────────────────────────────────────────────────┐
    │                       MONGODB ATLAS                             │
    │                                                                 │
    │      ┌───────────────────────────────────────────────┐         │
    │      │                 PartidaDoc                     │         │
    │      │  ┌──────────────────────────────────────────┐ │         │
    │      │  │           ParticipanteDoc[]              │ │         │
    │      │  │  (datos desnormalizados embebidos)       │ │         │
    │      │  └──────────────────────────────────────────┘ │         │
    │      └───────────────────────────────────────────────┘         │
    │                                                                 │
    └─────────────────────────────────────────────────────────────────┘
```

---

## 4. Flujo de Datos

```
┌──────────┐     ┌────────────┐     ┌───────────┐     ┌────────────┐     ┌─────────┐
│  Cliente │────►│ Controller │────►│  Service  │────►│ Repository │────►│   BD    │
│  (HTTP)  │◄────│  (REST)    │◄────│  (Logic)  │◄────│   (JPA)    │◄────│ (MySQL) │
└──────────┘     └────────────┘     └───────────┘     └────────────┘     └─────────┘
                                          │
                                          │ (Estadísticas)
                                          ▼
                                    ┌────────────┐     ┌─────────────┐
                                    │ Repository │────►│  MongoDB    │
                                    │  (Mongo)   │◄────│   Atlas     │
                                    └────────────┘     └─────────────┘
```

---

## 5. Cómo visualizar los diagramas PlantUML

### Opción 1: Online
1. Ve a [PlantUML Web Server](http://www.plantuml.com/plantuml/uml/)
2. Copia el contenido de los archivos `.puml`
3. Visualiza el diagrama generado

### Opción 2: VS Code
1. Instala la extensión "PlantUML"
2. Abre los archivos `.puml`
3. Presiona `Alt+D` para ver el preview

### Opción 3: IntelliJ IDEA
1. Instala el plugin "PlantUML Integration"
2. Abre los archivos `.puml`
3. Usa el panel de preview

---

*Documentación generada: Marzo 2026*
