package com.sadeem.smap.service;

import com.sadeem.smap.dto.UserDto;
import com.sadeem.smap.model.User;
import com.sadeem.smap.repository.UserRepository;
import com.sadeem.smap.util.EventResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Iterable<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Page<UserDto> getUsersPaginated(Pageable pageable) {
        return userRepository.findAll(pageable).map(this::convertToDto);
    }

    public Optional<UserDto> getUserById(Long id) {
        return userRepository.findById(id).map(this::convertToDto);
    }

    public EventResult createUser(UserDto userDto) throws IOException {
        User user = convertToEntity(userDto);
        userRepository.save(user);
        return new EventResult("success", "User created successfully");
    }

    public EventResult updateUser(UserDto userDto) throws IOException {
        Optional<User> optionalUser = userRepository.findById(userDto.getId());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setEmployeeName(userDto.getName());
            user.setBirthDate(userDto.getBirthDate());
            user.setEmploymentDate(userDto.getEmploymentDate());
            user.setExperienceYears(userDto.getExperienceYears());
            user.setDepartmentId(userDto.getDepartmentId());
            user.setGroupId(userDto.getGroupId());
            user.setUsername(userDto.getUsername());
            user.setPassword(userDto.getPassword());
            userRepository.save(user);
            return new EventResult("success", "User updated successfully");
        }
        return new EventResult("error", "User not found");
    }

    public EventResult deleteUser(Long id) {
        userRepository.deleteById(id);
        return new EventResult("success", "User deleted successfully");
    }

    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

    public void uploadProfilePicture(Long id, MultipartFile file) throws IOException {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            String filePath = "/opt/images/employees/" + id + ".jpg";
            Path path = Paths.get(filePath);
            Files.write(path, file.getBytes());
        }
    }

    private UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getEmployeeName());
        dto.setBirthDate(user.getBirthDate());
        dto.setEmploymentDate(user.getEmploymentDate());
        dto.setExperienceYears(user.getExperienceYears());
        dto.setDepartmentId(user.getDepartmentId());
        dto.setGroupId(user.getGroupId());
        dto.setUsername(user.getUsername());
        dto.setPassword(user.getPassword());
        return dto;
    }

    private User convertToEntity(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setEmployeeName(userDto.getName());
        user.setBirthDate(userDto.getBirthDate());
        user.setEmploymentDate(userDto.getEmploymentDate());
        user.setExperienceYears(userDto.getExperienceYears());
        user.setDepartmentId(userDto.getDepartmentId());
        user.setGroupId(userDto.getGroupId());
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        return user;
    }
}