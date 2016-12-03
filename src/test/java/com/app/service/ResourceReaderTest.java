package com.app.service;

import com.app.AppException;
import com.app.model.Account;
import com.app.model.Transaction;
import com.app.model.TransactionType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static com.app.Constants.ERROR_IN_INPUT_DATA;
import static com.app.Constants.ERROR_NO_BALANCE_RECORD_FOUND;
import static com.app.Constants.ERROR_NO_DATA_IN_INPUT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ResourceReaderTest {

    private Reader reader;

    @InjectMocks
    ResourceReader resourceReader;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_empty_file() throws IOException, AppException {
        reader = new StringReader("");

        assertThatThrownBy(() -> resourceReader.readFromMultipartFile(reader))
                .isInstanceOf(AppException.class)
                .hasMessage(ERROR_NO_DATA_IN_INPUT);
    }

    @Test
    public void test_no_balance_record() throws IOException, AppException {
        reader = new StringReader("1000,FT,20/03/2015,Transfer there.");

        assertThatThrownBy(() -> resourceReader.readFromMultipartFile(reader))
                .isInstanceOf(AppException.class)
                .hasMessage(ERROR_NO_BALANCE_RECORD_FOUND);
    }

    @Test
    public void test_only_balance_record_no_transactions() throws IOException, AppException {
        reader = new StringReader("1000");

        Account account = resourceReader.readFromMultipartFile(reader);
        assertThat(account.getBalance()).isEqualTo(BigDecimal.valueOf(1000));
        assertThat(account.getTransactions()).isNull();
    }

    @Test
    public void test_balance_and_transactions() throws IOException, AppException, ParseException {
        StringBuffer stringBuffer = new StringBuffer("1000\n")
                .append("-500,FT,20/03/2015,Transfer there.");

        SimpleDateFormat sdf = new SimpleDateFormat(Transaction.DATE_FORMAT);

        Transaction transaction = new Transaction();
        transaction.setBalance(BigDecimal.valueOf(-500));
        transaction.setType(TransactionType.FT);
        transaction.setDate(sdf.parse("20/03/2015"));
        transaction.setDescription("Transfer there.");

        reader = new StringReader(stringBuffer.toString());

        Account account = resourceReader.readFromMultipartFile(reader);
        assertThat(account.getBalance()).isEqualTo(BigDecimal.valueOf(500));
        assertThat(account.getTransactions()).isNotNull();
        assertThat(account.getTransactions().size()).isEqualTo(1);
        assertThat(account.getTransactions().get(0)).isEqualTo(transaction);
    }

    @Test
    public void test_error_in_data() {
        StringBuffer stringBuffer = new StringBuffer("1000\n")
                .append("-500,FT,20/0--3/2015,Transfer there.");
        reader = new StringReader(stringBuffer.toString());

        String msg = String.join(". ", ERROR_IN_INPUT_DATA,
                "Unparseable date: \"20/0--3/2015\"", "recordNumber=2, values=[-500, FT, 20/0--3/2015, Transfer there.]");

        assertThatThrownBy(() -> resourceReader.readFromMultipartFile(reader))
                .isInstanceOf(AppException.class)
                .hasMessage(msg);
    }
}
