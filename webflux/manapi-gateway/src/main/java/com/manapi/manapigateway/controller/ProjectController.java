package com.manapi.manapigateway.controller;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.manapi.manapigateway.dto.project.ProjectCreateDto;
import com.manapi.manapigateway.service.ProjectService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/project")
@Tag(name = "Projects")
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin
public class ProjectController {

    @Autowired
    ProjectService projectService;

    @GetMapping(value = "/{projectId}")
    public ResponseEntity<Object> getProjectById(@PathVariable String projectId) {
        var p = projectService.getProjectWithAuth(projectId);
        return new ResponseEntity<>(p, HttpStatus.OK);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<Object> getAllMyProjects() {
        var p = projectService.getAllProjectsFromUser();
        return new ResponseEntity<>(p, HttpStatus.OK);
    }

    @PostMapping(value = "/")
    public ResponseEntity<Object> createProject(@RequestBody @Valid ProjectCreateDto projectCreateDto) {
        var p = projectService.createProjectFromMono(projectCreateDto);
        return new ResponseEntity<>(p, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{projectId}")
    public ResponseEntity<Object> updateProject(@RequestBody @Valid ProjectCreateDto projectCreateDto, @PathVariable String projectId) {
        var p = projectService.updateProject(projectCreateDto, projectId);
        return new ResponseEntity<>(p, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{projectId}")
    public ResponseEntity<Object> disableProject(@PathVariable String projectId) {
        var p = projectService.disableProject(projectId);
        return new ResponseEntity<>(p, HttpStatus.OK);
    }
    
}
