package com.manapi.manapigateway.controller;

import jakarta.validation.Valid;

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
import com.manapi.manapigateway.dto.project_role.ProjectRoleUpdateDto;
import com.manapi.manapigateway.exception.UnauthorizedException;
import com.manapi.manapigateway.service.ProjectRoleService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/role")
@Tag(name = "Project roles")
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin
public class ProjectRoleController {

    @Autowired
    ProjectRoleService projectRoleService;

    @PostMapping(value = "/")
    public ResponseEntity<Object> createProjectRole(@RequestBody @Valid ProjectRoleCreateDto projectRoleCreateDto) throws UnauthorizedException {
        var p = projectRoleService.createRole(projectRoleCreateDto);
        return new ResponseEntity<>(p, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{projectRoleId}")
    public ResponseEntity<Object> updateProjectRole(@RequestBody @Valid ProjectRoleUpdateDto projectRoleUpdateDto, @PathVariable String projectRoleId) throws UnauthorizedException {
        var p = projectRoleService.updateRole(projectRoleUpdateDto, projectRoleId);
        return new ResponseEntity<>(p, HttpStatus.OK);
    }

    @PutMapping(value = "/{projectRoleId}/accept")
    public ResponseEntity<Object> acceptProjectRole(@PathVariable String projectRoleId) throws UnauthorizedException {
        var p = projectRoleService.acceptProjectRole(projectRoleId);
        return new ResponseEntity<>(p, HttpStatus.OK);
    }

    @PutMapping(value = "/{projectRoleId}/decline")
    public ResponseEntity<Object> declineProjectRole(@PathVariable String projectRoleId) throws UnauthorizedException {
        var p = projectRoleService.deleteProjectRole(projectRoleId);
        return new ResponseEntity<>(p, HttpStatus.OK);
    }

    @GetMapping(value = "/not-accepted")
    public ResponseEntity<Object> getAllMyInvitations() {
        var ls = projectRoleService.getAllInvitationsFromActualUser();
        return new ResponseEntity<>(ls, HttpStatus.OK);
    }
    
    
}
