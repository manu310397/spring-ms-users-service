package com.manoj.users.service;

import com.manoj.users.client.AlbumsServiceClient;
import com.manoj.users.dto.UserDTO;
import com.manoj.users.entity.UserEntity;
import com.manoj.users.repo.UsersRepo;
import com.manoj.users.web.model.response.AlbumResponseModel;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UsersServiceImpl implements UsersService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private UsersRepo usersRepo;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private RestTemplate restTemplate;
    private Environment environment;
    private AlbumsServiceClient albumsServiceClient;

    public UsersServiceImpl(UsersRepo usersRepo, BCryptPasswordEncoder bCryptPasswordEncoder, RestTemplate restTemplate, Environment environment, AlbumsServiceClient albumsServiceClient) {
        this.usersRepo = usersRepo;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.restTemplate = restTemplate;
        this.environment = environment;
        this.albumsServiceClient = albumsServiceClient;
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

    @Override
    public UserDTO getUserByUserId(String userId) {
        UserEntity entity = usersRepo.findByUserId(userId);

        if(entity == null) throw new UsernameNotFoundException(userId);

        UserDTO userDTO = new ModelMapper().map(entity, UserDTO.class);

        /*String albumsUrl = String.format(environment.getProperty("albums.url"), userId);

        ResponseEntity<List<AlbumResponseModel>> albumsListResponse = restTemplate.exchange(albumsUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<AlbumResponseModel>>() {
        });
        List<AlbumResponseModel> albumsList = albumsListResponse.getBody();*/

        logger.info("Before calling Albums Micro Service");
        List<AlbumResponseModel> albumsList = albumsServiceClient.getAlbums(userId);
        logger.info("After calling Albums Micro Service");

        userDTO.setAlbums(albumsList);

        return userDTO;
    }
}
