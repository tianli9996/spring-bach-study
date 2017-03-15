/**
 * 
 */
package day2.study.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import day2.study.model.CreditBill;

public class CreditBillProcessor implements ItemProcessor<CreditBill, CreditBill> {

	public static Logger logger = LoggerFactory.getLogger(CreditBillProcessor.class);
	public CreditBill process(CreditBill bill) throws Exception {
		logger.info(bill.toString());
		return bill;
	}
}
