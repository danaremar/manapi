package com.manapi.manapiproject.model.userstory;

import com.manapi.manapiproject.model.epic.EpicShowDto;
import com.manapi.manapiproject.model.milestone.MilestoneShowDto;
import com.manapi.manapiproject.model.util.NamedEntityShowDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserStoryShowDto extends NamedEntityShowDto {

    private Long number;
    
    private String asUser;

    private String iCan;

    private String soThat;

    private MilestoneShowDto milestone;

    private EpicShowDto epic;
    
}
