package com.manapi.manapigateway.model.user;

import com.manapi.manapigateway.model.subscription.Plan;
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

    /**
     * Get valid subscription
     * @return
     */
    public Subscription getValidSubscription() {
        return subscriptions.stream()
                .filter(x -> x.getEndDate() != null && x.getEndDate().after(new Date()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Get plan
     * 
     * @return
     */
    public Plan getActualPlan() {

        // find valid subscription
        Subscription s = getValidSubscription();

        // if exists: get plan -> if not: default plan
        Plan plan = new Plan();
        if (s == null || s.getPlan()==null) {
            return plan.getPlan("free");
        } else {
            return plan.getPlan(s.getPlan());
        }
    }

}
