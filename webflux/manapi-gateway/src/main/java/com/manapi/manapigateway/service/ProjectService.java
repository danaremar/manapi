package com.manapi.manapigateway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.MongoTemplate;
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
     * Check if user is allowed to perform an operation with Mono
     * 
     * @param project
     * @param roles   -> list of allowed roles
     * @throws UnauthorizedException
     */
    public Mono<Void> verifyMono(Project project, List<Integer> roles) {
        return userService.getCurrentUser()
                .filter(user -> (project != null && project.getActive() && project.getProjectRoles().stream()
                        .anyMatch(x -> roles.contains(x.getRole()) && x.getUserId().equals(user.getId()))))
                .switchIfEmpty(Mono.error(new UnauthorizedException()))
                .then();
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
     * Check if user is owner or admin with Mono
     * 
     * @param project
     */
    public Mono<Void> verifyOwnerOrAdminMono(Project project) {
        return verifyMono(project, List.of(0, 1));
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
     * Get project role mono by a given project id
     * 
     * @param projectRoleId
     * @return
     */
    public Mono<Project> getProjectMonoByProjectId(String projectId) {
        return Mono.fromCallable(() -> findProjectById(projectId))
                .switchIfEmpty(Mono.error(new UnauthorizedException()));
    }

    /**
     * Get project from id, checks permissions
     * 
     * @param projectId
     * @return
     * @throws UnauthorizedException
     */
    public Mono<ProjectShowDto> getProjectWithAuth(String projectId) {
        return getProjectMonoByProjectId(projectId)
                .flatMap(project -> verifyUserRelatedWithProjectMono(project).thenReturn(project))
                .map(x -> modelMapper.map(x, ProjectShowDto.class));
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
     * Create project from mono
     * 
     * @param projectCreateDto
     * @return
     */
    @Transactional
    public Mono<ProjectShowDto> createProjectFromMono(ProjectCreateDto projectCreateDto) {
        return userService.getCurrentUser().map(u -> createProject(projectCreateDto, u));
    }

    /**
     * Create project
     * 
     * @param projectCreateDto
     * @return
     */
    public ProjectShowDto createProject(ProjectCreateDto projectCreateDto, User user) {

        // create project properties
        Project project = modelMapper.map(projectCreateDto, Project.class);
        project.setCreationDate(new Date());
        project.setActive(true);
        project.setModificationDate(new Date());

        // create owner projectRole
        ProjectRole ownerProjectRole = new ProjectRole();
        ownerProjectRole.setId(UUID.randomUUID().toString());
        ownerProjectRole.setCreationDate(new Date());
        ownerProjectRole.setAccepted(true);
        ownerProjectRole.setActive(true);
        ownerProjectRole.setRole(0);
        ownerProjectRole.setUserId(user.getId());
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
     */
    public Mono<ProjectShowDto> updateProject(ProjectCreateDto projectUpdateDto, String projectId) {

        return getProjectMonoByProjectId(projectId)
                .flatMap(project -> verifyOwnerMono(project).thenReturn(project))
                .map(p -> {

                    // set new properties
                    p.setName(projectUpdateDto.getName());
                    p.setDescription(projectUpdateDto.getDescription());

                    // set modified date
                    p.setModificationDate(new Date());

                    // save & return
                    Project projectSaved = projectRepository.save(p);
                    return modelMapper.map(projectSaved, ProjectShowDto.class);

                });
    }

    /**
     * Disable project, not to delete
     * 
     * @param projectId
     */
    @Transactional
    public Mono<Void> disableProject(String projectId) {
        return getProjectMonoByProjectId(projectId)
                .flatMap(project -> verifyOwnerMono(project).thenReturn(project))
                .doOnNext(p -> {
                    p.setDeleteDate(new Date());
                    p.setActive(false);
                    p.setModificationDate(new Date());
                    projectRepository.save(p);
                })
                .then();
    }

}
