package uz.pdp.citymanagement_monolith.service.user;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import uz.pdp.citymanagement_monolith.domain.dto.user.UserInboxCreateDto;
import uz.pdp.citymanagement_monolith.domain.dto.user.UserInboxForUserDto;
import uz.pdp.citymanagement_monolith.domain.entity.user.MessageState;
import uz.pdp.citymanagement_monolith.domain.entity.user.UserInboxEntity;
import uz.pdp.citymanagement_monolith.domain.filters.Filter;
import uz.pdp.citymanagement_monolith.exception.DataNotFoundException;
import uz.pdp.citymanagement_monolith.repository.user.UserInboxRepositoryImpl;
import uz.pdp.citymanagement_monolith.repository.user.UserRepositoryImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserInboxService {
    private final UserInboxRepositoryImpl userInboxRepository;
    private final UserRepositoryImpl userRepository;
    private final ModelMapper modelMapper;
    public UserInboxForUserDto addNewMessage(UUID userId, UserInboxCreateDto userInboxCreateDto) {
        UserInboxEntity userInboxEntity = modelMapper.map(userInboxCreateDto, UserInboxEntity.class);
        userInboxEntity.setOwner(userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException("User not found!")));
        userInboxEntity.setState(MessageState.UNREAD);
        UserInboxEntity saved = userInboxRepository.save(userInboxEntity);
        return modelMapper.map(saved, UserInboxForUserDto.class);
    }

    public List<UserInboxForUserDto> getAllUserInbox(UUID userId, Filter filter) {
        if(filter == null) filter = new Filter();
        List<UserInboxEntity> allByOwner = userInboxRepository.getAllByOwner(userId, filter);
        List<UserInboxForUserDto> forUserDto = new ArrayList<>();
        allByOwner.forEach((inbox) -> forUserDto.add(modelMapper.map(inbox, UserInboxForUserDto.class)));
        return forUserDto;
    }
}
