import java.util.*;

/*
 **
 * TODO: implement the SRTF (Shortest Remaining Time First) scheduling algorithm.
 *
 * SRTF is also known as preemptive SJF
 */


public class SRTF extends Algorithm {

    public SRTF(List<Process> allProcesses){
        super(allProcesses);
    }

    @Override
    public void schedule(){
        // Processes yet to arrive (input is already in arrival order)
        Queue<Process> toArrive = new LinkedList<>(allProcesses);

        // Ready set ordered by SHORTEST REMAINING TIME
        PriorityQueue<Process> ready = new PriorityQueue<>(
                Comparator.comparingInt(Process::getRemainingTime)
        );

        int now = 0;

        // Initialize remaining times
        for (Process p : allProcesses) {
            p.setRemainingTime(p.getBurstTime());
        }

        while (!toArrive.isEmpty() || !ready.isEmpty()) {
            // Admit arrivals up to 'now'
            while (!toArrive.isEmpty() && toArrive.peek().getArrivalTime() <= now) {
                ready.add(toArrive.remove());
            }

            // If nothing is ready, jump to next arrival
            if (ready.isEmpty()) {
                now = toArrive.peek().getArrivalTime();
                continue;
            }

            // Run the job with the smallest remaining time for 1 unit (preemptive)
            Process cur = ready.poll();
            CPU.run(cur, 1);
            now += 1;
            cur.setRemainingTime(cur.getRemainingTime() - 1);

            // Admit any processes that arrived exactly at this time
            while (!toArrive.isEmpty() && toArrive.peek().getArrivalTime() <= now) {
                ready.add(toArrive.remove());
            }

            if (cur.getRemainingTime() == 0) {
                cur.setFinishTime(now);    // done
            } else {
                ready.add(cur);
            }
        }
    }
}





