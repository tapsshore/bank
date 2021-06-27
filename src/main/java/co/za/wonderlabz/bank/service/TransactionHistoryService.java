package co.za.wonderlabz.bank.service;


import co.za.wonderlabz.bank.domain.TransactionHistory;

public interface TransactionHistoryService {

    TransactionHistory save(TransactionHistory transactionHistory);
}
