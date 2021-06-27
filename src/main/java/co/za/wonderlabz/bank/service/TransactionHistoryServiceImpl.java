package co.za.wonderlabz.bank.service;

import co.za.wonderlabz.bank.domain.TransactionHistory;
import co.za.wonderlabz.bank.repo.TransactionHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class TransactionHistoryServiceImpl implements TransactionHistoryService {

    @Autowired
    private TransactionHistoryRepository transactionHistoryRepository;

    @Override
    public TransactionHistory save(TransactionHistory transactionHistory) {
        log.info("{}",transactionHistory);
        return transactionHistoryRepository.save(transactionHistory);
    }
}
