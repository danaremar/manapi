package com.manapi.manapigateway.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Mono;

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

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired(required = true)
    protected ModelMapper modelMapper;

    /**
     * Get project from a given project role id
     * 
     * @param projectRoleId
     * @return
     */
    private Project findProjectFromProjectRoleId(String projectRoleId) {
        Criteria criteria = Criteria.where("projectRoles.id").is(projectRoleId)
                .and("projectRoles.accepted").is(true)
                .and("projectRoles.active").is(true);

        Query query = new Query(criteria);
        return mongoTemplate.findOne(query, Project.class);
    }

    /**
     * Get project from a given project role id
     * 
     * @param projectRoleId
     * @return
     */
    public List<Project> findProjectsByProjectUserId(String userId) {
        Criteria criteria = Criteria.where("projectRoles.userId").is(userId)
                .and("projectRoles.accepted").is(true)
                .and("projectRoles.active").is(true);

        Query query = new Query(criteria);
        return mongoTemplate.find(query, Project.class);
    }

    /**
     * Get project role mono by a given project role id
     * 
     * @param projectRoleId
     * @return
     */
    private Mono<Project> getProjectMonoByProjectRoleId(String projectRoleId) {
        return Mono.fromCallable(() -> findProjectFromProjectRoleId(projectRoleId))
                .switchIfEmpty(Mono.error(new UnauthorizedException()));
    }

    /**
     * Create role
     * 
     * @param projectRoleCreateDto
     */
    @Transactional
    public Mono<Void> createRole(ProjectRoleCreateDto projectRoleCreateDto) {
        return projectService.getProjectMonoByProjectId(projectRoleCreateDto.getProjectId())
                .flatMap(p -> projectService.verifyOwnerOrAdminMono(p).thenReturn(p))
                .doOnNext(p -> {
                    // create project role
                    ProjectRole projectRole = new ProjectRole();
                    projectRole.setId(UUID.randomUUID().toString());
                    projectRole.setCreationDate(new Date());
                    projectRole.setModificationDate(new Date());
                    projectRole.setAccepted(false);
                    projectRole.setActive(true);
                    projectRole.setUserId(projectRoleCreateDto.getUserId());
                    projectRole.setRole(projectRoleCreateDto.getRole());

                    // add project role to project
                    p.getProjectRoles().add(projectRole);

                    // save project
                    projectRepository.save(p);
                })
                .then();
    }

    /**
     * Update role
     * 
     * @param projectRoleUpdateDto
     * @param projectRoleId 
     */
    @Transactional
    public Mono<Void> updateRole(ProjectRoleUpdateDto projectRoleUpdateDto, String projectRoleId) {
        return getProjectMonoByProjectRoleId(projectRoleId)
                .flatMap(p -> projectService.verifyOwnerOrAdminMono(p).thenReturn(p))
                .doOnNext(p -> {
                    // update project role
                    ProjectRole projectRole = p.getProjectRoles().stream()
                            .filter(x -> x.getId() != null && x.getId().equals(projectRoleId))
                            .findFirst()
                            .orElse(new ProjectRole());
                    projectRole.setModificationDate(new Date());
                    projectRole.setRole(projectRoleUpdateDto.getRole());

                    // delete project role from project
                    p.getProjectRoles().removeIf(x -> x.getId().equals(projectRoleId));

                    // add project role to project
                    p.getProjectRoles().add(projectRole);

                    // save project
                    projectRepository.save(p);
                })
                .then();
    }

    /**
     * Accept project invitation
     * 
     * @param projectRoleId
     */
    @Transactional
    public Mono<Void> acceptProjectRole(String projectRoleId) {
        return getProjectMonoByProjectRoleId(projectRoleId)
                .flatMap(p -> projectService.verifyUserRelatedWithProjectMono(p).thenReturn(p))
                .doOnNext(p -> {

                    // update project role
                    ProjectRole projectRole = p.getProjectRoles().stream()
                            .filter(x -> x.getId() != null && x.getId().equals(projectRoleId))
                            .findFirst()
                            .orElse(new ProjectRole());
                    projectRole.setModificationDate(new Date());
                    projectRole.setAccepted(true);

                    // delete project role from project
                    p.getProjectRoles().removeIf(x -> x.getId().equals(projectRoleId));

                    // add project role to project
                    p.getProjectRoles().add(projectRole);

                    // save project
                    projectRepository.save(p);
                })
                .then();
    }

    /**
     * Delete project role, used when user denies the invitation
     * 
     * @param projectRoleId
     */
    @Transactional
    public Mono<Void> deleteProjectRole(String projectRoleId) {
        return getProjectMonoByProjectRoleId(projectRoleId)
                .flatMap(p -> projectService.verifyUserRelatedWithProjectMono(p).thenReturn(p))
                .doOnNext(p -> {
                    // delete project role from project
                    p.getProjectRoles().removeIf(x -> x.getId().equals(projectRoleId));

                    // save project
                    projectRepository.save(p);
                })
                .then();
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
    @Transactional
    public Mono<List<ProjectRoleNotAcceptedDto>> getAllInvitationsFromActualUser() {
        return userService.getCurrentUser().map(u -> {

            // get id from user
            String userId = u.getId();

            // get list of projects
            List<Project> ls = findProjectsByProjectUserId(userId);

            return ls.stream()
                    .flatMap(p -> getProjectRolesNotAceptedByParams(p, userId).stream())
                    .toList();
        });
    }

}
