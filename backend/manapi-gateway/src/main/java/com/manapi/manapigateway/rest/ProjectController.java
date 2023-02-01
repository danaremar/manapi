package com.manapi.manapigateway.rest;

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

import com.manapi.manapigateway.model.projects.Project;
import com.manapi.manapigateway.model.projects.ProjectCreateDto;
import com.manapi.manapigateway.model.projects.ProjectRoleCreateDto;
import com.manapi.manapigateway.model.projects.ProjectRoleNotAcceptedDto;
import com.manapi.manapigateway.model.projects.ProjectRoleUpdateDto;
import com.manapi.manapigateway.model.projects.ProjectUpdateDto;
import com.manapi.manapigateway.model.util.Message;
import com.manapi.manapigateway.service.ProjectService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/project")
@Tag(name = "Project")
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin
public class ProjectController {

	@Autowired
	ProjectService projectService;

	@PostMapping
	public ResponseEntity<Object> createProject(@RequestBody @Valid ProjectCreateDto projectCreateDto) {
		try {
			projectService.createProject(projectCreateDto);
			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.CONFLICT);
		}
	}

	@PutMapping
	public ResponseEntity<Object> updateProject(@RequestBody @Valid ProjectUpdateDto projectUpdateDto) {
		try {
			projectService.update(projectUpdateDto);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.CONFLICT);
		}
	}

	@PutMapping(value = "/enable-disable/{projectId}")
	public ResponseEntity<Object> enableDisableProject(@PathVariable Long projectId) {
		try {
			projectService.enableOrDisableById(projectId);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.CONFLICT);
		}
	}

	@GetMapping(value = "/my-projects")
	public ResponseEntity<Object> getAllUserProjects() {
		try {
			List<Project> projects = projectService.findAllMyProjects();
			return new ResponseEntity<>(projects, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.CONFLICT);
		}
	}

	@PostMapping(value = "/role")
	public ResponseEntity<Object> createRoleProject(@RequestBody @Valid ProjectRoleCreateDto projectRoleCreateDto) {
		try {
			projectService.createRole(projectRoleCreateDto);
			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.CONFLICT);
		}
	}

	@PutMapping(value = "/role")
	public ResponseEntity<Object> updateRoleProject(@RequestBody @Valid ProjectRoleUpdateDto projectRoleUpdateDto) {
		try {
			projectService.updateRole(projectRoleUpdateDto);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.CONFLICT);
		}
	}

	@PutMapping(value = "/role/{roleId}/accept")
	public ResponseEntity<Object> acceptRoleProject(@PathVariable Long roleId) {
		try {
			projectService.acceptProjectRole(roleId);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.CONFLICT);
		}
	}

	@DeleteMapping(value = "/role/{roleId}/decline")
	public ResponseEntity<Object> deleteRoleProject(@PathVariable Long roleId) {
		try {
			projectService.deleteProjectRole(roleId);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.CONFLICT);
		}
	}

	@GetMapping(value = "/my-roles")
	public ResponseEntity<Object> getAllMyInvitations() {
		try {
			List<ProjectRoleNotAcceptedDto> allInvitations = projectService.getAllMyInvitations();
			return new ResponseEntity<>(allInvitations, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.CONFLICT);
		}
	}

}
