package com.manapi.manapigateway.service;

import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.manapi.manapigateway.dto.project_role.ProjectRoleCreateDto;
import com.manapi.manapigateway.dto.project_role.ProjectRoleNotAcceptedDto;
import com.manapi.manapigateway.dto.project_role.ProjectRoleUpdateDto;
import com.manapi.manapigateway.exception.UnauthorizedException;
import com.manapi.manapigateway.model.project.Project;
import com.manapi.manapigateway.model.project.ProjectRole;
import com.manapi.manapigateway.repository.ProjectRepository;

@Service
public class ProjectRoleService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @Autowired(required = true)
    protected ModelMapper modelMapper;

    /**
     * Get project from a given project role id
     * 
     * @param projectRoleId
     * @return
     * @throws UnauthorizedException
     */
    private Project findProjectFromProjectRoleId(String projectRoleId) throws UnauthorizedException {
        ProjectRole projectRoleExample = new ProjectRole();
        projectRoleExample.setId(projectRoleId);

        Project projectExample = new Project();
        projectExample.setProjectRoles(List.of(projectRoleExample));
        Example<Project> example = Example.of(projectExample);
        return projectRepository.findOne(example).orElseThrow(() -> new UnauthorizedException());
    }

    /**
     * Create role
     * 
     * @param projectRoleCreateDto
     * @throws UnauthorizedException
     */
    @Transactional
    public void createRole(ProjectRoleCreateDto projectRoleCreateDto) throws UnauthorizedException {

        // get project & verify permissions
        Project project = projectService.findProjectById(projectRoleCreateDto.getProjectId());
        projectService.verifyOwnerOrAdmin(project);

        // create project role
        ProjectRole projectRole = new ProjectRole();
        projectRole.setCreationDate(new Date());
        projectRole.setModificationDate(new Date());
        projectRole.setAccepted(false);
        projectRole.setActive(true);
        projectRole.setUserId(projectRoleCreateDto.getUserId());
        projectRole.setRole(projectRoleCreateDto.getRole());

        // add project role to project
        project.getProjectRoles().add(projectRole);

        // save project
        projectRepository.save(project);

    }

    /**
     * Update role
     * 
     * @param projectRoleUpdateDto
     * @throws UnauthorizedException
     */
    @Transactional
    public void updateRole(ProjectRoleUpdateDto projectRoleUpdateDto) throws UnauthorizedException {

        // get project & verify permissions
        Project project = findProjectFromProjectRoleId(projectRoleUpdateDto.getId());
        projectService.verifyOwnerOrAdmin(project);

        // delete project role from project
        project.getProjectRoles().removeIf(x -> x.getId().equals(projectRoleUpdateDto.getId()));

        // update project role
        ProjectRole projectRole = new ProjectRole();
        projectRole.setModificationDate(new Date());
        projectRole.setRole(projectRoleUpdateDto.getRole());

        // add project role to project
        project.getProjectRoles().add(projectRole);

        // save project
        projectRepository.save(project);

    }

    /**
     * Accept project invitation
     * 
     * @param projectRoleId
     * @throws UnauthorizedException
     */
    @Transactional
    public void acceptProjectRole(String projectRoleId) throws UnauthorizedException {

        // get project & verify permissions
        Project project = findProjectFromProjectRoleId(projectRoleId);
        projectService.verifyOwnerOrAdmin(project);

        // delete project role from project
        project.getProjectRoles().removeIf(x -> x.getId().equals(projectRoleId));

        // update project role
        ProjectRole projectRole = new ProjectRole();
        projectRole.setModificationDate(new Date());
        projectRole.setAccepted(true);

        // add project role to project
        project.getProjectRoles().add(projectRole);

        // save project
        projectRepository.save(project);

    }

    /**
     * Delete project role, used when user denies the invitation
     * 
     * @param projectRoleId
     * @throws UnauthorizedException
     */
    @Transactional
    public void deleteProjectRole(String projectRoleId) throws UnauthorizedException {

        // get project & verify permissions
        Project project = findProjectFromProjectRoleId(projectRoleId);
        projectService.verifyOwnerOrAdmin(project);

        // delete project role from project
        project.getProjectRoles().removeIf(x -> x.getId().equals(projectRoleId));

        // save project
        projectRepository.save(project);

    }

    /**
     * Get all projects role that are not accepted by user
     * 
     * @param project
     * @param userId
     * @return
     */
    private List<ProjectRoleNotAcceptedDto> getProjectRolesNotAceptedByParams(Project project, String userId) {
        return project.getProjectRoles().stream()
                .filter(p -> p.getUserId().equals(userId))
                .map(p -> new ProjectRoleNotAcceptedDto(p.getId(), p.getRole(), project.getName()))
                .toList();
    }

    /**
     * Get all invitations that actual users hasn't accepted yet
     * 
     * @return
     */
    public List<ProjectRoleNotAcceptedDto> getAllInvitationsFromActualUser() {

        String userId = userService.getCurrentUserMvc().getId();

        // example matcher
        ProjectRole projectRoleExample = new ProjectRole();
        projectRoleExample.setUserId(userId);
        Project projectExample = new Project();
        projectExample.setProjectRoles(List.of(projectRoleExample));
        Example<Project> example = Example.of(projectExample);

        // get list of projects
        List<Project> ls = projectRepository.findAll(example);
        
        return ls.stream()
            .flatMap(p -> getProjectRolesNotAceptedByParams(p,userId).stream())
            .toList();
    }

}
