import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Main {
    record Req(int pri, int order, String road) {}

    static final String FILE = "traffic_signals.txt";
    static List<String> phases = new ArrayList<>();                 // circular queue of phases
    static Map<String, List<String>> graph = new HashMap<>();       // road -> connected roads
    static Map<String, String> signal = new HashMap<>();            // road -> RED / YELLOW / GREEN
    static PriorityQueue<Req> emergencies =
            new PriorityQueue<>(Comparator.comparingInt(Req::pri).thenComparingInt(Req::order));
    static int cur = -1, count = 0;

    static String fix(String s) {                                   // "south " -> "South"
        s = s.trim();
        return s.isEmpty() ? "" : s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

    static void addRequest(String road, int pri) { emergencies.add(new Req(pri, count++, road)); }

    public static void main(String[] args) throws IOException {
        // ----- load file: line 1 = phases, line 2 = pending requests (road:priority,...) -----
        List<String> lines = Files.exists(Path.of(FILE)) ? Files.readAllLines(Path.of(FILE)) : List.of();
        String first = lines.isEmpty() || lines.get(0).isBlank() ? "North,East,South,West" : lines.get(0);
        for (String s : first.split(","))
            if (!fix(s).isEmpty() && !phases.contains(fix(s))) phases.add(fix(s));
        for (String r : phases) {
            signal.put(r, "RED");
            List<String> others = new ArrayList<>(phases);
            others.remove(r);
            graph.put(r, others);                                   // every road meets every other road
        }
        if (lines.size() > 1)
            for (String s : lines.get(1).split(",")) {
                String[] t = s.split(":");
                if (t.length == 2 && graph.containsKey(fix(t[0])) && t[1].trim().matches("[1-3]"))
                    addRequest(fix(t[0]), Integer.parseInt(t[1].trim()));
            }

        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n1. Start  2. Next phase  3. Emergency request  4. Status  5. Save and Exit");
            System.out.print("Enter your choice: ");
            if (!sc.hasNextLine()) break;
            String choice = sc.nextLine().trim();

            if (choice.equals("1")) {
                signal.replaceAll((k, v) -> "RED");
                cur = 0;
                signal.put(phases.get(0), "GREEN");
                System.out.println("Signal: " + phases.get(0) + " GREEN");

            } else if (choice.equals("2")) {
                nextPhase();

            } else if (choice.equals("3")) {
                System.out.print("Enter road of emergency vehicle: ");
                String[] t = sc.nextLine().trim().split("\\s+");    // e.g. "South" or "South 2"
                String road = fix(t[0]);
                if (!graph.containsKey(road)) {
                    System.out.println("Invalid road. Request ignored.");
                } else {
                    addRequest(road, t.length > 1 && t[1].matches("[1-3]") ? Integer.parseInt(t[1]) : 1);
                    System.out.println("Emergency request accepted: " + road);
                }

            } else if (choice.equals("4")) {
                if (cur < 0) System.out.println("Simulator not started. Choose option 1 first.");
                else System.out.println("Signal: " + phases.get(cur) + " GREEN  |  all: " + signal
                        + "  |  pending: " + emergencies.size());

            } else if (choice.equals("5")) {
                break;
            } else {
                System.out.println("Invalid choice. Enter 1 to 5.");
            }
        }
        save();
    }

    static void nextPhase() {
        if (cur < 0) { System.out.println("Simulator not started. Choose option 1 first."); return; }
        String now = phases.get(cur), target = null;

        while (target == null && !emergencies.isEmpty()) {          // greedy: most urgent request first
            String r = emergencies.poll().road();
            if (!r.equals(now)) target = r;                         // already green -> nothing to do
        }
        boolean emergency = target != null;
        if (!emergency) {
            if (phases.size() == 1) {
                System.out.println("Signal remains in the configured phase: " + now + " GREEN");
                return;
            }
            target = phases.get((cur + 1) % phases.size());         // circular: after last comes first
        }

        System.out.println(now + " GREEN -> YELLOW -> RED");        // safe transition
        signal.put(now, "RED");
        for (String n : graph.get(target))                          // no conflicting green allowed
            if (!signal.get(n).equals("RED")) { System.out.println("Unsafe transition blocked."); return; }
        cur = phases.indexOf(target);
        signal.put(target, "GREEN");
        System.out.println("Signal: " + target + " GREEN" + (emergency ? " (emergency priority)" : ""));
    }

    static void save() throws IOException {
        List<String> pending = new ArrayList<>();
        for (Req r : emergencies) pending.add(r.road() + ":" + r.pri());
        Files.write(Path.of(FILE), List.of(String.join(",", phases), String.join(",", pending)));
        System.out.println("State saved to " + FILE + ".");
    }
}