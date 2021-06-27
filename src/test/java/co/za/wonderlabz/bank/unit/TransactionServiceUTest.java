package co.za.wonderlabz.bank.unit;

import co.za.wonderlabz.bank.domain.Account;
import co.za.wonderlabz.bank.dtos.DepositRequestDto;
import co.za.wonderlabz.bank.dtos.DepositResponseDto;
import co.za.wonderlabz.bank.dtos.WithdrawRequestDto;
import co.za.wonderlabz.bank.dtos.WithdrawResponseDto;
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
    @Test
    public void shouldIncreaseBalance(){
        DepositRequestDto dto = DepositRequestDto.builder()
                .accountNumber("123456")
                .amount(BigDecimal.TEN)
                .build();
        account.setBalance(BigDecimal.TEN);
        when(accountRepository.findByAccountNumber(dto.getAccountNumber())).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        ResponseEntity<DepositResponseDto> depositResponse = transactionService.deposit(dto);

        assertEquals(HttpStatus.OK, depositResponse.getStatusCode());
        assertEquals(BigDecimal.valueOf(20), depositResponse.getBody().getNewBalance());
        verify(accountRepository, times(1)).save(any(Account.class));
    }
    @Test
    public void shouldFailToWithdrawIfAccountDoesNotExist() {
        WithdrawRequestDto dto = WithdrawRequestDto.builder()
                .accountNumber("123456")
                .amount(BigDecimal.TEN)
                .build();
        account.setBalance(BigDecimal.TEN);
        ResponseEntity<WithdrawResponseDto> withdrawResponse = transactionService.withdraw(dto);
        assertEquals(HttpStatus.BAD_REQUEST, withdrawResponse.getStatusCode());
        assertEquals("Account 123456 not found!", withdrawResponse.getBody().getMessage());
        verify(accountRepository, times(0)).save(any(Account.class));
    }
    @Test
    public void shouldDecreaseBalanceOnWithDraw() {
        WithdrawRequestDto dto = WithdrawRequestDto.builder()
                .accountNumber("123456")
                .amount(BigDecimal.valueOf(100_000))
                .build();
        account.setBalance(BigDecimal.TEN);
        ReflectionTestUtils.setField(transactionService, "overdraftAmount", BigDecimal.valueOf(100_000));
        when(accountRepository.findByAccountNumber(dto.getAccountNumber())).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        ResponseEntity<WithdrawResponseDto> withdrawResponse = transactionService.withdraw(dto);
        assertEquals(HttpStatus.OK, withdrawResponse.getStatusCode());
        assertEquals(BigDecimal.valueOf(-99_990), withdrawResponse.getBody().getNewBalance());
        verify(accountRepository, times(1)).save(any(Account.class));
    }
    @Test
    public void shouldFailIfWithdrawalAmountIsGreaterThanLimit(){
        WithdrawRequestDto dto = WithdrawRequestDto.builder()
                .accountNumber("123456")
                .amount(BigDecimal.valueOf(120_000))
                .build();
        account.setBalance(BigDecimal.TEN);
        ReflectionTestUtils.setField(transactionService, "overdraftAmount", BigDecimal.valueOf(100_000));
        when(accountRepository.findByAccountNumber(dto.getAccountNumber())).thenReturn(Optional.of(account));
        ResponseEntity<WithdrawResponseDto> withdrawResponse = transactionService.withdraw(dto);
        assertEquals(HttpStatus.BAD_REQUEST, withdrawResponse.getStatusCode());
        verify(accountRepository, times(0)).save(any(Account.class));
    }
    @Test
    public void shouldFailWithdrawalForInvalidAmount(){
        WithdrawRequestDto dto = WithdrawRequestDto.builder()
                .accountNumber("123456")
                .amount(BigDecimal.ZERO)
                .build();
        ReflectionTestUtils.setField(transactionService, "overdraftAmount", BigDecimal.valueOf(100_000));
        ResponseEntity<WithdrawResponseDto> withdrawResponse = transactionService.withdraw(dto);
        assertEquals(HttpStatus.BAD_REQUEST, withdrawResponse.getStatusCode());
        verify(accountRepository, times(0)).save(any(Account.class));
    }
}
