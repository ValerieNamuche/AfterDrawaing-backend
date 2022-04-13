package com.afterdrawing.backendapi.junit;

import com.afterdrawing.backendapi.core.entity.User;
import com.afterdrawing.backendapi.core.repository.UserRepository;
import com.afterdrawing.backendapi.core.service.UserService;
import com.afterdrawing.backendapi.exception.ResourceNotFoundException;
import com.afterdrawing.backendapi.resource.authentication.SignUpResource;
import com.afterdrawing.backendapi.service.AuthenticationService;
import com.afterdrawing.backendapi.service.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.constraints.Email;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class ElementImplServiceTest {

}
