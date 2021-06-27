package co.za.wonderlabz.bank.unit;

import co.za.wonderlabz.bank.repo.AccountRepository;
import co.za.wonderlabz.bank.service.AccountServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceUTest {
    @InjectMocks
    private AccountServiceImpl accountService;
    @Mock
    private AccountRepository accountRepository;


    @Test
    public void shouldFailToCreateSavingsAccountIfInitialDepositIsLessThan1000() {

    }

}
