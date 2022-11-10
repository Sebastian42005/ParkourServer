package com.example.spotmap.data.company;

import com.example.spotmap.data.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Data
@Entity
@NoArgsConstructor
@Table(name="company_requests")
public class CompanyRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @NotNull
    String username;

    @NotNull
    String message;
}
