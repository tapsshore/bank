package co.za.wonderlabz.bank.unit;

import co.za.wonderlabz.bank.domain.Account;
import co.za.wonderlabz.bank.dtos.DepositRequestDto;
import co.za.wonderlabz.bank.dtos.DepositResponseDto;
import co.za.wonderlabz.bank.repo.AccountRepository;
import co.za.wonderlabz.bank.service.TransactionHistoryService;
import co.za.wonderlabz.bank.service.TransactionServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceUTest {

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionHistoryService historyService;


    private Account account;

    @Before
    public void init() {
        account = new Account();
    }

    @Test
    public void shouldFailToDepositIfAccountDoesNotExist() {
        DepositRequestDto dto = DepositRequestDto.builder()
                .accountNumber("123456")
                .amount(BigDecimal.TEN)
                .build();
        ResponseEntity<DepositResponseDto> depositResponse = transactionService.deposit(dto);
        assertEquals(HttpStatus.BAD_REQUEST, depositResponse.getStatusCode());
        assertEquals("Account 123456 not found!", depositResponse.getBody().getMessage());
        verify(accountRepository, times(0)).save(any(Account.class));
    }
    @Test
    public void shouldFailToDepositIfAmountIsLessThanOne() {

        DepositRequestDto dto = DepositRequestDto.builder()
                .accountNumber("123456")
                .amount(BigDecimal.ZERO)
                .build();

        ResponseEntity<DepositResponseDto> depositResponse = transactionService.deposit(dto);

        assertEquals(HttpStatus.BAD_REQUEST, depositResponse.getStatusCode());
        assertEquals("Invalid deposit amount", depositResponse.getBody().getMessage());
        verify(accountRepository, times(0)).save(any(Account.class));
    }

}