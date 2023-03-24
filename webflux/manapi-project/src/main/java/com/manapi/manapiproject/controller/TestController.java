package com.manapi.manapiproject.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/project")
@CrossOrigin
public class TestController {

    @PreAuthorize("hasAuthority('OWNER')")
    @PostMapping(value = "/{projectId}/projects/testOwner")
    public ResponseEntity<Object> getSprintOwnerTest(@PathVariable String projectId) {
        return new ResponseEntity<>("OK OWNER", HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('OWNER') or hasAuthority('ADMIN')")
    @PostMapping(value = "/{projectId}/projects/testAdmin")
    public ResponseEntity<Object> getSprintAdminTest(@PathVariable String projectId) {
        return new ResponseEntity<>("OK ADMIN", HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('OWNER') or hasAuthority('ADMIN') or hasAuthority('MEMBER')")
    @PostMapping(value = "/{projectId}/projects/testMember")
    public ResponseEntity<Object> getSprintAdminMember(@PathVariable String projectId) {
        return new ResponseEntity<>("Todo ok", HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('OWNER') or hasAuthority('ADMIN') or hasAuthority('MEMBER') or hasAuthority('VISITOR')")
    @PostMapping(value = "/{projectId}/projects/testVisitor")
    public ResponseEntity<Object> getSprintAdminVisitor(@PathVariable String projectId) {
        return new ResponseEntity<>("Todo ok", HttpStatus.OK);
    }

}
