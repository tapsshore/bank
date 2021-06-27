package co.za.wonderlabz.bank.unit;

import co.za.wonderlabz.bank.domain.Account;
import co.za.wonderlabz.bank.dtos.CreateAccountRequestDto;
import co.za.wonderlabz.bank.enums.AccountType;
import co.za.wonderlabz.bank.repo.AccountRepository;
import co.za.wonderlabz.bank.service.AccountServiceImpl;
import org.dozer.Mapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceUTest {
    @InjectMocks
    private AccountServiceImpl accountService;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private Mapper mapper;

    private Account account;

    @Before
    public void init() {
        account = new Account();
    }
    @Test
    public void shouldFailToCreateSavingsAccountIfInitialDepositIsLessThan1000() {


        CreateAccountRequestDto dto =createAccountRequestDto();
        dto.setInitialDeposit(BigDecimal.TEN);
        dto.setAccountType(AccountType.SAVINGS);
        ReflectionTestUtils.setField(accountService,"initialDeposit",1000.00);
        when(mapper.map(any(CreateAccountRequestDto.class), any())).thenReturn(account);


    }
    private CreateAccountRequestDto createAccountRequestDto(){
        CreateAccountRequestDto createAccountRequestDto = new CreateAccountRequestDto();
        createAccountRequestDto.setAccountType(AccountType.CURRENT);
        createAccountRequestDto.setAddress("123");
        createAccountRequestDto.setEmailAddress("tet@haha.vv");
        createAccountRequestDto.setFullName("tstst");
        createAccountRequestDto.setIdNumber("tstst");
        createAccountRequestDto.setPhoneNumber("tstst");
        return createAccountRequestDto;
    }

}
