
import java.util.*;

/**
 * TODO: Implement the non-preemptive SJF (Shortest-Job First) scheduling algorithm.
 */

public class SJF extends Algorithm {

    public SJF(List<Process> allProcessList){
        super(allProcessList);
    }

    @Override
    public void schedule() {
        // processes that haven’t arrived yet
        Queue<Process> toArrive = new LinkedList<>(allProcesses);

        // ready queue sorted by shortest burst time
        PriorityQueue<Process> ready = new PriorityQueue<>(
                Comparator.comparingInt(Process::getBurstTime));

        int now = 0;

        while (!toArrive.isEmpty() || !ready.isEmpty()) {
            // move any processes that have arrived by "now" into the ready queue
            while (!toArrive.isEmpty() && toArrive.peek().getArrivalTime() <= now) {
                ready.add(toArrive.remove());
            }

            // if no process is ready, jump time to the next arrival
            if (ready.isEmpty()) {
                now = toArrive.peek().getArrivalTime();
                continue;
            }

            // pick the shortest job
            Process cur = ready.remove();
            int run = cur.getBurstTime();

            System.out.print("At time " + now + ": ");
            CPU.run(cur, run);

            now += run;

            cur.setRemainingTime(0);
            cur.setFinishTime(now);
        }
    }
}
