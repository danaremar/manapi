package com.manapi.manapigateway.controller;

import java.util.List;

import javax.validation.Valid;

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
import com.manapi.manapigateway.dto.project.ProjectListDto;
import com.manapi.manapigateway.dto.project.ProjectShowDto;
import com.manapi.manapigateway.exception.UnauthorizedException;
import com.manapi.manapigateway.service.ProjectService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/project")
@Tag(name = "Projects")
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin
public class ProjectController {

    @Autowired
    ProjectService projectService;

    @GetMapping(value = "/{projectId}")
    public ResponseEntity<Object> getProjectById(@PathVariable String projectId) throws UnauthorizedException {
        ProjectShowDto p = projectService.getProject(projectId);
        return new ResponseEntity<>(p, HttpStatus.OK);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<Object> getAllMyProjects() {
        Mono<List<ProjectListDto>> p = projectService.getAllProjectsFromUser();
        return new ResponseEntity<>(p, HttpStatus.OK);
    }

    @PostMapping(value = "/")
    public ResponseEntity<Object> createProject(@RequestBody @Valid ProjectCreateDto projectCreateDto) {
        ProjectShowDto p = projectService.createProject(projectCreateDto);
        return new ResponseEntity<>(p, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{projectId}")
    public ResponseEntity<Object> updateProject(@RequestBody @Valid ProjectCreateDto projectCreateDto, @PathVariable String projectId) throws UnauthorizedException {
        ProjectShowDto p = projectService.updateProject(projectCreateDto, projectId);
        return new ResponseEntity<>(p, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{projectId}")
    public ResponseEntity<Object> disableProject(@PathVariable String projectId) throws UnauthorizedException {
        projectService.disableProject(projectId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
}
