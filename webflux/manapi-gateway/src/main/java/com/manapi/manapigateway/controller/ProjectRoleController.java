package com.manapi.manapigateway.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.manapi.manapigateway.dto.project_role.ProjectRoleCreateDto;
import com.manapi.manapigateway.dto.project_role.ProjectRoleNotAcceptedDto;
import com.manapi.manapigateway.dto.project_role.ProjectRoleUpdateDto;
import com.manapi.manapigateway.exception.UnauthorizedException;
import com.manapi.manapigateway.service.ProjectRoleService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/project-role")
@Tag(name = "Project roles")
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin
public class ProjectRoleController {

    @Autowired
    ProjectRoleService projectRoleService;

    @PostMapping(value = "/")
    public ResponseEntity<Object> createProjectRole(@RequestBody @Valid ProjectRoleCreateDto projectRoleCreateDto) throws UnauthorizedException {
        projectRoleService.createRole(projectRoleCreateDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping(value = "/{projectId}")
    public ResponseEntity<Object> updateProjectRole(@RequestBody @Valid ProjectRoleUpdateDto projectRoleUpdateDto, @PathVariable String projectId) throws UnauthorizedException {
        projectRoleService.updateRole(projectRoleUpdateDto, projectId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/{projectId}/accept")
    public ResponseEntity<Object> acceptProjectRole(@PathVariable String projectId) throws UnauthorizedException {
        projectRoleService.acceptProjectRole(projectId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/{projectId}/decline")
    public ResponseEntity<Object> declineProjectRole(@PathVariable String projectId) throws UnauthorizedException {
        projectRoleService.deleteProjectRole(projectId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/not-accepted")
    public ResponseEntity<Object> getAllMyInvitations() {
        List<ProjectRoleNotAcceptedDto> ls = projectRoleService.getAllInvitationsFromActualUser();
        return new ResponseEntity<>(ls, HttpStatus.OK);
    }
    
    
}
