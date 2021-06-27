package co.za.wonderlabz.bank.unit;

import co.za.wonderlabz.bank.domain.TransactionHistory;
import co.za.wonderlabz.bank.repo.TransactionHistoryRepository;
import co.za.wonderlabz.bank.service.TransactionHistoryServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;


import java.math.BigDecimal;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class TransactionHistoryServiceUTest {

    @InjectMocks
    private TransactionHistoryServiceImpl transactionHistoryService;

    @Mock
    private TransactionHistoryRepository repository;

    @Test
    public void shouldSaveTransationHistory(){
        TransactionHistory transactionHistory = TransactionHistory.builder()
                .description("transation")
                .amount(BigDecimal.ONE)
                .account("xyz")
                .build();
        transactionHistoryService.save(transactionHistory);
        verify(repository, times(1)).save(transactionHistory);
    }
}
