package com.manapi.manapiproject.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.manapi.manapiproject.model.sprint.SprintCreateDto;
import com.manapi.manapiproject.model.sprint.SprintShowDto;
import com.manapi.manapiproject.model.util.Message;
import com.manapi.manapiproject.service.SprintService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Sprints")
@RequestMapping("/project/{projectId}/projects/sprint")
public class SprintController {

    @Autowired
    private SprintService sprintService;

    @PreAuthorize("hasAuthority('OWNER') or hasAuthority('ADMIN') or hasAuthority('MEMBER') or hasAuthority('VISITOR')")
    @GetMapping(value = "/list")
    public ResponseEntity<Object> getSprintsByProjectId(@PathVariable String projectId) {
        try {
            List<SprintShowDto> ls = sprintService.findSprintsByProjectId(projectId);
            return new ResponseEntity<>(ls, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.CONFLICT);
        }
    }

    @PreAuthorize("hasAuthority('OWNER') or hasAuthority('ADMIN')")
    @PostMapping()
    public ResponseEntity<Object> createSprint(@RequestBody @Valid SprintCreateDto sprintCreateDto, @PathVariable String projectId) {
        try {
            SprintShowDto s = sprintService.createSprint(sprintCreateDto, projectId);
            return new ResponseEntity<>(s, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.CONFLICT);
        }
    }

    @PreAuthorize("hasAuthority('OWNER') && hasAuthority('ADMIN')")
    @PutMapping(value = "/{sprintId}")
    public ResponseEntity<Object> updateSprint(@RequestBody @Valid SprintCreateDto sprintUpdateDto, @PathVariable String sprintId) {
        try {
            SprintShowDto s = sprintService.updateSprint(sprintUpdateDto, sprintId);
            return new ResponseEntity<>(s, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.CONFLICT);
        }
    }

    @PreAuthorize("hasAuthority('OWNER') && hasAuthority('ADMIN')")
    @DeleteMapping(value = "/{sprintId}")
    public ResponseEntity<Object> deleteSprint(@PathVariable String sprintId) {
        try {
            sprintService.deleteSprint(sprintId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.CONFLICT);
        }
    }
    
}
