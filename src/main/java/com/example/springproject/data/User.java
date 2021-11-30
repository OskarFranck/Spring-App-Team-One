package com.example.springproject.data;

import com.sun.istack.NotNull;
import lombok.*;

@Builder
@Getter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @NotNull
    private String email;
    private String userName;

}
