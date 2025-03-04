package com.sadeem.smap.service;

import com.sadeem.smap.dto.MachineDto;
import com.sadeem.smap.model.Machine;
import com.sadeem.smap.repository.*;
import com.sadeem.smap.util.SMAPUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MachineService {

    @Autowired
    private MachineRepository machineRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private RawMaterialRepository rawMaterialRepository;

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private AccessoriesRepository accessoriesRepository;

    public Iterable<MachineDto> getAllMachines() {
        return machineRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Page<MachineDto> getMachinesPaginated(Pageable pageable) {
        return machineRepository.findAll(pageable).map(this::convertToDto);
    }

    public Optional<MachineDto> getMachineById(Long id) {
        return machineRepository.findById(id).map(this::convertToDto);
    }

    public void createMachine(MachineDto machineDto) {
        Machine machine = convertToEntity(machineDto);
        machineRepository.save(machine);
    }

    public void updateMachine(MachineDto machineDto) {
        Optional<Machine> optionalMachine = machineRepository.findById(machineDto.getId());
        if (optionalMachine.isPresent()) {
            Machine machine = optionalMachine.get();
            machine.setDepartment(departmentRepository.findById(machineDto.getDepartmentId()).orElse(null));
            machine.setUnit(unitRepository.findById(machineDto.getPowerMeasurementId()).orElse(null));
            machine.setMachineName(machineDto.getName());
            machine.setMachineDescription(machineDto.getDescription());
            machine.setSerialNumber(machineDto.getSerial());
            machine.setEnergyConsumption(machineDto.getPowerQuantity());
            machine.setMachineCode(machineDto.getMachineCode());

            Set<RawMaterial> rawMaterials = SMAPUtil.mapIdsToEntities(
                    machineDto.getRawMaterialIds(), rawMaterialRepository::findById);
            machine.setMachineRawMaterials(rawMaterials.stream()
                    .map(rawMaterial -> new MachineRawMaterial(rawMaterial, machine))
                    .collect(Collectors.toSet()));

            Set<Worker> workers = SMAPUtil.mapIdsToEntities(
                    machineDto.getWorkerIds(), workerRepository::findById);
            machine.setWorkerMachines(workers.stream()
                    .map(worker -> new WorkerMachine(worker, machine))
                    .collect(Collectors.toSet()));

            Set<Accessories> accessories = SMAPUtil.mapIdsToEntities(
                    machineDto.getAccessoriesIds(), accessoriesRepository::findById);
            machine.setMachineAccessorieses(accessories.stream()
                    .map(accessory -> new MachineAccessories(accessory, machine))
                    .collect(Collectors.toSet()));

            machineRepository.save(machine);
        }
    }

    public void deleteMachine(Long id) {
        Machine machine = machineRepository.findById(id).orElseThrow();
        machine.setIsDeleted(true);
        machineRepository.save(machine);
    }

    private MachineDto convertToDto(Machine machine) {
        MachineDto dto = new MachineDto();
        dto.setId(machine.getId());
        dto.setName(machine.getMachineName());
        dto.setDescription(machine.getMachineDescription());
        dto.setSerial(machine.getSerialNumber());
        dto.setDepartmentId(machine.getDepartment().getId());
        dto.setPowerMeasurementId(machine.getUnit().getId());
        dto.setMachineCode(machine.getMachineCode());
        dto.setPowerQuantity(machine.getEnergyConsumption());
        dto.setRawMaterialIds(machine.getMachineRawMaterials().stream()
                .map(mrm -> mrm.getRawMaterial().getId())
                .collect(Collectors.toSet()));
        dto.setWorkerIds(machine.getWorkerMachines().stream()
                .map(wm -> wm.getWorker().getId())
                .collect(Collectors.toSet()));
        dto.setAccessoriesIds(machine.getMachineAccessorieses().stream()
                .map(mac -> mac.getAccessories().getId())
                .collect(Collectors.toSet()));
        return dto;
    }

    private Machine convertToEntity(MachineDto machineDto) {
        Machine machine = new Machine();
        machine.setId(machineDto.getId());
        machine.setDepartment(departmentRepository.findById(machineDto.getDepartmentId()).orElse(null));
        machine.setUnit(unitRepository.findById(machineDto.getPowerMeasurementId()).orElse(null));
        machine.setMachineName(machineDto.getName());
        machine.setMachineDescription(machineDto.getDescription());
        machine.setSerialNumber(machineDto.getSerial());
        machine.setEnergyConsumption(machineDto.getPowerQuantity());
        machine.setMachineCode(machineDto.getMachineCode());
        machine.setMachineRawMaterials(machineDto.getRawMaterialIds().stream()
                .map(rawMaterialRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(rawMaterial -> new MachineRawMaterial(rawMaterial, machine))
                .collect(Collectors.toSet()));
        machine.setWorkerMachines(machineDto.getWorkerIds().stream()
                .map(workerRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(worker -> new WorkerMachine(worker, machine))
                .collect(Collectors.toSet()));
        machine.setMachineAccessorieses(machineDto.getAccessoriesIds().stream()
                .map(accessoriesRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(accessory -> new MachineAccessories(accessory, machine))
                .collect(Collectors.toSet()));
        return machine;
    }
}