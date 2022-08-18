package com.rafael.peteventos.domain.converter;

import com.rafael.peteventos.domain.dto.GroupDto;
import com.rafael.peteventos.domain.entity.Group;

import java.util.List;
import java.util.stream.Collectors;

import static com.rafael.peteventos.domain.converter.UserDTOConverter.convertUserList;

public class GroupDTOConverter {

    public static GroupDto convertGroup(Group group) {
        return GroupDto.builder()
                .withIdGroup(group.getIdGroup())
                .withName(group.getName())
                .withDescription(group.getDescription())
                .withUsers(convertUserList(group.getUsers()))
                .withCourse(group.getCourse())
                .withCenter(group.getCenter())
                .build();
    }

    public static List<GroupDto> convertGroupList(List<Group> groups) {
        return groups.stream().map(GroupDTOConverter::convertGroup).collect(Collectors.toList());
    }
}
