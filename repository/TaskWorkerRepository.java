package com.sadeem.smap.repository;

import com.sadeem.smap.model.TaskWorker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TaskWorkerRepository extends JpaRepository<TaskWorker, Long> {

    /**
     * Find all task workers assigned to a specific worker within a date range.
     *
     * @param workerId  The ID of the worker.
     * @param startDate The start date of the range.
     * @param endDate   The end date of the range.
     * @return A list of TaskWorker entities.
     */
    @Query("SELECT tw FROM TaskWorker tw " +
           "JOIN FETCH tw.taskRunTime tr " +
           "JOIN FETCH tr.batch b " +
           "WHERE tw.worker.id = :workerId " +
           "AND b.date BETWEEN :startDate AND :endDate")
    List<TaskWorker> findTasksOfWorker(@Param("workerId") Long workerId,
                                      @Param("startDate") LocalDate startDate,
                                      @Param("endDate") LocalDate endDate);

    /**
     * Find all task workers working in a specific department within a date range.
     *
     * @param departmentId The ID of the department.
     * @param startDate     The start date of the range.
     * @param endDate       The end date of the range.
     * @return A list of TaskWorker entities.
     */
    @Query("SELECT tw FROM TaskWorker tw " +
           "JOIN FETCH tw.taskRunTime tr " +
           "JOIN FETCH tr.batch b " +
           "JOIN FETCH tw.worker w " +
           "JOIN FETCH w.employee e " +
           "WHERE e.department.id = :departmentId " +
           "AND b.date BETWEEN :startDate AND :endDate")
    List<TaskWorker> findDepartmentTasksDueToDate(@Param("departmentId") Long departmentId,
                                                 @Param("startDate") LocalDate startDate,
                                                 @Param("endDate") LocalDate endDate);

    /**
     * Find all task workers working on a specific machine within a date range.
     *
     * @param machineId The ID of the machine.
     * @param startDate  The start date of the range.
     * @param endDate    The end date of the range.
     * @return A list of TaskWorker entities.
     */
    @Query("SELECT tw FROM TaskWorker tw " +
           "JOIN FETCH tw.taskRunTime tr " +
           "JOIN FETCH tr.machine m " +
           "JOIN FETCH tr.batch b " +
           "WHERE m.id = :machineId " +
           "AND b.date BETWEEN :startDate AND :endDate")
    List<TaskWorker> findMachineTasksDueToDate(@Param("machineId") Long machineId,
                                              @Param("startDate") LocalDate startDate,
                                              @Param("endDate") LocalDate endDate);

    /**
     * Find all task workers working on a specific order within a date range.
     *
     * @param orderId    The ID of the order.
     * @param startDate  The start date of the range.
     * @param endDate    The end date of the range.
     * @return A list of TaskWorker entities.
     */
    @Query("SELECT tw FROM TaskWorker tw " +
           "JOIN FETCH tw.taskRunTime tr " +
           "JOIN FETCH tr.batch b " +
           "JOIN FETCH b.process p " +
           "WHERE p.order.id = :orderId " +
           "AND b.date BETWEEN :startDate AND :endDate")
    List<TaskWorker> findOrderTasksDueToDate(@Param("orderId") Long orderId,
                                            @Param("startDate") LocalDate startDate,
                                            @Param("endDate") LocalDate endDate);
}