package com.example.springproject.data;

import lombok.*;

@Builder
@Getter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @NonNull
    private String email;
    @NonNull
    private String userName;

}
