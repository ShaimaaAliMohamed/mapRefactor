package com.sadeem.smap.service;

import com.sadeem.smap.dto.GroupDto;
import com.sadeem.smap.model.Group;
import com.sadeem.smap.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    public Iterable<GroupDto> getAllGroups() {
        return groupRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Page<GroupDto> getGroupsPaginated(Pageable pageable) {
        return groupRepository.findAll(pageable).map(this::convertToDto);
    }

    public Optional<GroupDto> getGroupById(Long id) {
        return groupRepository.findById(id).map(this::convertToDto);
    }

    public void createGroup(GroupDto groupDto) {
        Group group = convertToEntity(groupDto);
        groupRepository.save(group);
    }

    public void updateGroup(GroupDto groupDto) {
        Optional<Group> optionalGroup = groupRepository.findById(groupDto.getId());
        if (optionalGroup.isPresent()) {
            Group group = optionalGroup.get();
            group.setGroupName(groupDto.getName());
            groupRepository.save(group);
        }
    }

    public void deleteGroup(Long id) {
        groupRepository.deleteById(id);
    }

    private GroupDto convertToDto(Group group) {
        GroupDto dto = new GroupDto();
        dto.setId(group.getId());
        dto.setName(group.getGroupName());
        return dto;
    }

    private Group convertToEntity(GroupDto groupDto) {
        Group group = new Group();
        group.setId(groupDto.getId());
        group.setGroupName(groupDto.getName());
        return group;
    }
}