package com.coelho.dtos;

import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;
    private String name;
    private String email;
}