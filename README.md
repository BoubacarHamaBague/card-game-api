# Card Game API

REST API for managing multi-player card games with player ranking and game statistics.

## Quick Start

### Prerequisites
- Java 17+
- Maven 3.8+

### Installation & Run

```bash
git clone https://github.com/BoubacarHamaBague/card-game-api.git
cd card-game-api

mvn clean install
mvn spring-boot:run

# API: http://localhost:8080
# Swagger UI: http://localhost:8080/swagger-ui.html
```

---

## How to Play

### Game Flow

#### Step 1: Create a Game
```bash
curl -X POST http://localhost:8080/api/games
```
Response:
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "remainingCards": 0,
  "playerCount": 0,
  "players": []
}
```

#### Step 2: Create a Deck
```bash
curl -X POST http://localhost:8080/api/decks
```
Response:
```json
{
  "id": "660e8400-e29b-41d4-a716-446655440001",
  "size": 52
}
```

#### Step 3: Add Deck to Game
```bash
curl -X POST http://localhost:8080/api/games/{gameId}/decks \
  -H "Content-Type: application/json" \
  -d '{"deckId":"{deckId}"}'
```

#### Step 4: Add Players
```bash
curl -X POST http://localhost:8080/api/games/{gameId}/players \
  -H "Content-Type: application/json" \
  -d '{"name":"Alice"}'
```
Response:
```json
{
  "id": "player-123",
  "name": "Alice",
  "handValue": 0,
  "cardCount": 0
}
```

#### Step 5: Deal Cards
```bash
curl -X POST "http://localhost:8080/api/games/{gameId}/players/{playerId}/deal?count=5"
```
Response: List of 5 cards dealt

#### Step 6: View Player's Hand
```bash
curl http://localhost:8080/api/games/{gameId}/players/{playerId}/cards
```
Response: Array of cards with total hand value

#### Step 7: Get Rankings
```bash
curl http://localhost:8080/api/games/{gameId}/players/ranking
```
Response:
```json
{
  "ranking": [
    {
      "position": 1,
      "playerId": "player-456",
      "playerName": "Bob",
      "handValue": 87,
      "cardCount": 10
    },
    {
      "position": 2,
      "playerId": "player-123",
      "playerName": "Alice",
      "handValue": 43,
      "cardCount": 5
    }
  ]
}
```

#### Step 8: Check Remaining Cards
```bash
curl http://localhost:8080/api/games/{gameId}/remaining/suits
```
Response: Count of remaining cards by suit

---

## Game Rules

### Card Values
| Card | Value |
|------|-------|
| Ace (A) | 1 |
| 2-10 | Face value |
| Jack (J) | 11 |
| Queen (Q) | 12 |
| King (K) | 13 |

### Deck Composition
- Standard Deck: 52 cards (13 ranks × 4 suits)
- Multiple Decks: Can add multiple decks to single game
- Suits: Hearts (♥), Diamonds (♦), Clubs (♣), Spades (♠)

### Dealing Rules
- **All-or-Nothing**: Must have enough cards for full request or get 0 cards
  - Request 5 cards with 3 remaining → Get 0 cards
  - Request 5 cards with 5+ remaining → Get 5 cards
- Hand value = sum of all card values
- Players ranked by highest hand value
- Removed players lose their cards

---

## API Endpoints

### Games
```
POST   /api/games              → Create game (201 Created)
DELETE /api/games/{gameId}     → Delete game (204 No Content)
```

### Players
```
POST   /api/games/{gameId}/players                 → Add player (201)
POST   /api/games/{gameId}/players/{playerId}/deal → Deal cards (200)
GET    /api/games/{gameId}/players/{playerId}/cards → Get hand (200)
GET    /api/games/{gameId}/players/ranking        → Get rankings (200)
```

### Decks
```
POST /api/decks                 → Create deck (201)
POST /api/games/{gameId}/decks  → Add deck to game (201)
```

### Statistics
```
GET /api/games/{gameId}/remaining/suits  → Cards by suit (200)
GET /api/games/{gameId}/remaining/cards  → All remaining cards (200)
```

### Error Responses
```
400 Bad Request      → Invalid input (blank name, negative count, malformed JSON)
404 Not Found        → Game/Player/Deck doesn't exist
415 Unsupported Media Type → Wrong Content-Type header
```

---

## Architecture

### System Design Overview

```
┌──────────────────────────────────────────────────────────────────┐
│                    HTTP CLIENT (REST)                             │
│             curl, Postman, Web Browser, etc.                     │
└──────────────────────────┬───────────────────────────────────────┘
                           │
                    HTTP Requests/Responses
                           │
┌──────────────────────────▼───────────────────────────────────────┐
│                 REST INTERFACE LAYER                              │
│  ┌─────────────┐  ┌──────────────┐  ┌──────────────────────────┐ │
│  │ Controllers │  │  Exception   │  │  Request Validation      │ │
│  │             │  │  Mappers     │  │  (URL Format, @Valid)    │ │
│  │ • Game      │  │              │  │                          │ │
│  │ • Player    │  │ @ControllerAdvice with @Order           │ │
│  │ • Deck      │  │ Returns:     │  │  Returns HTTP errors:    │ │
│  │ • Stats     │  │ • 400 Bad Request                        │ │
│  │             │  │ • 404 Not Found                          │ │
│  │ Converts    │  │ • 415 Unsupported Media Type             │ │
│  │ DTO ←→ JSON │  │ • 500 Internal Error                     │ │
│  └─────────────┘  └──────────────┘  └──────────────────────────┘ │
└──────────────────────────┬───────────────────────────────────────┘
                           │
                   Business Logic Calls
                           │
┌──────────────────────────▼───────────────────────────────────────┐
│               APPLICATION LAYER (Services)                        │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────────────────┐│
│  │ GameService  │  │PlayerService │  │   DeckService            ││
│  │              │  │              │  │                          ││
│  │ • Create     │  │ • Add        │  │ • Create Deck            ││
│  │ • Delete     │  │ • Stats      │  │ • Add to Game            ││
│  │ • Shuffle    │  │              │  │ • Atomic operations      ││
│  └──────────────┘  └──────────────┘  └──────────────────────────┘│
│  ┌──────────────────────────┐  ┌──────────────────────────────────┐│
│  │  CardDealingService      │  │  GameStatisticsService          ││
│  │                          │  │                                  ││
│  │ • Deal cards (atomic)    │  │ • Player rankings               ││
│  │ • All-or-Nothing logic   │  │ • Hand calculations             ││
│  └──────────────────────────┘  └──────────────────────────────────┘│
└──────────────────────────┬───────────────────────────────────────┘
                           │
                   Domain Model Operations
                           │
┌──────────────────────────▼───────────────────────────────────────┐
│                    DOMAIN LAYER                                   │
│              (Business Rules & Models)                            │
│                                                                   │
│  ┌─────────────────┐  ┌──────────────┐  ┌─────────────────────┐ │
│  │   Game          │  │  Player      │  │  Deck & GameDeck    │ │
│  │ (UUID)          │  │ (UUID)       │  │                     │ │
│  │ • Players[]     │  │ • Name       │  │ • Cards[]           │ │
│  │ • GameDeck      │  │ • Hand[]     │  │ • Size              │ │
│  │ • State         │  │ • HandValue  │  │ • Remaining         │ │
│  │ Immutable       │  │ Immutable    │  │ Immutable           │ │
│  └─────────────────┘  └──────────────┘  └─────────────────────┘ │
│                                                                   │
│  ┌─────────────────┐  ┌──────────────┐  ┌─────────────────────┐ │
│  │   Card          │  │  Suit        │  │  Rank               │ │
│  │                 │  │              │  │                     │ │
│  │ • Suit          │  │ HEARTS       │  │ ACE (1)             │ │
│  │ • Rank          │  │ DIAMONDS     │  │ 2-10 (face value)   │ │
│  │ • Value         │  │ CLUBS        │  │ JACK (11)           │ │
│  │ • Immutable     │  │ SPADES       │  │ QUEEN (12)          │ │
│  │                 │  │              │  │ KING (13)           │ │
│  └─────────────────┘  └──────────────┘  └─────────────────────┘ │
│                                                                   │
│  ┌──────────────────────────────────────────────────────────────┐│
│  │           Domain Exceptions (Business Errors)                ││
│  │  • GameNotFoundException                                     ││
│  │  • PlayerNotFoundException                                   ││
│  │  • DeckNotFoundException                                     ││
│  │  • InvalidGameException                                     ││
│  │  • InvalidPlayerException                                   ││
│  │  • InvalidCardException                                     ││
│  └──────────────────────────────────────────────────────────────┘│
└──────────────────────────┬───────────────────────────────────────┘
                           │
                   Repository Interface Calls
                           │
┌──────────────────────────▼───────────────────────────────────────┐
│              INFRASTRUCTURE LAYER                                 │
│            (In-Memory Persistence)                               │
│                                                                   │
│  ┌──────────────────────────┐  ┌────────────────────────────────┐│
│  │ InMemoryGameRepository   │  │ InMemoryDeckRepository         ││
│  │                          │  │                                ││
│  │ • Save Game              │  │ • Save Deck                    ││
│  │ • Find by ID             │  │ • Find by ID                   ││
│  │ • Delete                 │  │ • All Decks                    ││
│  │ • Concurrent Safe        │  │ • Thread-Safe                  ││
│  └──────────────────────────┘  └────────────────────────────────┘│
│                                                                   │
│  ┌──────────────────────────────────────────────────────────────┐│
│  │         Thread Safety Implementation                         ││
│  │  • ReentrantReadWriteLock for concurrent access             ││
│  │  • Atomic read-modify-write operations                      ││
│  │  • No blocking on readers (parallel reads)                  ││
│  │  • Exclusive write lock (single writer)                     ││
│  └──────────────────────────────────────────────────────────────┘│
└──────────────────────────┬───────────────────────────────────────┘
                           │
                 In-Memory Data Storage
                           │
┌──────────────────────────▼───────────────────────────────────────┐
│                    DATA STORAGE                                   │
│                                                                   │
│  Games Map:     UUID → Game object                               │
│  Decks Map:     UUID → Deck object                               │
│  Lock:          ReentrantReadWriteLock                           │
│                                                                   │
│  Features:                                                       │
│  • Fast in-memory access (no database latency)                   │
│  • All data lost on restart (expected for this POC)              │
│  • Thread-safe concurrent operations                             │
│  • Atomic transactions per operation                             │
└──────────────────────────────────────────────────────────────────┘
```

### Data Flow Example: Deal Cards

```
Client Request
    ↓
[REST Controller] Receives POST /games/{gameId}/players/{playerId}/deal?count=5
    ↓
[Validation] Check gameId (not blank), count (≥0), Content-Type (JSON)
    ↓
[CardDealingService] dealCards(gameId, playerId, count)
    ↓
[GameRepository] Read game → Acquire read lock
    ↓
[Domain] Game.dealCards() - All-or-Nothing logic
    - Check if enough cards available
    - If yes: deal all cards, update hand
    - If no: return empty list (atomic)
    ↓
[GameRepository] Write updated game → Acquire write lock
    ↓
[Response Mapper] Convert to JSON
    ↓
[Client] Receives 200 OK + card array
```

The application is built using Clean Architecture with 4 distinct layers:

#### 1. REST Interface Layer
- **Controllers**: GameController, PlayerController, DeckController, StatisticsController
- **DTOs**: Request/Response models
- **Exception Mappers**: @ControllerAdvice for centralized error handling
- **Filters**: Request validation (URL format checking)

#### 2. Application Layer
- **Services**: Orchestrate business logic
  - GameService: Game lifecycle management
  - PlayerService: Player management
  - DeckService: Deck management with atomic operations
  - CardDealingService: Card distribution
  - GameStatisticsService: Rankings and statistics

#### 3. Domain Layer (Business Rules)
- **Models**: Game, Player, Deck, Card, Suit, Rank
- **Value Objects**: Card with immutable Suit and Rank
- **Exceptions**: Specific domain exceptions
  - GameNotFoundException, PlayerNotFoundException, DeckNotFoundException
  - InvalidGameException, InvalidPlayerException, InvalidCardException

#### 4. Infrastructure Layer (Persistence)
- **Repositories**: In-memory storage
  - InMemoryGameRepository
  - InMemoryDeckRepository
- **Thread Safety**: ReentrantReadWriteLock for concurrent access
- **Atomic Operations**: Read-modify-write transactions

### Design Patterns

1. **Dependency Injection**: Constructor-based, no field injection
2. **Repository Pattern**: Abstract data access
3. **Service Layer Pattern**: Business logic encapsulation
4. **Exception Handling Pattern**: Centralized @ControllerAdvice
5. **Immutability**: Domain models are immutable where possible
6. **All-or-Nothing Semantics**: Card dealing is atomic

### Key Design Decisions

- **In-Memory Storage**: Fast, simple, no database overhead
- **Thread-Safe Operations**: ReentrantReadWriteLock ensures concurrent access safety
- **Atomic Transactions**: Game state changes are atomic (all succeed or all fail)
- **UUID for IDs**: Distributed ID generation without central database
- **Validation at Multiple Levels**: Input validation, business rule validation
- **Exception Hierarchy**: Specific exceptions for better error handling

---

## Testing

```bash
mvn test                 # Run all unit tests
mvn clean verify         # Run tests + JaCoCo coverage check (95%+ required)
```

**Test Coverage:**
- 225 comprehensive tests
- 95%+ JaCoCo code coverage (line coverage)
- Unit tests: Domain, Services, Repositories
- Integration tests: REST endpoints, exception handling
- Edge case tests: Boundary conditions, concurrent operations

**Test Categories:**
- Domain model tests (Card, Deck, Game, Player)
- Service layer tests (business logic)
- Controller tests (HTTP endpoints)
- Exception handler tests (error responses)
- Edge case tests (all-or-nothing semantics, 10 players + 4 decks)

---

## Code Quality

- **Architecture**: Clean Architecture with 4 clear layers
- **SOLID Principles**: Single responsibility, dependency injection
- **Code Coverage**: 95%+ line coverage with JaCoCo
- **Documentation**: Swagger/OpenAPI for REST endpoints
- **Validation**: Input validation at REST layer and business logic layer
- **Error Handling**: Specific exception mappers with proper HTTP status codes
