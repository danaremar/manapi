package com.manapi.manapigateway.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.manapi.manapigateway.model.projects.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {

	@Query("SELECT p FROM Project p LEFT JOIN ProjectRole pr ON p.id = pr.project.id where p.active=true and pr.user.id=:user_id and pr.accepted=true")
	List<Project> findAllProjectsByUserId(@Param("user_id") Long userId);

}
