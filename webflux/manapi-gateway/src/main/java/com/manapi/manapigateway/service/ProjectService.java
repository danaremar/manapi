package com.manapi.manapigateway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.transaction.annotation.Transactional;

import com.manapi.manapigateway.repository.ProjectRepository;

import reactor.core.publisher.Mono;

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

    @Autowired
    private MongoTemplate mongoTemplate;

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
     * Check if user is allowed to perform an operation with Mono
     * 
     * @param project
     * @param roles   -> list of allowed roles
     * @throws UnauthorizedException
     */
    public Mono<Void> verifyMono(Project project, List<Integer> roles) {
        return userService.getCurrentUser()
                .filter(user -> project != null && project.getActive() && project.getProjectRoles().stream()
                        .anyMatch(x -> roles.contains(x.getRole()) && x.getUserId().equals(user.getId())))
                .switchIfEmpty(Mono.error(new UnauthorizedException()))
                .then();
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
     * Check if user is owner with Mono
     * 
     * @param project
     */
    public Mono<Void> verifyOwnerMono(Project project) {
        return verifyMono(project, List.of(0));
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
     * Check if user is owner or admin with Mono
     * 
     * @param project
     */
    public Mono<Void> verifyOwnerOrAdminMono(Project project) {
        return verifyMono(project, List.of(0, 1));
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
     * Check if user is not visitor with Mono
     * 
     * @param project
     */
    public Mono<Void> verifyMemberMono(Project project) {
        return verifyMono(project, List.of(0, 1, 2));
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
     * Check if it's contained in the project with Mono
     * 
     * @param project
     */
    public Mono<Void> verifyUserRelatedWithProjectMono(Project project) {
        return verifyMono(project, List.of(0, 1, 2, 3));
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
     * Get all projects from user using mono
     * 
     * @return
     */
    public Mono<List<ProjectListDto>> getAllProjectsFromUser() {
        return userService.getCurrentUser().map(this::getAllProjectsFromUser);
    }

    /**
     * Get all projects from user
     * 
     * @param pageable
     * @return
     */
    public List<ProjectListDto> getAllProjectsFromUser(User u) {

        // filter inside projectRoles by userId, accepted & active
        Criteria criteria = Criteria.where("projectRoles.userId").is(u.getId())
                .and("projectRoles.accepted").is(true)
                .and("projectRoles.active").is(true);
        Query query = new Query(criteria);

        // return & map
        List<Project> ls = mongoTemplate.find(query, Project.class);
        return ls.stream()
                .map(p -> modelMapper.map(p, ProjectListDto.class))
                .toList();

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
        ownerProjectRole.setActive(true);
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
