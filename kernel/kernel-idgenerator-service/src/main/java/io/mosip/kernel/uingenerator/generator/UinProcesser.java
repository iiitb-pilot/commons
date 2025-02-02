package io.mosip.kernel.uingenerator.generator;

import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.mosip.kernel.core.idgenerator.spi.UinGenerator;
import io.mosip.kernel.uingenerator.constant.UinGeneratorConstant;
import io.mosip.kernel.uingenerator.repository.UinRepository;

@Component
public class UinProcesser {
	 private static final Logger LOGGER = LoggerFactory.getLogger(UinProcesser.class);

	/**
	 * Field for uinRepository
	 */
	@Autowired
	private UinRepository uinRepository;

	/**
	 * Field for uinGeneratorImpl
	 */
	@Autowired
	private UinGenerator uinGeneratorImpl;

	/**
	 * Long field for uin threshold count
	 */
	@Value("${mosip.kernel.uin.min-unused-threshold}")
	private long thresholdUinCount;

	@Value("${mosip.kernel.uin.uins-to-generate}")
	long uinsCount;

	/**
	 * Check whether to generate uin or not
	 * 
	 * @return true, if needs to generate uin
	 */
	public boolean shouldGenerateUins(Long startTime) {
		LOGGER.info("THAM1 - Uin threshold is {}", thresholdUinCount + " " + (System.currentTimeMillis() - startTime) + " ms");
		long freeUinsCount = uinRepository.countByStatus(UinGeneratorConstant.UNUSED);
		LOGGER.info("THAM1 - Number of free UINs in database is {}", freeUinsCount + " " + (System.currentTimeMillis() - startTime) + " ms");
		return freeUinsCount < thresholdUinCount;
	}

	/**
	 * Create list of uins
	 */
	public void generateUins(Long startTime) {
		LOGGER.info("THAM1 - Uin generateUins() entering " + (System.currentTimeMillis() - startTime) + " ms");
		long noOfUnUsedUins = uinRepository.countByStatusAndIsDeletedFalse(UinGeneratorConstant.UNUSED);
		LOGGER.info("THAM1 - Uin generateUins() No of Unused UIN " + (System.currentTimeMillis() - startTime) + " ms");
		uinGeneratorImpl.generateId(uinsCount <= noOfUnUsedUins ? 0 : uinsCount - noOfUnUsedUins);
		LOGGER.info("THAM1 - Uin generateUins() Existing " + (System.currentTimeMillis() - startTime) + " ms");

	}

}
