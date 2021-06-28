package co.za.wonderlabz.bank.domain;

import co.za.wonderlabz.bank.enums.AccountType;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class Account {
    private String fullName;
    private String address;
    @Id
    private String accountNumber;
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    private String idNumber;
    private String phoneNumber;
    private String emailAddress;
    private BigDecimal balance;
    private LocalDateTime dateCreated;

    @PrePersist
    public void init(){
        dateCreated = LocalDateTime.now();
    }
}
