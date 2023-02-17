package com.manapi.manapigateway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.manapi.manapigateway.repository.ProjectRepository;
import com.manapi.manapigateway.model.user.User;
import com.manapi.manapigateway.model.project.ProjectRole;
import com.manapi.manapigateway.dto.project.ProjectCreateDto;
import com.manapi.manapigateway.dto.project.ProjectListDto;
import com.manapi.manapigateway.dto.project.ProjectShowDto;
import com.manapi.manapigateway.exception.UnauthorizedException;
import com.manapi.manapigateway.model.project.Project;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserService userService;

    @Autowired(required = true)
    protected ModelMapper modelMapper;

    /**
     * Check if user is allowed to perform an operation
     * 
     * @param project
     * @param roles   -> list of allowed roles
     * @throws UnauthorizedException
     */
    private void verify(Project project, List<Integer> roles) throws UnauthorizedException {
        User user = userService.getCurrentUserMvc();
        if (project == null || user == null || !project.getActive() || project.getProjectRoles().stream()
                .noneMatch(x -> roles.contains(x.getRole()) && x.getUserId().equals(user.getId()))) {
            throw new UnauthorizedException();
        }
    }

    /**
     * Check if user is owner
     * 
     * @param project
     * @throws UnauthorizedException
     */
    public void verifyOwner(Project project) throws UnauthorizedException {
        verify(project, List.of(0));
    }

    /**
     * Check if user is owner or admin
     * 
     * @param project
     * @throws UnauthorizedException
     */
    public void verifyOwnerOrAdmin(Project project) throws UnauthorizedException {
        verify(project, List.of(0, 1));
    }

    /**
     * Check if user is not visitor
     * 
     * @param project
     * @throws UnauthorizedException
     */
    public void verifyMember(Project project) throws UnauthorizedException {
        verify(project, List.of(0, 1, 2));
    }

    /**
     * Check if it's contained in the project
     * 
     * @param project
     * @throws UnauthorizedException
     */
    public void verifyUserRelatedWithProject(Project project) throws UnauthorizedException {
        verify(project, List.of(0, 1, 2, 3));
    }

    /**
     * Find active project by id
     * 
     * @param id
     * @return
     */
    public Project findProjectById(String id) {
        Project project = new Project();
        project.setId(id);
        project.setActive(true);
        Example<Project> example = Example.of(project);
        return projectRepository.findOne(example).orElse(null);
    }

    /**
     * Get project from id, checks permissions
     * 
     * @param projectId
     * @return
     * @throws UnauthorizedException
     */
    public ProjectShowDto getProject(String projectId) throws UnauthorizedException {
        Project project = findProjectById(projectId);
        verifyUserRelatedWithProject(project);
        return modelMapper.map(project, ProjectShowDto.class);
    }

    /**
     * Get all projects from user
     * 
     * @param pageable
     * @return
     */
    public Page<ProjectListDto> getAllProjectsFromUser(Pageable pageable) {

        // example
        ProjectRole projectRoleExample = new ProjectRole();
        projectRoleExample.setUserId(userService.getCurrentUserMvc().getId());
        projectRoleExample.setActive(true);
        projectRoleExample.setAccepted(true);
        Project projectExample = new Project();
        projectExample.setProjectRoles(List.of(projectRoleExample));

        // matcher & example
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Project> example = Example.of(projectExample, matcher);

        // find
        Page<Project> projectPage = projectRepository.findAll(example, pageable);
        return projectPage.map(p -> modelMapper.map(p, ProjectListDto.class));

    }

    /**
     * Create project
     * 
     * @param projectCreateDto
     * @return
     */
    @Transactional
    public ProjectShowDto createProject(ProjectCreateDto projectCreateDto) {

        // create project properties
        Project project = modelMapper.map(projectCreateDto, Project.class);
        project.setCreationDate(new Date());
        project.setActive(true);
        project.setModificationDate(new Date());

        // create owner projectRole
        ProjectRole ownerProjectRole = new ProjectRole();
        ownerProjectRole.setCreationDate(new Date());
        ownerProjectRole.setAccepted(true);
        ownerProjectRole.setRole(0);
        ownerProjectRole.setUserId(userService.getCurrentUserMvc().getId());
        ownerProjectRole.setModificationDate(new Date());

        // add owner to project
        project.setProjectRoles(List.of(ownerProjectRole));

        // save & return project
        Project projectSaved = projectRepository.save(project);
        return modelMapper.map(projectSaved, ProjectShowDto.class);
    }

    /**
     * Update project
     * 
     * @param projectCreateDto
     * @return
     * @throws UnauthorizedException
     */
    public ProjectShowDto updateProject(ProjectCreateDto projectCreateDto, String projectId)
            throws UnauthorizedException {

        // get project & set new properties
        Project project = findProjectById(projectId);
        project.setName(projectCreateDto.getName());
        project.setDescription(projectCreateDto.getDescription());

        // check permissions
        verifyOwnerOrAdmin(project);

        // save & return project
        Project projectSaved = projectRepository.save(project);
        return modelMapper.map(projectSaved, ProjectShowDto.class);

    }

    /**
     * Disable project, not to delete
     * 
     * @param projectId
     * @throws UnauthorizedException
     */
    @Transactional
    public void disableProject(String projectId) throws UnauthorizedException {
        
        // get previous project
        Project project = findProjectById(projectId);

        // permissions
        verifyOwnerOrAdmin(project);

        // update
        project.setDeleteDate(new Date());
        project.setActive(false);
        project.setModificationDate(new Date());

        // save
        projectRepository.save(project);
    }

}
