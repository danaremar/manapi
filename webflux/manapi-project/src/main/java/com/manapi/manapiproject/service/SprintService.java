package com.manapi.manapiproject.service;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.manapi.manapiproject.model.sprint.Sprint;
import com.manapi.manapiproject.model.sprint.SprintCreateDto;
import com.manapi.manapiproject.model.sprint.SprintShowDto;
import com.manapi.manapiproject.repository.SprintRepository;

@Service
public class SprintService {

    @Autowired
    private SprintRepository sprintRepository;

    @Autowired(required = true)
    protected ModelMapper modelMapper;

    /**
     * Find active sprint by id
     * 
     * @param sprintId
     * @return
     */
    public Sprint findSprintById(String sprintId) {
        Sprint exampleSprint = new Sprint();
        exampleSprint.setActive(true);
        exampleSprint.setId(sprintId);
        Example<Sprint> example = Example.of(exampleSprint);
        return sprintRepository.findOne(example).orElse(null);
    }

    /**
     * Sprint -> SprintShowDto
     * 
     * @param sprint
     * @return
     */
    public SprintShowDto mapToSprintShowDto(Sprint sprint) {
        return modelMapper.map(sprint, SprintShowDto.class);
    }

    /**
     * Find sprints by project id, ordered by number desc
     * 
     * @param projectId
     * @return
     */
    public List<SprintShowDto> findSprintsByProjectId(String projectId) {
        Sprint exampleSprint = new Sprint();
        exampleSprint.setActive(true);
        exampleSprint.setProjectId(projectId);
        Example<Sprint> example = Example.of(exampleSprint);
        List<Sprint> ls = sprintRepository.findAll(example, Sort.by(Sort.Direction.DESC, "number"));
        return ls.stream().map(this::mapToSprintShowDto).toList();
    }

    /**
     * Count all sprint by project id, including non active
     * 
     * @param projectId
     * @return
     */
    public Long countSprintsByProjectId(String projectId) {
        Sprint exampleSprint = new Sprint();
        exampleSprint.setProjectId(projectId);
        Example<Sprint> example = Example.of(exampleSprint);
        return sprintRepository.count(example);
    }

    /**
     * Create sprint by given dto & projectId
     * 
     * @param sprintCreateDto
     * @param projectId
     * @return
     */
    @Transactional
    public SprintShowDto createSprint(SprintCreateDto sprintCreateDto, String projectId) {
        Long number = countSprintsByProjectId(projectId) + 1L;
        Sprint sprint = modelMapper.map(sprintCreateDto, Sprint.class);
        sprint.setProjectId(projectId);
        sprint.setNumber(number);
        sprint.setActive(true);
        sprint.setCreationDate(new Date());
        Sprint s = sprintRepository.save(sprint);
        return mapToSprintShowDto(s);
    }

    /**
     * Update sprint
     * 
     * @param sprintUpdateDto
     * @param sprintId
     * @return
     */
    @Transactional
    public SprintShowDto updateSprint(SprintCreateDto sprintUpdateDto, String sprintId) {
        Sprint sprint = findSprintById(sprintId);
        sprint.setName(sprintUpdateDto.getName());
        sprint.setStartDate(sprintUpdateDto.getStartDate());
        sprint.setEndDate(sprintUpdateDto.getEndDate());
        sprint.setCloseDate(sprintUpdateDto.getCloseDate());
        Sprint s = sprintRepository.save(sprint);
        return mapToSprintShowDto(s);
    }

    /**
     * Delete sprint
     * <p>
     * It's disabled, user cannot find anytime more
     * 
     * @param sprintId
     */
    @Transactional
    public void deleteSprint(String sprintId) {
        Sprint sprint = findSprintById(sprintId);
        sprint.setActive(false);
        sprintRepository.save(sprint);
    }

}
