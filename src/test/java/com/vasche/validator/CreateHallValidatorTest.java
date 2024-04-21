package com.vasche.validator;

import com.vasche.repository.HallRepository;
import com.vasche.dto.hall.CreateHallDto;
import com.vasche.entity.Hall;
import com.vasche.exception.RepositoryException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.vasche.util.constants.ErrorCodes.NOT_UNIQUE_NAME;
import static com.vasche.util.constants.ErrorCodes.NULL_NAME;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class CreateHallValidatorTest {

    @InjectMocks
    private CreateHallValidator validator = new CreateHallValidator();

    @Mock
    private HallRepository hallDao = new HallRepository();

    @Test
    void shouldPassValidation() throws RepositoryException {

        CreateHallDto dto = CreateHallDto.builder()
                .name("Hall 1")
                .build();

        doReturn(Optional.empty()).when(hallDao).findByName(dto.getName());

        ValidationResult actualResult = validator.isValid(dto);

        assertTrue(actualResult.getErrors().isEmpty());
    }

    @Test
    void nullName() throws RepositoryException {

        CreateHallDto dto = CreateHallDto.builder()
                .build();

        doReturn(Optional.empty()).when(hallDao).findByName(dto.getName());

        ValidationResult actualResult = validator.isValid(dto);

        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(NULL_NAME);
    }

    @Test
    void notUniqueName() throws RepositoryException {

        CreateHallDto dto = CreateHallDto.builder()
                .name("Name")
                .build();

        Hall hall = Hall.builder()
                .id(1)
                .name("Name")
                .build();

        doReturn(Optional.of(hall)).when(hallDao).findByName(dto.getName());

        ValidationResult actualResult = validator.isValid(dto);

        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(NOT_UNIQUE_NAME);
    }
}
