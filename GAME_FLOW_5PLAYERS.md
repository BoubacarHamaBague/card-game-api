# Game Flow - 5 Players Example

Complete game session with real HTTP requests and responses from the Card Game API.

---

## Step 1: Create Game

**REQUEST:**
```http
POST /api/games
Content-Type: application/json
```

**RESPONSE (201 Created):**
```json
{
  "id": "ea2d8d09-ba04-4c16-89d5-c925c14c5065",
  "remainingCards": 0,
  "playerCount": 0,
  "players": []
}
```

---

## Step 2: Create Standard Deck

**REQUEST:**
```http
POST /api/decks
Content-Type: application/json
```

**RESPONSE (201 Created):**
```json
{
  "id": "1be8967a-25c7-4ec8-97d7-5681f1d248a1",
  "size": 52
}
```

---

## Step 3: Add Deck to Game

**REQUEST:**
```http
POST /api/games/ea2d8d09-ba04-4c16-89d5-c925c14c5065/decks
Content-Type: application/json

{
  "deckId": "1be8967a-25c7-4ec8-97d7-5681f1d248a1"
}
```

**RESPONSE (200 OK):**
```json
{
  "id": "ea2d8d09-ba04-4c16-89d5-c925c14c5065",
  "remainingCards": 52,
  "playerCount": 0,
  "players": []
}
```

---

## Step 4: Shuffle Deck

**REQUEST:**
```http
POST /api/games/ea2d8d09-ba04-4c16-89d5-c925c14c5065/shuffle
Content-Type: application/json
```

**RESPONSE (200 OK):**
```json
{
  "id": "ea2d8d09-ba04-4c16-89d5-c925c14c5065",
  "remainingCards": 52,
  "playerCount": 0,
  "players": []
}
```

---

## Step 5: Add 5 Players

### Player 1: Alice

**REQUEST:**
```http
POST /api/games/ea2d8d09-ba04-4c16-89d5-c925c14c5065/players
Content-Type: application/json

{
  "name": "Alice"
}
```

**RESPONSE (201 Created):**
```json
{
  "id": "bcc00c59-8d44-457c-8660-b3ba0c5a73d7",
  "name": "Alice",
  "handValue": 0,
  "cardCount": 0
}
```

### Player 2: Bob

**REQUEST:**
```http
POST /api/games/ea2d8d09-ba04-4c16-89d5-c925c14c5065/players
Content-Type: application/json

{
  "name": "Bob"
}
```

**RESPONSE (201 Created):**
```json
{
  "id": "517a5f02-56af-4b45-9ba5-b9d3373b2e62",
  "name": "Bob",
  "handValue": 0,
  "cardCount": 0
}
```

### Player 3: Charlie

**REQUEST:**
```http
POST /api/games/ea2d8d09-ba04-4c16-89d5-c925c14c5065/players
Content-Type: application/json

{
  "name": "Charlie"
}
```

**RESPONSE (201 Created):**
```json
{
  "id": "94ece827-40ba-4fba-b461-41b091a63a10",
  "name": "Charlie",
  "handValue": 0,
  "cardCount": 0
}
```

### Player 4: Diana

**REQUEST:**
```http
POST /api/games/ea2d8d09-ba04-4c16-89d5-c925c14c5065/players
Content-Type: application/json

{
  "name": "Diana"
}
```

**RESPONSE (201 Created):**
```json
{
  "id": "11ea1c05-f902-4b75-ad44-cf1a77a62275",
  "name": "Diana",
  "handValue": 0,
  "cardCount": 0
}
```

### Player 5: Eve

**REQUEST:**
```http
POST /api/games/ea2d8d09-ba04-4c16-89d5-c925c14c5065/players
Content-Type: application/json

{
  "name": "Eve"
}
```

**RESPONSE (201 Created):**
```json
{
  "id": "3e4735ac-c8b3-4a68-a9c0-708cc465a6bf",
  "name": "Eve",
  "handValue": 0,
  "cardCount": 0
}
```

---

## Step 6: Deal 5 Cards to Each Player

**REQUEST (Alice):**
```http
POST /api/games/ea2d8d09-ba04-4c16-89d5-c925c14c5065/players/bcc00c59-8d44-457c-8660-b3ba0c5a73d7/deal?count=5
Content-Type: application/json
```

**RESPONSE (200 OK):**
```json
{
  "dealt": [
    {
      "suit": "Hearts",
      "rank": "9",
      "value": 9
    },
    {
      "suit": "Clubs",
      "rank": "Q",
      "value": 12
    },
    {
      "suit": "Hearts",
      "rank": "4",
      "value": 4
    },
    {
      "suit": "Clubs",
      "rank": "5",
      "value": 5
    },
    {
      "suit": "Hearts",
      "rank": "5",
      "value": 5
    }
  ],
  "totalDealt": 5
}
```

*Same deal operation repeated for Bob, Charlie, Diana, and Eve*

---

## Step 7: Get Player Hand (Alice)

**REQUEST:**
```http
GET /api/games/ea2d8d09-ba04-4c16-89d5-c925c14c5065/players/bcc00c59-8d44-457c-8660-b3ba0c5a73d7/cards
```

**RESPONSE (200 OK):**
```json
{
  "playerId": "bcc00c59-8d44-457c-8660-b3ba0c5a73d7",
  "playerName": "Alice",
  "hand": [
    {
      "suit": "Hearts",
      "rank": "9",
      "value": 9
    },
    {
      "suit": "Clubs",
      "rank": "Q",
      "value": 12
    },
    {
      "suit": "Hearts",
      "rank": "4",
      "value": 4
    },
    {
      "suit": "Clubs",
      "rank": "5",
      "value": 5
    },
    {
      "suit": "Hearts",
      "rank": "5",
      "value": 5
    }
  ],
  "totalValue": 35
}
```

---

## Step 8: Get Remaining Cards

**REQUEST:**
```http
GET /api/games/ea2d8d09-ba04-4c16-89d5-c925c14c5065/remaining/cards
```

**RESPONSE (200 OK):**
```json
{
  "total": 27,
  "cards": [
    {
      "suit": "Hearts",
      "rank": "J",
      "value": 11
    },
    {
      "suit": "Hearts",
      "rank": "7",
      "value": 7
    },
    {
      "suit": "Hearts",
      "rank": "6",
      "value": 6
    },
    {
      "suit": "Hearts",
      "rank": "3",
      "value": 3
    },
    {
      "suit": "Spades",
      "rank": "K",
      "value": 13
    },
    {
      "suit": "Spades",
      "rank": "Q",
      "value": 12
    },
    {
      "suit": "Spades",
      "rank": "10",
      "value": 10
    },
    {
      "suit": "Spades",
      "rank": "9",
      "value": 9
    },
    {
      "suit": "Spades",
      "rank": "8",
      "value": 8
    },
    {
      "suit": "Spades",
      "rank": "6",
      "value": 6
    },
    {
      "suit": "Spades",
      "rank": "3",
      "value": 3
    },
    {
      "suit": "Clubs",
      "rank": "K",
      "value": 13
    },
    {
      "suit": "Clubs",
      "rank": "J",
      "value": 11
    },
    {
      "suit": "Clubs",
      "rank": "10",
      "value": 10
    },
    {
      "suit": "Clubs",
      "rank": "9",
      "value": 9
    },
    {
      "suit": "Clubs",
      "rank": "7",
      "value": 7
    },
    {
      "suit": "Clubs",
      "rank": "6",
      "value": 6
    },
    {
      "suit": "Clubs",
      "rank": "4",
      "value": 4
    },
    {
      "suit": "Clubs",
      "rank": "A",
      "value": 1
    },
    {
      "suit": "Diamonds",
      "rank": "K",
      "value": 13
    },
    {
      "suit": "Diamonds",
      "rank": "Q",
      "value": 12
    },
    {
      "suit": "Diamonds",
      "rank": "J",
      "value": 11
    },
    {
      "suit": "Diamonds",
      "rank": "10",
      "value": 10
    },
    {
      "suit": "Diamonds",
      "rank": "9",
      "value": 9
    },
    {
      "suit": "Diamonds",
      "rank": "8",
      "value": 8
    },
    {
      "suit": "Diamonds",
      "rank": "5",
      "value": 5
    },
    {
      "suit": "Diamonds",
      "rank": "A",
      "value": 1
    }
  ]
}
```

---

## Step 9: Get Player Ranking

**REQUEST:**
```http
GET /api/games/ea2d8d09-ba04-4c16-89d5-c925c14c5065/players/ranking
```

**RESPONSE (200 OK):**
```json
{
  "ranking": [
    {
      "position": 1,
      "playerId": "bcc00c59-8d44-457c-8660-b3ba0c5a73d7",
      "playerName": "Alice",
      "handValue": 35,
      "cardCount": 5
    },
    {
      "position": 2,
      "playerId": "94ece827-40ba-4fba-b461-41b091a63a10",
      "playerName": "Charlie",
      "handValue": 31,
      "cardCount": 5
    },
    {
      "position": 3,
      "playerId": "11ea1c05-f902-4b75-ad44-cf1a77a62275",
      "playerName": "Diana",
      "handValue": 30,
      "cardCount": 5
    },
    {
      "position": 4,
      "playerId": "3e4735ac-c8b3-4a68-a9c0-708cc465a6bf",
      "playerName": "Eve",
      "handValue": 28,
      "cardCount": 5
    },
    {
      "position": 5,
      "playerId": "517a5f02-56af-4b45-9ba5-b9d3373b2e62",
      "playerName": "Bob",
      "handValue": 22,
      "cardCount": 5
    }
  ]
}
```

---

## Step 10: Get Remaining Cards by Suit

**REQUEST:**
```http
GET /api/games/ea2d8d09-ba04-4c16-89d5-c925c14c5065/remaining/suits
```

**RESPONSE (200 OK):**
```json
{
  "hearts": 4,
  "spades": 7,
  "clubs": 8,
  "diamonds": 8
}
```

---

## Summary

| Metric | Value |
|--------|-------|
| **Game ID** | ea2d8d09-ba04-4c16-89d5-c925c14c5065 |
| **Deck ID** | 1be8967a-25c7-4ec8-97d7-5681f1d248a1 |
| **Initial Deck Size** | 52 cards |
| **Players** | 5 (Alice, Bob, Charlie, Diana, Eve) |
| **Cards Dealt** | 25 (5 per player) |
| **Remaining Cards** | 27 |
| **Top Player** | Alice (35 points) |
| **Last Player** | Bob (22 points) |

### Final Game State
- **Hearts remaining:** 4
- **Spades remaining:** 7
- **Clubs remaining:** 8
- **Diamonds remaining:** 8
