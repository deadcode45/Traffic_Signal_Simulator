🚦 Traffic Signal Simulator

A Java console application that simulates the traffic signals of a four-road intersection (North, East, South, West) and gives priority to emergency vehicles while never allowing two conflicting green signals at the same time.

Problem Statement No.: 68
University: Rungta International Skills University, Bhilai, Chhattisgarh
School: School of Computer Science and Engineering
Session: 2026-27

📌 Problem Statement

Simulate traffic signals at an intersection. Use a circular queue to cycle through the signal phases, represent connected roads as a graph, and use a priority queue and a greedy rule to give priority to emergency vehicles while maintaining safe signal transitions.

✨ Features
Normal signal phases cycle in the order North → East → South → West → North
Emergency requests are served by priority (the most urgent request first)
Safe transition: GREEN -> YELLOW -> RED before the next road turns green
A road turns green only if all connected roads are RED
Road names are validated (unknown roads are ignored)
Signal configuration is loaded at startup and relevant state is saved on exit
🧠 Concepts Used
Concept	Where it is used
Circular Queue	Cycling through the signal phases using (cur + 1) % size
Graph	Roads and their connections (adjacency list), safety check before green
Priority Queue	Storing emergency requests (PriorityQueue<Req>)
Greedy Rule	The most urgent request is always served first
File Handling	Loading and saving traffic_signals.txt
📁 Project Structure
Traffic-Signal-Simulator/
├── Main.java                # complete program (single class)
├── traffic_signals.txt      # data file (created/updated by the program)
├── README.md
└── docs/
    ├── Interim_Report.pdf   # project interim report
    └── Presentation.pdf     # project presentation
⚙️ Requirements
JDK 21 (recommended)
Any editor (IntelliJ IDEA, VS Code) or just a terminal
▶️ How to Run
bash
javac Main.java
java Main

or directly (JDK 11+):

bash
java Main.java
🖥️ Menu
1. Start  2. Next phase  3. Emergency request  4. Status  5. Save and Exit
Option	What it does
1	Starts the simulator. The first road (North) turns GREEN.
2	Moves to the next phase. A waiting emergency request is served first.
3	Adds an emergency request. Type the road name, optionally with a priority (e.g. South 2). Default priority is 1.
4	Shows the signal of every road and the number of pending requests.
5	Saves the phases and pending requests to the file and exits.

Priority levels: 1 = Ambulance (highest), 2 = Fire engine, 3 = Police.
If two requests have the same priority, the earlier one is served first.

📄 Data File Format (traffic_signals.txt)
North,East,South,West
South:1
Line 1: phases in the order of the cycle
Line 2: pending emergency requests as road:priority (empty if none)

If the file does not exist, the default four phases are used.

✅ Test Cases
#	Case	Input	Expected Output
1	Normal input	Start with no emergency requests	Signal: North GREEN
2	Boundary / minimum input	Only one normal phase is configured	Signal remains in the configured phase: North GREEN
3	Invalid input	Request an unknown road Z	Invalid road. Request ignored.
4	Different valid scenario	North is green; emergency vehicle arrives from South	North GREEN -> YELLOW -> RED then Signal: South GREEN (emergency priority)
Sample Run (Test Case 4)
Enter your choice: 1
Signal: North GREEN
Enter your choice: 3
Enter road of emergency vehicle: South
Emergency request accepted: South
Enter your choice: 2
North GREEN -> YELLOW -> RED
Signal: South GREEN (emergency priority)
⏱️ Time Complexity
Operation	Complexity
Next phase (circular queue)	O(1)
Road validation	O(1) average
Safe-transition check	O(n)
Add / serve emergency request	O(log m)
Load / save file	O(n + m)

n = number of roads, m = pending requests

🚀 Future Scope
Timer-based automatic signal changes with different green times
Multiple intersections using a larger road graph
Separate signals for turns and pedestrians
Green time based on traffic density
Graphical user interface
👨‍💻 Author

Ayushman Kushwaha
ERP ID: RU-25-10366
B.Tech CSE (AI & ML) in association with Google
Guide: Mr. Surya Kumar Mani

📚 References
H. Schildt, Java: The Complete Reference, 12th ed., McGraw Hill, 2021
R. Sedgewick and K. Wayne, Algorithms, 4th ed., Addison-Wesley, 2011
Oracle, Java Documentation
Oracle, The Java Tutorials
