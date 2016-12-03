package com.app.service;
/**
 * Created by anna.karri on 01/12/16.
 */


import com.app.AppException;
import com.app.model.Account;
import com.app.model.Transaction;
import com.app.model.TransactionType;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.app.Constants.*;

/**
 * This class reads the csv file
 */

@Service
public class ResourceReader {

    public Account readFromMultipartFile(Reader reader) throws IOException, AppException {

        List<CSVRecord> records = getCSVRecords(reader);

        if (records.size() == 0) {
            throw new AppException(ERROR_NO_DATA_IN_INPUT);
        }

        Account account = new Account();
        if (records.get(0).size() > 1) {
            throw new AppException(ERROR_NO_BALANCE_RECORD_FOUND);
        }

        account.setBalance(getBigDecimal(records.get(0).get(0)));

        if (records.size() > 1) {
            List<CSVRecord> recordsTrans = records.subList(1, records.size());
            account.setTransactions(new ArrayList<>());
            for (CSVRecord recordTran : recordsTrans) {
                Transaction t;
                try {
                    t = new Transaction();
                    t.setBalance(getBigDecimal(recordTran.get(0)));
                    t.setType(getEnumType(recordTran.get(1)));
                    t.setDate(getDate(recordTran.get(2)));
                    t.setDescription(getStringValue(recordTran.get(3)));
                } catch (Exception e) {
                    String msg = String.join(". ", ERROR_IN_INPUT_DATA,
                            e.getMessage(), getDisplayString(recordTran));
                    throw new AppException(msg, e);
                }

                BigDecimal update = account.getBalance().add(t.getBalance());
                account.setBalance(update);
                account.getTransactions().add(t);
            }
        }
        return account;
    }

    private String getDisplayString(CSVRecord recordTran) {
        String str = recordTran.toString();
        return str.substring(str.indexOf("recordNumber"), str.length() - 1);
    }

    private List<CSVRecord> getCSVRecords(Reader reader) throws IOException {
        return CSVFormat.DEFAULT
                .parse(reader).getRecords();
    }

    private TransactionType getEnumType(String val) throws IllegalArgumentException {
        return StringUtils.isNotBlank(val) ? Enum.valueOf(TransactionType.class, val) : null;
    }

    private Date getDate(String val) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(Transaction.DATE_FORMAT);
        if (StringUtils.isNotBlank(val)) {
            return sdf.parse(val);
        }
        return null;
    }

    private static String getStringValue(String val) {
        return StringUtils.isNotBlank(val) ? val : null;
    }

    private static BigDecimal getBigDecimal(String val) {
        return StringUtils.isNotBlank(val) ? new BigDecimal(val) : null;
    }
}
