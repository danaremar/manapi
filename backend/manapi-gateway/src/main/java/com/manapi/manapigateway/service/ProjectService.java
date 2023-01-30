package com.manapi.manapigateway.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Example;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.manapi.manapigateway.config.ManapiMessages;
import com.manapi.manapigateway.model.projects.Project;
import com.manapi.manapigateway.model.projects.ProjectRole;
import com.manapi.manapigateway.model.projects.ProjectRoleNotAcceptedDto;
import com.manapi.manapigateway.model.users.User;
import com.manapi.manapigateway.repository.ProjectRepository;
import com.manapi.manapigateway.repository.ProjectRoleRepository;

@Service
public class ProjectService {

    @Autowired
	private ProjectRoleRepository projectRoleRepository;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private UserService userService;
	
	private void verify(Project project, List<Integer> roles) {
		String username = userService.getCurrentUsername();
		if (project==null || StringUtils.isEmpty(username) || !project.getActive() || project.getProjectRoles().stream()
				.noneMatch(x -> (roles.contains(x.getRole())) && (x.getUser().getUsername().equals(username)))) {
			throw new AccessDeniedException(ManapiMessages.USER_NOT_ALLOWED);
		}
	}

	public void verifyOwner(Project project) {
		verify(project, List.of(0));
	}
	
	public void verifyOwnerOrAdmin(Project project) {
		verify(project, List.of(0,1));
	}
	
	public void verifyMember(Project project) {
		verify(project, List.of(0,1,2));
	}
	
	public void verifyUserRelatedWithProject(Project project) {
		verify(project, List.of(0,1,2,3));
	}
	
	public List<User> usersInProject(Project project) {
		return project.getProjectRoles().parallelStream()
				.map(ProjectRole::getUser)
				.collect(Collectors.toList());
	}
	
	public List<String> usernamesInProject(Project project) {
		return usersInProject(project).stream()
				.map(User::getUsername)
				.collect(Collectors.toList());
	}

	@Transactional
	public ProjectRole findProjectRoleById(Long id) {
		return projectRoleRepository.findById(id).orElseThrow();
	}

	@Transactional
	public Project findProjectById(Long id) {
		return projectRepository.findById(id).orElseThrow();
	}

	@Transactional
	public void createProject(Project project) {
		Project projectSaved = projectRepository.save(project);

		ProjectRole projectRole = new ProjectRole();
		projectRole.setRole(0); // OWNER
		projectRole.setAccepted(true);
		projectRole.setUser(userService.getCurrentUser());
		projectRole.setProject(projectSaved);

		projectRoleRepository.save(projectRole);
	}

	@Transactional
	public void update(Project newProject) {
		Project oldProject = findProjectById(newProject.getId());

		verifyOwner(oldProject);

		oldProject.setName(newProject.getName());
		oldProject.setDescription(newProject.getDescription());

		projectRepository.save(oldProject);
	}

	@Transactional
	public void enableOrDisableById(Long projectId) {
		Project project = findProjectById(projectId);
		verifyOwner(project);

		Boolean activeNow = !project.getActive();
		project.setActive(activeNow);
		if (!activeNow) {
			project.setCloseDate(new Date());
		} else {
			project.setCloseDate(null);
		}
		projectRepository.save(project);
	}

	@Transactional
	public List<Project> findAllMyProjects() {
		Long userId = userService.getCurrentUser().getId();
		return findAllProjectsByUserId(userId);
	}

	@Transactional
	public List<Project> findAllProjectsByUserId(Long userId) {
		return projectRepository.findAllProjectsByUserId(userId);
	}

	/*
	 * PROJECT ROLES
	 * 
	 */

	private void verifySaveRole(ProjectRole saveProjectRole) {
		String username = userService.getCurrentUsername();
		ProjectRole userProjectRole = saveProjectRole.getProject().getProjectRoles().stream()
				.filter(x -> x.getUser().getUsername().equals(username)).findFirst()
				.orElseThrow(() -> new AccessDeniedException(ManapiMessages.USER_NOT_ALLOWED));

		if (!saveProjectRole.getUser().getUsername().equals(userService.getCurrentUsername())
				&& !List.of(1, 2, 3).contains(saveProjectRole.getRole()) // can't save an Owner role
				&& !List.of(0, 1).contains(userProjectRole.getRole())) { // must only be edited by Owner or admin
			throw new AccessDeniedException(ManapiMessages.USER_NOT_ALLOWED);
		}
	}

	private void verifyChangeMyRole(ProjectRole projectRole) {
		if (!projectRole.getUser().getUsername().equals(userService.getCurrentUsername())) {
			throw new AccessDeniedException(ManapiMessages.USER_NOT_ALLOWED);
		}
	}

	@Transactional
	public void acceptProjectRole(Long projectRoleId) {
		ProjectRole projectRole = findProjectRoleById(projectRoleId);
		verifyChangeMyRole(projectRole);
		projectRole.setAccepted(true);
		projectRoleRepository.save(projectRole);
	}

	@Transactional
	public void deleteProjectRole(Long projectRoleId) {
		ProjectRole projectRole = findProjectRoleById(projectRoleId);
		verifyChangeMyRole(projectRole);
		projectRoleRepository.deleteById(projectRoleId);
	}

	public Boolean existsOtherRole(ProjectRole projectRole) {
		ProjectRole exampleProjectRole = new ProjectRole();
		exampleProjectRole.setProject(projectRole.getProject());
		exampleProjectRole.setUser(projectRole.getUser());

		Example<ProjectRole> example = Example.of(exampleProjectRole);
		return projectRoleRepository.exists(example);
	}

	@Transactional
	public void createRole(ProjectRole projectRole) {
		verifySaveRole(projectRole);
		if (existsOtherRole(projectRole)) {
			throw new DuplicateKeyException("This role already exists");
		}
		projectRole.setAccepted(false);
		projectRoleRepository.save(projectRole);
	}

	@Transactional
	public void updateRole(ProjectRole projectRole) {
		ProjectRole oldProjectRole = findProjectRoleById(projectRole.getId());
		verifySaveRole(oldProjectRole);
		oldProjectRole.setRole(projectRole.getRole());
		projectRoleRepository.save(oldProjectRole);
	}

	@Transactional
	public List<ProjectRoleNotAcceptedDto> getAllInvitationsFromUser(User user) {
		ProjectRole exampleProjectRole = new ProjectRole();
		exampleProjectRole.setAccepted(false);
		exampleProjectRole.setUser(user);

		Example<ProjectRole> example = Example.of(exampleProjectRole);
		List<ProjectRole> projectRoles = projectRoleRepository.findAll(example);
		
		return projectRoles.stream()
				.filter(x->x.getProject().getActive())
				.map(x->new ProjectRoleNotAcceptedDto(x.getId(), x.getRole(), x.getProject().getName()))
				.collect(Collectors.toList());
	}
    
}
