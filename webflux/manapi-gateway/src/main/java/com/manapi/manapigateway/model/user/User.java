package com.manapi.manapigateway.model.user;

import com.manapi.manapigateway.model.subscription.Subscription;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    private String id;

    @Indexed(unique = true)
    private String email;

    @Indexed(unique = true)
    private String username;

    private String firstName;

    private String lastName;

    private String password;

    private String country;

    private Date creationDate;

    private Date deleteDate;

    private Boolean active;

    private Long failedRetries;

    private List<Subscription> subscriptions;

}
