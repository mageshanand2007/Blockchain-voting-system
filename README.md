# ChainVote — Blockchain Based E-Voting System

**"Digital Democracy + Blockchain Security"**

ChainVote is an educational electronic voting system designed to demonstrate how blockchain concepts (immutability, cryptographic hashing, distributed ledgers) can be applied to secure digital elections.

## 🚀 Features

- **Object-Oriented Java Backend**: A robust, console-based Java application demonstrating encapsulation, inheritance, polymorphism, and interfaces.
- **Modern Web Frontend**: A premium, dark-themed futuristic UI representing a high-security voting portal.
- **Blockchain Simulation**: Uses SHA-256 to hash blocks. Each vote is a transaction stored in a block, linked cryptographically to the previous block.
- **Integrity Verification**: Allows administrators and voters to verify the integrity of the blockchain to ensure no votes have been tampered with.
- **Role-Based Access**: Dedicated portals for Voters (to cast votes) and Admins (to manage the election).

> **Note:** This is an educational blockchain simulation and not a production election infrastructure. It uses simulated mock data and simplified cryptography for academic demonstration.

## 🛠️ Technology Stack

- **Backend**: Java (Standard Edition)
- **Frontend**: HTML5, CSS3, JavaScript (Vanilla)
- **Security**: SHA-256 Hashing (`MessageDigest`)
- **Data Structures**: `ArrayList`, `HashMap`, custom Objects

## 🏗️ Architecture & OOP Concepts Used

The Java backend heavily utilizes Object-Oriented principles:
- **Inheritance**: `Admin` and `Voter` extend the abstract `User` class.
- **Interfaces**: `Authenticatable`, `Votable`, and `BlockchainVerifiable` enforce contracts.
- **Encapsulation**: State such as passwords, block hashes, and voting statuses are kept private and accessed via getters/setters.
- **Polymorphism**: The `AuthenticationService` handles multiple types of `User` objects interchangeably.

### How the Blockchain Works (SHA-256)
Each `Block` contains:
- `blockIndex`: Its position in the chain.
- `timestamp`: When the block was created.
- `voteTransactionId`: The unique ID of the vote cast.
- `previousHash`: The cryptographic hash of the preceding block.
- `hash`: Its own SHA-256 hash.

`Hash = SHA256(index + timestamp + previousHash + voteTransactionId)`

If any block's data is modified, its hash changes, breaking the cryptographic link to the next block, triggering an Integrity Failure during verification.

## 🏃 How to Run

### Running the Java Console Backend
The Java backend operates entirely independently of the web frontend as a secure console application.
1. Open a terminal/command prompt.
2. Navigate to the `ChainVote/backend/src` directory.
3. Compile the application:
   ```bash
   javac -d ../bin model/*.java interfaces/*.java exceptions/*.java blockchain/*.java services/*.java main/*.java
   ```
4. Run the application:
   ```bash
   java -cp ../bin main.Main
   ```

### Running the Web Frontend
The web frontend is a standalone demonstration of the UI/UX using `localStorage` to simulate the backend state perfectly without requiring a complex web server setup.
1. Navigate to the `ChainVote/frontend/` folder.
2. Simply open `index.html` in any modern web browser.
3. You can test the full flow: login, view candidates, start election (admin), cast a vote (voter), and verify the blockchain.

## 🔑 Demo Credentials

**Admin Portal:**
- **ID:** admin
- **Password:** admin123

**Voter Portal:**
- **ID:** VOTER001 (or VOTER002, VOTER003)
- **Password:** password

## 📝 Usage Flow
1. **Admin Login**: Go to Admin Portal, login, and click **START ELECTION**.
2. **Voter Login**: Open Voter Login, enter `VOTER001` / `password`.
3. **Cast Vote**: Go to "Cast Vote", select a fictional candidate, and submit.
4. **Blockchain**: Go to the Blockchain Explorer to see the newly generated block. Click "Verify Blockchain" to test integrity.
5. **Tampering Demo**: As an Admin, use the "Simulate Tampering Attack" to modify a hash, then run "Verify Blockchain" again to see it fail.
6. **Results**: Admin clicks **END ELECTION** to view the final results dashboard.

## 🔮 Future Improvements
- Implement a true REST API using Spring Boot to connect the Java backend directly to the web frontend.
- Implement asymmetric cryptography (Public/Private keys) to sign individual vote transactions.
- Transition to a truly distributed P2P node architecture.
- Add database persistence (e.g., PostgreSQL) instead of in-memory maps.

## ⚠️ Limitations
- The Java application uses in-memory data structures (data resets on restart).
- The web frontend relies on LocalStorage for state management as a demonstration.
- Passwords in the demo are stored in plain text for educational simplicity.
