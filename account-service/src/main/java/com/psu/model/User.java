package com.psu.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(collection = "users")
public class User {

    @Id
    private Long id;

    private String username;
    private String password;
    private String email;

    // Remember to use @AllArgsConstructor or @RequiredArgsConstructor if necessary

}
