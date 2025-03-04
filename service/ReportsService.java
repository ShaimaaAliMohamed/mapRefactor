package com.sadeem.smap.service;

import com.sadeem.smap.dto.*;
import com.sadeem.smap.model.*;
import com.sadeem.smap.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportsService {

    @Autowired
    private BatchRepository batchRepository;

    @Autowired
    private ProcessRepository processRepository;

    @Autowired
    private TaskCreateRepository taskCreateRepository;

    @Autowired
    private TaskRunTimeRepository taskRunTimeRepository;

    @Autowired
    private TaskWorkerRepository taskWorkerRepository;

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private MachinePowerRepository machinePowerRepository;

    /**
     * Generate department process report.
     */
    public List<DepartmentTasksSheet> getDepartmentProcess(Integer departmentId) {
        List<Process> processes = processRepository.findByDepartmentId(departmentId);
        Map<Integer, DepartmentTasksSheet> processMap = new HashMap<>();

        for (Process process : processes) {
            int completeQuantity = 0;
            int wastedQuantity = 0;
            int totalQuantity = process.getOrder().getOrderQuantity() * process.getProductPart().getQuantity();

            List<Batch> batches = batchRepository.findByProcessId(process.getProcessId(), "completed");
            for (Batch batch : batches) {
                int errorSize = batch.getBatchRunTimeErrors().stream()
                        .mapToInt(BatchRunTimeError::getErrorSize)
                        .sum();
                completeQuantity += batch.getBatchSize() - errorSize;
                wastedQuantity += errorSize;
            }

            DepartmentTasksSheet sheet = processMap.computeIfAbsent(process.getProcessId(), id -> new DepartmentTasksSheet());
            sheet.setProcessName(process.getDescription());
            sheet.setCompletedQuantity(completeQuantity);
            sheet.setWastedQuantity(wastedQuantity);
            sheet.setTotalQuantity(totalQuantity);
        }

        return new ArrayList<>(processMap.values());
    }

    /**
     * Generate order profile report.
     */
    public OrderProfile getOrderProfile(Integer orderId) {
        List<Process> processes = processRepository.findByOrderId(orderId);
        if (processes.isEmpty()) return null;

        Order order = processes.get(0).getOrder();
        Product product = order.getProduct();
        Integer orderQuantity = order.getOrderQuantity();
        Date orderCreated = order.getOrderCreated();

        Map<Integer, EntityGeneric> rawMaterialMap = new HashMap<>();
        for (Process process : processes) {
            RawMaterial rawMaterial = process.getProductPart().getRawMaterial();
            double totalPartRawMaterial = process.getProductPart().getPart().getDimension().calculateVolume()
                    * process.getProductPart().getQuantity();

            int completeQuantity = 0;
            int wastedQuantity = 0;
            List<Batch> batches = batchRepository.findByProcessId(process.getProcessId(), "completed");
            for (Batch batch : batches) {
                int errorSize = batch.getBatchRunTimeErrors().stream()
                        .mapToInt(BatchRunTimeError::getErrorSize)
                        .sum();
                completeQuantity += batch.getBatchSize() - errorSize;
                wastedQuantity += errorSize;
            }

            EntityGeneric entity = rawMaterialMap.computeIfAbsent(rawMaterial.getRawMaterialId(), id -> new EntityGeneric());
            entity.setName(rawMaterial.getName());
            entity.setCompletedQuantity(entity.getCompletedQuantity() + (completeQuantity * totalPartRawMaterial));
            entity.setUncompletedQuantity(entity.getUncompletedQuantity() + ((orderQuantity - completeQuantity) * totalPartRawMaterial));
            entity.setWastedQuantity(entity.getWastedQuantity() + (wastedQuantity * totalPartRawMaterial));
        }

        return new OrderProfile(orderCreated, orderQuantity, product, rawMaterialMap.values());
    }

    /**
     * Generate worker performance report.
     */
    public List<WorkerPerformanceDto> getWorkerPerformance(LocalDate startDate, LocalDate endDate) {
        List<TaskWorker> taskWorkers = taskWorkerRepository.findTasksByWorkerDueToDate(startDate, endDate);
        Map<Integer, WorkerPerformanceDto> workerMap = new HashMap<>();

        for (TaskWorker taskWorker : taskWorkers) {
            TaskRunTime taskRunTime = taskWorker.getTaskRunTime();
            int workerId = taskWorker.getWorker().getWorkerId();

            long totalTime = 0L;
            if (taskWorker.getStartTime() != null && taskWorker.getEndTime() != null) {
                totalTime = taskWorker.getEndTime().getTime() - taskWorker.getStartTime().getTime();
            }

            int batchSize = taskRunTime.getBatch().getBatchSize();
            int failureQuantity = taskRunTime.getBatchRunTimeErrors().stream()
                    .mapToInt(BatchRunTimeError::getErrorSize)
                    .sum();
            int successQuantity = batchSize - failureQuantity;

            WorkerPerformanceDto dto = workerMap.computeIfAbsent(workerId, id -> new WorkerPerformanceDto());
            dto.setName(taskWorker.getWorker().getEmployee().getName());
            dto.setDepartment(taskWorker.getWorker().getEmployee().getDepartment().getDepartmentName());
            dto.setAverageTime(dto.getAverageTime() + totalTime);
            dto.setSuccessQuantity(dto.getSuccessQuantity() + successQuantity);
            dto.setFailureQuantity(dto.getFailureQuantity() + failureQuantity);
            dto.setTotalQuantity(dto.getTotalQuantity() + batchSize);
        }

        // Calculate average time and performance rate
        workerMap.values().forEach(dto -> {
            dto.setAverageTime(SMAPUtil.round(dto.getAverageTime() / (60 * 60 * 1000), 2));
            dto.setPerformanceRate(SMAPUtil.round(
                    dto.getAverageTime() / ((dto.getAverageTime() / dto.getTotalQuantity()) * dto.getSuccessQuantity()), 2));
        });

        return new ArrayList<>(workerMap.values());
    }

    /**
     * Get machine power data.
     */
    public Object getMachinePower(String machineId, String start) {
        Series[] allSeries = new Series[3];
        allSeries[0] = new Series("Volt");
        allSeries[1] = new Series("Amber");
        allSeries[2] = new Series("Power Factor");

        try {
            LocalDate startDate = LocalDate.parse(start);
            List<MachinePower> machinePowers = machinePowerRepository.getByMachineIdInTime(Integer.parseInt(machineId), startDate);

            for (MachinePower power : machinePowers) {
                allSeries[0].getData().add(new Object[]{power.getCreatedAt().toEpochMilli(), power.getVolt()});
                allSeries[1].getData().add(new Object[]{power.getCreatedAt().toEpochMilli(), power.getAmber()});
                allSeries[2].getData().add(new Object[]{power.getCreatedAt().toEpochMilli(), power.getPf()});
            }

            return allSeries;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * Get worker task event failures.
     */
    public List<WorkerTaskEventFailureDto> getWorkerTaskFailures(String workerId, LocalDate startDate, LocalDate endDate) {
        List<TaskWorker> taskWorkers = taskWorkerRepository.findTasksOfWorker(Integer.parseInt(workerId), startDate, endDate);
        List<WorkerTaskEventFailureDto> failures = new ArrayList<>();

        for (TaskWorker taskWorker : taskWorkers) {
            TaskRunTime taskRunTime = taskWorker.getTaskRunTime();
            if (!taskRunTime.getBatchRunTimeErrors().isEmpty()) {
                int failureQuantity = taskRunTime.getBatchRunTimeErrors().stream()
                        .mapToInt(BatchRunTimeError::getErrorSize)
                        .sum();

                for (TaskEvent taskEvent : taskRunTime.getTaskEvents()) {
                    if (taskEvent.getEventId().equals(Constant.TASK_FAILURE)) {
                        WorkerTaskEventFailureDto dto = new WorkerTaskEventFailureDto();
                        dto.setMessage(taskEvent.getReason());
                        dto.setDate(taskEvent.getDate());
                        dto.setQuantity(failureQuantity);
                        dto.setMachine(taskRunTime.getMachine().getMachineName());
                        dto.setOrderNumber(taskRunTime.getBatch().getProcess().getOrder().getOrderName());
                        dto.setTaskName(taskRunTime.getTask().getTaskName());
                        dto.setQualityName(taskRunTime.getUserTaskRunTimes().get(0).getUser().getEmployee().getName());

                        failures.add(dto);
                    }
                }
            }
        }

        return failures;
    }
}