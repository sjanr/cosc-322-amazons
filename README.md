# Game of Amazons AI – Team 18

This repository contains the source code for our Game of Amazons AI player, developed for the COSC 322 final tournament at UBC Okanagan. The player connects to the provided game server to play 10x10 Amazons matches in real-time.

## 🏅 Tournament Recognition

Our AI placed **3rd out of 22 teams** in the UBCO COSC 322 Game of Amazons Tournament!

![COSC 322 Certificate](cosc-322-certificate.png)


---

## 👥 Team Members
- Anuk Ahangamgoda  
- Ashish Nayak  
- Jan Suratos  
- Tawana Ndlovu  

---

## 🛠 Techniques Implemented
- Minimax Search Algorithm  
- Alpha-Beta Pruning  
- Voronoi-Based Heuristic Evaluation  
- Iterative Deepening  
- Zobrist Hashing  
- Transposition Table (Memoization)  
- Game Rule Enforcement & Move Validation  
- Action Factory for Generating Legal Moves  

---

## 📈 Development Summary

📈 Development Summary
We began the project by completing warm-up demos and reviewing the SmartFox game server’s API. After experimenting with the provided HumanPlayer class, we developed a custom player that maintains an internal game board, validates actions, and communicates effectively with the server. This setup allowed us to simulate moves and apply game logic independently of the server's visual interface.

To generate all legal queen and arrow combinations, we implemented an Action Factory capable of dynamically exploring every direction on the board. For rule enforcement, we developed robust move validation logic that checks all aspects of gameplay legality.

For decision-making, we implemented the Minimax algorithm with Alpha-Beta pruning, supported by iterative deepening to manage branching complexity and enforce time constraints. Our utility function is based on Voronoi-based territory evaluation, which estimates influence over the board based on queen proximity — an approach inspired by several AI research papers.

To improve efficiency, we integrated Zobrist hashing to compute unique board states quickly and used a transposition table for memoization, significantly reducing redundant evaluations during search.

Our development briefly stalled due to remote connectivity issues over reading week. After setting up the UBC Okanagan VPN, we were able to resume work seamlessly through our GitHub repository, enabling effective remote collaboration and version control.

---

## 📅 Planned Features (and Achievements)
- ✅ Board state management  
- ✅ Move generation using an Action Factory  
- ✅ Full rule-based move validation  
- ✅ Heuristic design inspired by spatial dominance  
- ✅ Alpha-Beta pruning with iterative deepening for timing control  
- ✅ Zobrist hashing for fast board comparison and memoization  
- ✅ Tournament-ready performance under strict time limits  

---

## ⚠️ Note
This player requires a connection to the SmartFox game server used in COSC 322. It will not run as a standalone application without the server backend.

---

## 📂 Repository Structure
- `TeamPlayer.java` – Main AI player logic  
- `Board.java` – Board state and utility computation  
- `ActionFactory.java` – Move and arrow generation logic  
- `Minimax.java` – Recursive search with pruning and memoization  
- `ZobristHash.java` – Efficient board state hashing  
- `COSC322TestHumanPlayer.java` – For testing server communication manually  
