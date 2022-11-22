package com.manoj.users.service;

import com.manoj.users.dto.UserDTO;
import com.manoj.users.entity.UserEntity;
import com.manoj.users.repo.UsersRepo;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class UsersServiceImpl implements UsersService {

    private UsersRepo usersRepo;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UsersServiceImpl(UsersRepo usersRepo, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.usersRepo = usersRepo;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
//        UserEntity userFromDB = usersRepo.findByEmail(userDTO.getEmail());

//        if(userFromDB != null) throw new RuntimeException("User already exists");

//        List<AddressDTO> addressList = userDTO.getAddresses().stream()
//                .map(addressDTO -> {
//                    addressDTO.setAddressId(utils.generateAddressId(25));
//                    addressDTO.setUserDetails(userDTO);
//                    return addressDTO;
//                })
//                .collect(Collectors.toList());

//        userDTO.setAddresses(addressList);

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        userDTO.setUserId(UUID.randomUUID().toString());

        UserEntity userEntity = mapper.map(userDTO, UserEntity.class);


        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
//        userEntity.setEmailVerificationToken(utils.generateEmailVerificationToken(publicUserId));
        userEntity.setEmailVerificationStatus(Boolean.FALSE);

        UserEntity savedUser = usersRepo.save(userEntity);

        UserDTO response = mapper.map(savedUser, UserDTO.class);

        return response;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userFromDB = usersRepo.findByEmail(email);

        if(userFromDB == null) throw new UsernameNotFoundException(email);

        return new User(userFromDB.getEmail(), userFromDB.getEncryptedPassword(), true,
                true, true, true, new ArrayList<>());
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        UserDTO result = new UserDTO();

        UserEntity userFromDB = usersRepo.findByEmail(email);

        if(userFromDB == null) throw new UsernameNotFoundException(email);

        BeanUtils.copyProperties(userFromDB, result);

        return result;
    }
}
