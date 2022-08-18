package com.rafael.peteventos.domain.service;

import com.rafael.peteventos.domain.dto.GroupDto;
import com.rafael.peteventos.domain.entity.AuthenticatedUser;
import com.rafael.peteventos.domain.entity.Group;
import com.rafael.peteventos.domain.util.JwtUtil;
import com.rafael.peteventos.integration.repository.impl.AuthenticatedUserRepository;
import com.rafael.peteventos.integration.repository.impl.GroupRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.rafael.peteventos.domain.converter.GroupDTOConverter.convertGroup;
import static com.rafael.peteventos.domain.converter.GroupDTOConverter.convertGroupList;

@Service
@AllArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final String GROUP_NOT_FOUND = "Grupo não encontrado";
    private final AuthenticatedUserRepository authenticatedUserRepository;
    private final String INVALID_BEARER = "Bearer token inválido.";

    public GroupDto getGroup(String bearer) {
        Optional<AuthenticatedUser> userOptional = authenticatedUserRepository.findByEmail(JwtUtil.getEmail(bearer));
        if(userOptional.isPresent()) {
            return convertGroup(groupRepository.findById(userOptional.get().getGroup().getIdGroup()).get());
        } else {
            throw new RuntimeException(INVALID_BEARER);
        }
    }

    public List<GroupDto> getGroups() {
        return convertGroupList(groupRepository.findAll());
    }

    public void postGroup(Group group) {
        groupRepository.save(group);
    }

    public void putGroup(Group group) {
        Optional<Group> optionalGroup = groupRepository.findById(group.getIdGroup());
        if(optionalGroup.isPresent()) {
            groupRepository.save(group);
        } else {
            throw new RuntimeException(GROUP_NOT_FOUND);
        }
    }
}
