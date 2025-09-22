import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * TODO: implement the RR (Round Robin) scheduling algorithm
 */
public class RR extends Algorithm{

    private final Queue<Process> readyQueue = new LinkedList<>();
    private final Queue<Process> processesToArrive;

    public RR(List<Process> allProcesses){
        super(allProcesses);
        processesToArrive = new LinkedList<>(allProcesses);
    }

    private int now = 0;
    private int quantum = 10;

    @Override
    public void schedule(){
        System.out.println("Round Robin:");

        while (!readyQueue.isEmpty() || !processesToArrive.isEmpty()){
            if (readyQueue.isEmpty()) {
                Process process = processesToArrive.remove();
                if (now < process.getArrivalTime()) {
                    //advance the simulation clock to the next process's arrival time
                    now = process.getArrivalTime();
                }
                readyQueue.add(process);
            }

            Process currentProcess = readyQueue.remove();
            int quantum = currentProcess.getBurstTime();
            CPU.run(currentProcess, quantum);
            now += quantum;
            currentProcess.setRemainingTime(currentProcess.getRemainingTime() - quantum);

            if (currentProcess.getRemainingTime() > 0) {
                readyQueue.add(currentProcess);
            } else {
                readyQueue.remove(currentProcess);
                currentProcess.setRemainingTime(0);
                currentProcess.setFinishTime(now);
                System.out.println("Process " + currentProcess.getName() + " completed at time " + now);
            }
            while(!processesToArrive.isEmpty() && processesToArrive.peek().getArrivalTime()<=now){
                readyQueue.add(processesToArrive.remove());
            }



        }

    }
}