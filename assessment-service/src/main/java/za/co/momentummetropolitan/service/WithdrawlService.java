package za.co.momentummetropolitan.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.function.Supplier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import za.co.momentummetropolitan.entities.Client;
import za.co.momentummetropolitan.entities.ClientProduct;
import za.co.momentummetropolitan.entities.FinancialProduct;
import za.co.momentummetropolitan.enums.FinancialProductsEnum;
import za.co.momentummetropolitan.exceptions.ClientProductIdNotFoundException;
import za.co.momentummetropolitan.exceptions.RetirementAgeNotAttainedException;
import za.co.momentummetropolitan.exceptions.WithdrawAmountExceedsBalanceException;
import za.co.momentummetropolitan.repository.ClientFinancialProductRepository;
import za.co.momentummetropolitan.repository.ClientRepository;
import za.co.momentummetropolitan.repository.FinancialProductRepository;

@Service
public class WithdrawlService {
    private static final long MINIMUM_RETIREMENT_AGE = 65;
    private static final BigDecimal PERCENTAGE = BigDecimal.valueOf(100);

    private final ClientRepository clientRepo;
    private final ClientFinancialProductRepository clientProductsRepo;
    private final FinancialProductRepository financialProductRepo;
    private final Supplier<LocalDate> dateSupplier;
    private final BigDecimal maxWithdrawPercentage;

    public WithdrawlService(final ClientRepository clientRepo, 
            final ClientFinancialProductRepository clientProductsRepo, 
            final FinancialProductRepository financialProductRepo, final Supplier<LocalDate> dateSupplier,
            @Value("${mmi.investment.max-withdraw-percentage}") final BigDecimal maxWithdrawPercentage) {
        this.clientRepo = clientRepo;
        this.clientProductsRepo = clientProductsRepo;
        this.dateSupplier = dateSupplier;
        this.financialProductRepo = financialProductRepo;
        this.maxWithdrawPercentage = maxWithdrawPercentage;
    }

    public void withdraw(final Long clientProductId, final BigDecimal withdrawAmount) {
        final ClientProduct foundClientProduct = clientProductsRepo.findById(clientProductId)
                .orElseThrow(() -> new ClientProductIdNotFoundException(clientProductId));

        final Client client = clientRepo.findById(foundClientProduct.getClientId()).get();
        final FinancialProduct financialProduct = financialProductRepo.findById(
                foundClientProduct.getFinancialProductId()).get();

        // check retirement age of client greater than 65 years of age
        if (FinancialProductsEnum.RETIREMENT.equals(financialProduct.getProductType())) {
            final LocalDate dob = client.getDateOfBirth();
            final long yearsDifference = ChronoUnit.YEARS.between(dob, dateSupplier.get());

            if (yearsDifference <= MINIMUM_RETIREMENT_AGE) {
                throw new RetirementAgeNotAttainedException(yearsDifference, dob);
            }
        }

        // Withdraw amount exceeds the balance check
        final BigDecimal balance = foundClientProduct.getBalance();

        if (withdrawAmount.compareTo(balance) > 0) {
            throw new WithdrawAmountExceedsBalanceException(balance, withdrawAmount);
        }
        
        // check that withdrawl amount not greater than 90% of the balance
        final BigDecimal withdrawPercentage = withdrawAmount.divide(balance, RoundingMode.HALF_UP)
                .divide(PERCENTAGE, RoundingMode.CEILING);
        if (withdrawPercentage.compareTo(maxWithdrawPercentage) > 0) {
            throw new WithdrawAmountExceedsBalanceException(balance, withdrawAmount);
        }

        foundClientProduct.setBalance(balance.subtract(withdrawAmount));
        clientProductsRepo.save(foundClientProduct);
    }
}
