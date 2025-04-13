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

We began the project by completing warm-up demos and studying the SmartFox game server's API. After experimenting with the provided HumanPlayer class, we created our own external player object capable of maintaining an internal game board and communicating with the server.

Alongside implementation, we studied a variety of research papers and thesis work on heuristic-based search, which inspired our custom territory evaluation using Voronoi diagrams. These heuristics became key to our AI’s strategic decision-making during matches.

Our development was briefly stalled due to network access issues over reading week. Once we set up the UBC Okanagan VPN, we were able to resume collaboration remotely via our shared GitHub repository.

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
